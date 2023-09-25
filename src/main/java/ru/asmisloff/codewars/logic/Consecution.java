package ru.asmisloff.codewars.logic;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;

public class Consecution {

    public static List<boolean[]> combinations(int i) {
        List<boolean[]> res = new ArrayList<>();
        for (int j = 0; j < pow(2, i); j++) {
            var a = new boolean[i];
            var s = Integer.toBinaryString(j);
            var shift = i - s.length();
            for (int k = 0; k < s.length(); k++) {
                a[k + shift] = s.charAt(k) == '1';
            }
            res.add(a);
        }
        return res;
    }

    private boolean impl(boolean a, boolean b) {
        return !a || b;
    }
}
