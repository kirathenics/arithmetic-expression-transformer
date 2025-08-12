package org.example.processors;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Expression processor implementation that uses regular expressions
 * to find and evaluate arithmetic expressions in the given text.
 * Supports integers, floating point numbers, optional signs, and operators (+, -, *, /)
 * with optional spaces. Keeps punctuation after the expression.
 */
//public class RegexExpressionProcessor implements ExpressionProcessor {
//
//    private static final Pattern INNER_BRACKETS_PATTERN = Pattern.compile("\\(([^()]+)\\)");
//    private static final Pattern SIMPLE_EXPR_PATTERN = Pattern.compile(
//            "-?\\d+(?:\\.\\d+)?(?:\\s*[-+*/]\\s*-?\\d+(?:\\.\\d+)?)+" // простое выражение без скобок
//    );
//
//    @Override
//    public String process(String input) {
//        if (input == null || input.isEmpty()) {
//            return input;
//        }
//
//        String result = input;
//
//        // Обрабатываем корректно замкнутые внутренние скобки, пока они есть
//        boolean found;
//        do {
//            found = false;
//            Matcher matcher = INNER_BRACKETS_PATTERN.matcher(result);
//            StringBuilder sb = new StringBuilder();
//            int lastEnd = 0;
//
//            while (matcher.find()) {
//                found = true;
//                String innerExpr = matcher.group(1);
//                String replacement;
//                try {
//                    double val = evalExpression(innerExpr);
//                    replacement = formatDouble(val);
//                } catch (EvaluationException e) {
//                    replacement = "[ERROR: " + e.getMessage() + "]";
//                } catch (Exception e) {
//                    replacement = "[ERROR: Unknown error]";
//                }
//
//                sb.append(result, lastEnd, matcher.start());
//                sb.append(replacement);
//                lastEnd = matcher.end();
//            }
//
//            if (found) {
//                sb.append(result.substring(lastEnd));
//                result = sb.toString();
//            }
//        } while (found);
//
//        // После обработки скобок — вычисляем простые выражения вне скобок
//        result = replaceSimpleExpressions(result);
//
//        return result;
//    }
//
//    private String replaceSimpleExpressions(String input) {
//        Matcher matcher = SIMPLE_EXPR_PATTERN.matcher(input);
//        StringBuilder sb = new StringBuilder();
//
//        while (matcher.find()) {
//            String expr = matcher.group();
//            String replacement;
//            try {
//                double val = evalExpression(expr);
//                replacement = formatDouble(val);
//            } catch (EvaluationException e) {
//                replacement = "[ERROR: " + e.getMessage() + "]";
//            } catch (Exception e) {
//                replacement = "[ERROR: Unknown error]";
//            }
//            matcher.appendReplacement(sb, replacement);
//        }
//        matcher.appendTail(sb);
//
//        return sb.toString();
//    }
//
//    private double evalExpression(String expr) throws EvaluationException {
//        List<String> tokens = tokenize(expr);
//        List<String> rpn = toRPN(tokens);
//        return evalRPN(rpn);
//    }
//
//    private List<String> tokenize(String expr) throws EvaluationException {
//        List<String> tokens = new ArrayList<>();
//        char[] chars = expr.toCharArray();
//        int i = 0;
//        while (i < chars.length) {
//            char c = chars[i];
//            if (Character.isWhitespace(c)) {
//                i++;
//                continue;
//            }
//
//            if ("+-*/".indexOf(c) >= 0) {
//                if (c == '-') {
//                    if (tokens.isEmpty() || "+-*/".contains(tokens.get(tokens.size() - 1))) {
//                        int start = i;
//                        do {
//                            i++;
//                        } while (i < chars.length && (Character.isDigit(chars[i]) || chars[i] == '.'));
//                        tokens.add(expr.substring(start, i));
//                        continue;
//                    }
//                }
//                tokens.add(String.valueOf(c));
//                i++;
//            } else if (Character.isDigit(c) || c == '.') {
//                int start = i;
//                while (i < chars.length && (Character.isDigit(chars[i]) || chars[i] == '.')) {
//                    i++;
//                }
//                tokens.add(expr.substring(start, i));
//            } else {
//                throw new EvaluationException("Unknown operator");
//            }
//        }
//        return tokens;
//    }
//
//    private List<String> toRPN(List<String> tokens) throws EvaluationException {
//        List<String> output = new ArrayList<>();
//        Deque<String> stack = new ArrayDeque<>();
//
//        for (String token : tokens) {
//            if (isNumber(token)) {
//                output.add(token);
//            } else if (isOperator(token)) {
//                while (!stack.isEmpty() && isOperator(stack.peek()) &&
//                        precedence(stack.peek()) >= precedence(token)) {
//                    output.add(stack.pop());
//                }
//                stack.push(token);
//            } else {
//                throw new EvaluationException("Unknown operator");
//            }
//        }
//        while (!stack.isEmpty()) {
//            output.add(stack.pop());
//        }
//        return output;
//    }
//
//    private boolean isNumber(String token) {
//        try {
//            Double.parseDouble(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    private boolean isOperator(String token) {
//        return "+-*/".contains(token);
//    }
//
//    private int precedence(String op) {
//        if (op.equals("+") || op.equals("-")) return 1;
//        if (op.equals("*") || op.equals("/")) return 2;
//        return 0;
//    }
//
//    private double evalRPN(List<String> tokens) throws EvaluationException {
//        Deque<Double> stack = new ArrayDeque<>();
//        for (String token : tokens) {
//            if (isNumber(token)) {
//                stack.push(Double.parseDouble(token));
//            } else if (isOperator(token)) {
//                if (stack.size() < 2) {
//                    throw new EvaluationException("Invalid expression");
//                }
//                double b = stack.pop();
//                double a = stack.pop();
//                double res = switch (token) {
//                    case "+" -> a + b;
//                    case "-" -> a - b;
//                    case "*" -> a * b;
//                    case "/" -> {
//                        if (b == 0) {
//                            throw new EvaluationException("Division by zero");
//                        }
//                        yield a / b;
//                    }
//                    default -> throw new EvaluationException("Unknown operator");
//                };
//                stack.push(res);
//            } else {
//                throw new EvaluationException("Unknown operator");
//            }
//        }
//        if (stack.size() != 1) {
//            throw new EvaluationException("Invalid expression");
//        }
//        return stack.pop();
//    }
//
//    private String formatDouble(double val) {
//        if (val == (long) val) {
//            return String.valueOf((long) val);
//        } else {
//            return String.valueOf(val);
//        }
//    }
//
//    private static class EvaluationException extends Exception {
//        public EvaluationException(String message) {
//            super(message);
//        }
//    }
//}





//public class RegexExpressionProcessor implements ExpressionProcessor {
//
//    private static final Pattern INNER_BRACKETS_PATTERN = Pattern.compile("\\(([^()]+)\\)");
//    private static final Pattern SIMPLE_EXPR_PATTERN = Pattern.compile(
//            "-?\\d+(?:\\.\\d+)?(?:\\s*[-+*/]\\s*-?\\d+(?:\\.\\d+)?)+"
//    );
//    // Допустимые символы в выражении (цифры, операторы, пробелы, точки)
//    private static final Pattern VALID_MATH_EXPR = Pattern.compile("[0-9+\\-*/.\\s]+");
//
//    @Override
//    public String process(String input) {
//        if (input == null || input.isEmpty()) {
//            return input;
//        }
//
//        String result = input;
//
//        // Обработка внутренних скобок
//        boolean found;
//        do {
//            found = false;
//            Matcher matcher = INNER_BRACKETS_PATTERN.matcher(result);
//            StringBuilder sb = new StringBuilder();
//            int lastEnd = 0;
//
//            while (matcher.find()) {
//                found = true;
//                String innerExpr = matcher.group(1);
//
//                String replacement;
//                if (isValidMathExpression(innerExpr)) {
//                    try {
//                        double val = evalExpression(innerExpr);
//                        replacement = formatDouble(val);
//                    } catch (Exception e) {
//                        // При ошибке вычисления оставляем исходный текст в скобках
//                        replacement = "(" + innerExpr + ")";
//                    }
//                } else {
//                    // Если внутри скобок не математическое выражение — не меняем
//                    replacement = "(" + innerExpr + ")";
//                }
//
//                sb.append(result, lastEnd, matcher.start());
//                sb.append(replacement);
//                lastEnd = matcher.end();
//            }
//
//            if (found) {
//                sb.append(result.substring(lastEnd));
//                result = sb.toString();
//            }
//        } while (found);
//
//        // Обработка простых выражений вне скобок
//        result = replaceSimpleExpressions(result);
//
//        return result;
//    }
//
//    private boolean isValidMathExpression(String expr) {
//        return VALID_MATH_EXPR.matcher(expr).matches();
//    }
//
//    private String replaceSimpleExpressions(String input) {
//        Matcher matcher = SIMPLE_EXPR_PATTERN.matcher(input);
//        StringBuilder sb = new StringBuilder();
//
//        while (matcher.find()) {
//            String expr = matcher.group();
//            String replacement;
//            if (isValidMathExpression(expr)) {
//                try {
//                    double val = evalExpression(expr);
//                    replacement = formatDouble(val);
//                } catch (Exception e) {
//                    // При ошибке вычисления не заменяем
//                    replacement = expr;
//                }
//            } else {
//                replacement = expr;
//            }
//            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
//        }
//        matcher.appendTail(sb);
//
//        return sb.toString();
//    }
//
//    // --- Методы вычисления выражения (токенизация, преобразование в RPN, вычисление RPN) ---
//
//    private double evalExpression(String expr) throws EvaluationException {
//        List<String> tokens = tokenize(expr);
//        List<String> rpn = toRPN(tokens);
//        return evalRPN(rpn);
//    }
//
//    private List<String> tokenize(String expr) throws EvaluationException {
//        List<String> tokens = new ArrayList<>();
//        char[] chars = expr.toCharArray();
//        int i = 0;
//        while (i < chars.length) {
//            char c = chars[i];
//            if (Character.isWhitespace(c)) {
//                i++;
//                continue;
//            }
//
//            if ("+-*/".indexOf(c) >= 0) {
//                // Обработка унарного минуса
//                if (c == '-') {
//                    if (tokens.isEmpty() || "+-*/".contains(tokens.get(tokens.size() - 1))) {
//                        int start = i;
//                        i++;
//                        while (i < chars.length && (Character.isDigit(chars[i]) || chars[i] == '.')) {
//                            i++;
//                        }
//                        tokens.add(expr.substring(start, i));
//                        continue;
//                    }
//                }
//                tokens.add(String.valueOf(c));
//                i++;
//            } else if (Character.isDigit(c) || c == '.') {
//                int start = i;
//                while (i < chars.length && (Character.isDigit(chars[i]) || chars[i] == '.')) {
//                    i++;
//                }
//                tokens.add(expr.substring(start, i));
//            } else {
//                throw new EvaluationException("Unknown operator or symbol: '" + c + "'");
//            }
//        }
//        return tokens;
//    }
//
//    private List<String> toRPN(List<String> tokens) throws EvaluationException {
//        List<String> output = new ArrayList<>();
//        Deque<String> stack = new ArrayDeque<>();
//
//        for (String token : tokens) {
//            if (isNumber(token)) {
//                output.add(token);
//            } else if (isOperator(token)) {
//                while (!stack.isEmpty() && isOperator(stack.peek()) &&
//                        precedence(stack.peek()) >= precedence(token)) {
//                    output.add(stack.pop());
//                }
//                stack.push(token);
//            } else {
//                throw new EvaluationException("Unknown operator: " + token);
//            }
//        }
//        while (!stack.isEmpty()) {
//            output.add(stack.pop());
//        }
//        return output;
//    }
//
//    private boolean isNumber(String token) {
//        try {
//            Double.parseDouble(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    private boolean isOperator(String token) {
//        return "+-*/".contains(token);
//    }
//
//    private int precedence(String op) {
//        if (op.equals("+") || op.equals("-")) return 1;
//        if (op.equals("*") || op.equals("/")) return 2;
//        return 0;
//    }
//
//    private double evalRPN(List<String> tokens) throws EvaluationException {
//        Deque<Double> stack = new ArrayDeque<>();
//        for (String token : tokens) {
//            if (isNumber(token)) {
//                stack.push(Double.parseDouble(token));
//            } else if (isOperator(token)) {
//                if (stack.size() < 2) {
//                    throw new EvaluationException("Invalid expression");
//                }
//                double b = stack.pop();
//                double a = stack.pop();
//                double res = switch (token) {
//                    case "+" -> a + b;
//                    case "-" -> a - b;
//                    case "*" -> a * b;
//                    case "/" -> {
//                        if (b == 0) throw new EvaluationException("Division by zero");
//                        yield a / b;
//                    }
//                    default -> throw new EvaluationException("Unknown operator");
//                };
//                stack.push(res);
//            } else {
//                throw new EvaluationException("Unknown operator");
//            }
//        }
//        if (stack.size() != 1) {
//            throw new EvaluationException("Invalid expression");
//        }
//        return stack.pop();
//    }
//
//    private String formatDouble(double val) {
//        if (val == (long) val) {
//            return String.valueOf((long) val);
//        } else {
//            return String.valueOf(val);
//        }
//    }
//
//    private static class EvaluationException extends Exception {
//        public EvaluationException(String message) {
//            super(message);
//        }
//    }
//}











//public class RegexExpressionProcessor implements ExpressionProcessor {
//
//    private static final Pattern INNER_BRACKETS_PATTERN = Pattern.compile("\\(([^()]+)\\)");
//    private static final Pattern SIMPLE_EXPR_PATTERN = Pattern.compile(
//            "-?\\d+(?:\\.\\d+)?(?:\\s*[-+*/]\\s*-?\\d+(?:\\.\\d+)?)+"
//    );
//    private static final Pattern VALID_MATH_EXPR = Pattern.compile("[0-9+\\-*/.\\s]+");
//
//    @Override
//    public String process(String input) {
//        if (input == null || input.isEmpty()) {
//            return input;
//        }
//
//        String result = input;
//
//        // Обработка внутренних скобок
//        boolean found;
//        do {
//            found = false;
//            Matcher matcher = INNER_BRACKETS_PATTERN.matcher(result);
//            StringBuilder sb = new StringBuilder();
//            int lastEnd = 0;
//
//            while (matcher.find()) {
//                found = true;
//                String innerExpr = matcher.group(1);
//
//                String replacement;
//                if (isValidMathExpression(innerExpr)) {
//                    try {
//                        double val = evalExpression(innerExpr);
//                        replacement = formatDouble(val);
//                    } catch (Exception e) {
//                        replacement = "(" + innerExpr + ")";
//                    }
//                } else {
//                    replacement = "(" + innerExpr + ")";
//                }
//
//                sb.append(result, lastEnd, matcher.start());
//                sb.append(replacement);
//                lastEnd = matcher.end();
//            }
//
//            if (found) {
//                sb.append(result.substring(lastEnd));
//                result = sb.toString();
//            }
//        } while (found);
//
//        // Обработка простых выражений вне скобок
//        result = replaceSimpleExpressions(result);
//
//        return result;
//    }
//
//    private boolean isValidMathExpression(String expr) {
//        return VALID_MATH_EXPR.matcher(expr).matches();
//    }
//
//    private String replaceSimpleExpressions(String input) {
//        Matcher matcher = SIMPLE_EXPR_PATTERN.matcher(input);
//        StringBuilder sb = new StringBuilder();
//
//        while (matcher.find()) {
//            String expr = matcher.group();
//            String replacement;
//            if (isValidMathExpression(expr)) {
//                try {
//                    double val = evalExpression(expr);
//                    replacement = formatDouble(val);
//                } catch (Exception e) {
//                    replacement = expr;
//                }
//            } else {
//                replacement = expr;
//            }
//            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
//        }
//        matcher.appendTail(sb);
//
//        return sb.toString();
//    }
//
//    private double evalExpression(String expr) throws EvaluationException {
//        List<String> tokens = tokenize(expr);
//        List<String> rpn = toRPN(tokens);
//        return evalRPN(rpn);
//    }
//
//    private List<String> tokenize(String expr) throws EvaluationException {
//        List<String> tokens = new ArrayList<>();
//        char[] chars = expr.toCharArray();
//        int i = 0;
//        while (i < chars.length) {
//            char c = chars[i];
//            if (Character.isWhitespace(c)) {
//                i++;
//                continue;
//            }
//
//            if ("+-*/".indexOf(c) >= 0) {
//                if (c == '-') {
//                    if (tokens.isEmpty() || "+-*/".contains(tokens.get(tokens.size() - 1))) {
//                        int start = i;
//                        i++;
//                        while (i < chars.length && (Character.isDigit(chars[i]) || chars[i] == '.')) {
//                            i++;
//                        }
//                        tokens.add(expr.substring(start, i));
//                        continue;
//                    }
//                }
//                tokens.add(String.valueOf(c));
//                i++;
//            } else if (Character.isDigit(c) || c == '.') {
//                int start = i;
//                while (i < chars.length && (Character.isDigit(chars[i]) || chars[i] == '.')) {
//                    i++;
//                }
//                tokens.add(expr.substring(start, i));
//            } else {
//                throw new EvaluationException("Unknown operator or symbol: '" + c + "'");
//            }
//        }
//        return tokens;
//    }
//
//    private List<String> toRPN(List<String> tokens) throws EvaluationException {
//        List<String> output = new ArrayList<>();
//        Deque<String> stack = new ArrayDeque<>();
//
//        for (String token : tokens) {
//            if (isNumber(token)) {
//                output.add(token);
//            } else if (isOperator(token)) {
//                while (!stack.isEmpty() && isOperator(stack.peek()) &&
//                        precedence(stack.peek()) >= precedence(token)) {
//                    output.add(stack.pop());
//                }
//                stack.push(token);
//            } else {
//                throw new EvaluationException("Unknown operator: " + token);
//            }
//        }
//        while (!stack.isEmpty()) {
//            output.add(stack.pop());
//        }
//        return output;
//    }
//
//    private boolean isNumber(String token) {
//        try {
//            Double.parseDouble(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    private boolean isOperator(String token) {
//        return "+-*/".contains(token);
//    }
//
//    private int precedence(String op) {
//        if (op.equals("+") || op.equals("-")) return 1;
//        if (op.equals("*") || op.equals("/")) return 2;
//        return 0;
//    }
//
//    private double evalRPN(List<String> tokens) throws EvaluationException {
//        Deque<Double> stack = new ArrayDeque<>();
//        for (String token : tokens) {
//            if (isNumber(token)) {
//                stack.push(Double.parseDouble(token));
//            } else if (isOperator(token)) {
//                if (stack.size() < 2) {
//                    throw new EvaluationException("Invalid expression");
//                }
//                double b = stack.pop();
//                double a = stack.pop();
//                double res;
//                switch (token) {
//                    case "+" -> res = a + b;
//                    case "-" -> res = a - b;
//                    case "*" -> res = a * b;
//                    case "/" -> {
//                        if (b == 0) {
//                            throw new EvaluationException("Division by zero");
//                        }
//                        res = a / b;
//                    }
//                    default -> throw new EvaluationException("Unknown operator");
//                }
//                stack.push(res);
//            } else {
//                throw new EvaluationException("Unknown operator");
//            }
//        }
//        if (stack.size() != 1) {
//            throw new EvaluationException("Invalid expression");
//        }
//        return stack.pop();
//    }
//
//    private String formatDouble(double val) {
//        if (val == (long) val) {
//            return String.valueOf((long) val);
//        } else {
//            return String.valueOf(val);
//        }
//    }
//
//    private static class EvaluationException extends Exception {
//        public EvaluationException(String message) {
//            super(message);
//        }
//    }
//}



public class RegexExpressionProcessor implements ExpressionProcessor {

    private static final Pattern INNER_BRACKETS_PATTERN = Pattern.compile("\\(([^()]+)\\)");
    private static final Pattern SIMPLE_EXPR_PATTERN = Pattern.compile(
            "-?\\d+(?:\\.\\d+)?(?:\\s*[-+*/]\\s*-?\\d+(?:\\.\\d+)?)+" // простое выражение без скобок
    );
    private static final Pattern VALID_MATH_EXPR = Pattern.compile("[0-9+\\-*/.\\s]+");


    @Override
    public String process(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String result = input;

        // Обрабатываем корректно замкнутые внутренние скобки, пока они есть
        boolean found;
        do {
            found = false;
            Matcher matcher = INNER_BRACKETS_PATTERN.matcher(result);
            StringBuilder sb = new StringBuilder();
            int lastEnd = 0;

//            while (matcher.find()) {
//                found = true;
//                String innerExpr = matcher.group(1);
//                String replacement;
//                if (isValidMathExpression(innerExpr)) {
//                    try {
//                        double val = evalExpression(innerExpr);
//                        replacement = formatDouble(val);
//                    } catch (Exception e) {
//                        replacement = "(" + innerExpr + ")";
//                    }
//                } else {
//                    replacement = "(" + innerExpr + ")";
//                }
//
//                sb.append(result, lastEnd, matcher.start());
//                sb.append(replacement);
//                lastEnd = matcher.end();
//            }

            while (matcher.find()) {
                String innerExpr = matcher.group(1);
                String replacement;
                if (isValidMathExpression(innerExpr)) {
                    try {
                        double val = evalExpression(innerExpr);
                        replacement = formatDouble(val);
                    } catch (Exception e) {
                        replacement = "(" + innerExpr + ")";
                    }
                } else {
                    replacement = "(" + innerExpr + ")";
                }

                sb.append(result, lastEnd, matcher.start());
                sb.append(replacement);
                lastEnd = matcher.end();

                // Сравниваем с исходной подстрокой — matcher.group(0) это вся скобочная часть, включая скобки
                if (!replacement.equals(matcher.group(0))) {
                    found = true; // Только если была реальная замена
                }
            }

            if (found) {
                sb.append(result.substring(lastEnd));
                result = sb.toString();
            }
        } while (found);

        // После обработки скобок — вычисляем простые выражения вне скобок
        result = replaceSimpleExpressions(result);

        return result;
    }

    private boolean isValidMathExpression(String expr) {
        return VALID_MATH_EXPR.matcher(expr).matches();
    }

    private String replaceSimpleExpressions(String input) {
        Matcher matcher = SIMPLE_EXPR_PATTERN.matcher(input);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String expr = matcher.group();
            String replacement;
            try {
                double val = evalExpression(expr);
                replacement = formatDouble(val);
            } catch (EvaluationException e) {
                replacement = "[ERROR: " + e.getMessage() + "]";
            } catch (Exception e) {
                replacement = "[ERROR: Unknown error]";
            }
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    private double evalExpression(String expr) throws EvaluationException {
        List<String> tokens = tokenize(expr);
        List<String> rpn = toRPN(tokens);
        return evalRPN(rpn);
    }

    private List<String> tokenize(String expr) throws EvaluationException {
        List<String> tokens = new ArrayList<>();
        char[] chars = expr.toCharArray();
        int i = 0;
        while (i < chars.length) {
            char c = chars[i];
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            if ("+-*/".indexOf(c) >= 0) {
                if (c == '-') {
                    if (tokens.isEmpty() || "+-*/".contains(tokens.get(tokens.size() - 1))) {
                        int start = i;
                        do {
                            i++;
                        } while (i < chars.length && (Character.isDigit(chars[i]) || chars[i] == '.'));
                        tokens.add(expr.substring(start, i));
                        continue;
                    }
                }
                tokens.add(String.valueOf(c));
                i++;
            } else if (Character.isDigit(c) || c == '.') {
                int start = i;
                while (i < chars.length && (Character.isDigit(chars[i]) || chars[i] == '.')) {
                    i++;
                }
                tokens.add(expr.substring(start, i));
            } else {
                throw new EvaluationException("Unknown operator");
            }
        }
        return tokens;
    }

    private List<String> toRPN(List<String> tokens) throws EvaluationException {
        List<String> output = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();

        for (String token : tokens) {
            if (isNumber(token)) {
                output.add(token);
            } else if (isOperator(token)) {
                while (!stack.isEmpty() && isOperator(stack.peek()) &&
                        precedence(stack.peek()) >= precedence(token)) {
                    output.add(stack.pop());
                }
                stack.push(token);
            } else {
                throw new EvaluationException("Unknown operator");
            }
        }
        while (!stack.isEmpty()) {
            output.add(stack.pop());
        }
        return output;
    }

    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isOperator(String token) {
        return "+-*/".contains(token);
    }

    private int precedence(String op) {
        if (op.equals("+") || op.equals("-")) return 1;
        if (op.equals("*") || op.equals("/")) return 2;
        return 0;
    }

    private double evalRPN(List<String> tokens) throws EvaluationException {
        Deque<Double> stack = new ArrayDeque<>();
        for (String token : tokens) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new EvaluationException("Invalid expression");
                }
                double b = stack.pop();
                double a = stack.pop();
                double res = switch (token) {
                    case "+" -> a + b;
                    case "-" -> a - b;
                    case "*" -> a * b;
                    case "/" -> {
                        if (b == 0) {
                            throw new EvaluationException("Division by zero");
                        }
                        yield a / b;
                    }
                    default -> throw new EvaluationException("Unknown operator");
                };
                stack.push(res);
            } else {
                throw new EvaluationException("Unknown operator");
            }
        }
        if (stack.size() != 1) {
            throw new EvaluationException("Invalid expression");
        }
        return stack.pop();
    }

    private String formatDouble(double val) {
        if (val == (long) val) {
            return String.valueOf((long) val);
        } else {
            return String.valueOf(val);
        }
    }

    private static class EvaluationException extends Exception {
        public EvaluationException(String message) {
            super(message);
        }
    }
}
