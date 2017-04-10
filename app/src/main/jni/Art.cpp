#include "Art.h"

void ArtHookVersionLargerThan22(JNIEnv *env, jobject targetMethod, jobject replaceMethod)
{
    jmethodID jmethodTarget; // r4@1
    jmethodID jmethodReplace; // r0@1

    jmethodTarget = env->FromReflectedMethod(targetMethod);
    jmethodReplace = env->FromReflectedMethod(replaceMethod);
    *(int *)(*(int *)jmethodReplace + 8) = *(int *)(*(int *)jmethodTarget + 8);
    *(int *)(*(int *)jmethodReplace + 84) = *(int *)(*(int *)jmethodTarget + 84);
    *(int *)(*(int *)jmethodReplace + 132) = *(int *)(*(int *)jmethodTarget + 132) - 1;
    *(int *)jmethodTarget = *(int *)jmethodReplace;
    *((int *)jmethodTarget + 2) = *((int *)jmethodReplace + 2);
    *((int *)jmethodTarget + 3) = *((int *)jmethodReplace + 3);
    *((int *)jmethodTarget + 1) = *((int *)jmethodReplace + 1);
    *((int *)jmethodTarget + 4) = *((int *)jmethodReplace + 4);
    *((int *)jmethodTarget + 6) = *((int *)jmethodReplace + 6);
    *((int *)jmethodTarget + 5) = *((int *)jmethodReplace + 5);
    *((int *)jmethodTarget + 7) = *((int *)jmethodReplace + 7);
    *((int *)jmethodTarget + 8) = *((int *)jmethodReplace + 8);
    *((int *)jmethodTarget + 9) = *((int *)jmethodReplace + 9);
}

void ArtHookVersion22(JNIEnv *env, jobject targetMethod, jobject replaceMethod)
{
    jmethodID jmethodTarget; // r4@1
    jmethodID jmethodReplace; // r0@1

    jmethodTarget = env->FromReflectedMethod(targetMethod);
    jmethodReplace = env->FromReflectedMethod(replaceMethod);
    *(int *)(*((int *)jmethodReplace + 2) + 8) = *(int *)(*((int *)jmethodTarget + 2) + 8);
    *(int *)(*((int *)jmethodReplace + 2) + 68) = *(int *)(*((int *)jmethodTarget + 2) + 68);
    *(int *)(*((int *)jmethodReplace + 2) + 104) = *(int *)(*((int *)jmethodTarget + 2) + 104) - 1;
    *((int *)jmethodTarget + 2) = *((int *)jmethodReplace + 2);
    *((int *)jmethodTarget + 4) = *((int *)jmethodReplace + 4);
    *((int *)jmethodTarget + 5) = *((int *)jmethodReplace + 5);
    *((int *)jmethodTarget + 3) = *((int *)jmethodReplace + 3);
    *((int *)jmethodTarget + 6) = *((int *)jmethodReplace + 6);
    *((int *)jmethodTarget + 8) = *((int *)jmethodReplace + 8);
    *((int *)jmethodTarget + 7) = *((int *)jmethodReplace + 7);
    *((int *)jmethodTarget + 9) = *((int *)jmethodReplace + 9);
    *((int *)jmethodTarget + 10) = *((int *)jmethodReplace + 10);
    *((int *)jmethodTarget + 11) = *((int *)jmethodReplace + 11);
}

/*
 *
 */
void ArtHookVersionLessThan22(JNIEnv *env, jobject targetMethod, jobject replaceMethod)
{
    jmethodID jmethodTarget;
    jmethodID jmethodReplace;

    jmethodTarget = env->FromReflectedMethod(targetMethod);
    jmethodReplace = env->FromReflectedMethod(replaceMethod);
    *(int *)(*((int *)jmethodReplace + 2) + 8) = *(int *)(*((int *)jmethodTarget + 2) + 8);
    *(int *)(*((int *)jmethodReplace + 2) + 68) = *(int *)(*((int *)jmethodTarget + 2) + 68);
    *(int *)(*((int *)jmethodReplace + 2) + 104) = *(int *)(*((int *)jmethodTarget + 2) + 104) - 1;
    *((int *)jmethodTarget + 2) = *((int *)jmethodReplace + 2);
    *((int *)jmethodTarget + 7) = *((int *)jmethodReplace + 7);
    *((int *)jmethodTarget + 13) = *((int *)jmethodReplace + 13);
    *((int *)jmethodTarget + 3) = *((int *)jmethodReplace + 3);
    *((int *)jmethodTarget + 5) = *((int *)jmethodReplace + 5);
    *((int *)jmethodTarget + 4) = *((int *)jmethodReplace + 4);
    *((int *)jmethodTarget + 19) = *((int *)jmethodReplace + 19);
    *((int *)jmethodTarget + 9) = *((int *)jmethodReplace + 9);
    *((int *)jmethodTarget + 12) = *((int *)jmethodReplace + 12);
    *((int *)jmethodTarget + 15) = *((int *)jmethodReplace + 15);
    *((int *)jmethodTarget + 8) = *((int *)jmethodReplace + 8);
    *((int *)jmethodTarget + 10) = *((int *)jmethodReplace + 10);
    *((int *)jmethodTarget + 11) = *((int *)jmethodReplace + 11);
    *((int *)jmethodTarget + 18) = *((int *)jmethodReplace + 18);
    *((int *)jmethodTarget + 17) = *((int *)jmethodReplace + 17);
    *((int *)jmethodTarget + 16) = *((int *)jmethodReplace + 16);
}
