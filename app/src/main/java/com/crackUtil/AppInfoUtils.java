package com.crackUtil;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.List;

/**
 * Created by AqCxBoM on 2017/3/24.
 */

public class AppInfoUtils {
    public static String MD5Code;

    //获取当前包版本信息 PackageInfo还有很多与APP信息相关字段
    public static String getAppVersion(Context ct)
    {
        PackageManager pm = ct.getPackageManager();
        String packName = ct.getPackageName();
        try {
            PackageInfo pi = pm.getPackageInfo(packName, PackageManager.GET_CONFIGURATIONS);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
    //获取当前APK的签名并对其取MD5值字符串
    public static String getSign(Context context)
    {
        //获取包名
        String packname = context.getPackageName();
        //获取签名
        Signature[] arrayOfSignature = getRawSignature(context, packname);
        if ((arrayOfSignature == null) || (arrayOfSignature.length == 0))
        {
            Log.i("ysj", "signs is null");
        }
        else
        {
            //取签名拿MD5值
            int i = arrayOfSignature.length;
            for (int j = 0; j < i; j++) {
                //对签名求MD5
                MD5Code = getMessageDigest(arrayOfSignature[j].toByteArray());
            }
        }
        return MD5Code;
    }

    public static String getAppSignature(Context context, String appPackageName) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iter = apps.iterator();
        if (appPackageName != null && !appPackageName.isEmpty()) {
            while (iter.hasNext()) {
                PackageInfo packageinfo = iter.next();
                String packageName = packageinfo.packageName;
                if (packageName.contains(appPackageName)) {
                    String Sig = packageinfo.signatures[0].toCharsString();
                    String MD5 = getMessageDigest(Sig.getBytes());
                    LogUtils.DOLOG(appPackageName + " SigMD5: ", MD5);
                    LogUtils.DOLOG(appPackageName + " Sig: ", Sig);
                    return packageName + " SigMD5: " + MD5 + "\n" + "Sig: " + Sig;
                }
            }
        }
        return null;
    }

    public static void showAppSignature(Context context, String appPackageName) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iter = apps.iterator();
        if (appPackageName != null) {
            while (iter.hasNext()) {
                PackageInfo packageinfo = iter.next();
                String packageName = packageinfo.packageName;
                if (packageName.equals(appPackageName)) {
                    String Sig = packageinfo.signatures[0].toCharsString();
                    LogUtils.DOLOG(appPackageName + " Sig: ", Sig);
                    LogUtils.DOLOG(appPackageName + " SigMD5: ", getMessageDigest(Sig.getBytes()));
                    return;
                }
            }
        } else {
            while (iter.hasNext()) {
                PackageInfo packageinfo = iter.next();
                String packageName = packageinfo.packageName;
                String Sig = packageinfo.signatures[0].toCharsString();
                LogUtils.DOLOG(packageName + " Sig: ", Sig);
                LogUtils.DOLOG(packageName + " SigMD5: ", getMessageDigest(Sig.getBytes()));
            }
        }
    }

    private static Signature[] getRawSignature(Context context, String packageName)
    {
        if ((packageName == null) || (packageName.length() == 0)) {
            return null;
        }
        PackageInfo localPackageInfo;
        //获取PackageManager
        PackageManager localPackageManager = context.getPackageManager();
        try
        {
            //获取PackageInfo
            localPackageInfo = localPackageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            //返回签名数组
            if (localPackageInfo != null) {
                return localPackageInfo.signatures;
            }
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
        }
        return null;
    }

    //取byteAry的MD5字符串
    public static final String getMessageDigest(byte[] ByteAry)
    {
        char[] arrayOfChar1 = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        char[] arrayOfChar2;
        try
        {
            //获取MD5实例
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(ByteAry);
            //对参数byteAry求MD5值
            byte[] arrayOfByte = localMessageDigest.digest();
            int i = arrayOfByte.length;
            arrayOfChar2 = new char[i * 2];
            int k = 0;
            for (int j = 0; j < i; j++)
            {
                //按byte数组挨个取出
                int m = arrayOfByte[j];
                int n = k + 1;
                //右移指令优先&指令
                //取字节高4位，拿对应字符数组的字符
                arrayOfChar2[k] = arrayOfChar1[(0xF & m >> 4)];
                k = n + 1;
                //取字节低4位，拿对应字符数组的字符
                arrayOfChar2[n] = arrayOfChar1[(0xF & m)];
                //0xd2 => "d2"
            }
            return new String(arrayOfChar2);
        }
        catch (Exception localException) {
        }

        return null;
    }

    //返回字符串的MD5摘要
    public static final byte[] getRawDigest(byte[] paramArrayOfByte)
    {
        try
        {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            return localMessageDigest.digest();
        }
        catch (Exception localException) {}
        return null;
    }
}
