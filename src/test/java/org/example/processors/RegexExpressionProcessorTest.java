package org.example.processors;

import io.qameta.allure.*;
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
        return new RegexExpressionProcessor();
    }
}