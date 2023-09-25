package ru.asmisloff.codewars.logic;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class ConsecutionTest {

    @Test
    void combinations() {
        Consecution.combinations(3).forEach(a -> System.out.println(Arrays.toString(a)));
    }
}