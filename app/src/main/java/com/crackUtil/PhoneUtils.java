package com.crackUtil;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.Process;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * Created by AqCxBoM on 2017/3/24.
 * 必须在AndroidManifest.xml中声明权限
 * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
 * 否则IMSI IMEI 无法读取返回空值
 */

public class PhoneUtils {
    public static final int TYPE_CMOBILE = 1;   //移动
    public static final int TYPE_CTELNET = 3;   //电信
    public static final int TYPE_CUION = 2;     //联通
    public static final int TYPE_NONE = 0;      //未知
//    public static String wifimac(Context arg6) {
//        Object v0_1;
//        try {
//            WifiManager wm = (WifiManager) arg6.getSystemService(Context.WIFI_SERVICE);
//            List v5 = wm.getScanResults();
//            int v4;
//            for(v4 = 0; v4 < v5.size(); ++v4) {
//                int v3;
//                for(v3 = 1; v3 < v5.size(); ++v3) {
//                    if(((ScanResults)v5.get(v4)).level < v5.get(v3).level) {
//                        v0_1 = v5.get(v4);
//                        v5.set(v4, v5.get(v3));
//                        v5.set(v3, v0_1);
//                    }
//                }
//            }
//
//            StringBuffer v3_1 = new StringBuffer();
//            Iterator v4_1 = v5.iterator();
//            int v1 = 0;
//            while(v4_1.hasNext()) {
//                v0_1 = v4_1.next();
//                if(v1 > 4) {
//                    break;
//                }
//
//                ++v1;
//                v3_1.append(((ScanResult)v0_1).BSSID).append(",");
//            }
//
//            if(v3_1.length() <= 0) {
//                return "";
//            }
//
//            String v0_2 = v3_1.substring(0, v3_1.length() - 1).replace(":", "");
//            return v0_2;
//        }
//        catch(Exception v0) {
//        }
//
//        return "";
//    }
    //获取Wifi MAC地址 <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    public static String getMacAddress(Context arg2) {
        String mac;
        try {
            WifiManager wm = (WifiManager) arg2.getSystemService(Context.WIFI_SERVICE);
            mac = wm.getConnectionInfo().getMacAddress();
            if(mac != null && !mac.equals("")) {
                return mac;
            }

            mac = "unknown";
        }
        catch(Exception v0) {
            mac = "unknown";
        }

        return mac;
    }

    //获取IMEI码，结果与getIMEI一样，适用于双卡机？
    public static String getSubscriberId(Context context, int nIndex) {
        String v0 = nIndex == 1 ? "iphonesubinfo2" : "iphonesubinfo";
        try {
            Method methodGetService = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String
                    .class);
            methodGetService.setAccessible(true);
            Object ser = methodGetService.invoke(null, v0);
            if(ser == null && nIndex == 1) {
                ser = methodGetService.invoke(null, "iphonesubinfo1");
            }

            if(ser == null) {
                throw new Exception();
            }

            methodGetService = Class.forName("com.android.internal.telephony.IPhoneSubInfo$Stub").getDeclaredMethod(
                    "asInterface", IBinder.class);
            methodGetService.setAccessible(true);
            ser = methodGetService.invoke(null, ser);
            v0 = (String)ser.getClass().getMethod("getIMSI").invoke(ser);
        }
        catch(Exception v0_1) {
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            v0 = tm.getSubscriberId();
        }

        return v0;
    }
    //获取手机卡运营商类型
    public static int getProvidersType(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            //获取手机卡运营商号码
            String opCode = tm.getNetworkOperator();
            //如果返回空或者不是以460开头
            if(TextUtils.isEmpty(opCode) || !opCode.startsWith("460")) {
                opCode = tm.getSimOperator();
                if(TextUtils.isEmpty(opCode) || !opCode.startsWith("460")) {
                    String v0 = PhoneUtils.getIMSI(context);
                    if(TextUtils.isEmpty(v0)) {
                        return TYPE_NONE;
                    }
                    else {
                        opCode = v0.substring(0, 5);
                    }
                }

                if(TextUtils.isEmpty(opCode)) {
                    return TYPE_NONE;
                }

            }
            switch(Integer.parseInt(opCode)) {
                case 46001: {
                    return TYPE_CUION;
                }
                case 46003: {
                    return TYPE_CTELNET;
                }
                case 46005: {
                    return TYPE_CTELNET;
                }
                case 46006: {
                    return TYPE_CUION;
                }
                case 460001: {
                    return TYPE_CUION;
                }
                case 46000:
                case 46002:
                case 46007:
                case 460000:
                case 460002: {
                    return TYPE_CMOBILE;
                }
                case 460003: {
                    return TYPE_CTELNET;
                }
                default:
                    return TYPE_NONE;
            }
        }
        catch(Exception v1) {
            v1.printStackTrace();
        }
        return TYPE_NONE;
    }
    //手机号(有些手机号无法获取，是因为运营商在SIM中没有写入手机号)
    public static String getLine1Number(Context context) {
        String v1 = "";
        try {
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            v1 = tm.getLine1Number();
        }
        catch(Exception v0) {
        }
        return v1;
    }
    //获取手机SIM卡的序列号
    public static String getSimSerialNumber(Context context) {
        String v1 = "";
        try {
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            v1 = tm.getSimSerialNumber();
        }
        catch(Exception v0) {
        }
        return v1;
    }
    //获取ISO国家码，相当于提供SIM卡的国家码。
    public static String getSimCountryIso(Context context) {
        String v1 = "";
        try {
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            v1 = tm.getSimCountryIso();
        }
        catch(Exception v0) {
        }
        return v1;
    }
    //手机串号:GSM手机的 IMEI 和 CDMA手机的 MEID.
    public static String getAndroidID(Context context) {
        String android_id = "";
        try {
            android_id = android.provider.Settings.Secure.getString(context.getContentResolver(), "android_id");
        }
        catch(Exception v0) {
        }
        return android_id;
    }
    //手机串号:GSM手机的 IMEI 和 CDMA手机的 MEID.
    public static String getIMEI(Context context) {
        String v1 = "";
        try {
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            v1 = tm.getDeviceId();
        }
        catch(Exception v0) {
        }
        return v1;
    }
    //获取客户id，在gsm中是imsi号
    public static String getIMSI(Context context) {
        String SubId = "";
        int v5 = 15;
        try {
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            //获取客户id，在gsm中是imsi号
            SubId = tm.getSubscriberId();

            if(!TextUtils.isEmpty(SubId) && (SubId.startsWith("460"))) {
                return SubId;
            }
            //如果 上面的返回不足于代表手机卡的运营商状态
            //运营商名称,注意：仅当用户已在网络注册时有效,在CDMA网络中结果也许不可靠
            SubId = PhoneUtils.getNetworkOperator(context);
            if((TextUtils.isEmpty(SubId)) || !SubId.startsWith("460")) {
                SubId = "46000";
            }

            SubId = SubId + PhoneUtils.randomImsi(context);
            if(SubId.length() < v5) {
                SubId = SubId + "000000000000000";
            }

            if(SubId.length() > v5) {
                SubId = SubId.substring(0, 15);
            }

        }
        catch(Exception v0) {
        }
        return SubId;
    }
    //运营商名称,注意：仅当用户已在网络注册时有效,在CDMA网络中结果也许不可靠
    public static String getNetworkOperator(Context context) {
        String v1;
        try {
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            v1 = tm.getNetworkOperator();
        }
        catch(Exception v0) {
            v1 = "";
        }

        if(v1 == null) {
            v1 = "";
        }

        return v1;
    }
    private static String randomImsi(Context context) {
        try {
            //手机串号:GSM手机的 IMEI 和 CDMA手机的 MEID.
            String v3 = PhoneUtils.getIMEI(context);
            StringBuffer v0 = new StringBuffer();
            int v2;
            for(v2 = 0; v2 < v3.length(); ++v2) {
                char v1 = v3.charAt(v2);
                if(!Character.isDigit(v1)) {
                    v0.append("0");
                }
                else {
                    v0.append(v1);
                }
            }

            String v4_1 = v0.toString();
            return v4_1;
        }
        catch(Exception v4) {
            return "2561158629";
        }
    }
    //检测是否存在手机卡
    public static boolean isSimExist(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.hasIccCard();
    }
    //判断上网状态，不管是WIFI还是蜂窝
    public static boolean isOnline(Context context) {
        ConnectivityManager cm  = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo v1 = cm.getActiveNetworkInfo();
        boolean v2 = v1 == null || !v1.isConnected() ? false : true;
        return v2;
    }
    //取得sim的状态，是否可用
    public static boolean getSimUsable(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            if(tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
                return true;
            }
//            StringBuffer inf = new StringBuffer();
//            switch(tm.getSimState()){ //getSimState()取得sim的状态  有下面6中状态
//                case TelephonyManager.SIM_STATE_ABSENT :inf.append("无卡");return inf.toUTF8String();
//                case TelephonyManager.SIM_STATE_UNKNOWN :inf.append("未知状态");return inf.toUTF8String();
//                case TelephonyManager.SIM_STATE_NETWORK_LOCKED :inf.append("需要NetworkPIN解锁");return inf.toUTF8String();
//                case TelephonyManager.SIM_STATE_PIN_REQUIRED :inf.append("需要PIN解锁");return inf.toUTF8String();
//                case TelephonyManager.SIM_STATE_PUK_REQUIRED :inf.append("需要PUK解锁");return inf.toUTF8String();
//                case TelephonyManager.SIM_STATE_READY :break;
            }
        catch(Exception v3) {
        }

        return false;
    }

    //判断当前是否在主线程
    public static boolean isMainProcess(Context context) {
        int nMyPid = Process.myPid();
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        Iterator it = am.getRunningAppProcesses().iterator();
        while(it.hasNext()) {
                ActivityManager.RunningAppProcessInfo rpi = (ActivityManager.RunningAppProcessInfo)it.next();
                if(rpi.pid != nMyPid) {
                    continue;
                }

                if(TextUtils.isEmpty(rpi.processName)) {
                    continue;
                }

                if(rpi.processName.contains(":")) {
                    continue;
                }

                return true;
        }

        return false;
    }
}
