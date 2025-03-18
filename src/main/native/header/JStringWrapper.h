
#ifndef _WIN32HELPER_JSTRINGWRAPPER_H_
#define _WIN32HELPER_JSTRINGWRAPPER_H_
#ifdef __cplusplus
extern "C" {
#endif

#include "JniUtils.h"

class JStringWrapper {
    public:
        JStringWrapper() {
            this->jniEnv = nullptr;
        }

        JStringWrapper(jstring jstr) {
            this->jstr = jstr;
        }

        JStringWrapper(JNIEnv *jniEnv, jstring jstr) {
            this->jniEnv = jniEnv;
            this->jstr = jstr;
            this->wstr = convertJStringToWString(jniEnv, jstr);
        }

        JStringWrapper(std::wstring wstr) {
            this->wstr = wstr;
        }

        JStringWrapper(JNIEnv *jniEnv, std::wstring wstr) {
            this->jniEnv = jniEnv;
            this->jstr = convertWStringToJString(jniEnv, wstr);
            this->wstr = wstr;
        }

        operator std::wstring() {
            return this->wstr;
        }

        operator jstring() {
            return convertWStringToJString(this->jniEnv, this->wstr);
        }

        bool operator==(const std::wstring& other) const {
            return wstr == other;
        }

        jstring toJString() {
            return convertWStringToJString(jniEnv, wstr);
        }

    private:
        JNIEnv* jniEnv;
        std::wstring wstr;
        jstring jstr;
};

#ifdef __cplusplus
}
#endif
#endif
