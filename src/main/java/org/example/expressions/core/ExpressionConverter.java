package org.example.expressions.core;

import java.util.*;

public class ExpressionConverter {

    /**
     * Converts an infix token list to postfix (RPN) using Shunting Yard algorithm.
     *
     * @param tokens infix tokens
     * @return postfix token list
     */
    public List<String> infixToPostfix(List<String> tokens) throws EvaluationException {
        List<String> output = new ArrayList<>();
        Deque<String> ops = new ArrayDeque<>();

        for (String token : tokens) {
            if (MathUtils.isNumber(token)) {
                output.add(token);
            } else if (MathUtils.isOperator(token)) {
                while (!ops.isEmpty() && MathUtils.isOperator(ops.peek())) {
                    assert ops.peek() != null;
                    if (!(MathUtils.precedence(ops.peek()) >= MathUtils.precedence(token))) break;
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
                    throw new EvaluationException("Mismatched parentheses");
                }
            } else {
                throw new EvaluationException("Unknown operator or symbol: '" + token + "'");
            }
        }
        while (!ops.isEmpty()) {
            String top = ops.pop();
            if ("(".equals(top) || ")".equals(top)) throw new EvaluationException("Mismatched parentheses");
            output.add(top);
        }
        return output;
    }
}
