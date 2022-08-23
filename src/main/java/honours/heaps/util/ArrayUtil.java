package honours.heaps.util;

import java.util.Arrays;

public class ArrayUtil {
    // https://stackoverflow.com/a/784842
    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

}
