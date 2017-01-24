package com.crackUtil;

/**
 * Created by AqCxBoM on 2017/1/19.
 */
import android.util.Log;

import java.util.Set;

public class LogUtils {
    public enum ELogSwitch{ DISABLE, ENABLE}
    public enum ELogLevel{
        NORMAL, NORMAL2, VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT
    }
    private final static String mTAG = "AqCxBoM";
    private final static String mCT = "Run here!";
    private final static String WARNING = "Here got some program!";
    private final static String WARNINGPRE =
            "=====================================WARNINGPRE=====================================";
    private final static String WARNINGSUF =
            "=====================================WARNINGSUF=====================================";
    private static ELogSwitch mLogSwitch = ELogSwitch.ENABLE;
    private static ELogLevel mNLogLevel = ELogLevel.INFO;
    private static void logi(String ct){ Log.i(mTAG, ct); }
    private static void logv(String ct){ Log.v(mTAG, ct); }
    private static void logw(String ct){ Log.w(mTAG, ct); }
    private static void loge(String ct){ Log.e(mTAG, ct); }
    private static void logd(String ct){ Log.d(mTAG, ct); }
    private static ELogLevel getCurLogLevel() { return mNLogLevel; }
    private static ELogSwitch isLogSwitchStatus() { return mLogSwitch; };
    private static void setLogSwitchStatus(ELogSwitch switchNow) { mLogSwitch = switchNow; }
    private static boolean isDisbleLog() { return isLogSwitchStatus() == ELogSwitch.DISABLE ? true : false; }
    private static boolean isOutLogLevel(ELogLevel lv) {
        return lv == ELogLevel.ASSERT || lv == ELogLevel.NORMAL2 ? true : false;
    }

    //以下为可供外界调用的接口
    public static void DisbleLog() { setLogSwitchStatus(ELogSwitch.DISABLE);}
    public static void SetLogLevel(ELogLevel nLevel){ mNLogLevel = nLevel; }
    public static void ResetLogLevel(){ mNLogLevel = ELogLevel.INFO;}
    public static void DOWARNING() {
        DOLOGONCE(WARNING, ELogLevel.ERROR);
        //PrintStack();
    }

    public static void PrintStack(){
        Exception ep = new Exception(mTAG);
        DOLOGONCE(WARNINGPRE, ELogLevel.ERROR);
        ep.printStackTrace();
        DOLOGONCE(WARNINGSUF, ELogLevel.ERROR);
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
        if (!isOutLogLevel(getCurLogLevel()) && !isDisbleLog()){
            switch(getCurLogLevel()){
            case INFO: LOGI(mCT); break;
            case VERBOSE: LOGV(mCT); break;
            case WARN: LOGW(mCT); break;
            case ERROR: LOGE(mCT); break;
            case DEBUG: LOGD(mCT); break; }
        }
    }
    public static void DOLOG(String ct){
        if (!isOutLogLevel(getCurLogLevel()) && !isDisbleLog()){
            switch(getCurLogLevel()){
            case INFO: LOGI(ct); break;
            case VERBOSE: LOGV(ct); break;
            case WARN: LOGW(ct); break;
            case ERROR: LOGE(ct); break;
            case DEBUG: LOGD(ct); break; }
        }
    }

    public static void DOLOG(String tag,String ct){
        if (!isOutLogLevel(getCurLogLevel()) && !isDisbleLog()){
            switch(getCurLogLevel()){
            case INFO: LOGITAG(tag, ct); break;
            case VERBOSE: LOGVTAG(tag, ct); break;
            case WARN: LOGWTAG(tag, ct); break;
            case ERROR: LOGETAG(tag, ct); break;
            case DEBUG: LOGDTAG(tag, ct); break; }
        }
    }
    public static void DOLOGONCE(String tag, String ct, ELogLevel lv){
        ELogLevel curLv = getCurLogLevel();
        SetLogLevel(lv);
        if (!isOutLogLevel(getCurLogLevel()) && !isDisbleLog()){
            switch(getCurLogLevel()){
            case INFO: LOGITAG(tag, ct); break;
            case VERBOSE: LOGVTAG(tag, ct); break;
            case WARN: LOGWTAG(tag, ct); break;
            case ERROR: LOGETAG(tag, ct); break;
            case DEBUG: LOGDTAG(tag, ct); break; }
        }
        SetLogLevel(curLv);
    }
    public static void DOLOGONCE(String ct, ELogLevel lv){
        ELogLevel curLv = getCurLogLevel();
        SetLogLevel(lv);
        if (!isOutLogLevel(getCurLogLevel()) && !isDisbleLog()){
            switch(getCurLogLevel()){
            case INFO: LOGI(ct); break;
            case VERBOSE: LOGV(ct); break;
            case WARN: LOGW(ct); break;
            case ERROR: LOGE(ct); break;
            case DEBUG: LOGD(ct); break; }
        }
        SetLogLevel(curLv);
    }
}