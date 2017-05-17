#include "jni.h"
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/system_properties.h> // getAndroidDeviceID_Serial
#include "md5.h"
#include "ObfuscationDefin.h"

#ifndef COMMON_H_
#define COMMON_H_

//函数参数说明
#define IN
#define OUT
/*
 * Toast使用到的常量定义
 */
#define LENGTH_SHORT 0;
#define LENGTH_LONG 1;

#include <android/log.h>
#ifndef LOGCAT_TAG
#define ENABLE_LOGCAT true
#define LOGCAT_LEVEL ANDROID_LOG_INFO
#define LOGCAT_TAG "AqCxBoM"
#define MYLOGE(...) if(ENABLE_LOGCAT) __android_log_print(ANDROID_LOG_ERROR, LOGCAT_TAG, __VA_ARGS__)
#define MYLOGI(...) if(ENABLE_LOGCAT) __android_log_print(LOGCAT_LEVEL, LOGCAT_TAG, __VA_ARGS__)
#define LOGI(...) if(ENABLE_LOGCAT) __android_log_print(LOGCAT_LEVEL, LOGCAT_TAG, __VA_ARGS__)
#define LOGE(...) if(ENABLE_LOGCAT) __android_log_print(LOGCAT_LEVEL, LOGCAT_TAG, __VA_ARGS__)
#define LOGW(...) if(ENABLE_LOGCAT) __android_log_print(LOGCAT_LEVEL, LOGCAT_TAG, __VA_ARGS__)
#define ALOG(...) if(ENABLE_LOGCAT) __android_log_print(LOGCAT_LEVEL, __VA_ARGS__)
#define MYLOG(...) \
  if(ENABLE_LOGCAT) \
    __android_log_print(LOGCAT_LEVEL, __VA_ARGS__)
#endif

extern "C" jstring JNICALL stringFromJNI (IN JNIEnv *pEnv, IN jobject obj);
extern "C" int registerNativeMethods(IN JNIEnv *env, IN const char *className,
                                     IN JNINativeMethod *gMethods, IN int numMethods);
extern "C" jfieldID getFieldID(IN JNIEnv *env, IN jclass cls, IN const char *name, IN const char *sig, IN bool isStatic);
extern "C" jmethodID getMethodID(IN JNIEnv* env, IN jclass cls, IN const char *funName, IN const char *sig, IN bool isStatic);
extern "C" jclass findAppClass(IN JNIEnv *jenv, IN const char *apn);
//获取Context对象
extern "C" jobject getGlobalContext(IN JNIEnv *env);

extern "C" bool getDeviceID_Serial(OUT char *deviceID);//serial number
//SimSerialNumber
extern "C" bool getSimSerialNumber(IN JNIEnv *env, OUT char *pOut);
//获取AndroidID
extern "C" bool getAndroidID(IN JNIEnv *env, OUT char *pBufOut);
//getIMSI
extern "C" bool getIMSI(IN JNIEnv *env, OUT char *pOut);
//getDeviceID(设备ID)
extern "C" bool getIMEI(IN JNIEnv *env, OUT char *pOut);
//获取签名信息
extern "C" bool getSelfMD5Sig(IN JNIEnv *env, OUT char *pBufOut);
//获取AndroidManifrest.xml配置Application中metaData定义的指定名的字段的纯数字
extern "C" bool getIntMetaDate(IN JNIEnv *env, IN jstring metaName, OUT char *pOut);
//获取AndroidManifrest.xml配置Application中metaData定义的指定名的string字段
extern "C" bool getStringMetaDate(IN JNIEnv *env, IN jstring metaName, OUT char *pOut);
//获取当前APP的包名
extern "C" bool getPackageName(IN JNIEnv *env, OUT char *pBufOut);

extern "C" void showSelfSig(IN JNIEnv *env);

//获取毫秒数信息
extern "C" long long getUnixTime();
//获取对应字符串的MD5摘要
extern "C" bool getMD5(IN char *pStr, OUT char *pMD5);
//转换int为对应进制数据
extern "C" char* itoa_my(IN long long value,OUT char *pBufOut,IN int radix);
#endif
