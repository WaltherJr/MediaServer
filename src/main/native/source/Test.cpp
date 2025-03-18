#include <windows.h>
#include <tchar.h>
#include <string>
#include <iostream>
#include <sstream>
#include <vector>

std::vector<HWND> finalWindowHandles;

std::string hwndToString(HWND hwnd) {
    std::ostringstream oss;
    oss << reinterpret_cast<uintptr_t>(hwnd); // Convert HWND to a string (by default, as a decimal integer)
    return oss.str();
}

// Callback function for EnumWindows
BOOL CALLBACK EnumWindowsProc(HWND hwnd, LPARAM lParam) {
    const int length = 256;
    TCHAR windowTitle[length];
    TCHAR className[length];
    TCHAR* classnameToSearchFor = (TCHAR*)lParam;

    // Get window title
    if (GetWindowText(hwnd, windowTitle, length) > 0) {
        // Get window class name
        GetClassName(hwnd, className, length);

        // Check if it is a Chrome window (class name for Chrome is "Chrome_WidgetWin_1")
        if (_tcscmp(className, _T(classnameToSearchFor)) == 0) {
        // if (_tcsstr(windowTitle, _T("Google Chrome")) || _tcscmp(className, _T("Chrome_WidgetWin_1")) == 0) {
            std::wcout << L"Found Chrome window: " << windowTitle << std::endl;
            finalWindowHandles.push_back(hwnd);
            // return FALSE;  // Stop enumeration when found
        }
    }
    return TRUE;  // Continue enumeration
}

void printWindowTitle(HWND windowHandle) {
    const int bufferSize = 512;
    wchar_t windowText[bufferSize] = {0};
    int textLength = GetWindowTextW(windowHandle, windowText, bufferSize);

    if (textLength > 0) {
        std::wstring wstr(windowText, textLength);
        std::wcout << L"Window Text: " << wstr << std::endl;

    } else {
        std::wcout << L"Failed to get window text. Error: " << GetLastError() << std::endl;
    }
}

void enumerateChildWindows(HWND hwndParent)
{
    HWND hwndChild = NULL;  // Start with NULL to find the first child window

    while ((hwndChild = FindWindowExW(hwndParent, hwndChild, NULL, NULL)) != NULL) {
        // Here, hwndChild is a valid child window handle.
        // You can perform operations on the child window like getting its title or class name.

        wchar_t windowTitle[256];
        wchar_t className[256];

        GetWindowTextW(hwndChild, windowTitle, sizeof(windowTitle) / sizeof(wchar_t));
        GetClassNameW(hwndChild, className, sizeof(className) / sizeof(wchar_t));

        std::wcout << L"Child Window Title: " << windowTitle << std::endl;
        std::wcout << L"Child Window Class: " << className << std::endl;

        if (wcscmp(className, L"Chrome_RenderWidgetHostHWND") == 0) {
            std::cout << "HAAAAAAAAAAAAAAAAAAAAAAA!" << std::endl;
            enumerateChildWindows(hwndChild);
        }
    }
}

int main() {
    // EnumWindows(EnumWindowsProc, (LPARAM) "Chrome_WidgetWin_1");
    EnumWindows(EnumWindowsProc, (LPARAM) "Chrome_WidgetWin_1");

    std::cout << "=======================================================================" << std::endl;
    for (const HWND& parentWindow : finalWindowHandles) {
        std::string parentWindowStr = hwndToString(parentWindow);
        std::cout << "FINAL WINDOW HANDLE: " << parentWindowStr << std::endl;
        printWindowTitle(parentWindow);
        enumerateChildWindows(parentWindow);
    }
    std::cout << "=======================================================================" << std::endl;

    return 0;
}
