package org.example.expressions.core;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class ExpressionEvaluator {
    private final ExpressionParser parser;
    private final ExpressionConverter converter;

    public ExpressionEvaluator(ExpressionParser parser, ExpressionConverter converter) {
        this.parser = parser;
        this.converter = converter;
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
    public String evalExpression(String expr) {
        try {
            List<String> tokens = parser.tokenize(expr);
            List<String> postfix = converter.infixToPostfix(tokens);
            double value = evaluatePostfix(postfix);
            return MathUtils.formatDouble(value);
        } catch (EvaluationException | ArithmeticException ex) {
            return "[ERROR: " + ex.getMessage() + "]";
        }
    }

    /**
     * Evaluates an expression in Reverse Polish Notation (RPN) form.
     *
     * @param tokens the RPN tokens
     * @return the evaluated result as a double
     * @throws EvaluationException if the expression is invalid or contains errors
     */
    private double evaluatePostfix(List<String> tokens) throws EvaluationException {
        Deque<Double> stack = new ArrayDeque<>();
        for (String token : tokens) {
            if (MathUtils.isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (MathUtils.isOperator(token)) {
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
}
