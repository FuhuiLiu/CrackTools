package com.crackUtil;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;

import android.os.Process;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * Created by AqCxBoM on 2017/3/26.
 */

public class SystemUtils {
    private static String TAG = "AqCxBoM";
    public static String mCPU_ABI;
    public static String mPPATH;
    private static void LOG(String tag, Object obj)
    {
        Log.i(tag, (String)obj);
    }
    private static void LOG(Object obj)
    {
        Log.i(TAG, (String)obj);
    }

    //检索已安装程序
    public static void listInstalledPackage(Context context)
    {
        //Object v1_2 = context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = context.getPackageManager();
        //通过PackageManager检查权限
        //pm.checkPermission()
        Iterator it = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES).iterator();
        while(it.hasNext()) {
            PackageInfo pi = (PackageInfo)it.next();
            LOG(pi.packageName);
        }
    }

    //通过检索指定目录是否存在su文件来判断机器是否有root权限
    public static boolean isRoot() {
        boolean bRet = true;
        String[] strAry = new String[]{"/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/"};
        int i = 0;
        try {
            while (i < strAry.length) {
                if (new File(strAry[i] + "su").exists()) {
                    bRet = true;
                    break;
                }
                ++i;
            }
        } catch (Exception v0_1) {
        }
        return bRet;
    }

    public static int getPPid(int pid) {
        int nResult = -1;
        //注意导入类 import android.os.Process;
        Class clsProc = Process.class;
        try {
            Method md = clsProc.getDeclaredMethod("getParentPid", Integer.TYPE);
            nResult = (int)md.invoke(null, pid);
        }
        catch(Throwable e) {
            e.printStackTrace();
        }

        return nResult;
    }
    //从 /proc/cpuinfo 中读取CPU信息
    public static String getCpuInfo() {
        BufferedReader br;
        FileReader fr;
        String str = null;
        try {
            fr = new FileReader("/proc/cpuinfo");
            br = new BufferedReader(fr, 1024);
            str = br.readLine();
            br.close();
            fr.close();
        }
        catch(FileNotFoundException v3) {
        }
        catch(IOException v6) {}

        String v8 = str != null ? str.substring(str.indexOf(':') + 1).trim() : "";

        return v8;
    }
    //获取app标签即程序名
    public static String getAppLabel(Context pContext) {
        try {
            PackageManager pm = pContext.getPackageManager();
            String pn = pContext.getPackageName();
            PackageInfo pi = pm.getPackageInfo(pn, PackageManager.GET_ACTIVITIES);
            String label = pm.getApplicationLabel(pi.applicationInfo).toString();
            return label;
        }
        catch(PackageManager.NameNotFoundException v0) {
            v0.printStackTrace();
        }

        return null;
    }
    //获取系统SDK版本
    public static int getSystemVersion() {
        int nVersion = 0;
        try {
            nVersion = Build.VERSION.class.getField("SDK_INT").getInt(null);
        }
        catch(Exception v1) {
            try {
                Object obj = Build.VERSION.class.getField("SDK").get(null);
                nVersion = Integer.parseInt((String)obj);
            }
            catch(Exception v2) {
                v2.printStackTrace();
            }
        }

        return nVersion;
    }
    //获取CPU架构信息
    public static String getCPUABI() {
        String cpuabi = "";
        try {
            if (new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop ro.product.cpu.abi")
                    .getInputStream())).readLine().contains("x86")) {
                cpuabi = "x86";
            } else
                cpuabi = "arm";
        } catch (Exception v1) {
        }
        return cpuabi;
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
