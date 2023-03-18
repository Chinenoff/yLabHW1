package org.example.homework1;

import java.util.Random;
import java.util.Scanner;

public class Guess {
    public static void main(String[] args) {
        final int MAXATTEMPTS = 10;
        var trysCount = 0;
        var userNumber = 0;
        int number = new Random().nextInt(100); // здесь загадывается число от 1 до 99
        System.out.println("Я загадал число от 1 до 99. У тебя " + MAXATTEMPTS + " попыток угадать.");
        Scanner input = new Scanner(System.in);
        for (int i = 1; i <= MAXATTEMPTS; i++) {
            trysCount++;
            System.out.print("Введите ваше число: ");
            userNumber = input.nextInt();
            if (userNumber > number)
                System.out.format("Мое число меньше! У тебя осталось %d попыток \n", MAXATTEMPTS - trysCount);
            else if (userNumber < number)
                System.out.format("Мое число больше! У тебя осталось %d попыток \n", MAXATTEMPTS - trysCount);
            else {
                System.out.format("Ты угадал с %d попытки \n", trysCount);
                break;
            }
            if (trysCount == MAXATTEMPTS) {
                System.out.println("Ты не угадал!!!");
                break;
            }
        }
        System.out.println("GAME OVER");
    }
}
