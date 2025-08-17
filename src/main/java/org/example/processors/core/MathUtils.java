package org.example.processors.core;

public class MathUtils {
    public static boolean isNumber(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(token);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isOperator(String s) {
        return "+".equals(s) || "-".equals(s) || "*".equals(s) || "/".equals(s);
    }

    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    public static int precedence(String op) {
        return switch (op) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            default -> 0;
        };
    }

    /**
     * Converts a double to string without unnecessary .0
     *
     * @param value value to format
     * @return string representation, e.g. "4" instead of "4.0"
     */
    private String formatDouble(double value) {
        if (value == (int) value) {
            return String.valueOf((int) value);
        } else {
            return String.valueOf(value);
        }
    }
}
