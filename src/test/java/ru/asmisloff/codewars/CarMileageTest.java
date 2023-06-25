package ru.asmisloff.codewars;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CarMileageTest {

    @ParameterizedTest
    @MethodSource
    public void testIsInteresting(int number, int[] awesomePhrases, int result) {
        assertEquals(result, CarMileage.isInteresting(number, awesomePhrases));
    }

    @ParameterizedTest
    @MethodSource
    public void testIsAnyDigitFollowedByAllZeros(int number, boolean result) {
        try {
            var method = CarMileage.class.getDeclaredMethod("isAnyDigitFollowedByAllZeros", String.class);
            method.setAccessible(true);
            assertEquals(result, method.invoke(CarMileage.class, Integer.toString(number)));
        } catch (
            NoSuchMethodException |
            InvocationTargetException |
            IllegalAccessException e
        ) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    @ParameterizedTest
    @MethodSource
    public void testDigitsAreSequentialIncrementing(int number, boolean result) {
        try {
            var method = CarMileage.class.getDeclaredMethod("digitsAreSequentialIncrementing", String.class);
            method.setAccessible(true);
            assertEquals(result, method.invoke(CarMileage.class, Integer.toString(number)));
        } catch (
            NoSuchMethodException |
            InvocationTargetException |
            IllegalAccessException e
        ) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    @Test
    public void testDigitsAreSequentialDecrementing() {
        var p = requireNonNull(getPredicate("digitsAreSequentialDecrementing"));
        assertEquals(true, p.apply("9876543210"));
        assertEquals(true, p.apply("43210"));
        assertEquals(true, p.apply("65432"));
        assertEquals(false, p.apply("98765432101"));
        assertEquals(false, p.apply("65435"));
    }

    @Test
    public void testIsPalindrome() {
        var p = requireNonNull(getPredicate("isPalindrome"));
        assertEquals(true, p.apply("123321"));
        assertEquals(true, p.apply("333"));
        assertEquals(true, p.apply("1234567890987654321"));
        assertEquals(false, p.apply("1223321"));
        assertEquals(false, p.apply("1233021"));
    }

    private static List<Arguments> testDigitsAreSequentialIncrementing() {
        return List.of(
            Arguments.of(12345, true),
            Arguments.of(123450, false),
            Arguments.of(1234567890, true),
            Arguments.of(345, true),
            Arguments.of(7890, true)
        );
    }

    private static List<Arguments> testIsAnyDigitFollowedByAllZeros() {
        return List.of(
            Arguments.of(3000, true),
            Arguments.of(1000, true),
            Arguments.of(1110, false),
            Arguments.of(1220, false),
            Arguments.of(300, true)
        );
    }

    private static List<Arguments> testIsInteresting() {
        return List.of(
            Arguments.of(3, new int[]{1337, 256}, 0),
            Arguments.of(99, new int[]{1337, 256}, 1),
            Arguments.of(3236, new int[]{1337, 256}, 0),
            Arguments.of(11207, new int[]{}, 0), // 0
            Arguments.of(11208, new int[]{}, 0), // 0
            Arguments.of(11209, new int[]{}, 1), // 1
            Arguments.of(11210, new int[]{}, 1), // 1
            Arguments.of(11211, new int[]{}, 2), // 2
            Arguments.of(1335, new int[]{1337, 256}, 1), // 1
            Arguments.of(1336, new int[]{1337, 256}, 1), // 1
            Arguments.of(1337, new int[]{1337, 256}, 2), // 2
            Arguments.of(99919911, new int[]{}, 0)
        );
    }

    private @Nullable Function<String, Boolean> getPredicate(String name) {
        try {
            var method = CarMileage.class.getDeclaredMethod(name, String.class);
            method.setAccessible(true);
            return (String number) -> {
                try {
                    return (Boolean) method.invoke(CarMileage.class, number);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            };
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}