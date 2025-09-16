package org.example.expressions.factory;

import org.example.expressions.processors.ExpressionProcessor;
import org.example.expressions.processors.ManualExpressionProcessor;

public class ManualExpressionProcessorFactory extends ExpressionProcessorFactory {
    @Override
    public ExpressionProcessor createProcessor() {
        return new ManualExpressionProcessor(createEvaluator(), createValidator());
    }
}
