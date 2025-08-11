package org.example.processors;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Manual implementation of arithmetic expression processing without regular expressions.
 * Supports +, -, *, /, parentheses, nested expressions, negative numbers and decimal (double) values.
 * Provides error feedback on malformed expressions (unclosed brackets, unknown operators, division by zero).
 */
public class ManualExpressionProcessor implements ExpressionProcessor {
    @Override
    public String process(String input) {
        StringBuilder sb = new StringBuilder(input);
        Deque<Integer> openStack = new ArrayDeque<>();

        int i = 0;
        while (i < sb.length()) {
            char c = sb.charAt(i);
            if (c == '(') {
                openStack.push(i);
                i++;
            } else if (c == ')') {
                if (openStack.isEmpty()) {
                    sb.insert(i, "[ERROR: Unmatched closing bracket at position " + i + "]");
                    i += ("[ERROR: Unmatched closing bracket at position " + i + "]").length() + 1;
                    continue;
                }
                int start = openStack.pop();
                int end = i;
                String inner = sb.substring(start + 1, end);
                String replacement = evalSafe(inner);
                sb.replace(start, end + 1, replacement);
                i = start + replacement.length();
            } else {
                i++;
            }
        }

        while (!openStack.isEmpty()) {
            int pos = openStack.pop();
            String err = "[ERROR: Unclosed bracket at position " + pos + "]";
            sb.insert(pos, err);
        }

        // Check for expressions without brackets
        sb = new StringBuilder(replacePlainExpressions(sb.toString()));

        return sb.toString();
    }

    /**
     * Replaces plain arithmetic expressions in the given text with their evaluated results.
     * <p>
     * The method scans through the text and detects sequences that look like arithmetic
     * expressions (e.g., "2 + 2" or "7 - 3"). If an expression is found, it is evaluated
     * and replaced with its computed value while preserving punctuation and spacing.
     * </p>
     *
     * <p>
     * Detection rules:
     * <ul>
     *   <li>Expressions can start with a digit, '+' or '-'.</li>
     *   <li>Expressions can contain digits, operators (+, -, *, /), decimal points, and whitespace.</li>
     *   <li>Valid decimal points are detected using {@code isDecimalPoint()}.</li>
     *   <li>Punctuation after the expression is preserved.</li>
     * </ul>
     * </p>
     *
     * @param text The input string possibly containing arithmetic expressions.
     * @return The processed string with evaluated expressions replaced by their results.
     */
    private String replacePlainExpressions(String text) {
        StringBuilder result = new StringBuilder();
        int pos = 0;

        while (pos < text.length()) {
            char ch = text.charAt(pos);
            if (isDigit(ch) || ch == '-' || ch == '+') {
                int endPos = pos;
                boolean hasOperator = false;
                int lastMeaningfulIndex = -1;

                while (endPos < text.length()) {
                    char c = text.charAt(endPos);
                    if (isDigit(c) || isOperator(c) || (c == '.' && isDecimalPoint(text, endPos))) {
                        lastMeaningfulIndex = endPos;
                        if (isOperator(c)) hasOperator = true;
                        endPos++;
                    } else if (isWhitespace(c)) {
                        endPos++;
                    } else {
                        break;
                    }
                }

                if (lastMeaningfulIndex == -1) {
                    result.append(text.charAt(pos));
                    pos++;
                    continue;
                }

                String candidate = text.substring(pos, lastMeaningfulIndex + 1);
                String punctuation = text.substring(lastMeaningfulIndex + 1, endPos);

                if (hasOperator && isPotentialExpression(candidate)) {
                    String replacement = evalSafe(candidate);
                    result.append(replacement).append(punctuation);
                    pos = endPos;
                    continue;
                }
            }
            result.append(text.charAt(pos));
            pos++;
        }
        return result.toString();
    }

    /**
     * Checks if the given string is a potential arithmetic expression.
     * <p>
     * The expression is considered "potential" if it can be tokenized
     * into at least three parts (e.g., operand, operator, operand).
     * This method does not fully validate the syntax but ensures that
     * the structure resembles an arithmetic operation.
     * </p>
     *
     * @param expr The expression string to check.
     * @return {@code true} if the string looks like a valid arithmetic expression, {@code false} otherwise.
     */
    private boolean isPotentialExpression(String expr) {
        try {
            List<String> tokens = tokenize(expr);
            return tokens.size() >= 3;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Safely evaluates an arithmetic expression and returns the formatted result.
     * <p>
     * This method first tokenizes the expression, converts it to postfix notation,
     * and then evaluates the postfix expression. The result is formatted into a string.
     * If an error occurs during tokenization, conversion, or evaluation
     * (e.g., invalid syntax or division by zero), an error message is returned instead of throwing an exception.
     * </p>
     *
     * @param expr The arithmetic expression to evaluate.
     * @return The result of the evaluation as a string, or an error message if evaluation fails.
     */
    private String evalSafe(String expr) {
        try {
            List<String> tokens = tokenize(expr);
            List<String> postfix = infixToPostfix(tokens);
            double value = evaluatePostfix(postfix);
            return formatDouble(value);
        } catch (IllegalArgumentException | ArithmeticException ex) {
            return "[ERROR: " + ex.getMessage() + "]";
        }
    }

    /**
     * Tokenizes a mathematical expression without using regex.
     * Supports negative numbers, decimals, parentheses and operators.
     *
     * @param expr the raw expression, e.g. "2 + 3*(4-1.5)"
     * @return list of tokens like ["2", "+", "3", "*", "(", "4", "-", "1.5", ")"]
     */
    private List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        StringBuilder number = new StringBuilder();
        boolean expectUnary = true; // at start or after operator/open parenthesis we can have unary minus

        for (int idx = 0; idx < expr.length(); idx++) {
            char ch = expr.charAt(idx);
            if (Character.isWhitespace(ch)) {
                continue;
            }

            if (isDigit(ch) || ch == '.') {
                number.append(ch);
                expectUnary = false;
            } else if (ch == '-' && expectUnary) {
                // unary minus, part of number
                number.append(ch);
                expectUnary = false;
            } else {
                if (!number.isEmpty()) {
                    tokens.add(number.toString());
                    number.setLength(0);
                }

                if (isOperator(ch)) {
                    tokens.add(String.valueOf(ch));
                    expectUnary = true;
                } else if (ch == '(' || ch == ')') {
                    tokens.add(String.valueOf(ch));
                    expectUnary = ch == '('; // after '(' can have unary
                } else {
                    throw new IllegalArgumentException("Unknown operator or symbol: '" + ch + "'");
                }
            }
        }

        if (!number.isEmpty()) {
            tokens.add(number.toString());
        }

        return tokens;
    }

    /**
     * Converts an infix token list to postfix (RPN) using Shunting Yard algorithm.
     *
     * @param tokens infix tokens
     * @return postfix token list
     */
    private List<String> infixToPostfix(List<String> tokens) {
        List<String> output = new ArrayList<>();
        Deque<String> ops = new ArrayDeque<>();

        for (String token : tokens) {
            if (isNumber(token)) {
                output.add(token);
            } else if (isOperator(token)) {
                while (!ops.isEmpty() && isOperator(ops.peek())) {
                    assert ops.peek() != null;
                    if (!(precedence(ops.peek()) >= precedence(token))) break;
                    output.add(ops.pop());
                }
                ops.push(token);
            } else if ("(".equals(token)) {
                ops.push(token);
            } else if (")".equals(token)) {
                boolean foundOpen = false;
                while (!ops.isEmpty()) {
                    String top = ops.pop();
                    if ("(".equals(top)) {
                        foundOpen = true;
                        break;
                    } else {
                        output.add(top);
                    }
                }
                if (!foundOpen) {
                    throw new IllegalArgumentException("Mismatched parentheses");
                }
            } else {
                throw new IllegalArgumentException("Unknown operator or symbol: '" + token + "'");
            }
        }

        while (!ops.isEmpty()) {
            String top = ops.pop();
            if ("(".equals(top) || ")".equals(top)) {
                throw new IllegalArgumentException("Mismatched parentheses");
            }
            output.add(top);
        }

        return output;
    }

    /**
     * Evaluates a postfix expression list.
     *
     * @param postfix RPN tokens
     * @return computed double result
     */
    private double evaluatePostfix(List<String> postfix) {
        Deque<Double> stack = new ArrayDeque<>();
        for (String token : postfix) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Invalid expression");
                }
                double b = stack.pop();
                double a = stack.pop();
                double res = switch (token) {
                    case "+" -> a + b;
                    case "-" -> a - b;
                    case "*" -> a * b;
                    case "/" -> {
                        if (b == 0) throw new ArithmeticException("Division by zero");
                        yield a / b;
                    }
                    default -> throw new IllegalArgumentException("Unknown operator: " + token);
                };
                stack.push(res);
            } else {
                throw new IllegalArgumentException("Invalid postfix token: " + token);
            }
        }
        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression: leftover values");
        }
        return stack.pop();
    }

    /**
     * Returns true if the string is a valid number (including negative and decimal) without regex.
     */
    private boolean isNumber(String token) {
        if (token == null || token.isEmpty()) return false;
        int len = token.length();
        int i = 0;
        boolean hasDecimal = false;

        if (token.charAt(0) == '+' || token.charAt(0) == '-') {
            if (len == 1) return false;
            i = 1;
        }

        for (; i < len; i++) {
            char c = token.charAt(i);
            if (c == '.') {
                if (hasDecimal) return false;
                hasDecimal = true;
            } else if (!isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private boolean isOperator(String s) {
        return "+".equals(s) || "-".equals(s) || "*".equals(s) || "/".equals(s);
    }

    private boolean isWhitespace(char c) {
        return c == ' ';
    }

    private boolean isDecimalPoint(String text, int index) {
        return index > 0 && index < text.length() - 1
                && Character.isDigit(text.charAt(index - 1))
                && Character.isDigit(text.charAt(index + 1));
    }

    private int precedence(String op) {
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
