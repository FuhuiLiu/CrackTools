package com.encryptionOP;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by AqCxBoM on 2017/5/16.
 * 演示常见MD5加解密及数据储存
 */

public class EncryptionUtils {
    private static final char[] mHexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
            'b', 'c', 'd', 'e', 'f'};

    /**
     * 读取输出流，用key解密并存入输出流
     * @param is 输入流
     * @param os 输出流
     * @param keyStr key
     * @return 操作成功与否
     */
    public static boolean decodeFileContextAndSave(InputStream is, OutputStream os, String keyStr) {
        boolean bRet = false;
        try {
            int nLen = 8;
            IvParameterSpec ips = new IvParameterSpec(new byte[nLen]);
            byte[] md5Key = EncryptionUtils.getMD5Digest(keyStr.getBytes());
            byte[] byteAry = new byte[nLen];
            for(int i = 0; i < nLen; ++i) {
                byteAry[i] = md5Key[i];
            }

            SecretKeySpec key = new SecretKeySpec(byteAry, "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, ips);
            CipherOutputStream cos = new CipherOutputStream(os, cipher);
            byte[] byteAryOut = new byte[2048];
            int nRead;
            do {
                nRead = is.read(byteAryOut);
                if(nRead > 0) {
                    cos.write(byteAryOut, 0, nRead);
                }
            }
            while(nRead > 0);

            cos.flush();
            cos.close();
            bRet = true;
        }
        catch(Exception v1_1) {
            v1_1.printStackTrace();
        }
        return bRet;
    }
    /**
     * DES解密对应字符串
     * @param oldStr 目标被转成可视字符串的源
     * @param keyString 解密key
     * @return 对应UTF-8字符串或空
     */
    public static String decode2UTF8Str(String oldStr, String keyString) {
        int v6 = 8;
        String str = null;
        try {
            //先转成原始byteAry
            byte[] byteAry1 = EncryptionUtils.converString2byteAry(oldStr);
            IvParameterSpec ips = new IvParameterSpec(new byte[8]);
            //拿key字符串的md5摘要作加密key
            byte[] key = EncryptionUtils.getMD5Digest(keyString.getBytes());
            byte[] byteAry = new byte[8];
            int i;
            for(i = 0; i < v6; ++i) {
                byteAry[i] = key[i];
            }

            SecretKeySpec Secretkey = new SecretKeySpec(byteAry, "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, Secretkey, ips);
            byte[] byteAry2 = cipher.doFinal(byteAry1);
            return new String(byteAry2, "UTF-8");
        }
        catch(Exception v0) {
            v0.printStackTrace();
        }
        return "";
    }

    /**
     * 获取byteAry数组的MD5摘要
     * @param byteAry 目标数组
     * @return 摘要数组
     */
    protected static byte[] getMD5Digest(byte[] byteAry) {
        byte[] v0_1;
        try {
            v0_1 = MessageDigest.getInstance("MD5").digest(byteAry);
        }
        catch(Exception v0) {
            v0_1 = null;
        }

        return v0_1;
    }

    /**
     * 转换目标string为其byte数组
     * @param strArg 目标string
     * @return 对应byte数组
     */
    public static byte[] converString2byteAry(String strArg) {
        int nNeed = strArg.length() / 2;
        byte[] temp = new byte[nNeed];
        /**
         * 做类似的转换
         * "1cf098" ==> 0x1c 0xf0 0x98
         */
        for(int i = 0; i < nNeed; ++i) {
            //两两取出字符串数据并转成int值，取出其byte值保存
            String strSub = strArg.substring(i * 2, i * 2 + 2);
            Integer nValue = Integer.valueOf(strSub, 16);
            temp[i] = nValue.byteValue();
        }
        return temp;
    }
    /**
     * 读取输入流内所有内容到String返回
     * @param is 输入流对象
     * @return 空或目标数据
     */
    public static String read2UTF8String(InputStream is)
    {
        int nRead = 0;
        try
        {
            //读取文件内容
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] temp = new byte[2048];
            while (nRead != -1)
            {
                baos.write(temp, 0, nRead);
                nRead = is.read(temp);
            }
            //转成UTF-8内容
            return toUTF8String(baos.toByteArray());
        }
        catch (Exception e) {}
        return "";
    }

    /**
     * 将byteAry内所有数据作为UTF-8数据转换返回
     * @param byteAry 数据
     * @return 空或目标数据
     */
    private static String toUTF8String(byte[] byteAry)
    {
        String strRet = (byteAry == null) ? "" : byteAry2UTF8Str(byteAry, 0, byteAry.length);
        return strRet;
    }

    /**
     * 将byteAry内所有数据作为UTF-8数据转换返回
     * @param byteAry byteAry 数据
     * @param offset 起始位置
     * @param len 长度
     * @return 空或目标数据
     */
    private static String byteAry2UTF8Str(byte[] byteAry, int offset, int len)
    {
        String strRet = null;
        try
        {
            if (byteAry != null) {
                strRet = new String(byteAry, offset, len, "UTF-8");
            }
        }
        catch (UnsupportedEncodingException e)
        {
            strRet = "";
        }
        return strRet;
    }

    /**
     * DES加密对应字符串
     * @param oldStr 目标
     * @param keyString 解密key
     * @return 对应byte数组或空
     */
    public static byte[] encode2UTF8Str(String oldStr, String keyString) {
        int keyLen = 8;
        try {
            //string=>byteAry
            byte[] byteAry1 = oldStr.getBytes("UTF-8");
            IvParameterSpec ips = new IvParameterSpec(new byte[8]);
            //拿key字符串的md5摘要作加密key
            byte[] key = EncryptionUtils.getMD5Digest(keyString.getBytes());
            byte[] byteAry = new byte[keyLen];
            int i;
            for(i = 0; i < keyLen; ++i) {
                byteAry[i] = key[i];
            }

            SecretKeySpec Secretkey = new SecretKeySpec(byteAry, "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, Secretkey, ips);
            byte[] byteAry2 = cipher.doFinal(byteAry1);
            return byteAry2;
        }
        catch(Exception v0) {
            v0.printStackTrace();
        }
        return null;
    }
    /**
     * 挨个取出byteAry数组中的byte数据，转换成对应的字符（0-f）
     * @param byteAry 目标数组
     * @return 转换后的结果字符串
     */
    public static String converbyteAry2String(byte[] byteAry) {
//        int len = byteAry.length;
//        char[] charAry = new char[len << 1];
//        int nIndex = 0;
//        for(int i = 0; i < len; ++i) {
//            byte b = byteAry[i];
//            int b1 = (b & 0xf0);
//            int b2 = (b & 0xf);
//            charAry[nIndex++] = EncryptionUtils.mHexDigits[b1 >>> 4];//0
//            charAry[nIndex++] = EncryptionUtils.mHexDigits[b2];//1
//        }
//        return new String(charAry);

        StringBuilder sb = new StringBuilder();
        /**
         * 做类似的转换
         * 0x1c 0xf0 0x98 ==> "1cf098"
         */
        for (int i = 0; i < byteAry.length; i++)
        {
            byte b = byteAry[i];
            int nHight = b & 0xf0;
            int nLow = b & 0xf;
            sb.append(EncryptionUtils.mHexDigits[nHight >> 4]);
            sb.append(EncryptionUtils.mHexDigits[nLow]);
        }
        return sb.toString();
    }
}
