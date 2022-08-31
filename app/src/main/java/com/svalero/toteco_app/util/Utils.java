package com.svalero.toteco_app.util;

public class Utils {
    public static double roundNumber(float number) {
        return Math.round(number * 100.0) / 100.0;
    }

    public static double roundNumber(double number) {
        return Math.round(number * 100.0) / 100.0;
    }
}
