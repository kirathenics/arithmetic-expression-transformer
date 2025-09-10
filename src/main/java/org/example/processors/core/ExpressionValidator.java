package org.example.processors.core;

import java.util.List;
import java.util.regex.Pattern;

public class ExpressionValidator {

    private final ExpressionParser parser;

    public ExpressionValidator(ExpressionParser parser) {
        this.parser = parser;
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
    public boolean isPotentialExpression(String expr) {
        try {
            List<String> tokens = parser.tokenize(expr);
            if (tokens.size() < 3) return false;
            boolean hasNumber = tokens.stream().anyMatch(MathUtils::isNumber);
            boolean hasOperator = tokens.stream().anyMatch(MathUtils::isOperator);
            return hasNumber && hasOperator;
        } catch (EvaluationException e) {
            return false;
        }
    }

    /**
     * Checks if the provided expression string contains only valid
     * mathematical characters (digits, operators, decimal points, and spaces).
     *
     * @param validExpr the regex pattern to validate against
     * @param expr the expression to validate
     * @return true if the expression is a valid math expression format, false otherwise
     */
    public boolean isValidMathExpression(Pattern validExpr, String expr) {
        return expr != null && validExpr.matcher(expr).matches();
    }
}
