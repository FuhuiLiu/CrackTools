package aqcxbom.cracktools;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.JSONTest.myJson;
import com.crackUtil.AppInfoUtils;
import com.crackUtil.GZipUtils;
import com.crackUtil.PhoneUtils;
import com.crackUtil.SystemUtils;
import com.crackUtil.LogUtils;
import com.encryptionOP.EncryptionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.crackUtil.PhoneUtils.TYPE_CMOBILE;
import static com.crackUtil.PhoneUtils.TYPE_CTELNET;
import static com.crackUtil.PhoneUtils.TYPE_CUION;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    public static Activity mActivity;
    private static EditText mEditTextPackName;
    private static TextView mTextViewSigInfo;
    private static Button mBtnGetInfo;
    private static Button mBtnGetSig;
    private static Button mBtnTest;
    private static Button mBtnClickTest;
    private static Button mBtnGenerateLink;

    public static native void JNITest();
    public static native void JNITest(Button btnClickBtn);
    public native String getSring();
    static{
        System.loadLibrary("native");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        mTextViewSigInfo = (TextView) findViewById(R.id.text);
        //mTextViewSigInfo.setMovementMethod(ScrollingMovementMethod.getInstance());
        mEditTextPackName = (EditText)findViewById(R.id.package_edt);
        mBtnGetSig = (Button)findViewById(R.id.btnGetSig);
        mBtnGetSig.setOnClickListener(this);
        mBtnGetInfo = (Button)findViewById(R.id.btnInfo);
        mBtnGetInfo.setOnClickListener(this);
        mBtnTest = (Button)findViewById(R.id.btnTest);
        mBtnTest.setOnClickListener(this);
        mBtnClickTest = (Button)findViewById(R.id.btnClickTestBtn);
        mBtnClickTest.setOnClickListener(this);
        mBtnGenerateLink = (Button)findViewById(R.id.btnGenerateLink);
        mBtnGenerateLink.setOnClickListener(this);
    }
    private static void btnTest()
    {
        LogUtils.DOLOG("测试按钮被点击！");
        JSONUsage();
    }
    private static void btnClickTest()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JNITest(mBtnTest);
            }
        }).start();
    }
    private static void btnGenerateLink()
    {
        try {
            String appKey = mEditTextPackName.getText().toString();
            if (appKey == null || appKey.isEmpty())
            {
                mTextViewSigInfo.setText("输入AppKey");
                return;
            }
            String FMT = "sdk_version=%s&reason=%d&apk_key=%s&apk_keyso=%s&package_name=%s&apk_sig=%s&android_id=%s&imei=%s&imsi=%s&sim=%s&timestamp=%s";
            String MD5VERIFYCODE = "d577dd1a3489655f63a58d1a52a8e0b9"; //与版本有关的MD5固定串
            //获取app签名MD5+固定串求其MD5摘要并转成String
            String appsig = AppInfoUtils.getSign(mActivity);
            String appsig2 = appsig + MD5VERIFYCODE;
            byte[] sig2Digest = MessageDigest.getInstance("MD5").digest(appsig2.getBytes());
            String strsig2Digest = EncryptionUtils.converbyteAry2String(sig2Digest);
            //生成固定验证格式请求
            String baseData = String.format(FMT.toLowerCase(), 0.2, 0, appKey, appKey, AppInfoUtils.getPackageName(mActivity), strsig2Digest, PhoneUtils.getAndroidID(mActivity),
                    PhoneUtils.getIMEI(mActivity), PhoneUtils.getIMSI(mActivity), PhoneUtils.getSimSerialNumber(mActivity), System.currentTimeMillis());
            //对生成固定验证+固定串再求其MD5摘要并转成String
            String baseData2 = baseData + MD5VERIFYCODE;
            byte[] baseData2Digest = MessageDigest.getInstance("MD5").digest(baseData2.getBytes());
            String strbaseData2Digest = EncryptionUtils.converbyteAry2String(baseData2Digest);
            //结果拼到固定验证后面以sig标识，形成最终的验证串
            String baseDataResult = String.format("%s&sig=%s", baseData, strbaseData2Digest);
            //拼上远程网址并显示
            String link = "http://cnsdk.zhuqueok.com/ab_api/plugin.php?"+baseDataResult;
            mTextViewSigInfo.setText(link);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    private static void GZipTestfun()
    {
        String strTest = "66fe4d02dc7a0b5240373c12429b19d68e5d2b92795d579121c4465f3d1bfa90f94360e2ac76179e10c606457da165723b3b808f0dcdf09edf5bb8d95bef6282478f2ad9a0f8f79fa15e4475512c71cf23a0fc77ef417703c5e7792ffef8a75dd7c77c90fb51aa98da82cf08f613b543c3157699d31d994dfa62f0a108745c0ee56778a1b67ca4844de7be7df9cfa2136629d3cc210a0d9282f2f456aa51c7f0a4f7475a2a667b1345e1caae146ce8da0cee9c0dd2ea2cfb7df98ebdc510567b9fb7e2cbaf35b820aa7502c73767135be83212a27fb5ccc51c710c338b3b3aeae04724f3eea57200c74d50dc637272c6176a477c4a8d42db85dd21a1fc3ffb44b9ae2abe9b9a249ee2f5fbca4f166e80e8d34aa8461a5a4c178a265df34e4e18a5e3560deb487b86";

        byte[] arrOutput2 = { 0x36, 0x36, 0x66, 0x65, 0x34, 0x64, 0x30, 0x32, 0x64, 0x63, 0x37, 0x61, 0x30, 0x62, 0x35, 0x32, 0x34, 0x30, 0x33, 0x37, 0x33, 0x63, 0x31, 0x32, 0x34, 0x32, 0x39, 0x62, 0x31, 0x39, 0x64, 0x36, 0x38, 0x65, 0x35, 0x64, 0x32, 0x62, 0x39, 0x32, 0x37, 0x39, 0x35, 0x64, 0x35, 0x37, 0x39, 0x31, 0x32, 0x31, 0x63, 0x34, 0x34, 0x36, 0x35, 0x66, 0x33, 0x64, 0x31, 0x62, 0x66, 0x61, 0x39, 0x30, 0x66, 0x39, 0x34, 0x33, 0x36, 0x30, 0x65, 0x32, 0x61, 0x63, 0x37, 0x36, 0x31, 0x37, 0x39, 0x65, 0x31, 0x30, 0x63, 0x36, 0x30, 0x36, 0x34, 0x35, 0x37, 0x64, 0x61, 0x31, 0x36, 0x35, 0x37, 0x32, 0x33, 0x62, 0x33, 0x62, 0x38, 0x30, 0x38, 0x66, 0x30, 0x64, 0x63, 0x64, 0x66, 0x30, 0x39, 0x65, 0x64, 0x66, 0x35, 0x62, 0x62, 0x38, 0x64, 0x39, 0x35, 0x62, 0x65, 0x66, 0x36, 0x32, 0x38, 0x32, 0x34, 0x37, 0x38, 0x66, 0x32, 0x61, 0x64, 0x39, 0x61, 0x30, 0x66, 0x38, 0x66, 0x37, 0x39, 0x66, 0x61, 0x31, 0x35, 0x65, 0x34, 0x34, 0x37, 0x35, 0x35, 0x31, 0x32, 0x63, 0x37, 0x31, 0x63, 0x66, 0x32, 0x33, 0x61, 0x30, 0x66, 0x63, 0x37, 0x37, 0x65, 0x66, 0x34, 0x31, 0x37, 0x37, 0x30, 0x33, 0x63, 0x35, 0x65, 0x37, 0x37, 0x39, 0x32, 0x66, 0x66, 0x65, 0x66, 0x38, 0x61, 0x37, 0x35, 0x64, 0x64, 0x37, 0x63, 0x37, 0x37, 0x63, 0x39, 0x30, 0x66, 0x62, 0x35, 0x31, 0x61, 0x61, 0x39, 0x38, 0x64, 0x61, 0x38, 0x32, 0x63, 0x66, 0x30, 0x38, 0x66, 0x36, 0x31, 0x33, 0x62, 0x35, 0x34, 0x33, 0x63, 0x33, 0x31, 0x35, 0x37, 0x36, 0x39, 0x39, 0x64, 0x33, 0x31, 0x64, 0x39, 0x39, 0x34, 0x64, 0x66, 0x61, 0x36, 0x32, 0x66, 0x30, 0x61, 0x31, 0x30, 0x38, 0x37, 0x34, 0x35, 0x63, 0x30, 0x65, 0x65, 0x35, 0x36, 0x37, 0x37, 0x38, 0x61, 0x31, 0x62, 0x36, 0x37, 0x63, 0x61, 0x34, 0x38, 0x34, 0x34, 0x64, 0x65, 0x37, 0x62, 0x65, 0x37, 0x64, 0x66, 0x39, 0x63, 0x66, 0x61, 0x32, 0x31, 0x33, 0x36, 0x36, 0x32, 0x39, 0x64, 0x33, 0x63, 0x63, 0x32, 0x31, 0x30, 0x61, 0x30, 0x64, 0x39, 0x32, 0x38, 0x32, 0x66, 0x32, 0x66, 0x34, 0x35, 0x36, 0x61, 0x61, 0x35, 0x31, 0x63, 0x37, 0x66, 0x30, 0x61, 0x34, 0x66, 0x37, 0x34, 0x37, 0x35, 0x61, 0x32, 0x61, 0x36, 0x36, 0x37, 0x62, 0x31, 0x33, 0x34, 0x35, 0x65, 0x31, 0x63, 0x61, 0x61, 0x65, 0x31, 0x34, 0x36, 0x63, 0x65, 0x38, 0x64, 0x61, 0x30, 0x63, 0x65, 0x65, 0x39, 0x63, 0x30, 0x64, 0x64, 0x32, 0x65, 0x61, 0x32, 0x63, 0x66, 0x62, 0x37, 0x64, 0x66, 0x39, 0x38, 0x65, 0x62, 0x64, 0x63, 0x35, 0x31, 0x30, 0x35, 0x36, 0x37, 0x62, 0x39, 0x66, 0x62, 0x37, 0x65, 0x32, 0x63, 0x62, 0x61, 0x66, 0x33, 0x35, 0x62, 0x38, 0x32, 0x30, 0x61, 0x61, 0x37, 0x35, 0x30, 0x32, 0x63, 0x37, 0x33, 0x37, 0x36, 0x37, 0x31, 0x33, 0x35, 0x62, 0x65, 0x38, 0x33, 0x32, 0x31, 0x32, 0x61, 0x32, 0x37, 0x66, 0x62, 0x35, 0x63, 0x63, 0x63, 0x35, 0x31, 0x63, 0x37, 0x31, 0x30, 0x63, 0x33, 0x33, 0x38, 0x62, 0x33, 0x62, 0x33, 0x61, 0x65, 0x61, 0x65, 0x30, 0x34, 0x37, 0x32, 0x34, 0x66, 0x33, 0x65, 0x65, 0x61, 0x35, 0x37, 0x32, 0x30, 0x30, 0x63, 0x37, 0x34, 0x64, 0x35, 0x30, 0x64, 0x63, 0x36, 0x33, 0x37, 0x32, 0x37, 0x32, 0x63, 0x36, 0x31, 0x37, 0x36, 0x61, 0x34, 0x37, 0x37, 0x63, 0x34, 0x61, 0x38, 0x64, 0x34, 0x32, 0x64, 0x62, 0x38, 0x35, 0x64, 0x64, 0x32, 0x31, 0x61, 0x31, 0x66, 0x63, 0x33, 0x66, 0x66, 0x62, 0x34, 0x34, 0x62, 0x39, 0x61, 0x65, 0x32, 0x61, 0x62, 0x65, 0x39, 0x62, 0x39, 0x61, 0x32, 0x34, 0x39, 0x65, 0x65, 0x32, 0x66, 0x35, 0x66, 0x62, 0x63, 0x61, 0x34, 0x66, 0x31, 0x36, 0x36, 0x65, 0x38, 0x30, 0x65, 0x38, 0x64, 0x33, 0x34, 0x61, 0x61, 0x38, 0x34, 0x36, 0x31, 0x61, 0x35, 0x61, 0x34, 0x63, 0x31, 0x37, 0x38, 0x61, 0x32, 0x36, 0x35, 0x64, 0x66, 0x33, 0x34, 0x65, 0x34, 0x65, 0x31, 0x38, 0x61, 0x35, 0x65, 0x33, 0x35, 0x36, 0x30, 0x64, 0x65, 0x62, 0x34, 0x38, 0x37, 0x62, 0x38, 0x36 };
        String inputStr = "zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org";
        try {
            String a2 = new String(arrOutput2);
            inputStr = a2;
            System.err.println("原文:\t" + inputStr);

            byte[] input = inputStr.getBytes();
            System.err.println("长度:\t" + input.length);

            byte[] data = GZipUtils.compress(input);
            System.err.println("压缩后:\t");
            System.err.println("长度:\t" + data.length);

            String strOut = new String(data);
            System.err.println("数据:\t" + strOut);

            byte[] output = GZipUtils.decompress(data);
            String outputStr = new String(output);
            System.err.println("解压缩后:\t" + outputStr);
            System.err.println("长度:\t" + output.length);

            int n = 0;
        }catch(IOException e){
            e.printStackTrace();
        }
        catch(Exception ee){
            ee.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        hideSoftInputKeyboard();
        switch(view.getId())
        {
        case R.id.btnInfo:
            getHardWareInfo();
            break;
        case R.id.btnGetSig:
            getSig();
            break;
        case R.id.btnTest:
            btnTest();
            break;
        case R.id.btnClickTestBtn:
            btnClickTest();
            break;
        case R.id.btnGenerateLink:
            btnGenerateLink();
            break;
        default:
        }
    }
    private void getSig(){
        String packName = mEditTextPackName.getText().toString();
        String sigInfo = AppInfoUtils.getAppSignature(mActivity, packName);
        if(sigInfo == null)
        {
            sigInfo = "没有这个包名";
            Toast.makeText(mActivity, sigInfo, Toast.LENGTH_SHORT).show();
        }
        mTextViewSigInfo.setText(sigInfo);
    }
    private void getHardWareInfo()
    {
        String str;
        str = "CurrentRuntimeValue: " + SystemUtils.getCurrentRuntimeValue();
        str += '\n';
        str += "AndroidID: " + PhoneUtils.getAndroidID(MainActivity.this);
        str += '\n';
        str += "IMEI: " + PhoneUtils.getIMEI(MainActivity.this);
        str += '\n';
        str += "IMSI: " + PhoneUtils.getIMSI(MainActivity.this);
        str += '\n';
        str += "AvailMemory: " + SystemUtils.getAvailMemory(MainActivity.this);
        str += '\n';
        str += "TotalMemory: " + SystemUtils.getTotalMemory(MainActivity.this);
        str += '\n';
        str += "SimExist: " + PhoneUtils.isSimExist(MainActivity.this);
        str += '\n';
        str += "SimUsable: " + PhoneUtils.getSimUsable(MainActivity.this);
        str += '\n';
        str += "SimSerialNumber: " + PhoneUtils.getSimSerialNumber(MainActivity.this);
        str += '\n';
        int nType = PhoneUtils.getProvidersType(MainActivity.this);
        String strType = "0";
        if(nType == TYPE_CMOBILE)
            strType = "移动";
        else if(nType == TYPE_CTELNET)
            strType = "电信";
        else if(nType == TYPE_CUION)
            strType = "联通";
        else
            strType = "未知";
        str += "ProvidersType: " + strType;
        str += '\n';
        str += "SimCountryIso: " + PhoneUtils.getSimCountryIso(MainActivity.this);
        str += '\n';
        str += "SimNumber: " + PhoneUtils.getLine1Number(MainActivity.this);
        str += '\n';
        str += "NetworkOperator: " + PhoneUtils.getNetworkOperator(MainActivity.this);
        str += '\n';
        str += "MacAddress: " + PhoneUtils.getMacAddress(MainActivity.this);
        str += '\n';
        str += "isOnline: " + PhoneUtils.isOnline(MainActivity.this);
        str += '\n';
        str += "SystemVersion: " + SystemUtils.getSystemVersion();
        str += '\n';
        str += "CPUABI: " + SystemUtils.getCPUABI();
        str += '\n';
        str += "CpuInfo: " + SystemUtils.getCpuInfo();
        str += '\n';
        str += "isRoot: " + SystemUtils.isRoot();
        str += '\n';
        str += "VM_Version: " + SystemUtils.getVM_Version();
        str += '\n';
        str += "OSName: " + SystemUtils.getOSName();
        str += '\n';
        mTextViewSigInfo.setText(str);
        LogUtils.DOLOG(str);
    }
    private void hideSoftInputKeyboard()
    {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    // json生成加密
    private static void JSONUsage(){
        String ct = myJson.initJson();
        byte[] result = EncryptionUtils.encode2UTF8Str(ct, "sdk_config");
        String str = EncryptionUtils.converbyteAry2String(result);
        LogUtils.DOLOG(str);

        String crypt = "5f91a0e4fd77c641d2b904415b65669963806d7b239b7192ba2634dad108b4cfc635c4dbf87af6353818c683c3be5d01ed1ac2264e48cdd633bcec4217b9e89c6c05405a87bce93c67cf48e24658c05aa73a33df60293593c41dd29553db8282005c9b8663cdfa238f2e19f59cc0a62ae38768148e4e42981ddebc6304b71273d3b9b07d6c3ffb1e862123420a8b862c2879e72b33733037593f8f97670af72a5e87d0d142e7ff8e4b16b19b34dd726de62fc10d16a27d1313160f70ebcd41c44b8bc9564148b793d4f726caf43f6aa7485dc7a2bb835e0a863ec5345051b4038ee1d8c1bfc97f0867de8686baeca92988f96658a713254bd7b84f92f497cefe14a2c21a2608d57a41875187a822c4fccf4cca66cdd3869a641a712e98fe7b701273e9362b179f57e0298c2dc2687b4b33068100e5fee9d6358caccc7c207861e0d464e9fdc9586cc3dd09dc75c82fb03e510e0b5866edc5fcb303b56cf3e9cca865454f944c5d47a712c5cc2b106633da590faad6b061aa985901f19113286077b9f0c3140b5541";
        String decrypt = EncryptionUtils.decode2UTF8Str(crypt, "sdk_config");
        LogUtils.DOLOG(decrypt);
    }
    //加解密使用示例
    private static void EncryptionUsage()
    {
        try {
            InputStream is = MainActivity.mActivity.getAssets().open("cmnraw/data_pay_base");
            String strSavePath = MainActivity.mActivity.getFilesDir().getAbsoluteFile() + File.separator + "sdk_pay_base";
            FileOutputStream fos = new FileOutputStream(strSavePath);
            EncryptionUtils.decodeFileContextAndSave(is, fos, "sdk_config");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            InputStream is = MainActivity.mActivity.getAssets().open("cmnraw/config.xml");
            String read = EncryptionUtils.read2UTF8String(is);
            String decode = EncryptionUtils.decode2UTF8Str(read, "sdk_config");
            LogUtils.DOLOG(decode);

            byte[] byteAry = EncryptionUtils.encode2UTF8Str(decode, "sdk_config");
            String enResult = EncryptionUtils.converbyteAry2String(byteAry);
            LogUtils.DOLOG(enResult);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
