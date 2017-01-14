package com.crackUtil;

import android.util.Log;

/**
 * Created by AqCxBoM on 2016/12/26.
 */

public class LogUtils {
    public enum ELogLevel{
        NORMAL, NORMAL2, VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT
    }
    private final static String TAG = "AqCxBoM";
    private final static String CT = "Run here!";
    private static ELogLevel nILogLevel = ELogLevel.INFO;
    private static void logi(String ct){ Log.i(TAG, ct); }
    private static void logv(String ct){ Log.v(TAG, ct); }
    private static void logw(String ct){ Log.w(TAG, ct); }
    private static void loge(String ct){ Log.e(TAG, ct); }
    private static void logd(String ct){ Log.d(TAG, ct); }

    //以下为可供外界调用的接口
    public static void SetLogLevel(ELogLevel nLevel){ nILogLevel = nLevel; }
    public static void ResetLogLevel(){ nILogLevel = ELogLevel.INFO;}

    public static void PrintStack(){
        Exception ep = new Exception(TAG);
        ep.printStackTrace();
    }

    public static void LOGI(String ct){ logi(ct); }
    public static void LOGV(String ct){ logv(ct); }
    public static void LOGW(String ct){ logw(ct); }
    public static void LOGE(String ct){ loge(ct); }
    public static void LOGD(String ct){ logd(ct); }

    public static void LOGITAG(String tag,String ct){ Log.i(tag, ct); }
    public static void LOGVTAG(String tag,String ct){ Log.v(tag, ct); }
    public static void LOGWTAG(String tag,String ct){ Log.w(tag, ct); }
    public static void LOGETAG(String tag,String ct){ Log.e(tag, ct); }
    public static void LOGDTAG(String tag,String ct){ Log.d(tag, ct); }

    public static void LOGPATH(int nIndex){ DOLOG("Current Index: " + nIndex); }
    public static void LOGTAGPATH(String tag, int nIndex){ DOLOG(tag, "Current Index: " + nIndex); }

    public static void DOLOG(){
        if (nILogLevel == ELogLevel.ASSERT || nILogLevel == ELogLevel.NORMAL )
            return;
        else
            switch(nILogLevel){
            case INFO: LOGI(CT); break;
            case VERBOSE: LOGV(CT); break;
            case WARN: LOGW(CT); break;
            case ERROR: LOGE(CT); break;
            case DEBUG: LOGD(CT); break; }
    }
    public static void DOLOG(String ct){
        if (nILogLevel == ELogLevel.ASSERT || nILogLevel == ELogLevel.NORMAL )
            return;
        else
            switch(nILogLevel){
            case INFO: LOGI(ct); break;
            case VERBOSE: LOGV(ct); break;
            case WARN: LOGW(ct); break;
            case ERROR: LOGE(ct); break;
            case DEBUG: LOGD(ct); break; }
    }

    public static void DOLOG(String tag,String ct){
        if (nILogLevel == ELogLevel.ASSERT || nILogLevel == ELogLevel.NORMAL )
            return;
        else
            switch(nILogLevel){
            case INFO: LOGITAG(tag, ct); break;
            case VERBOSE: LOGVTAG(tag, ct); break;
            case WARN: LOGWTAG(tag, ct); break;
            case ERROR: LOGETAG(tag, ct); break;
            case DEBUG: LOGDTAG(tag, ct); break; }
    }
}