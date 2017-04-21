# Sets the minimum version of CMake required to build your native library.
# This ensures that a certain set of CMake features is available to
# your build.

cmake_minimum_required(VERSION 3.4.1)

# Specifies a library name, specifies whether the library is STATIC or
# SHARED, and provides relative paths to the source code. You can
# define multiple libraries by adding multiple add.library() commands,
# and CMake builds them for you. When you build your app, Gradle
# automatically packages shared libraries with your APK.

#生成共享库名、类型及源文件，注意这里的源文件不要加后缀，它会自己拼
add_library( native SHARED src/main/jni/native src/main/jni/common/Art src/main/jni/common/common
    src/main/jni/common/md5)

include_directories(src/main/jni/)
include_directories(src/main/jni/common/)

#日志相关
find_library( log-lib log )
# Links your native library against one or more other native libraries.
target_link_libraries( native ${log-lib} )