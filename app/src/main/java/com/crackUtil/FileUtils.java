package com.crackUtil;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.dynamic.IDynamic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import aqcxbom.cracktools.MainActivity;
import dalvik.system.DexClassLoader;

/**
 * Created by AqCxBoM on 2017/3/26.
 */

public class FileUtils {
    private static String TAG = "AqCxBoM";

    public static void dyLoad(Context ct) {
        //获取要动态加载的dex包路径，这个加载包必须经过dx优化
        //context.getFilesDir() => /data/data/com.example.testdemo/files
        String fullPath = ct.getDir("firstpaylibs", 0).getAbsolutePath() + File.separator + "DynamicTest_dx.jar";
        //调用DexClassLoader加载我们的dex文件
        //Environment.getExternalStorageDirectory().toUTF8String() 4.1.2后App不能直接从sdcard中加载字节码
        String tempPath = ct.getDir("dyLoad", Context.MODE_PRIVATE).getAbsolutePath();
        DexClassLoader cl = new DexClassLoader(fullPath, tempPath, null, ct.getClassLoader());
        Class libProviderClass;
        try {
            //动态加载我们的类并调用
            libProviderClass = cl.loadClass("com.dynamic.DynamicTest");
            IDynamic lib = (IDynamic) libProviderClass.newInstance();
            Toast.makeText(MainActivity.mActivity, lib.getHelloWorld(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //释放jar文件到我们的app目录
    public static void releaseLib(Context ct) {
        int i = 0;
        AssetManager amg = ct.getAssets();
        try {
            //获取assets/firstpay/libs目录下的文件名列表
            String[] strAry = amg.list("firstpay/libs");
            int nFileSize = strAry.length;
            if (nFileSize <= 0) {
                Log.i(TAG, "path no found!");
                return;
            }
            //将assets/firstpay/libs目录下的所有文件都保存到app/app_firstpaylibs/目录下
            while (i < nFileSize) {
                //挨个取目录名
                String str = strAry[i];
                //获取路径 ==> /data/data/aqcxbom.cracktools/app_firstpaylibs/firstpay.jar
                String fullPath = ct.getDir("firstpaylibs", 0).getAbsolutePath() + File.separator + str;
                //如果存在就先删除该文件
                if (new File(fullPath).exists()) {
                    new File(fullPath).delete();
                }
                //保存文件到目录
                saveFile(amg.open("firstpay/libs/" + str), fullPath);
                i++;
                Log.i(TAG, fullPath);
            }
        } catch (IOException v0_1) {
            Log.i(TAG, v0_1.toString());
            v0_1.printStackTrace();
        }
        return;
    }

    //保存文件
    private static void saveFile(InputStream is, String fullPath) {
        int size = 1024;
        try {
            byte[] byteAry = new byte[size];
            FileOutputStream fos = new FileOutputStream(fullPath);
            while (true) {
                int readSize = is.read(byteAry);
                if (readSize == -1) {
                    break;
                }
                fos.write(byteAry, 0, readSize);
            }
            is.close();
            fos.flush();
            fos.close();
        } catch (Exception v0_1) {
            v0_1.printStackTrace();
            return;
        }
    }

    //遍历Zip文件 这个在干嘛？META-INF/????
    public static String readchannelfile(Context ct, String name) {
        ZipFile zipfile = null;
        ApplicationInfo ai = ct.getApplicationInfo();
        //context.getFilesDir() => /data/data/com.example.testdemo/files
        //获取自身APK包存放路径 ==> /data/app/aqcxbom.cracktools-2.apk
        //类似的功能ai.dataDir ==> /data/data/aqcxbom.craktools
        String path = ai.sourceDir;
        //合成目标文件名前缀
        String findName = "META-INF/" + name;
        String retStr = "";
        try {
            //给定路径以zip方式打开
            zipfile = new ZipFile(path);
            //获取所有的目录总概
            Enumeration en = zipfile.entries();
            //循环遍历
            while (en.hasMoreElements()) {
                //获取文件名
                path = ((ZipEntry) en.nextElement()).getName();
                if (!path.startsWith(findName))
                    continue;
                //分割文件名
                String[] v2_1 = path.split("_");
                return v2_1 == null || v2_1.length < 2 ? retStr : path.substring(v2_1[0].length() + 1);
            }
        } catch (Throwable v0_1) {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException v1_1) {
                    v1_1.printStackTrace();
                }
            }
        }

        return "";
    }

    //读取assets\payconfig
    public static void readFile(Context ct) {
        try {
            //读取文件内容
            int nRead;
            InputStream is = ct.getAssets().open("payconfig");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] byteAry = new byte[1024];
            do {
                nRead = is.read(byteAry);
                if (nRead > 0) {
                    baos.write(byteAry, 0, nRead);
                }
            }
            while (nRead > 0);
            String s1 = baos.toString();
            byte[] s2 = baos.toByteArray();
            String s3 = new String(s2, "UTF-8");
            String ss = "";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取metaData数据
    public static void getMetaData(Context ct) {
        try {
            PackageManager pm = ct.getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(
                    ct.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo.metaData != null) {
                boolean mMetaDataSDK_CMGAME_d = applicationInfo.metaData.getBoolean("FILTER_DISABLE", false);
                String mMetaDataCMGAME_EXIT_e = applicationInfo.metaData.getString("WRAP_SDK");
                String ss = "";
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
