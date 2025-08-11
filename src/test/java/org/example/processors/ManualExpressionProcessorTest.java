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
    @Description("Checks if processor correctly sums two integers")
    void testSimpleAddition() {
        String input = "The result is 2 + 2.";
        String expected = "The result is 4.";
        String actual = processor.process(input);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Floating-point addition")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly sums number with floating point")
    void testFloatingPointNumber() {
        String input = "Pi is about 2.14 + 1.";
        String expected = "Pi is about 3.14.";
        String actual = processor.process(input);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Negative numbers")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Checks if processor correctly process negative numbers")
    void testNegativeNumbers() {
        String input = "The sum is -5 + 3.";
        String expected = "The sum is -2.";
        String actual = processor.process(input);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Simple addition inside parentheses")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly sums two integers inside parentheses")
    void testSimpleAdditionWithParentheses() {
        String input = "The result is (2 + 2).";
        String expected = "The result is 4.";
        String actual = processor.process(input);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Multiplication and parentheses")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly subtracts two integers inside parentheses")
    void testWithMultiplicationAndParentheses() {
        String input = "Expression: (2 + 3 * (4 - 1))";
        String expected = "Expression: 11";
        String actual = processor.process(input);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Nested parentheses")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly multiplies two numbers inside parentheses")
    void testNestedParentheses() {
        String input = "Nested: ((1 + 2) * (3 + 4))";
        String expected = "Nested: 21";
        String actual = processor.process(input);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Double numbers")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly multiplies two numbers inside parentheses")
    void testWithDoubleNumbers() {
        String input = "Price: (3.5 * 2)";
        String expected = "Price: 7";
        String actual = processor.process(input);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Negative numbers inside parentheses")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Checks if processor correctly process negative numbers inside parentheses")
    void testNegativeNumbersWithParentheses() {
        String input = "Balance: (-2 + -3)";
        String expected = "Balance: -5";
        String actual = processor.process(input);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Division")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly divides two numbers inside parentheses")
    void testDivision() {
        String input = "Quotient: (10 / 2)";
        String expected = "Quotient: 5";
        String actual = processor.process(input);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Division by zero")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Checks if processor correctly handles the division by 0 operation")
    void testDivisionByZero() {
        String input = "Failing: (5 / 0)";
        String actual = processor.process(input);
        assertTrue(actual.contains("[ERROR"));
        assertTrue(actual.contains("Division by zero"));
    }

    @Test
    @DisplayName("Unclosed parentheses")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Checks if processor correctly handles unclosed parentheses")
    void testUnclosedParentheses() {
        String input = "Bad input: (3 + (4 - 2)";
        String actual = processor.process(input);
        assertTrue(actual.contains("[ERROR"));
        assertTrue(actual.contains("Unclosed bracket"));
    }

    @Test
    @DisplayName("Unknown operator")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Checks if processor correctly handles unknown operator")
    void testUnknownOperator() {
        String input = "Bad op: (2 $ 2)";
        String actual = processor.process(input);
        assertTrue(actual.contains("[ERROR"));
        assertTrue(actual.contains("Unknown operator"));
    }

    @Test
    @DisplayName("Multiple expressions")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly handles multiple expressions in text")
    void testMultipleExpressionsInText() {
        String input = "First: 1 + 2, Second: (2 * 3)";
        String expected ="First: 3, Second: 6";
        String actual = processor.process(input);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Complex mixed expressions")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly handles Complex mixed expressions in text")
    void testComplexMixedText() {
        String input = "Area: 3.5 * 2, Perimeter: 2 * (3.5 + 2).";
        String expected = "Area: 7, Perimeter: 11.";
        String actual = processor.process(input);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Expression without spaces")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Checks if processor correctly handles expressions without spaces")
    void testExpressionWithoutSpaces() {
        String input = "The value is 10+5.";
        String expected = "The value is 15.";
        String actual = processor.process(input);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Expression with extra spaces")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Checks if processor correctly handles expressions with extra spaces")
    void testExpressionWithExtraWhitespaces() {
        String input = "The value is   7   -   2   .";
        String expected = "The value is   5   .";
        String actual = processor.process(input);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Expression with multiple punctuation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Checks if processor correctly handles expressions with multiple punctuation")
    void testExpressionWithMultiplePunctuation() {
        String input = "The result is 2 + 2... Amazing!";
        String expected = "The result is 4... Amazing!";
        String actual = processor.process(input);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("No expressions")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly handles no expressions in text")
    void testNoExpressions() {
        String input = "There is nothing to compute.";
        String actual = processor.process(input);
        assertEquals(input, actual);
    }

    @Test
    @DisplayName("Floating-point precision")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly computes floating-point precision operation")
    void testFloatingPointPrecision() {
        String input = "Total: (0.1 + 0.2)";
        String actual = processor.process(input);
        assertTrue(actual.contains("0.3"));
    }
}