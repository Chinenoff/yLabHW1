package org.example;

import java.util.Scanner;

public class Pell {
    public static void main(String[] args) {
        long result = 0;
        long pn1 = 1;
        long pn2;
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            for (int i = 1; i <= n; i++) {
                pn2 = pn1;
                pn1 = result;
                result = 2 * pn1 + pn2;
            }
            System.out.println(result);
        }
    }
}
