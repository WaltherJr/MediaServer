#include <jni.h>
#include <stdio.h>
#include <string>
#include <sstream>
#include <windows.h>
#include "../header/CppUtils.h"
#include "../header/JniUtils.h"
#include "../header/WStringWrapper.h"
#include "../header/JStringWrapper.h"
#include "../header/ArrayList.h"
#include "../header/org_eriksandsten_utils_Win32Helper.h"

using namespace std;

HWND finalWindowHandle = NULL;
jint globalProcessId;
JStringWrapper globalClassName;

BOOL CALLBACK EnumWindowsProc_getWindowCaptionsByProcessId(HWND hWnd, LPARAM lParam) {
    DWORD pId;
    GetWindowThreadProcessId(hWnd, &pId);
    auto list = (ArrayList<jstring>*) lParam;

    if ((DWORD) globalProcessId == pId) {
        jstring windowTitle_wStr = convertWStringToJString(list->getJNIEnv(), getWin32WindowTitle(hWnd) + SEPARATOR);
        jstring finalString = concatenateJStrings(list->getJNIEnv(), windowTitle_wStr, convertJLongToJString(list->getJNIEnv(), (jlong) hWnd));
        list->add(finalString);
    }

    return TRUE;
}

BOOL CALLBACK EnumWindowsProc_getWindowCaptionsByWindowClassName(HWND hWnd, LPARAM lParam) {
    const wstring currentWindowClassName = getWin32WindowClassName(hWnd);
    const wstring currentWindowTitle = getWin32WindowTitle(hWnd);
    auto dataPointer = reinterpret_cast<VectorWithValue<wstring, wstring>*>(lParam);

    if (dataPointer->additionalValue == currentWindowClassName) {
        dataPointer->array->push_back(currentWindowTitle);
    }

    return TRUE;
}

BOOL CALLBACK EnumWindowsProc_getWindowHandlesByWindowClassName(HWND hWnd, LPARAM lParam) {
    const wstring currentWindowClassName = getWin32WindowClassName(hWnd);
    auto dataPointer = reinterpret_cast<VectorWithValue<HWND, wstring>*>(lParam);

    if (dataPointer->additionalValue == currentWindowClassName) {
        dataPointer->array->push_back(hWnd);
    }

    return TRUE;
}

BOOL CALLBACK EnumWindowsProc(HWND hWnd, LPARAM lParam) {
    DWORD pId;
    GetWindowThreadProcessId(hWnd, &pId);

    if (pId == (DWORD)lParam) {
        finalWindowHandle = hWnd;
        return FALSE;
    }

    return TRUE;
}

JNIEXPORT jobject JNICALL Java_org_eriksandsten_utils_Win32Helper_getWindowCaptionsByProcessId(JNIEnv *jniEnv, jobject obj, jint processId) {
    // Create a new ArrayList in Java
    ArrayList<jstring> listWithAdditionalProcessId(jniEnv);
    globalProcessId = processId;
    // EnumWindows(EnumWindowsProc_helloWorld, (LPARAM) &listWithAdditionalProcessId);

    return listWithAdditionalProcessId.getObject();
}

JNIEXPORT jobject JNICALL Java_org_eriksandsten_utils_Win32Helper_getWindowCaptionsByWindowClassName(JNIEnv *jniEnv, jobject obj, jstring className) {
    vector<wstring> windowTitleStrings;
    VectorWithValue<wstring, wstring> data = {&windowTitleStrings, convertJStringToWString(jniEnv, className)};
    EnumWindows(EnumWindowsProc_getWindowCaptionsByWindowClassName, (LPARAM) &data);

    return ArrayList<jstring>(jniEnv, windowTitleStrings).getObject();
}

JNIEXPORT jobject JNICALL Java_org_eriksandsten_utils_Win32Helper_getWindowHandlesByWindowClassName(JNIEnv *jniEnv, jobject obj, jstring className) {
    vector<HWND> windowHandles;
    VectorWithValue<HWND, wstring> data = {&windowHandles, convertJStringToWString(jniEnv, className)};
    EnumWindows(EnumWindowsProc_getWindowHandlesByWindowClassName, (LPARAM) &data);

    return ArrayList<jobject>(jniEnv, windowHandles).getObject();
}

JNIEXPORT jlong JNICALL Java_org_eriksandsten_utils_Win32Helper_getWindowHandleByProcessId(JNIEnv* jniEnv, jobject obj, jint processId) {
    EnumWindows(EnumWindowsProc, (LPARAM) processId);
    return (jlong) finalWindowHandle;
}

JNIEXPORT jstring JNICALL Java_org_eriksandsten_utils_Win32Helper_getWindowTitleByWindowHandle(JNIEnv* jniEnv, jobject obj, jlong windowHandle) {
    return getWin32WindowTitle_jString(jniEnv, (HWND) windowHandle);
}

JNIEXPORT jlong JNICALL Java_org_eriksandsten_utils_Win32Helper_getWindowHandleByWindowTitle(JNIEnv* jniEnv, jobject obj, jstring windowTitle) {
    const char *strChars = jniEnv->GetStringUTFChars(windowTitle, nullptr);
    HWND w = FindWindow(NULL, strChars);
    return (jlong) w;
}

// https://stackoverflow.com/questions/19136365/win32-setforegroundwindow-not-working-all-the-time
JNIEXPORT void JNICALL Java_org_eriksandsten_utils_Win32Helper_setActiveWindow(JNIEnv* jniEnv, jobject obj, jlong windowHandle) {
    HWND hWnd = reinterpret_cast<HWND>(windowHandle);

    HWND foregroundWindow = GetForegroundWindow();
    DWORD windowThreadProcessId = GetWindowThreadProcessId(foregroundWindow, 0);
    DWORD currentThreadId = GetCurrentThreadId();
    AttachThreadInput(windowThreadProcessId, currentThreadId, TRUE);
    BringWindowToTop(hWnd);
    ShowWindow(hWnd, win32_utils::DW_SHOW);
    AttachThreadInput(windowThreadProcessId, currentThreadId, FALSE);
}
