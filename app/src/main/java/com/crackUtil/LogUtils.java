package com.crackUtil;

/**
 * Created by AqCxBoM on 2017/1/19.
 */
import android.util.Log;

import java.util.Set;

public class LogUtils {
    public enum ELogSwitch{ DISABLE, ENABLE} //日志开关状态枚举类
    public enum ELogLevel{ //日志输出等级枚举类
        NORMAL, NORMAL2, VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT
    }
    private final static String mTAG = "AqCxBoM"; //默认TAG
    private final static String mCT = "Run here!"; //默认CT
    private final static String WARNING = "Here got a problem!"; //默认WARN字符串
    private final static String WARNINGPRE =
            "=====================================WARNINGPRE=====================================";
    private final static String WARNINGSUF =
            "=====================================WARNINGSUF=====================================";
    private static ELogSwitch mLogSwitch = ELogSwitch.ENABLE; //默认日志开关状态为开启
    private static ELogLevel mNLogLevel = ELogLevel.INFO; //默认日志输出等级为INFO

    private static void logi(String ct){ Log.i(mTAG, ct); }
    private static void logv(String ct){ Log.v(mTAG, ct); }
    private static void logw(String ct){ Log.w(mTAG, ct); }
    private static void loge(String ct){ Log.e(mTAG, ct); }
    private static void logd(String ct){ Log.d(mTAG, ct); }

    private static void LOGITAG(String tag,String ct){ Log.i(tag, ct); } //以INFO等级输出参数LOG，TAG
    private static void LOGVTAG(String tag,String ct){ Log.v(tag, ct); } //以VERBOSE等级输出参数LOG，TAG
    private static void LOGWTAG(String tag,String ct){ Log.w(tag, ct); } //以WARN等级输出参数LOG，TAG
    private static void LOGETAG(String tag,String ct){ Log.e(tag, ct); } //以ERROR等级输出参数LOG，TAG
    private static void LOGDTAG(String tag,String ct){ Log.d(tag, ct); } //以DEBUG等级输出参数LOG，TAG

    private static ELogLevel getCurLogLevel() { return mNLogLevel; } //获取当前Log输出等级
    private static ELogSwitch getLogSwitchStatus() { return mLogSwitch; }; //获取日志输出开关状态
    private static void setLogSwitchStatus(ELogSwitch switchNow) { mLogSwitch = switchNow; } //设置日志输出开关状态
    //返回当前日志开关是否关闭
    private static boolean isDisbleLog() { return getLogSwitchStatus() == ELogSwitch.DISABLE ? true : false; }
    //获取当前日志输出等是否过界
    private static boolean isOutLogLevel(ELogLevel lv) {
        return lv == ELogLevel.ASSERT || lv == ELogLevel.NORMAL2 ? true : false;
    }

    //以下为可供外界调用的接口
    //关闭日志输出功能
    public static void DisbleLog() { setLogSwitchStatus(ELogSwitch.DISABLE);}
    //设置日志输出等级
    public static void SetLogLevel(ELogLevel nLevel){ mNLogLevel = nLevel; }
    //重置日志输出等级为INFO
    public static void ResetLogLevel(){ mNLogLevel = ELogLevel.INFO;}
    //输出警告日志信息
    public static void DOWARNING() {
        DOLOGONCE(WARNING, ELogLevel.ERROR);
    }

    /**
        打印栈信息，输出格式
         =====================================WARNINGPRE=====================================
         调用栈信息
         =====================================WARNINGSUF=====================================
     */
    public static void PrintStack(){
        Exception ep = new Exception(mTAG);
        DOLOGONCE(WARNINGPRE, ELogLevel.ERROR);
        ep.printStackTrace();
        DOLOGONCE(WARNINGSUF, ELogLevel.ERROR);
    }

    //以INFO等级输出参数LOG，TAG为AqCxBoM
    public static void LOGI(String ct){ logi(ct); }
    //以VERBOSE等级输出参数LOG，TAG为AqCxBoM
    public static void LOGV(String ct){ logv(ct); }
    //以WARN等级输出参数LOG，TAG为AqCxBoM
    public static void LOGW(String ct){ logw(ct); }
    //以ERROR等级输出参数LOG，TAG为AqCxBoM
    public static void LOGE(String ct){ loge(ct); }
    //以DEBUG等级输出参数LOG，TAG为AqCxBoM
    public static void LOGD(String ct){ logd(ct); }

    //打印Index
    public static void LOGPATH(int nIndex){ DOLOG("Current Index: " + nIndex); }
    public static void LOGPATH(String tag, int nIndex){ DOLOG(tag, "Current Index: " + nIndex); }

    /**
     * 输出默认日志，格式为 AqCxBoM: "Run here!"
     */
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
    /**
     * 输出默认TAG日志，格式为 AqCxBoM: ct
     */
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
    /**
     * 输出指定日志，格式为 tag: ct
     */
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

    /**
     * 以指定TAG\CT\LOGLEVEL，输出日志（日志等级一次性）
     * @param tag
     * @param ct
     * @param lv
     */
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

    /**
     * 以默认TAG，指定CT\LOGLEVEL，输出日志（日志等级一次性）
     * @param ct
     * @param lv
     */
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