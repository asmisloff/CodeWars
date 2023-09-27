package ru.asmisloff.codewars.logic;


import java.util.Arrays;

public class LogicalFollowing {

    public static final BooleanFunction impl = args -> impl(args[0], args[1]);
    public static final BooleanFunction and = args -> args[0] && args[1];
    public static final BooleanFunction not = args -> !args[0];

    public static boolean checkFollowing(BooleanFunction assumption, BooleanFunction conclusion, int numberOfArgs) {
        var register = new boolean[numberOfArgs];
        do {
            boolean assumptionResult = assumption.apply(register);
            boolean conclusionResult = conclusion.apply(register);
            System.out.printf(
                "Аргумент: %s, посылка: %b, заключение: %b\n",
                Arrays.toString(register), assumptionResult, conclusionResult
            );
            if (assumptionResult && !conclusionResult) {
                return false;
            }
        } while (!next(register));
        return true;
    }

    public static boolean next(boolean[] a) {
        boolean overflow = false;
        for (int i = a.length - 1; i >= 0; i--) {
            boolean bit = a[i];
            if (!bit) {
                a[i] = true;
                overflow = false;
                break;
            } else {
                a[i] = false;
                overflow = true;
            }
        }
        return overflow;
    }

    public static boolean impl(boolean a, boolean b) {
        return !a || b;
    }
}