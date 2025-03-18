
#ifndef _WIN32HELPER_ARRAYLIST_H_
#define _WIN32HELPER_ARRAYLIST_H_

#include <vector>
#include <cstdio>
#include "JniUtils.h"

using namespace std;

template <typename T, typename D>
struct VectorWithValue {
    vector<T>* array;
    D additionalValue;
};

class JavaMethod {
    public:
        JavaMethod(const char* name, const char* signature) {
            this->name = name;
            this->signature = signature;
        }

        const char* getName() {
            return this->name;
        }

        const char* getSignature() {
            return this->signature;
        }

    protected:
        const char* name;
        const char* signature;
};

class Object {
    public:
        Object(JNIEnv* jniEnv, jclass javaClass, JavaMethod constructor) {
            this->jniEnv = jniEnv;
            this->javaClass = javaClass;
            this->javaCtor = jniEnv->GetMethodID(javaClass, constructor.getName(), constructor.getSignature());
        }

        JNIEnv* getJNIEnv() {
            return this->jniEnv;
        }

        jclass getJavaClass() {
            return this->javaClass;
        }

        jmethodID getJavaCtor() {
            return this->javaCtor;
        }

        jobject getObject() {
            return this->javaObject;
        }

    protected:
        JNIEnv* jniEnv;
        jclass javaClass;
        jmethodID javaCtor;
        jobject javaObject;
};

template <typename T>
class Integer: public Object {
    public:
        Integer(JNIEnv* jniEnv, T value): Object(jniEnv, jniEnv->FindClass("java/lang/Integer"), JavaMethod("<init>", "(I)V")) {
            this->javaObject = jniEnv->NewObject(this->javaClass, javaCtor, value);
            this->value = value;
        }

        T getValue() {
            return value;
        }

    protected:
        T value;
};

template <typename T>
class ArrayList: public Object {
    public:
        ArrayList(JNIEnv* jniEnv): Object(jniEnv, jniEnv->FindClass("java/util/ArrayList"), JavaMethod("<init>", "()V")) {
            this->javaObject = jniEnv->NewObject(this->javaClass, javaCtor);
            this->javaAddMethod = jniEnv->GetMethodID(this->javaClass, "add", "(Ljava/lang/Object;)Z");
            this->javaGetMethod = jniEnv->GetMethodID(this->javaClass, "get", "(I)Ljava/lang/Object;");
            this->javaSizeMethod = jniEnv->GetMethodID(this->javaClass, "size", "()I");
        }

        ArrayList(JNIEnv* jniEnv, vector<wstring>& strings): ArrayList(jniEnv) {
            for (const wstring& str : strings) {
                this->add(convertWStringToJString(jniEnv, str));
            }
        }

        ArrayList(JNIEnv* jniEnv, vector<HWND>& windowHandles): ArrayList(jniEnv) {
            for (const HWND& windowHandle : windowHandles) {
                this->add(Integer(jniEnv, windowHandle).getObject());

            }
        }

        bool add(T value) {
            return this->jniEnv->CallBooleanMethod(this->javaObject, this->javaAddMethod, value);
        }

        T get(int index) {
            return static_cast<T>(this->jniEnv->CallObjectMethod(this->object, this->javaGetMethod, index));
        }

        int size() {
            return this->jniEnv->CallIntMethod(this->object, this->javaSizeMethod);
        }

    protected:
        jmethodID javaAddMethod;
        jmethodID javaGetMethod;
        jmethodID javaSizeMethod;
};

#endif
