package honours.heaps.util;

import java.util.HashMap;

public class NumUtil {
    public static int floorLog2(int x) {
        return (int)Math.floor(Math.log(x) / Math.log(2));
    }

    public static int findLowestCommonParent(int l, int r) {
        HashMap<Integer, Boolean> visited = new HashMap<>();

        visited.put(l, true);

        while(parent(l) != 0) {
            visited.put(l, true);
            l = parent(l);
        }

        visited.put(l, true);

        while(!visited.getOrDefault(r, false)) {
            r = parent(r);
        }

        return r;
    }

    public static int pow2(int a) {
        return 1 << a;
    }

    public static int parent(int i) {
        return i / 2;
    }

    public static int[] children(int i) {
        return new int[]{2*i, 2*i + 1};
    }
}
