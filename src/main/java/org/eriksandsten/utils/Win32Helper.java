package org.eriksandsten.utils;

import java.util.List;

public class Win32Helper {
    public native List<String> getWindowCaptionsByProcessId(int processId);
    public native List<String> getWindowCaptionsByWindowClassName(String className);
    public native List<Integer> getWindowHandlesByWindowClassName(String className);
    public native long getWindowHandleByProcessId(int pid);
    public native String getWindowTitleByWindowHandle(long windowHandle);
    public native long getWindowHandleByWindowTitle(String windowTitle);
    public native void setActiveWindow(long windowHandle);

    static {
        System.loadLibrary("win32helper");
    }

    public Long getWindowHandleByProcessId_nonNative(long processId) {
        long windowHandle = getWindowHandleByProcessId(Long.valueOf(processId).intValue());
        String windowTitle = getWindowTitleByWindowHandle(windowHandle);
        List<String> apas = getWindowCaptionsByProcessId(Long.valueOf(processId).intValue());

        // setActiveWindow(windowHandle);

        return windowHandle;
    }

    public List<Long> getProcessIdsByName(String processName) {
            var apa = ProcessHandle.allProcesses()
                    .filter(ProcessHandle::isAlive)
                    .filter(ph -> ph.info().command().isPresent() && ph.info().command().get().contains(processName))
                    .map(ph -> {
                        var processID = ph.pid();
                        var processCommand = ph.info().command().get();
                        return processID;
                    });

            return apa.toList();
    }
}
