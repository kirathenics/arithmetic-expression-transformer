package org.example.expressions.factory;

import org.example.expressions.core.ExpressionConverter;
import org.example.expressions.core.ExpressionEvaluator;
import org.example.expressions.core.ExpressionParser;
import org.example.expressions.core.ExpressionValidator;
import org.example.expressions.processors.ExpressionProcessor;

public abstract class ExpressionProcessorFactory {
    public abstract ExpressionProcessor createProcessor();

    protected ExpressionEvaluator createEvaluator() {
        return new ExpressionEvaluator(new ExpressionParser(), new ExpressionConverter());
    }

    protected ExpressionValidator createValidator() {
        return new ExpressionValidator(new ExpressionParser());
    }
}

