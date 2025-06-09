package java8;


public class LogUtil {

    public static void logCaller() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // 第4个元素是logCaller方法的调用，第5个元素才是真正的调用者
        for (int i = 0; i < stackTrace.length; i++) {
            if(i > 0) {
                System.out.println(stackTrace[i].getFileName()+ stackTrace[i].getMethodName()+ stackTrace[i].getLineNumber());
            }
        }
        StackTraceElement callerElement = stackTrace[stackTrace.length-2];
        System.out.println("Caller method: " + callerElement.getFileName()+ callerElement.getMethodName()+ callerElement.getLineNumber());

    }

    public static void main(String[] args) {
        methodA();
    }

    public static void methodA() {
        logCaller();
    }
}
