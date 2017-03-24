package com.crackUtil;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import java.security.MessageDigest;

/**
 * Created by AqCxBoM on 2017/3/24.
 */

public class AppSign {
    public static String MD5Code;

    public static String getSign(Context context)
    {
        String packname = context.getPackageName();
        Signature[] arrayOfSignature = getRawSignature(context, packname);
        if ((arrayOfSignature == null) || (arrayOfSignature.length == 0))
        {
            Log.i("ysj", "signs is null");
        }
        else
        {
            int i = arrayOfSignature.length;
            for (int j = 0; j < i; j++) {
                MD5Code = getMessageDigest(arrayOfSignature[j].toByteArray());
            }
        }
        return MD5Code;
    }

    private static Signature[] getRawSignature(Context paramContext, String paramString)
    {
        if ((paramString == null) || (paramString.length() == 0)) {
            return null;
        }
        PackageInfo localPackageInfo;
        PackageManager localPackageManager = paramContext.getPackageManager();
        try
        {
            localPackageInfo = localPackageManager.getPackageInfo(paramString, PackageManager.GET_SIGNATURES);
            if (localPackageInfo == null) {
                return null;
            }
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
            return null;
        }
        return localPackageInfo.signatures;
    }

    public static final String getMessageDigest(byte[] paramArrayOfByte)
    {
        char[] arrayOfChar1 = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        char[] arrayOfChar2 = null;
        try
        {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            int i = arrayOfByte.length;
            arrayOfChar2 = new char[i * 2];
            int j = 0;
            int k = 0;
            for (j = 0; j < i; j++)
            {
                int m = arrayOfByte[j];
                int n = k + 1;
                //右移指令优先&指令
                arrayOfChar2[k] = arrayOfChar1[(0xF & m >>> 4)];
                k = n + 1;
                arrayOfChar2[n] = arrayOfChar1[(m & 0xF)];
            }
            return new String(arrayOfChar2);
        }
        catch (Exception localException) {
        }

        return null;
    }

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
