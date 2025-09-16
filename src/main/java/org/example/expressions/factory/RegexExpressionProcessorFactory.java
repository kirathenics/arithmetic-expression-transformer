package org.example.expressions.factory;

import org.example.expressions.processors.ExpressionProcessor;
import org.example.expressions.processors.RegexExpressionProcessor;

public class RegexExpressionProcessorFactory extends ExpressionProcessorFactory {
    @Override
    public ExpressionProcessor createProcessor() {
        return new RegexExpressionProcessor(createEvaluator(), createValidator());
    }
}
