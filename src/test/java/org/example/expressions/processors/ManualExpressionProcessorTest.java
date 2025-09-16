package org.example.expressions.processors;

import io.qameta.allure.*;
import org.example.expressions.core.ExpressionConverter;
import org.example.expressions.core.ExpressionEvaluator;
import org.example.expressions.core.ExpressionParser;
import org.example.expressions.core.ExpressionValidator;
import org.junit.jupiter.api.*;

/**
 * Unit tests for ManualExpressionProcessor using JUnit 5 and Allure.
 */
@Feature("Manual Parsing")
@Story("Evaluate math expressions manually")
@Tag("manual")
@DisplayName("ManualExpressionProcessor Tests")
class ManualExpressionProcessorTest extends AbstractExpressionProcessorTest {
    @Override
    protected ExpressionProcessor createProcessor() {
        return new ManualExpressionProcessor(new ExpressionEvaluator(new ExpressionParser(), new ExpressionConverter()), new ExpressionValidator(new ExpressionParser()));
    }
}