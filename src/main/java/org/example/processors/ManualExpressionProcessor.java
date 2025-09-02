package org.example.processors;

import org.example.processors.core.EvaluationException;
import org.example.processors.core.ExpressionEvaluator;
import org.example.processors.core.ExpressionParser;
import org.example.processors.core.MathUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Manual implementation of arithmetic expression processing without regular expressions.
 * Supports +, -, *, /, parentheses, nested expressions, negative numbers and decimal (double) values.
 * Provides error feedback on malformed expressions (unclosed brackets, unknown operators, division by zero).
 */
public class ManualExpressionProcessor implements ExpressionProcessor {

    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();

    /**
     * Processes the given input string by evaluating arithmetic expressions.
     * <p>
     * The method performs the following steps:
     * <ul>
     *   <li>Scans the text for matching parentheses and evaluates mathematical expressions inside them.</li>
     *   <li>Supports nested parentheses; evaluation starts from the innermost expressions.</li>
     *   <li>If the content inside parentheses is not a valid mathematical expression, it is left unchanged.</li>
     *   <li>After processing parentheses, evaluates plain (non-parenthesized) expressions in the text.</li>
     *   <li>Supports addition, subtraction, multiplication, division, decimal values, and negative numbers.</li>
     *   <li>Ignores unmatched parentheses and non-mathematical content inside them.</li>
     * </ul>
     *
     * @param input the input string possibly containing arithmetic expressions
     * @return a string with all valid expressions replaced by their evaluated results;
     *         non-mathematical content remains unchanged
     */
    @Override
    public String process(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

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
                    i++;
                    continue;
                }
                int start = openStack.pop();
                int end = i;
                String inner = sb.substring(start + 1, end);

                if (isPotentialExpression(inner)) {
                    String replacement = evaluator.evalExpression(inner);
                    sb.replace(start, end + 1, replacement);
                    i = start + replacement.length();
                } else {
                    i++;
                }
            } else {
                i++;
            }
        }

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
            if (Character.isDigit(ch) || ch == '-' || ch == '+') {
                int endPos = pos;
                boolean hasOperator = false;
                int lastMeaningfulIndex = -1;

                while (endPos < text.length()) {
                    char c = text.charAt(endPos);
                    if (Character.isDigit(c) || MathUtils.isOperator(c) || (c == '.' && MathUtils.isDecimalPoint(text, endPos))) {
                        lastMeaningfulIndex = endPos;
                        if (MathUtils.isOperator(c)) hasOperator = true;
                        endPos++;
                    } else if (Character.isWhitespace(c)) {
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
                    String replacement = evaluator.evalExpression(candidate);
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
            ExpressionParser parser = new ExpressionParser();
            List<String> tokens = parser.tokenize(expr);

            if (tokens.size() < 3) return false;
            boolean hasNumber = tokens.stream().anyMatch(MathUtils::isNumber);
            boolean hasOperator = tokens.stream().anyMatch(MathUtils::isOperator);
            return hasNumber && hasOperator;
        } catch (EvaluationException e) {
            return false;
        }
    }
}
