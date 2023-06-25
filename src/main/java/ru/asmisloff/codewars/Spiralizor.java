package ru.asmisloff.codewars;

public class Spiralizor {

    public static int[][] spiralize(int size) {
        final int[][] res = new int[size][size];
        int row = 0;
        int col = -1;
        int startRow, startCol;
        do {
            startRow = row;
            startCol = col;
            while (isCellAllowed(res, row, col + 1)) {
                res[row][++col] = 1;
            }
            while (isCellAllowed(res, row + 1, col)) {
                res[++row][col] = 1;
            }
            while (isCellAllowed(res, row, col - 1)) {
                res[row][--col] = 1;
            }
            while (isCellAllowed(res, row - 1, col)) {
                res[--row][col] = 1;
            }
        } while (row != startRow || col != startCol);
        return res;
    }

    private static boolean isCellAllowed(int[][] m, int row, int col) {
        final int lastIndex = m.length - 1;
        if (row < 0 || row > lastIndex || col < 0 || col > lastIndex) {
            return false;
        }
        int cnt = 0;
        if (col > 0) {
            cnt += m[row][col - 1];
        }
        if (row > 0) {
            cnt += m[row - 1][col];
        }
        if (col < lastIndex) {
            cnt += m[row][col + 1];
        }
        if (row < lastIndex) {
            cnt += m[row + 1][col];
        }

        return cnt <= 1;
    }

}
