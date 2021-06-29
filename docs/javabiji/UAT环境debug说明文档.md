# UAT环境debug说明文档

### 1.默认release-1.7.0的代码拉取下来，是无法读取到uat的配置文件

### 2.修改bootstrap.yml成如下配置

将读取配置文件的的地址改为uat的配置中心地址（从eureka上看）

```yaml
spring:
  application:
    name: touchless-sdap
  cloud:
    config:
      name: public,redis,mongo,touchless-sdap,mysql
server:
  port: 14015
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
  mapper-locations: classpath:mapper/**.xml
---
spring:
  profiles: local
  cloud:
    config:
      uri: http://10.93.20.119:13001/
      label: sprint1.3
eureka:
  client:
    serviceUrl:
      defaultZone: http://nikeRegisterCenter:48RYUHrA@localhost:8761/eureka/
logging:
  config: classpath:logback-dev.xml
---
spring:
  profiles: dev
  cloud:
    config:
      discovery:
        enabled: true
        service-id: touchless-config
      label: dev
eureka:
  client:
    serviceUrl:
      defaultZone: http://cloudUser:dev@10.93.20.119:13001/eureka/
logging:
  config: classpath:logback-dev.xml
---
spring:
  profiles: test
  cloud:
    config:
      discovery:
        enabled: true
        service-id: touchless-config
      label: dev
eureka:
  client:
    serviceUrl:
      defaultZone: http://cloudUser:weAreExcellent@10.93.20.9:13001/eureka/
logging:
  config: classpath:logback-dev.xml
---
spring:
  profiles: test1
  cloud:
    config:
      discovery:
        enabled: true
        service-id: touchless-config
      label: dev
eureka:
  client:
    serviceUrl:
      defaultZone: http://cloudUser:local@localhost:13001/eureka/
logging:
  config: classpath:logback-dev.xml
---
spring:
  profiles: test2
  cloud:
    config:
      discovery:
        enabled: true
        service-id: touchless-config
      label: dev
eureka:
  client:
    serviceUrl:
      defaultZone: http://cloudUser:local@localhost:13001/eureka/
logging:
  config: classpath:logback-dev.xml
---
spring:
  profiles: test3
  cloud:
    config:
      discovery:
        enabled: true
        service-id: touchless-config
      label: dev
eureka:
  client:
    serviceUrl:
      defaultZone: http://cloudUser:local@localhost:13001/eureka/
logging:
  config: classpath:logback-dev.xml
#---
#spring:
#  profiles: uat
#  cloud:
#    config:
#      discovery:
#        enabled: true
#        service-id: touchless-config
#      label: release-1.7.0
#eureka:
#  client:
#    serviceUrl:
#      defaultZone: http://cloudUser:nonZeroSumGame@10.92.37.239:13001/eureka/,http://cloudUser:nonZeroSumGame@10.92.36.210:13001/eureka/,http://cloudUser:nonZeroSumGame@10.92.36.222:13001/eureka/,http://cloudUser:nonZeroSumGame@10.92.37.112:13001/eureka/,http://cloudUser:nonZeroSumGame@10.92.39.12:13001/eureka/
#logging:
#  config: classpath:logback-prod.xml
---
spring:
  profiles: uat
  cloud:
    config:
      #      discovery:
      #        enabled: true
      #        service-id: touchless-config
      uri: http://10.92.37.194:13011/
    #      label: sec-sprint2.5
    label: release-1.7.0
eureka:
  client:
    serviceUrl:
      #      defaultZone: http://cloudUser:dev@10.93.20.119:13001/eureka/
      defaultZone: http://localhost:8761/eureka/
logging:
  config: classpath:logback-dev.xml
---
spring:
  profiles: prod
  cloud:
    config:
      discovery:
        enabled: true
        service-id: touchless-config
      label: release-1.7.0
eureka:
  client:
    serviceUrl:
      defaultZone: http://cloudUser:EGamPan9dtpjfYQC@10.92.56.39:13001/eureka/,http://cloudUser:EGamPan9dtpjfYQC@10.92.56.156:13001/eureka/,http://cloudUser:EGamPan9dtpjfYQC@10.92.59.198:13001/eureka/,http://cloudUser:EGamPan9dtpjfYQC@10.92.57.228:13001/eureka/,http://cloudUser:EGamPan9dtpjfYQC@10.92.58.21:13001/eureka/
logging:
  config: classpath:logback-prod.xml

```

### 3.若希望在控制台看到日志，则还需要修改logback-dev.xml成如下配置：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>
    <!--log path use spring boot defualt :org/springframework/boot/logging/logback/defaults.xml-->
    <property name="LOG_PATH" value="${LOG_PATH:-.}" />

    <property name="LOG_FILE" value="touchless.log"/>

    <!-- console -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
<!--            <pattern>${CONSOLE_LOG_PATTERN}</pattern>-->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %level %-25.25thread %-35.35logger{35} %X{className:-*} %X{methodName:-*} %msg{}%n</pattern>
            <charset>utf8</charset>
        </encoder>
    </appender>

    <!-- daily file -->
    <appender name="info"
        class="ch.qos.logback.core.rolling.RollingFileAppender">
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <FileNamePattern>${LOG_PATH}/touchless_%d{yyyy-MM-dd}.log</FileNamePattern>
            <!--file store days -->
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <!--format -->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{30} - %msg%n</pattern>
        </encoder>
        <!--file max size -->
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <MaxFileSize>10MB</MaxFileSize>
        </triggeringPolicy>
        <filter class="ch.qos.logback.classic.filter.LevelFilter"><!-- just print ERROR level -->
			<level>INFO</level>
			<onMatch>ACCEPT</onMatch>
			<onMismatch>DENY</onMismatch>
		</filter>
    </appender>

    <appender name="debug"
        class="ch.qos.logback.core.rolling.RollingFileAppender">
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <FileNamePattern>${LOG_PATH}/touchless_%d{yyyy-MM-dd}.log</FileNamePattern>
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{30} - %msg%n</pattern>
        </encoder>
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <MaxFileSize>10MB</MaxFileSize>
        </triggeringPolicy>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
			<level>DEBUG</level>
			<onMatch>ACCEPT</onMatch>
			<onMismatch>DENY</onMismatch>
		</filter>
    </appender>

    <appender name="warn"
        class="ch.qos.logback.core.rolling.RollingFileAppender">
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <FileNamePattern>${LOG_PATH}/touchless_%d{yyyy-MM-dd}.log</FileNamePattern>
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{30} - %msg%n</pattern>
        </encoder>
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <MaxFileSize>10MB</MaxFileSize>
        </triggeringPolicy>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
			<level>WARN</level>
			<onMatch>ACCEPT</onMatch>
			<onMismatch>DENY</onMismatch>
		</filter>
    </appender>

    <appender name="error"
        class="ch.qos.logback.core.rolling.RollingFileAppender">
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <FileNamePattern>${LOG_PATH}/touchless_%d{yyyy-MM-dd}.log</FileNamePattern>
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{30} - %msg%n</pattern>
        </encoder>
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <MaxFileSize>10MB</MaxFileSize>
        </triggeringPolicy>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
			<level>ERROR</level>
			<onMatch>ACCEPT</onMatch>
			<onMismatch>DENY</onMismatch>
		</filter>
    </appender>
    	<logger name="com.nike.mapper" level="INFO"/>
    <appender name="all"
        class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_FILE}</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <FileNamePattern>${LOG_PATH}/touchless_%d{yyyy-MM-dd}_%i.log</FileNamePattern>
            <MaxFileSize>10MB</MaxFileSize>
            <MaxHistory>30</MaxHistory>
            <totalSizeCap>3GB</totalSizeCap>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
<!--            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{30} - %msg%n</pattern>-->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %level %-25.25thread %-35.35logger{35} %X{className:-*} %X{methodName:-*} %msg{}%n</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
			<level>INFO</level>
		</filter>
    </appender>
    <!-- this value append for application.yml,spring.profiles.active=dev -->

    <springProfile name="default">
        <root level="INFO">
            <appender-ref ref="STDOUT" />
            <appender-ref ref="all" />
        </root>
    </springProfile>
    <springProfile name="local">
        <root level="INFO">
	      <appender-ref ref="STDOUT" />
	      <appender-ref ref="all" />
	    </root>
    </springProfile>
    <springProfile name="dev">
        <root level="INFO">
	      <appender-ref ref="STDOUT" />
	      <appender-ref ref="all" />
	    </root>
	    <!-- show mybatis sql -->
	    <logger name="com.nike.mapper" level="debug" additivity="false">
            <appender-ref ref="STDOUT" />
        </logger>
    </springProfile>
    <springProfile name="test">
        <root level="INFO">
	      <appender-ref ref="STDOUT" />
	      <appender-ref ref="all" />
	    </root>
    </springProfile>
    <springProfile name="test1">
        <root level="INFO">
	      <appender-ref ref="STDOUT" />
	      <appender-ref ref="all" />
	    </root>
    </springProfile>
    <springProfile name="test2">
        <root level="INFO">
	      <appender-ref ref="STDOUT" />
	      <appender-ref ref="all" />
	    </root>
    </springProfile>
    <springProfile name="test3">
        <root level="INFO">
	      <appender-ref ref="STDOUT" />
	      <appender-ref ref="all" />
	    </root>
    </springProfile>
    <springProfile name="uat">
        <root level="INFO">
            <appender-ref ref="STDOUT" />
            <appender-ref ref="all" />
        </root>
        <!-- show mybatis sql -->
        <logger name="com.nike.mapper" level="debug" additivity="false">
            <appender-ref ref="STDOUT" />
        </logger>
    </springProfile>

</configuration>

```



