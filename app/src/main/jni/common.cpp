//
// Created by Administrator on 2017/4/10.
//
#include "common.h"

JNIEXPORT jstring JNICALL stringFromJNI (JNIEnv *pEnv, jobject obj)
{
    return pEnv->NewStringUTF("Hello from C++");
}

/*
 * 清除调用jni函数可能出现的异常有则清除并返回true
 */
bool ClearException(JNIEnv *jenv){
    jthrowable exception = jenv->ExceptionOccurred(); //检测是否有异常产生
    if (exception != NULL) {        //如果有异常产生则清除并返回true
        jenv->ExceptionDescribe();
        jenv->ExceptionClear();
        return true;
    }
    return false;
}

int registerNativeMethods(JNIEnv *env, const char *className,
                                 JNINativeMethod *gMethods, int numMethods) {
    jclass clazz;
    clazz = env->FindClass(className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        return JNI_FALSE;
    }
    return JNI_TRUE;
}


//获取域ID
jfieldID getFieldID(JNIEnv *env, jclass cls, const char *name, const char *sig, bool isStatic)
{
    jfieldID fieldID = NULL;
    jfieldID getMethodID = NULL;
    if(isStatic)
        fieldID = env->GetStaticFieldID(cls, name, sig);
    else
        fieldID = env->GetFieldID(cls, name, sig);

    return !ClearException(env) ? fieldID : 0;
}
/*
 * 获取对应方法的ID
 */
jmethodID getMethodID(JNIEnv* env, jclass cls, const char *funName, const char *sig, bool isStatic)
{
//  if(ENABLE_LOGCAT)
//  {
//    MYLOGI("getMethodID funName:%s",funName);
//    MYLOGI("getMethodID sig:%s",sig);
//    MYLOGI("getMethodID isStatic:%s",isStatic == true ? "true" : "false");
//  }
    jmethodID retMethodID = NULL;
    if(isStatic)
        retMethodID =env->GetStaticMethodID(cls, funName, sig);
    else
        retMethodID = env->GetMethodID(cls, funName, sig);

    return !ClearException(env) ? retMethodID : NULL;
}

#define MAXCLASSNAMEBUFLEN 0x50
/*
 * 函数功能：加载对应名称的类并返回该类
 * 参数： jenv JNIEnv*指针
 *       const char * apn 目标类路径（参数类似于这种形式：aqcxbom/KernelUtils）
 */
jclass findAppClass(JNIEnv *jenv, const char *apn) {
    char adjustApn[MAXCLASSNAMEBUFLEN];
    if(!apn)
    {
        ALOG(LOGCAT_TAG, "ClassName empty!!!");
        return NULL;
    }
    else
    {
        int nLen = strlen(apn);
        strcpy(adjustApn, apn);
        if(nLen > MAXCLASSNAMEBUFLEN)
        {
            MYLOGI("Adjust ClassNameBuf Overload!!!");
            //抛个异常最好
            return NULL;
        }
        for(int i = 0; i < nLen; i++)
        {
            if(apn[i] == '.')
                adjustApn[i] = '/';
        }
    }

    jclass cls = jenv->FindClass(adjustApn);
    if (!ClearException(jenv)) {
        MYLOGI("jenv->FindClass found: %p", cls);
        return cls;
    }

    //获取ApplicationLoaders
    jclass clasApplicationLoaders = jenv->FindClass(
            "android/app/ApplicationLoaders");
    jthrowable exception = jenv->ExceptionOccurred();
    if (ClearException(jenv)) {
        ALOG("Exception", "No class : %s", "android/app/ApplicationLoaders");
        return NULL;
    }

    jfieldID fieldApplicationLoaders = jenv->GetStaticFieldID(
            clasApplicationLoaders, "gApplicationLoaders",
            "Landroid/app/ApplicationLoaders;");
    if (ClearException(jenv)) {
        ALOG("Exception", "No Static Field :%s", "gApplicationLoaders");
        return NULL;
    }
    jobject objApplicationLoaders = jenv->GetStaticObjectField(
            clasApplicationLoaders, fieldApplicationLoaders);
    if (ClearException(jenv)) {
        ALOG("Exception", "GetStaticObjectField is failed [%s]",
             "gApplicationLoaders");
        return NULL;
    }
    jfieldID fieldLoaders = jenv->GetFieldID(clasApplicationLoaders, "mLoaders", "Landroid/util/ArrayMap;");
    if (ClearException(jenv)) {
        fieldLoaders = jenv->GetFieldID(clasApplicationLoaders, "mLoaders", "Ljava/util/Map;");
        if(ClearException(jenv)){
            ALOG("Exception", "No Field :%s", "mLoaders");
            return NULL;
        }
    }
    jobject objLoaders = jenv->GetObjectField(objApplicationLoaders,
                                              fieldLoaders);
    if (ClearException(jenv)) {
        ALOG("Exception", "No object :%s", "mLoaders");
        return NULL;
    }

    jclass classHashMap = jenv->GetObjectClass(objLoaders);
    jmethodID methodValues = jenv->GetMethodID(classHashMap, "values",
                                               "()Ljava/util/Collection;");
    jobject values = jenv->CallObjectMethod(objLoaders, methodValues);

    jclass classValues = jenv->GetObjectClass(values);
    jmethodID methodToArray = jenv->GetMethodID(classValues, "toArray",
                                                "()[Ljava/lang/Object;");
    if (ClearException(jenv)) {
        ALOG("Exception", "No Method:%s", "toArray");
        return NULL;
    }

    jobjectArray classLoaders = (jobjectArray) jenv->CallObjectMethod(values,
                                                                      methodToArray);
    if (ClearException(jenv)) {
        ALOG("Exception", "CallObjectMethod failed :%s", "toArray");
        return NULL;
    }

    int size = jenv->GetArrayLength(classLoaders);

    for (int i = 0; i < size; i++) {
        jobject classLoader = jenv->GetObjectArrayElement(classLoaders, i);
        jclass clazzCL = jenv->GetObjectClass(classLoader);
        jmethodID loadClass = jenv->GetMethodID(clazzCL, "loadClass",
                                                "(Ljava/lang/String;)Ljava/lang/Class;");
        jstring param = jenv->NewStringUTF(adjustApn);
        jclass tClazz = (jclass) jenv->CallObjectMethod(classLoader, loadClass, param);
        jenv->DeleteLocalRef(param);
        if (ClearException(jenv)) {
            continue;
        }
        MYLOGI("mLoaders found: %p", tClazz);
        return tClazz;
    }
    MYLOGI("mLoaders ret: NULL");
    return NULL;
}