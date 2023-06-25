package ru.asmisloff.codewars;

import java.util.Arrays;
import java.util.function.IntPredicate;

public class CarMileage {

    public static int isInteresting(int number, int[] awesomePhrases) {
        IntPredicate match = i -> i == number;
        IntPredicate approxMatch = phrase -> phrase == number + 1 || phrase == number + 2;

        if (check(number) || anyOf(awesomePhrases, match)) {
            return 2;
        } else if (check(number + 1) || check(number + 2) || anyOf(awesomePhrases, approxMatch)) {
            return 1;
        }

        return 0;
    }

    private static boolean anyOf(int[] arr, IntPredicate pred) {
        return Arrays.stream(arr).anyMatch(pred);
    }

    private static boolean check(long n) {
        if (n < 100) {
            return false;
        }
        String s = Long.toString(n);
        return isAnyDigitFollowedByAllZeros(s)
            || everyDigitIsTheSameNumber(s) || digitsAreSequentialIncrementing(s)
            || digitsAreSequentialDecrementing(s) || isPalindrome(s);
    }

    private static boolean isAnyDigitFollowedByAllZeros(String n) {
        if (n.charAt(0) == '0') {
            return false;
        }
        for (int i = 1; i < n.length(); ++i) {
            if (n.charAt(i) != '0') {
                return false;
            }
        }
        return true;
    }

    private static boolean everyDigitIsTheSameNumber(String n) {
        return n.chars().distinct().count() == 1;
    }

    private static boolean digitsAreSequentialIncrementing(String n) {
        int size = n.length();
        if (size > 10) {
            return false;
        }
        for (int i = 1; i < size - 1; i++) {
            if (n.charAt(i) - n.charAt(i - 1) != 1) {
                return false;
            }
        }
        if (n.charAt(size - 2) == '9') {
            return n.charAt(size - 1) == '0';
        }
        return n.charAt(size - 1) - n.charAt(size - 2) == 1;
    }

    private static boolean digitsAreSequentialDecrementing(String n) {
        int sz = n.length();
        if (sz > 10) {
            return false;
        }
        for (int i = 1; i < sz; i++) {
            if (n.charAt(i - 1) - n.charAt(i) != 1) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPalindrome(String n) {
        int left = 0;
        int right = n.length() - 1;
        while (left < right) {
            if (n.charAt(left++) != n.charAt(right--)) {
                return false;
            }
        }
        return true;
    }
}
