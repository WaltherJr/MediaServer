#include <jni.h>
#include <string>
#include <vector>
#include <windows.h>

#ifndef _WIN32HELPER_UTILS_H_
#define _WIN32HELPER_UTILS_H_

const std::wstring SEPARATOR = L"||";

jstring convertWStringToJString(JNIEnv* jniEnv, const std::wstring& str);

jobject convertHWNDToJIntegerObject(JNIEnv* jniEnv, const HWND& windowHandle);

std::wstring convertJStringToWString(JNIEnv* jniEnv, jstring str);

std::wstring getWin32WindowTitle(HWND windowHandle);

jstring getWin32WindowTitle_jString(JNIEnv* jniEnv, HWND windowHandle);

std::wstring getWin32WindowClassName(HWND windowHandle);

std::string convertJLongToString(jlong value);

jstring convertJLongToJString(JNIEnv* jniEnv, jlong value);

jstring concatenateJStrings(JNIEnv *jniEnv, jstring str1, jstring str2);

#endif
