package org.example;

public class MultTable {
    public static void main(String[] args) {
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                int mult = i * j;
                System.out.format("%d x %d = %d \n", i, j, mult);
            }
        }
    }
}
