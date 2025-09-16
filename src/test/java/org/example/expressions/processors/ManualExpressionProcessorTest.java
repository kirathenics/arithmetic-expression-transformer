package org.example.expressions.processors;

import io.qameta.allure.*;
import org.example.expressions.factory.ManualExpressionProcessorFactory;
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
    protected ExpressionProcessor createTestProcessor() {
        return new ManualExpressionProcessorFactory().createProcessor();
    }
}