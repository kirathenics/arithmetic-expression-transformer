package org.example.processors;

import io.qameta.allure.*;
import org.example.processors.core.ExpressionParser;
import org.example.processors.core.ExpressionValidator;
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
        return new ManualExpressionProcessor(new ExpressionValidator(new ExpressionParser()));
    }
}