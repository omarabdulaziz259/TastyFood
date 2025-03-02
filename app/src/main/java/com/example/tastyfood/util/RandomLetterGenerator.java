package com.example.tastyfood.util;
import java.util.Random;

public class RandomLetterGenerator {
    public static char getRandomLetter() {
        String allowedLetters = "abcdefghijklmnoprstvw"; // Excluding q, u, x, z
        Random random = new Random();
        return allowedLetters.charAt(random.nextInt(allowedLetters.length()));
    }
}