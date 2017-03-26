package com.crackUtil;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.Iterator;

/**
 * Created by AqCxBoM on 2017/3/26.
 */

public class KernelUtils {
    private static String TAG = "AqCxBoM";
    private static void LOG(String tag, Object obj)
    {
        Log.i(tag, (String)obj);
    }
    private static void LOG(Object obj)
    {
        Log.i(TAG, (String)obj);
    }
    //查找指定服务名
    public static boolean isExistService(Context context, String name) {
        boolean bRet = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        Iterator it = am.getRunningServices(0x7FFFFFFF).iterator();
        while (it.hasNext()) {
            ActivityManager.RunningServiceInfo rsi = (ActivityManager.RunningServiceInfo) it.next();
            if (rsi.service == null) {
                continue;
            }
            String className = rsi.service.getClassName().toString();
            //LOG(className);
            if (className.equals(name)) {


                bRet = true;
                LOG("MicroMsg.Util", "mAttributeId = " + name + " is running");
                break;
            }
        }
        if (!bRet)
            LOG("MicroMsg.Util", "mAttributeId=" + name + " is not running");
        return bRet;
    }
    public static void ergodicProcess(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        Iterator it = am.getRunningAppProcesses().iterator();
        while(it.hasNext()) {
            ActivityManager.RunningAppProcessInfo rapi = (ActivityManager.RunningAppProcessInfo)it.next();
            int nPid = rapi.pid;    //The pid of this process; 0 if none
            int nUid = rapi.uid;    //The user id of this process.
            //当前进程的所有包名 All packages that have been loaded into the process.
            String[] ppp = rapi.pkgList;
            //当前进程的包名
            String processName = rapi.processName;
            LOG("pid:" + nPid + " uid:" + nUid + " processName:" + processName);
        }
    }
    //检索是否存在指定包名的进程
    public static boolean isExistProcess(Context context, String Name) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        Iterator it = am.getRunningAppProcesses().iterator();
        while(it.hasNext()) {
            ActivityManager.RunningAppProcessInfo rapi = (ActivityManager.RunningAppProcessInfo)it.next();
            int nPid = rapi.pid;    //The pid of this process; 0 if none
            int nUid = rapi.uid;    //The user id of this process.
            //当前进程的所有包名 All packages that have been loaded into the process.
            String[] ppp = rapi.pkgList;
            //当前进程的包名
            String processName = rapi.processName;
            LOG("pid:" + nPid + " uid:" + nUid + " processName:" + processName);
            if (processName.equals(Name))
            {
                LOG("find match with processName: " + Name);
                return true;
            }
        }
        return false;
    }
}
