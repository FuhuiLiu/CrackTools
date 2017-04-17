#include "jni.h"
#include <stdio.h>
#include <stdlib.h>
#include <sys/system_properties.h> // getAndroidDeviceID_Serial
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

extern "C" jstring JNICALL stringFromJNI (JNIEnv *pEnv, jobject obj);
extern "C" int registerNativeMethods(JNIEnv *env, const char *className,
                                     JNINativeMethod *gMethods, int numMethods);
extern "C" jfieldID getFieldID(JNIEnv *env, jclass cls, const char *name, const char *sig, bool isStatic);
extern "C" jmethodID getMethodID(JNIEnv* env, jclass cls, const char *funName, const char *sig, bool isStatic);
extern "C" jclass findAppClass(JNIEnv *jenv, const char *apn);
extern "C" jobject getGlobalContext(JNIEnv *env);

extern "C" bool getPackageName(IN JNIEnv *env, OUT char *pBufOut);
extern "C" bool getDeviceID_Serial(char *deviceID);//serial number
extern "C" bool getSubscriberId(JNIEnv *env, char* pOut);
extern "C" bool getDeviceID(JNIEnv *env, char* pOut);


extern "C" void showSelfSig(JNIEnv *env);
#endif
