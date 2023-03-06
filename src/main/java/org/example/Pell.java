package org.example;

import java.util.Scanner;

public class Pell {
    public static void main(String[] args) {
        int result = 0;
        int pn1 = 1;
        int pn2;
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
