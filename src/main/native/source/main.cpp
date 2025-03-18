#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <functional>
#include "CppUtils.h"

class StringWrapper {
    public:
        StringWrapper(std::wstring value) : value(value) {}

        std::wstring getValue() const {
            return value;
        }

    private:
        std::wstring value;
};

int main() {
    std::vector<StringWrapper> vec{ StringWrapper(L"1"), StringWrapper(L"2"), StringWrapper(L"3") };

    auto transformed = cpp_utils::mapVectorToType<int, StringWrapper>(vec, [](const StringWrapper& sw) { return std::stoi(sw.getValue()) + 1; });

    // Print results
    for (int num : transformed) {
        std::wcout << num << L" ";
    }

    return 0;
}
