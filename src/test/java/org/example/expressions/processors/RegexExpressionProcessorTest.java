package org.example.expressions.processors;

import io.qameta.allure.*;
import org.example.expressions.factory.RegexExpressionProcessorFactory;
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
    protected ExpressionProcessor createTestProcessor() {
        return new RegexExpressionProcessorFactory().createProcessor();
    }
}