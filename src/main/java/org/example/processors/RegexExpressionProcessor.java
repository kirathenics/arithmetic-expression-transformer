package org.example.processors;

import org.example.processors.core.ExpressionEvaluator;
import org.example.processors.core.ExpressionValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Expression processor implementation that uses regular expressions
 * to find and evaluate arithmetic expressions in the given text.
 * Supports integers, floating point numbers, optional signs, and operators (+, -, *, /)
 * with optional spaces. Keeps punctuation after the expression.
 */
public class RegexExpressionProcessor implements ExpressionProcessor {

    private static final Pattern INNER_BRACKETS_PATTERN = Pattern.compile("\\(([^()]+)\\)");
    private static final Pattern SIMPLE_EXPR_PATTERN = Pattern.compile(
            "-?\\d+(?:\\.\\d+)?(?:\\s*[-+*/]\\s*-?\\d+(?:\\.\\d+)?)+"
    );
    private static final Pattern VALID_MATH_EXPR = Pattern.compile("[0-9+\\-*/.\\s]+");

    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();
    private final ExpressionValidator validator;

    public RegexExpressionProcessor(ExpressionValidator validator) {
        this.validator = validator;
    }

    /**
     * Processes the given input string, evaluating mathematical expressions
     * inside parentheses and replacing them with their computed results.
     * Also evaluates plain expressions outside parentheses.
     *
     * @param input the input text containing potential mathematical expressions
     * @return the processed text with expressions replaced by results
     */
    @Override
    public String process(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String result = input;

        boolean found;
        do {
            found = false;
            Matcher matcher = INNER_BRACKETS_PATTERN.matcher(result);
            StringBuilder sb = new StringBuilder();
            int lastEnd = 0;

            while (matcher.find()) {
                String innerExpr = matcher.group(1);
                String replacement;
                if (validator.isValidMathExpression(VALID_MATH_EXPR, innerExpr)) {
                    try {
                        replacement = evaluator.evalExpression(innerExpr);
                    } catch (Exception e) {
                        replacement = "(" + innerExpr + ")";
                    }
                } else {
                    replacement = "(" + innerExpr + ")";
                }

                sb.append(result, lastEnd, matcher.start());
                sb.append(replacement);
                lastEnd = matcher.end();

                if (!replacement.equals(matcher.group(0))) {
                    found = true;
                }
            }

            if (found) {
                sb.append(result.substring(lastEnd));
                result = sb.toString();
            }
        } while (found);

        result = replaceSimpleExpressions(result);

        return result;
    }

    /**
     * Finds and evaluates all simple mathematical expressions in the given text
     * (expressions without parentheses) and replaces them with their results.
     *
     * @param input the text to search for simple expressions
     * @return the text with evaluated expressions replaced by results
     */
    private String replaceSimpleExpressions(String input) {
        Matcher matcher = SIMPLE_EXPR_PATTERN.matcher(input);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String expr = matcher.group();
            String replacement;
            try {
                replacement = evaluator.evalExpression(expr);
            } catch (Exception e) {
                replacement = "[ERROR: Unknown error]";
            }
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}
