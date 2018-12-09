package com.lh.hgmall.util;

public class Log4jUtil {
    public static String getDebugInfo(){
        StackTraceElement[] lvStacks=Thread.currentThread().getStackTrace();
        return  "Class Name："+lvStacks[3].getClassName() + ",Function Name：" + lvStacks[3].getMethodName()
                +",Line："+ lvStacks[3].getLineNumber() + "\n";
    }
}
