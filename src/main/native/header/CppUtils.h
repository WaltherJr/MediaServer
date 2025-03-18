#include <algorithm>
#include <vector>

namespace win32_utils {
    const DWORD DW_SHOW = 5;
}

namespace cpp_utils {
    template <typename A, typename B, typename Func>
    std::vector<A> mapVectorToType(const std::vector<B>& vec, Func mappingFunc) {
        std::vector<A> transformed;
        transformed.reserve(vec.size()); // Optimize memory allocation
        std::transform(vec.begin(), vec.end(), std::back_inserter(transformed), mappingFunc);
        return transformed;
    }

}
