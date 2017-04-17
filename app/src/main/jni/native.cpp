//
// Created by Administrator on 2017/4/10.
//
#include "native.h"

static const char *gClassName = "aqcxbom/cracktools/MainActivity";
static JNINativeMethod gMethods[] = {
        //{"ArtHook", "(Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;)Z", (void*)ArtHookVersionLessThan22},
        {"getSring", "()Ljava/lang/String;", (void *)stringFromJNI},
};

void test(JNIEnv *env)
{
    char temp[0xff] = "\0";

    if(getPackageName(env, temp))
    {
        MYLOGI("getPackageName %s", temp);
    }

    if(getDeviceID(env, temp))
    {
        MYLOGI("getDeviceID %s", temp);
    }

    if(getSubscriberId(env, temp))
    {
        MYLOGI("getSubscriberId %s", temp);
    }

    if(getDeviceID_Serial(temp))
    {
        MYLOGI("getDeviceID_Serial %s", temp);
    }
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOGI("JNI_OnLoad");
    JNIEnv *env = NULL;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
#ifdef ENABLE_OUTPUT_SELFSIGINFO
    showSelfSig(env);
#endif
    test(env);

    if (registerNativeMethods(env, gClassName, gMethods,
                              sizeof(gMethods) / sizeof(gMethods[0])) == JNI_FALSE) {
        return JNI_ERR;
    }
    LOGI("So load success");
    return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved) {
    LOGI("JNI_OnUnload");
    return;
}