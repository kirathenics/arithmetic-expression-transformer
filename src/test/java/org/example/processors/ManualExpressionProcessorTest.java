package org.example.processors;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for ManualExpressionProcessor using JUnit 5 and Allure.
 */
@Epic("Expression Processing")
@Feature("Manual Parsing")
@Story("Evaluate math expressions manually")
@Owner("Bondarenko Kirill")
@Severity(SeverityLevel.CRITICAL)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@Tag("manual")
@DisplayName("ManualExpressionProcessor Tests")
class ManualExpressionProcessorTest {

    private ManualExpressionProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new ManualExpressionProcessor();
    }

    @Test
    @DisplayName("Simple addition")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly sums two integers inside parentheses")
    void testSimpleAddition() {
        String input = "The result is (2 + 2).";
        String output = processor.process(input);
        assertEquals("The result is 4.", output);
    }

    @Test
    @DisplayName("Multiplication and parentheses")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly subtracts two integers inside parentheses")
    void testWithMultiplicationAndParentheses() {
        String input = "Expression: (2 + 3 * (4 - 1))";
        String output = processor.process(input);
        assertEquals("Expression: 11", output);
    }

    @Test
    @DisplayName("Nested parentheses")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly multiplies two numbers inside parentheses")
    void testNestedParentheses() {
        String input = "Nested: ((1 + 2) * (3 + 4))";
        String output = processor.process(input);
        assertEquals("Nested: 21", output);
    }

    @Test
    @DisplayName("Double numbers")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly multiplies two numbers inside parentheses")
    void testWithDoubleNumbers() {
        String input = "Price: (3.5 * 2)";
        String output = processor.process(input);
        assertEquals("Price: 7", output);
    }

    @Test
    @DisplayName("Negative numbers")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Checks if processor correctly process negative numbers")
    void testNegativeNumbers() {
        String input = "Balance: (-2 + -3)";
        String output = processor.process(input);
        assertEquals("Balance: -5", output);
    }

    @Test
    @DisplayName("Division")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly divides two numbers inside parentheses")
    void testDivision() {
        String input = "Quotient: (10 / 2)";
        String output = processor.process(input);
        assertEquals("Quotient: 5", output);
    }

    @Test
    @DisplayName("Division by zero")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Checks if processor correctly handles the division by 0 operation")
    void testDivisionByZero() {
        String input = "Failing: (5 / 0)";
        String output = processor.process(input);
        assertTrue(output.contains("[ERROR"));
        assertTrue(output.contains("Division by zero"));
    }

    @Test
    @DisplayName("Unclosed parentheses")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Checks if processor correctly handles unclosed parentheses")
    void testUnclosedParentheses() {
        String input = "Bad input: (3 + (4 - 2)";
        String output = processor.process(input);
        assertTrue(output.contains("[ERROR"));
        assertTrue(output.contains("Unclosed bracket"));
    }

    @Test
    @DisplayName("Unknown operator")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Checks if processor correctly handles unknown operator")
    void testUnknownOperator() {
        String input = "Bad op: (2 $ 2)";
        String output = processor.process(input);
        assertTrue(output.contains("[ERROR"));
        assertTrue(output.contains("Unknown operator"));
    }

    @Test
    @DisplayName("Multiple expressions")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly handles multiple expressions in text")
    void testMultipleExpressionsInText() {
        String input = "First: (1 + 2), Second: (2 * 3)";
        String output = processor.process(input);
        assertEquals("First: 3, Second: 6", output);
    }

    @Test
    @DisplayName("No expressions")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly handles no expressions in text")
    void testNoExpressions() {
        String input = "There is nothing to compute.";
        String output = processor.process(input);
        assertEquals(input, output);
    }

    @Test
    @DisplayName("Floating-point precision")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly computes floating-point precision operation")
    void testFloatingPointPrecision() {
        String input = "Total: (0.1 + 0.2)";
        String output = processor.process(input);
        assertTrue(output.contains("0.3"));
    }
}