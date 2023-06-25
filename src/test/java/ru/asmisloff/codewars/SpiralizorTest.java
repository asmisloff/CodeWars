package ru.asmisloff.codewars;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class SpiralizorTest {

    @Test
    void test5() {
        var s = Spiralizor.spiralize(5);
        for (var row : s) {
            System.out.println(Arrays.toString(row));
        }
    }

    @Test
    void test10() {
        var s = Spiralizor.spiralize(10);
        for (var row : s) {
            System.out.println(Arrays.toString(row));
        }
    }

    @Test
    void test20() {
        var s = Spiralizor.spiralize(20);
        for (var row : s) {
            System.out.println(Arrays.toString(row));
        }
    }

}