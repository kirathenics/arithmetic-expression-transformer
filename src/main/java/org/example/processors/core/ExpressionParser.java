package org.example.processors.core;

import java.util.ArrayList;
import java.util.List;

public class ExpressionParser {

    /**
     * Tokenizes a mathematical expression without using regex.
     * Supports negative numbers, decimals, parentheses and operators.
     *
     * @param expr the raw expression, e.g. "2 + 3*(4-1.5)"
     * @return list of tokens like ["2", "+", "3", "*", "(", "4", "-", "1.5", ")"]
     */
    public List<String> tokenize(String expr) throws EvaluationException {
        List<String> tokens = new ArrayList<>();
        StringBuilder number = new StringBuilder();
        boolean expectUnary = true; // at start or after operator/open parenthesis we can have unary minus

        for (char ch : expr.toCharArray()) {
            if (Character.isWhitespace(ch)) {
                continue;
            }

            if (Character.isDigit(ch) || ch == '.') {
                number.append(ch);
                expectUnary = false;
            } else if (ch == '-' && expectUnary) {
                number.append(ch);
                expectUnary = false;
            } else {
                if (!number.isEmpty()) {
                    tokens.add(number.toString());
                    number.setLength(0);
                }

                if (MathUtils.isOperator(ch)) {
                    tokens.add(String.valueOf(ch));
                    expectUnary = true;
                } else if (ch == '(' || ch == ')') {
                    tokens.add(String.valueOf(ch));
                    expectUnary = ch == '(';
                } else {
                    throw new EvaluationException("Unknown operator or symbol: '" + ch + "'");
                }
            }
        }
        if (!number.isEmpty()) {
            tokens.add(number.toString());
        }
        return tokens;
    }
}
