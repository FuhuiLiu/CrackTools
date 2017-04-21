package com.crackUtil;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.text.format.Formatter;
import android.util.Log;

import android.os.Process;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
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
    //查找指定服务名 遍历服务
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
    public static String getOSName()
    {
        return System.getProperty("os.name");
    }
    public static String getVM_Version()
    {
        String vmVersion = System.getProperty("java.vm.version");
        return vmVersion;
    }
    public static String getCurrentRuntimeValue()
    {
        String SELECT_RUNTIME_PROPERTY = "persist.sys.dalvik.vm.lib";
        String LIB_DALVIK = "libdvm.so";
        String LIB_ART = "libart.so";
        String LIB_ART_D = "libartd.so";
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            try {
                Method get = systemProperties.getMethod("get",
                        String.class, String.class);
                if (get == null) {
                    return "WTF?!";
                }
                try {
                    final String value = (String)get.invoke(
                            systemProperties, SELECT_RUNTIME_PROPERTY,
                        /* Assuming default is */"Dalvik");
                    if (LIB_DALVIK.equals(value)) {
                        return "Dalvik";
                    } else if (LIB_ART.equals(value)) {
                        return "ART";
                    } else if (LIB_ART_D.equals(value)) {
                        return "ART debug build";
                    }

                    return value;
                } catch (IllegalAccessException e) {
                    return "IllegalAccessException";
                } catch (IllegalArgumentException e) {
                    return "IllegalArgumentException";
                } catch (InvocationTargetException e) {
                    return "InvocationTargetException";
                }
            } catch (NoSuchMethodException e) {
                return "SystemProperties.get(String key, String def) method is not found";
            }
        } catch (ClassNotFoundException e) {
            return "SystemProperties class is not found";
        }
    }
    public static String getAvailMemory(Context context) {// 获取android当前可用内存大小

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存

        return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
    }

    public static String getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");
//            for (String num : arrayOfString) {
//                Log.i(str2, num + "\t");
//            }

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (IOException e) {
        }
        return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }
}
