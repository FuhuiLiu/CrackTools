LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := ghttp
LOCAL_ARM_MODE := arm
LOCAL_SRC_FILES := \
	ghttp.c \
	http_date.c \
	http_hdrs.c \
	http_req.c \
	http_resp.c \
	http_trans.c \
	http_uri.c \
	http_base64.c 
LOCAL_C_INCLUDES := .
include $(BUILD_SHARED_LIBRARY)