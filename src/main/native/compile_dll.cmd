javac -h ./header C:/Users/eriks/Desktop/MediaServer/src/main/java/org/eriksandsten/utils/Win32Helper.java
g++ -static -shared -fPIC -o win32helper.dll -I"C:/mingw64/include/c++/14.2.0" -I"C:/Users/eriks/.jdks/openjdk-23.0.2/include/win32" -I"C:/Users/eriks/.jdks/openjdk-23.0.2/include" ./source/JniUtils.cpp ./source/Win32Helper.cpp
copy /Y "win32helper.dll" "C:/Users/eriks/Desktop/MediaServer/win32helper.dll"
