#include <common.h>

#ifndef ART__H_20170310
#define ART__H_20170310

extern "C" void ArtHookVersionLargerThan22(JNIEnv *env, jobject targetMethod, jobject replaceMethod);
extern "C" void ArtHookVersion22(JNIEnv *env, jobject targetMethod, jobject replaceMethod);
extern "C" void ArtHookVersionLessThan22(JNIEnv *env, jobject targetMethod, jobject replaceMethod);

#endif
