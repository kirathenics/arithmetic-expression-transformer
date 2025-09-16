package org.example.expressions.processors;

import io.qameta.allure.*;
import org.example.expressions.core.ExpressionConverter;
import org.example.expressions.core.ExpressionEvaluator;
import org.example.expressions.core.ExpressionParser;
import org.example.expressions.core.ExpressionValidator;
import org.junit.jupiter.api.*;

/**
 * Unit tests for RegexExpressionProcessor using JUnit 5 and Allure.
 */
@Feature("RegEx Parsing")
@Story("Evaluate math expressions with RegEx")
@Tag("RegEx")
@DisplayName("RegexExpressionProcessor Tests")
class RegexExpressionProcessorTest extends AbstractExpressionProcessorTest {
    @Override
    protected ExpressionProcessor createProcessor() {
        return new RegexExpressionProcessor(new ExpressionEvaluator(new ExpressionParser(), new ExpressionConverter()), new ExpressionValidator(new ExpressionParser()));
    }
}