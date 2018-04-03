package com.benedetto.lars.ashman.enums;

import java.util.Random;

public enum GhostColor {
    RED, ORANGE, PINK, BLUE,;

    public static GhostColor getRandomColor() {
        Random random = new Random();
        int rand = random.nextInt(4);
        switch (rand) {
            case 0:
                return RED;
            case 1:
                return ORANGE;
            case 2:
                return PINK;
            case 3:
                return BLUE;
        }
        return BLUE;
    }
}
