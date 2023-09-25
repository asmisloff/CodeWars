package ru.asmisloff.codewars.runes;

import java.util.Set;

interface Operator {
    int apply(int a, int b);
}

public class Runes {

    private static final Set<Character> knownOperators = Set.of('+', '-', '*');
    private static final int M = 1000000;

    public static int solveExpression(final String expression) {
        var tokens = parse(expression);
        var op = tokens.op();
        for (char c = '0'; c <= '9'; ++c) {
            var n1 = tokens.n1().option(c);
            if (n1 > M) continue;
            var n2 = tokens.n2().option(c);
            if (n2 > M) continue;
            var res = tokens.result().option(c);
            if (res > M) continue;
            if (op.apply(n1, n2) == res) {
                return c - '0';
            }
        }
        return -1;
    }

    private static Operator getOperator(char c) {
        return switch (c) {
            case '+' -> Integer::sum;
            case '-' -> (a, b) -> a - b;
            case '*' -> (a, b) -> a * b;
            default -> throw new IllegalArgumentException(String.format("Unknown operator %s", c));
        };
    }

    private static Tokens parse(String expression) {
        int opIdx = -1;
        int eqIdx = -1;
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (opIdx == -1 && knownOperators.contains(c) && i > 0) {
                opIdx = i;
            } else if (opIdx > -1 && c == '=') {
                eqIdx = i;
                break;
            }
        }
        if (opIdx == -1 || eqIdx == -1) {
            throw new IllegalArgumentException("Malformed expression");
        }
        var n1 = new PartialNumber(expression.substring(0, opIdx));
        var op = getOperator(expression.charAt(opIdx));
        var n2 = new PartialNumber(expression.substring(opIdx + 1, eqIdx));
        var res = new PartialNumber(expression.substring(eqIdx + 1));
        return new Tokens(n1, op, n2, res);
    }
}

record Tokens(PartialNumber n1, Operator op, PartialNumber n2, PartialNumber result) {
}

class PartialNumber {

    private final String src;

    public PartialNumber(String stringRepr) {
        src = stringRepr;
    }

    public int option(char digit) {
        String g = src.replace('?', digit);
        if (g.length() > 1 && g.charAt(0) == '0' || g.startsWith("-0") || src.indexOf(digit) != -1) {
            return 1000001;
        }
        return Integer.parseInt(g);
    }
}
