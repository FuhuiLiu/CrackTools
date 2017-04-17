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


/*
 * add time: 2016年8月18日15:04:52
 * Function: 获取Context对象
 */
jobject getGlobalContext(JNIEnv *env)
{
    jclass activityThread = env->FindClass("android/app/ActivityThread");

    jmethodID currentActivityThread = env->GetStaticMethodID(activityThread,
                                                             "currentActivityThread",
                                                             "()Landroid/app/ActivityThread;");

    jobject at = env->CallStaticObjectMethod(activityThread, currentActivityThread);

    jmethodID getApplication = env->GetMethodID(activityThread,
                                                "getApplication",
                                                "()Landroid/app/Application;");

    jobject context = env->CallObjectMethod(at, getApplication);

    return context;
}

/**
 * C的字符串转jstring的字符串
 */
jstring func_CStr2Jstring( JNIEnv* env, const char* pat)
{
    jstring strRet = NULL;
    jclass strClass = env->FindClass("Ljava/lang/String;");
    jmethodID mID = env->GetMethodID(strClass, "<init>",
                                     "([BLjava/lang/String;)V");
    jbyteArray bytes = env->NewByteArray((jsize) strlen(pat));
    env->SetByteArrayRegion(bytes, 0, (jsize) strlen(pat), (jbyte*) pat); //将char* 转换为byte数组
    jstring encoding = env->NewStringUTF("GB2312");
    strRet = (jstring) env->NewObject(strClass, mID, bytes, encoding);
    env->DeleteLocalRef(encoding);
    return strRet;
}

/**

    利用Java的String类来完成字符编码转换

*/
char* func_Jstring2CStr(JNIEnv* env, jstring jstr)
{
    MYLOGI("enter func_CStr2Jstring 1");
    char* rtn = NULL;
    jclass clsstring = env->FindClass("java/lang/String");
    jstring strencode = env->NewStringUTF("GB2312"); //转换成Cstring的GB2312，兼容ISO8859-1
    //jmethodID (*GetMethodID)(JNIEnv*, jclass, const char*, const char*);第二个参数是方法名，第三个参数是getBytes方法签名
    //获得签名：javap -s java/lang/String: (Ljava/lang/String;)[B
    jmethodID mid = env->GetMethodID(clsstring, "getBytes",
                                     "(Ljava/lang/String;)[B");
    //等价于调用这个方法String.getByte("GB2312");
    //将jstring转换成字节数组
    MYLOGI("enter func_CStr2Jstring 2");
    //用Java的String类getByte方法将jstring转换为Cstring的字节数组
    jbyteArray barr = (jbyteArray) env->CallObjectMethod(jstr, mid, strencode);

    env->DeleteLocalRef(strencode);

    MYLOGI("enter func_CStr2Jstring 3");
    jsize alen = env->GetArrayLength(barr);
    MYLOGI("enter func_CStr2Jstring 4");
    jbyte* ba = env->GetByteArrayElements(barr, JNI_FALSE);
    MYLOGI("alen=%d\n", alen);
    if (alen > 0)
    {
        rtn = (char*) malloc(alen + 1 + 128);
        MYLOGI("rtn address == %p", &rtn);    //输出rtn地址
        memcpy(rtn, ba, alen);
        rtn[alen] = 0;            //"\0"
    }
    env->ReleaseByteArrayElements(barr, ba, 0);
    return rtn;
}

//返回签名字符串
jstring func_loadSignature(JNIEnv* env, jobject obj)
{
    // 获得Context类
    jclass cls = env->GetObjectClass(obj);
    // 得到getPackageManager方法的ID
    jmethodID mid = env->GetMethodID(cls, "getPackageManager", "()Landroid/content/pm/PackageManager;");

    // 获得应用包的管理器
    jobject pm = env->CallObjectMethod(obj, mid);

    // 得到getPackageName方法的ID
    mid = env->GetMethodID(cls, "getPackageName", "()Ljava/lang/String;");
    // 获得当前应用包名
    jstring packageName = (jstring)env->CallObjectMethod(obj, mid);

    // 获得PackageManager类
    cls = env->GetObjectClass(pm);
    // 得到getPackageInfo方法的ID
    mid  = env->GetMethodID(cls, "getPackageInfo", "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    // 获得应用包的信息
    jobject packageInfo = env->CallObjectMethod(pm, mid, packageName, 0x40); //GET_SIGNATURES = 64;
    // 获得PackageInfo 类
    cls = env->GetObjectClass(packageInfo);
    // 获得签名数组属性的ID
    jfieldID fid = env->GetFieldID(cls, "signatures", "[Landroid/content/pm/Signature;");
    // 得到签名数组
    jobjectArray signatures = (jobjectArray)env->GetObjectField(packageInfo, fid);
    // 得到签名
    jobject sign = env->GetObjectArrayElement(signatures, 0);

    // 获得Signature类
    cls = env->GetObjectClass(sign);
    // 得到toCharsString方法的ID
    mid = env->GetMethodID(cls, "toCharsString", "()Ljava/lang/String;");

    // 返回当前应用签名信息
    return (jstring)env->CallObjectMethod(sign, mid);
}

/*
 * 对输入的jstring进行MD5 HASH计算并返回
 */
jbyteArray func_GetMD5Hash(JNIEnv* env, jstring strSig)
{
    jbyteArray retResult = NULL;
    if(!strSig)
        return retResult;
    jclass clsmd = findAppClass(env, "java/security/MessageDigest");
    jclass clsString = findAppClass(env, "java/lang/String");
    if (!clsmd || !clsString) {
        MYLOGI("MessageDigest cls get faild!");
        return retResult;
    }

    jmethodID idgetIns = getMethodID(env, clsmd, "getInstance",
                                     "(Ljava/lang/String;)Ljava/security/MessageDigest;", true);
    jmethodID iddigest = getMethodID(env, clsmd, "digest", "([B)[B", false);
    jmethodID idgetBytes = getMethodID(env, clsString, "getBytes",
                                       "(Ljava/lang/String;)[B", false);
    jmethodID idlength = getMethodID(env, clsString, "length", "()I", false);
    if (!idgetIns || !iddigest || !idgetBytes || !idlength) {
        MYLOGI("func_GetMD5Hash methodIDs get faild!");
        return retResult;
    }
    jstring strMD5 = env->NewStringUTF("MD5");
    jstring strUTF8 = env->NewStringUTF("UTF-8");
    //获取MD5加密实例 MessageDigest.getInstance("MD5") => MessageDigest det
    jobject objmd = env->CallStaticObjectMethod(clsmd, idgetIns, strMD5);
    //strSig.getBytes("UTF-8") ==> 得到 byte[] by
    jobject objByteArySig = env->CallObjectMethod(strSig, idgetBytes, strUTF8);

    //释放内存
    env->DeleteLocalRef(strMD5);
    env->DeleteLocalRef(strUTF8);

    if (!objmd || !objByteArySig) {
        MYLOGI("MessageDigest obj get faild!");
        return retResult;
    }
    //det.digest(by);
    retResult = (jbyteArray) env->CallObjectMethod(objmd, iddigest,
                                                   objByteArySig);
    return retResult;
}

/*
 * 转换MD5的HASH结果为可输出的字符串，注意这个返回指针得手动释放
 */
char* func_Convert2HumenReadable(JNIEnv *env, jbyteArray byteAryArg)
{
    //获取长度
    jsize nOutSize = env->GetArrayLength(byteAryArg);
    char* pNew = new char[nOutSize];
    if(!pNew)
        return pNew;

    //CMySmartPtr<char*> smart(pNew);

    //byte指针依次读出来转换成16进存放到new出的空间
    jbyte *by = env->GetByteArrayElements(byteAryArg, 0);
    /* 太low
    sprintf((char*) pNew, "%X%X%X%X%X%X%X%X%X%X%X%X%X%X%X%X",
        *(unsigned char*) &by[0], *(unsigned char*) &by[1],
        *(unsigned char*) &by[2], *(unsigned char*) &by[3],
        *(unsigned char*) &by[4], *(unsigned char*) &by[5],
        *(unsigned char*) &by[6], *(unsigned char*) &by[7],
        *(unsigned char*) &by[8], *(unsigned char*) &by[9],
        *(unsigned char*) &by[10], *(unsigned char*) &by[11],
        *(unsigned char*) &by[12], *(unsigned char*) &by[13],
        *(unsigned char*) &by[14], *(unsigned char*) &by[15]);
    */
    for (int i = 0; i < nOutSize; i++) {
        sprintf((char*) (&pNew[i] + i), "%X", *(const char*) &by[i]);
    }
    //byte*输出
    return pNew;
}

//输出签名信息
void showSelfSig(JNIEnv *env)
{
  MYLOGI("showSelfSig");
  jstring strSig = func_loadSignature(env, getGlobalContext(env));

  jbyteArray objdigestResult = func_GetMD5Hash(env, strSig);
  if (objdigestResult)
  {
    char *pMD5ThisPackage = func_Convert2HumenReadable(env, objdigestResult);
    if (pMD5ThisPackage)
    {
      MYLOGI("ThisPackage Sig MD5 from NDK: %s", pMD5ThisPackage);
    }
  } else
  MYLOGE("objdigestResult error");
}

bool getDeviceID_Serial(char *deviceID)//serial number
{
    __system_property_get("ro.serialno",deviceID);
    return true;
}
bool getSubscriberId(JNIEnv *env, char* pOut)
{
    jobject mContext = getGlobalContext(env);
    if(mContext == 0){
        return false;
    }
    jclass cls_context = env->FindClass("android/content/Context");
    if(cls_context == 0){
        return false;
    }
    jmethodID getSystemService = env->GetMethodID(cls_context, "getSystemService", "(Ljava/lang/String;)Ljava/lang/Object;");
    if(getSystemService == 0){
        return false;
    }
    jfieldID TELEPHONY_SERVICE = env->GetStaticFieldID(cls_context, "TELEPHONY_SERVICE", "Ljava/lang/String;");
    if(TELEPHONY_SERVICE == 0){
        return false;
    }
    jstring str = (jstring)env->GetStaticObjectField(cls_context, TELEPHONY_SERVICE);
    jobject telephonymanager = env->CallObjectMethod(mContext, getSystemService, str);
    if(telephonymanager == 0){
        return false;
    }
    jclass cls_tm = env->FindClass("android/telephony/TelephonyManager");
    if(cls_tm == 0){
        return false;
    }

    jmethodID getSubscriberId = env->GetMethodID(cls_tm, "getSubscriberId", "()Ljava/lang/String;");
    if(getSubscriberId == 0){
        return -1;
    }
    jstring SubscriberId = (jstring)env->CallObjectMethod(telephonymanager, getSubscriberId);
    if(SubscriberId == 0)
    {
        MYLOGI("getSubscriberId == null");
        return false;
    }
    const char* pSubscriberId = env->GetStringUTFChars(SubscriberId, 0);
    if(pSubscriberId == NULL)
        return false;

    strcpy(pOut, pSubscriberId);
    //MYLOGI("JNI SubscriberId %s", pSubscriberId);
    env->ReleaseStringUTFChars(SubscriberId, pSubscriberId);
    return true;
}

bool getDeviceID(JNIEnv *env, char* pOut)
{
    jobject mContext = getGlobalContext(env);
    if(mContext == 0){
        return false;
    }
    jclass cls_context = env->FindClass("android/content/Context");
    if(cls_context == 0){
        return false;
    }
    jmethodID getSystemService = env->GetMethodID(cls_context, "getSystemService", "(Ljava/lang/String;)Ljava/lang/Object;");
    if(getSystemService == 0){
        return false;
    }
    jfieldID TELEPHONY_SERVICE = env->GetStaticFieldID(cls_context, "TELEPHONY_SERVICE", "Ljava/lang/String;");
    if(TELEPHONY_SERVICE == 0){
        return false;
    }
    jstring str = (jstring)env->GetStaticObjectField(cls_context, TELEPHONY_SERVICE);
    jobject telephonymanager = env->CallObjectMethod(mContext, getSystemService, str);
    if(telephonymanager == 0){
        return false;
    }
    jclass cls_tm = env->FindClass("android/telephony/TelephonyManager");
    if(cls_tm == 0){
        return false;
    }
    jmethodID getDeviceId = env->GetMethodID(cls_tm, "getDeviceId", "()Ljava/lang/String;");
    if(getDeviceId == 0){
        return false;
    }
    jstring deviceid = (jstring)env->CallObjectMethod(telephonymanager, getDeviceId);

    const char* pDeviceid = env->GetStringUTFChars(deviceid, 0);
    if(pDeviceid == NULL)
        return false;

    strcpy(pOut, pDeviceid);
    //MYLOGI("JNI Deviceid %s", pDeviceid);
    env->ReleaseStringUTFChars(deviceid, pDeviceid);
    return true;
}
/**
 * 获取当前APP的包名
 * 参数: env
 *       pBufOut 传出结果
 * 返回值: 结果可用返回true,否则返回false
 */
bool getPackageName(IN JNIEnv *env, OUT char *pBufOut)
{
//    getAndroidDeviceID(env, getGlobalContext(env));
//    char temp[0xff] = "\0";
//    getAndroidDeviceID_Serial(temp);
//    MYLOGI("JNI DeviceID_Serial %s", temp);

    jobject context = getGlobalContext(env);
    jclass cls_context = env->FindClass("android/content/Context");
    if(cls_context == NULL){
        MYLOGI("cls_context == NULL");
        return false;
    }
    jmethodID  id_getPackageName = env->GetMethodID(cls_context, "getPackageName", "()Ljava/lang/String;");
    if (id_getPackageName == NULL)
    {
        MYLOGI("id_getPackageName == NULL");
        return false;
    }
    jstring str_PackageName = (jstring)env->CallObjectMethod(context, id_getPackageName);
    if (str_PackageName == NULL) {
        MYLOGI("str_PackageName == NULL");
        return false;
    }
    const char* pPackaageName = env->GetStringUTFChars(str_PackageName, false);
    if (pPackaageName == NULL){
        MYLOGI("pPackaageName == NULL");
        return false;
    }
    strcpy(pBufOut, pPackaageName);
    //MYLOGI("%s", pBufOut);
    env->ReleaseStringUTFChars(str_PackageName, pPackaageName);
    return true;
}