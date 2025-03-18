#include <stdio.h>
#include <sstream>
#include <cstdio>
#include "../header/JniUtils.h"

using namespace std;

wstring getWin32WindowTitle(HWND windowHandle) {
    wstring input;
    input.resize(GetWindowTextLengthW(windowHandle));
    GetWindowTextW(windowHandle, input.data(), input.size() + 1); // Include null character
    return input;
}

jstring getWin32WindowTitle_jString(JNIEnv* jniEnv, HWND windowHandle) {
    return convertWStringToJString(jniEnv, getWin32WindowTitle(windowHandle));
}

wstring getWin32WindowClassName(HWND windowHandle) {
    wstring str(512, L'\0');
    int length = GetClassNameW(windowHandle, str.data(), str.size());
    str.resize(length);
    return str;
}

jobject convertHWNDToJIntegerObject(JNIEnv* jniEnv, const HWND& windowHandle) {
    jclass integerClass = jniEnv->FindClass("java/lang/Integer");
    jmethodID constructor = jniEnv->GetMethodID(integerClass, "<init>", "(I)V");
    jobject integerObject = jniEnv->NewObject(integerClass, constructor, windowHandle);

    return integerObject;
}

wstring convertJStringToWString(JNIEnv* jniEnv, jstring jstr) {
    const jchar* chars = jniEnv->GetStringChars(jstr, nullptr);
    jsize length = jniEnv->GetStringLength(jstr);
    wstring result;
    result.assign(reinterpret_cast<const wchar_t*>(chars), length);
    jniEnv->ReleaseStringChars(jstr, chars);

    return result;
}

jstring convertWStringToJString(JNIEnv* jniEnv, const wstring& wstr) {
    jchar* chars = new jchar[wstr.size()];

    for (size_t i = 0; i < wstr.size(); ++i) {
        chars[i] = static_cast<jchar>(wstr[i]);
    }

    jstring jstr = jniEnv->NewString(chars, wstr.size());
    delete[] chars;

    return jstr;
}

string convertJLongToString(jlong value) {
    ostringstream oss;
    oss << value;
    string numberStr = oss.str();
    return numberStr;
}

jstring convertJLongToJString(JNIEnv* jniEnv, jlong value) {
    ostringstream oss;
    oss << value;
    string numberStr = oss.str();
    return jniEnv->NewStringUTF(numberStr.c_str());
}

jstring concatenateJStrings(JNIEnv *jniEnv, jstring jstr1, jstring jstr2) {
    const char *str1Chars = jniEnv->GetStringUTFChars(jstr1, nullptr);
    const char *str2Chars = jniEnv->GetStringUTFChars(jstr2, nullptr);

    string str1(str1Chars);
    string str2(str2Chars);
    jniEnv->ReleaseStringUTFChars(jstr1, str1Chars);
    jniEnv->ReleaseStringUTFChars(jstr2, str2Chars);

    return jniEnv->NewStringUTF((str1 + str2).c_str());
}
