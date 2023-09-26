package ru.asmisloff.codewars.logic;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class LogicalFollowing {

    public static boolean checkFollowing(BooleanFunction assumption, BooleanFunction conclusion, int numberOfArgs) {
        var register = new boolean[numberOfArgs];
        do {
            if (assumption.apply(register) && !conclusion.apply(register)) {
                return false;
            }
        } while (!next(register));
        return true;
    }

    public static void next(boolean[] a) {
        boolean overflow = false;
        for (int i = a.length - 1; i >= 0; i++) {
            boolean bit = a[i];
            if (!bit) {
                a[i] = true;
                return;
            } else {
                a[i] = false;
            }
        }
    }

    private BooleanFunction impl = new BooleanFunction() {
        @java.lang.Override
        public boolean apply(boolean[] args) {
            return !args[0] || args[1];
        }
    }

    private BooleanFunction and = new BooleanFunction() {
        @java.lang.Override
        public boolean apply(boolean[] args) {
            return args[0] && args[1];
        }
    }

    private BooleanFunction not = new BooleanFunction() {
        @java.lang.Override
        public boolean apply(boolean[] args) {
            return !args[0];
        }
    }
}

interface BooleanFunction {
    boolean apply(boolean[] args);
}