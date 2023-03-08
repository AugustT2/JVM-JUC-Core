#!/bin/ksh
#ident  "%W%"


retry_count=0
max_retry=3

abort() {
    echo $1
    exit 1
}

usageAndExit() {
    echo "Usage:  ./runMarsEod.ksh <BATCH NAME> <HTTP_PORT>"
    exit 1
}

runBatch() {
     job=$1
     if [ $# -ge 2 ]
     then
        cobDate=$( printf "%s\n" "$2" | sed 's/ /%20/g' )
        request=http://${HOST_SERVER}:${HTTP_PORT}/rest/odbatch/${job}?cob-date=date:${cobDate}
     else
       request=http://${HOST_SERVER}:${HTTP_PORT}/rest/odbatch/${job}
     fi
     print "Executing curl: curl ${request} "

     jobId=$(curl ${request})
     print "job id: ${jobId}"

     batch_state="[Status=RUNNING"
     while [[ ${batch_state} =~ "[Status=RUNNING" ]] || [[ ${batch_state} =~ "[Status=TRADE_PROCESSING_COMPLETE" ]] || [[ ${batch_state} =~ "[Status=WAITING_FOR_ML_CONFIRMATION" ]] || [[ ${batch_state} =~ "[Status=NEW" ]] || [[ ${batch_state} =~ "[Status=READY" ]] || [[ ${batch_state} =~ "[Status=ML_CONFIRMATION_RECEIVED" ]] || [[ ${batch_state} =~ "[Status=COMPLETED" ]] || [[ ${batch_state} =~ "[Status=NOTRADES" ]] ; do
           print "sleeping at $(date) for ${STATUS_CHECK_SLEEP:=300} seconds"
           sleep ${STATUS_CHECK_SLEEP}
           batch_state=$(curl http://${HOST_SERVER}:${HTTP_PORT}/rest/odbatch/BatchJobState/${jobId})
           print "State for batch ${job} with job id: ${jobId} is [${batch_state}]"
           # shellcheck disable=SC1072
           if [[ ${batch_state} == "" ]]; then
              print "State for batch ${job} with job id: ${jobId} is empty, will retry..."
              while [ "$retry_count" -le "$max_retry" ]; do
                (( retry_count += 1 ))
                batch_state=$(curl http://${HOST_SERVER}:${HTTP_PORT}/rest/odbatch/BatchJobState/${jobId})
                print "retry $retry_count time, and job state is ${batch_state}"
                if [[ ${batch_state} != "" ]]; then
                    break
                fi
              done
           fi
     done
     print "State for batch ${job} with job id: ${jobId} is ${batch_state}"
     if [[ ${batch_state} =~ "[Status=BATCH_SUMMARY_PERSISTED" ]]
     then

        return 0
     elif [[ ${batch_state} =~ "[Status=FAILED" ]]
     then
        print "Batch failed. Getting BatchStatusMessage..."
        batch_status_message=$(curl http://${HOST_SERVER}:${HTTP_PORT}/rest/odbatch/BatchStatusMessage/${jobId})
        print "BatchStatusMessage: ${batch_status_message}"
        if [[ ${batch_status_message} =~ "StatusMessage: \"No trades found for batch job\"" ]]
        then
            return 2
        else
            return 1
        fi
     else
        return 1
     fi
}

#------------------------------------------------------------------------------------------

echo "Running runMarsEod.ksh vFLOW_INTG(if running from Jenkins build number is ${BUILD_NUMBER})."
echo "Command line: ./runMarsEod.ksh $* "

__REAL_SCRIPTDIR=$( cd -P -- "$(dirname -- "$(command -v -- "$0")")" && pwd -P )
. $__REAL_SCRIPTDIR/initialize_script.ksh

#check if this script is overriden and if so use that one instead
SCRIPT_NAME=$(basename $0)
echo "this currently running script is $__REAL_SCRIPTDIR/$SCRIPT_NAME"
echo "checking for overriden script ${OVERRIDE_DIR}bin/$SCRIPT_NAME"
#only call the override if it exists and it is not equal to this current running script which would cause a circular dependency
if [[ -e ${OVERRIDE_DIR}/bin/$SCRIPT_NAME ]]
then
  if [[ $__REAL_SCRIPTDIR/$SCRIPT_NAME = ${OVERRIDE_DIR}bin/$SCRIPT_NAME ]]
  then
    echo "The override script is running already so will not initiate a circular dependency by executing it"
  else
    echo "detected an override of $SCRIPT_NAME so using that one instead"
    ${OVERRIDE_DIR}/bin/$SCRIPT_NAME $*
    RET_VAL=$?
    echo "overriden script ${OVERRIDE_DIR}/bin/$SCRIPT_NAME returned value $RET_VAL"
    exit $RET_VAL
  fi
else
  echo "no override script was found"
fi

echo "BUSINESS = ${BUSINESS}"

if [[ $# -eq 0 ]];then
    usageAndExit
fi

HTTP_PORT=$1
eodBatch=$2



if [ $# -ge 3 ]
then
 if [[ $# -eq 3 ]] && [[ $3 = "true" ]]
 then
  sleep 60
 elif [[ $# -eq 3 ]] && [[ $3 = "T0" ]]
 then
  runT0=true
 else
  runWithPaa=true
  paaRBJob=$3
  paaBFJob=$4
 fi
else
 runWithPaa=false
fi

echo "need to run jobs ${eodBatch} ,  ${paaRBJob} and  ${paaBFJob} at $(date)"

echo "checking if a web server is running on ${HOST_SERVER}:${HTTP_PORT}"
loopCtr=0
status_code=$(curl -o /dev/null -w %{http_code} ${HOST_SERVER}:${HTTP_PORT})
while [[ "$?" -ne 0 ]] || [[ $status_code -eq 404 ]]; do
  echo "status_code:${status_code}"
  echo "Connection to ${HOST_SERVER} on port ${HTTP_PORT} failed. sleeping ${SERVER_INITIALISATION_WAIT_TIME:=10} seconds"
  ((loopCtr=loopCtr+1))
  if [[ $loopCtr -gt ${MAX_INITIALIZATION_ATTEMPTS:=10} ]]
  then
    echo "EOD Risk for Batch ${eodBatch} failed - could not validate the server was up and running on ${HOST_SERVER}:${HTTP_PORT} after ${MAX_INITIALIZATION_ATTEMPTS} tries"
    exit 2
  else
    echo "That was connection attempt number $loopCtr out of $MAX_INITIALIZATION_ATTEMPTS"
  fi
  sleep ${SERVER_INITIALISATION_WAIT_TIME:=10}
  status_code=$(curl -o /dev/null -w %{http_code} ${HOST_SERVER}:${HTTP_PORT})
done
echo "status_code:${status_code}"
echo "Connection to ${HOST_SERVER} on port ${HTTP_PORT} succeeded"


echo "Getting cob date for batch ${eodBatch}"
#echo "Executing curl: curl http://${HOST_SERVER}:${HTTP_PORT}/rest/odbatch/BatchJobCobDate/${eodBatch}"
#cobDate1=$(curl "http://${HOST_SERVER}:${HTTP_PORT}/rest/odbatch/BatchJobCobDate/${eodBatch}")
if [[ ${runT0} = "true" ]]
then
cobDate1=`cat ${MARS_RT_DIR}/job_valuation_T0_date.txt`
else
cobDate1=`cat ${MARS_RT_DIR}/job_valuation_date.txt`
fi

preCobDate=$(date -d "${cobDate1} -1 day" +%Y%m%d)

day_or_week=`date +%w`
if [[ "${day_or_week}" == 1 || "${day_or_week}" == 6 || "${day_or_week}" == 0 ]] ; then
  preCobDate="${cobDate1}"
else
  preCobDate="${preCobDate}"
fi

if [[ "${eodBatch}" =~ "XING_REVAL" ]];then
  last_modify_time=`stat -L -c %Y  ${MARS_RT_DIR}/job_valuation_date.txt`
  echo "last_modify_time: ${last_modify_time}"
  file_modify_date=`date '+%Y-%m-%d %H:%M:%S' -d @${last_modify_time}`
  echo "file_modify_date: ${file_modify_date}"
  limit_time=`date -d "${file_modify_date}  12 hour" +'%Y-%m-%d %H:%M:%S'`
  echo "limittime: ${limit_time}"
  current_time=$(date "+%Y-%m-%d %H:%M:%S")
  echo "current time:${current_time}"

  t1=`date -d "$file_modify_date" +%s`
  t2=`date -d "$limit_time" +%s`
  t3=`date -d "$current_time" +%s`
  if [[ $t3 -gt $t1 ]] && [[ $t3 -lt $t2 ]] ; then
    cobDate1="${preCobDate}"
    echo "batch Job Need change EOD to:${cobDate1}"
  fi
fi

echo "cob date: ${cobDate1}"

echo "running job ${eodBatch} at $(date)"
runBatch ${eodBatch} "${cobDate1}"
eodBatchState=$?
echo "EOD Risk for Batch ${eodBatch} has  batch state:${eodBatchState}"

if [[ ${eodBatchState} = 0 ]]
then
    echo "EOD Risk for Batch ${eodBatch} completed successfully at $(date). batch state:${eodBatchState}"

    if [[ ${runWithPaa} = "true" ]]
    then
        echo "About to trigger the PAA process for batch ${eodBatch}"
        runBatch ${paaRBJob}  "${cobDate1}"
        eodPaaRBBatchState=$?
        if [[ ${eodPaaRBBatchState} = 0 ]]
        then
            echo "PAA RB for Batch ${eodBatch}  completed successfully at $(date). batch state:${eodPaaRBBatchState}"
        else
            echo "PAA RB for Batch ${eodBatch}  failed at $(date). batch state:${eodPaaRBBatchState}"
        fi
        runBatch ${paaBFJob} "${cobDate1}"
        eodPaaBFBatchState=$?
        if [[ ${eodPaaBFBatchState} = 0 ]]
        then
            echo "PAA BF for Batch ${eodBatch}  completed successfully at $(date). batch state:${eodPaaBFBatchState}"
            exit 0
        else
            echo "PAA BF for Batch ${eodBatch}  failed at $(date). batch state:${eodPaaBFBatchState}"
            exit 1
        fi
    fi
else
    echo "EOD Risk for Batch ${eodBatch}  failed at $(date). batch state:${eodBatchState}"
    exit ${eodBatchState}
fi


