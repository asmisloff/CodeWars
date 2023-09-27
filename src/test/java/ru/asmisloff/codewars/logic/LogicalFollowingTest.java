package ru.asmisloff.codewars.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static ru.asmisloff.codewars.logic.LogicalFollowing.*;

class LogicalFollowingTest {

    @Test
    @DisplayName("Сочетания")
    void combinations() {
        var expected = new boolean[][]{
            {false, false, false},
            {false, false, true},
            {false, true, false},
            {false, true, true},
            {true, false, false},
            {true, false, true},
            {true, true, false},
            {true, true, true}
        };
        var actual = new boolean[3];
        int i = 0;
        do {
            System.out.println(Arrays.toString(actual));
            assertArrayEquals(expected[i], actual);
            ++i;
        } while (!next(actual));
        assertEquals(expected.length, i);
    }

    @Test
    @DisplayName("Тавтология-01")
    void testTautology_01() {
        assertTrue(checkFollowing(and, and, 2));
    }

    @Test
    @DisplayName("Тавтология-02")
    void testTautology_02() {
        assertTrue(checkFollowing(not, not, 1));
    }

    @Test
    @DisplayName("Тавтология-03")
    void testTautology_03() {
        assertTrue(checkFollowing(impl, impl, 2));
    }

    @Test
    @DisplayName("Тавтология-04")
    void testTautology_04() {
        BooleanFunction implAndImpl = args -> {
            var a = args[0];
            var b = args[1];
            var c = args[2];
            return and.apply(impl.apply(a, b), impl.apply(b, c));
        };
        assertTrue(checkFollowing(implAndImpl, implAndImpl, 3));
    }

    @Test
    @DisplayName("И => НЕ И")
    void test() {
        assertFalse(checkFollowing(and, (args) -> !(args[0] && args[1]), 2));
    }

    @Test
    @DisplayName("1. из В=>A и (не В) следует (не А)")
    void option1() {
        BooleanFunction left = args -> impl.apply(args[1], args[0]) && !args[1];
        BooleanFunction right = args -> !args[0];
        boolean result = checkFollowing(left, right, 2);
        assertFalse(result);
    }

    @Test
    @DisplayName("2. из (A=>(B=>C)) следует (B=>(A=>C))")
    void option2() {
        BooleanFunction left = args -> impl(args[0], impl(args[1], args[2]));
        BooleanFunction right = args -> impl(args[1], impl(args[0], args[2]));
        boolean result = checkFollowing(left, right, 3);
        assertTrue(result);
    }

    @Test
    @DisplayName("3. из B=>A и (не А) следует (не В)")
    void option3() {
        BooleanFunction left = args -> impl(args[1], args[0]) && !args[0];
        BooleanFunction right = args -> !args[1];
        boolean result = checkFollowing(left, right, 2);
        assertTrue(result);
    }

    @Test
    @DisplayName("4. из (A=>B) и (A=>(не B)) следует (не A)")
    void option4() {
        BooleanFunction left = args -> impl(args[0], args[1]) && impl(args[0], !args[1]);
        BooleanFunction right = args -> !args[0];
        boolean result = checkFollowing(left, right, 2);
        assertTrue(result);
    }

    @Test
    @DisplayName("5. из A=>B и (не B) следует (не A)")
    void option5() {
        BooleanFunction left = args -> impl(args[0], args[1]) && !args[1];
        BooleanFunction right = args -> !args[0];
        boolean result = checkFollowing(left, right, 2);
        assertTrue(result);
    }

    @Test
    @DisplayName("6. из (A=>B) и (B=>C) следует (A=>C)")
    void option6() {
        BooleanFunction left = args -> impl(args[0], args[1]) && impl(args[1], args[2]);
        BooleanFunction right = args -> impl(args[0], args[2]);
        boolean result = checkFollowing(left, right, 3);
        assertTrue(result);
    }

    @Test
    @DisplayName("7. из (А=>(B=>C)) следует ((A=>B)=>C)")
    void option7() {
        BooleanFunction left = args -> impl(args[0], impl(args[1], args[2]));
        BooleanFunction right = args -> impl(impl(args[0], args[1]), args[2]);
        boolean result = checkFollowing(left, right, 3);
        assertFalse(result);
    }
}