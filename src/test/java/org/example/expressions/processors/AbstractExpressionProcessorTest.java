package org.example.expressions.processors;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Abstract base class for testing any ExpressionProcessor implementation.
 */
@Epic("Expression Processing")
@Owner("Bondarenko Kirill")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public abstract class AbstractExpressionProcessorTest {

    private ExpressionProcessor processor;

    protected abstract ExpressionProcessor createProcessor();

    @BeforeEach
    void setUp() {
        processor = createProcessor();
    }

    @Step("Process expression: \"{input}\"")
    private String processExpression(String input) {
        return processor.process(input);
    }

    @Step("Verify: expected=\"{expected}\", actual=\"{actual}\"")
    private void verifyResultEquals(String expected, String actual) {
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Simple addition")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly sums two integers")
//    @Story("")
    void testSimpleAddition() {
        final String input = "The result is 2 + 2.";
        final String expected = "The result is 4.";

        String actual = processExpression(input);
        verifyResultEquals(expected, actual);
    }

    @Test
    @DisplayName("Floating-point addition")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly sums number with floating point")
    void testFloatingPointNumber() {
        final String input = "Pi is about 2.14 + 1.";
        final String expected = "Pi is about 3.14.";

        String actual = processExpression(input);
        verifyResultEquals(expected, actual);
    }

    @Test
    @DisplayName("Negative numbers")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Checks if processor correctly process negative numbers")
    void testNegativeNumbers() {
        final String input = "The sum is -5 + 3.";
        final String expected = "The sum is -2.";

        String actual = processExpression(input);
        verifyResultEquals(expected, actual);
    }

    @Test
    @DisplayName("Simple addition inside parentheses")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly sums two integers inside parentheses")
    void testSimpleAdditionWithParentheses() {
        final String input = "The result is (2 + 2).";
        final String expected = "The result is 4.";

        String actual = processExpression(input);
        verifyResultEquals(expected, actual);
    }

    @Test
    @DisplayName("Multiplication and parentheses")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly subtracts two integers inside parentheses")
    void testWithMultiplicationAndParentheses() {
        final String input = "Expression: (2 + 3 * (4 - 1))";
        final String expected = "Expression: 11";

        String actual = processExpression(input);
        verifyResultEquals(expected, actual);
    }

    @Test
    @DisplayName("Nested parentheses")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly multiplies two numbers inside parentheses")
    void testNestedParentheses() {
        final String input = "Nested: ((1 + 2) * (3 + 4))";
        final String expected = "Nested: 21";

        String actual = processExpression(input);
        verifyResultEquals(expected, actual);
    }

    @Test
    @DisplayName("Double numbers")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly multiplies two numbers inside parentheses")
    void testWithDoubleNumbers() {
        final String input = "Price: (3.5 * 2)";
        final String expected = "Price: 7";

        String actual = processExpression(input);
        verifyResultEquals(expected, actual);
    }

    @Test
    @DisplayName("Negative numbers inside parentheses")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Checks if processor correctly process negative numbers inside parentheses")
    void testNegativeNumbersWithParentheses() {
        final String input = "Balance: (-2 + -3)";
        final String expected = "Balance: -5";

        String actual = processExpression(input);
        verifyResultEquals(expected, actual);
    }

    @Test
    @DisplayName("Division")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly divides two numbers inside parentheses")
    void testDivision() {
        final String input = "Quotient: (10 / 2)";
        final String expected = "Quotient: 5";

        String actual = processExpression(input);
        verifyResultEquals(expected, actual);
    }

    @Test
    @DisplayName("Division by zero")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Checks if processor correctly handles the division by 0 operation")
    void testDivisionByZero() {
        final String input = "Failing: (5 / 0)";

        String actual = processExpression(input);

        Allure.step("Verify that result contains error markers", () -> {
            assertTrue(actual.contains("[ERROR"), "Expected error marker in result");
            assertTrue(actual.contains("Division by zero"), "Expected division by zero message");
        });
    }

    @Test
    @DisplayName("Unclosed parentheses")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Checks if processor correctly handles unclosed parentheses")
    void testUnclosedParentheses() {
        final String input = "Unclosed parentheses: (3 + (4 - 2)";
        final String expected = "Unclosed parentheses: (5";

        String actual = processExpression(input);
        verifyResultEquals(expected, actual);
    }

    @Test
    @DisplayName("Unclosed parentheses with text")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Checks if processor correctly handles unclosed parentheses with text")
    void testUnclosedParenthesesWithText() {
        final String input = "Unclosed parentheses: (test text (3 + (4 - 2) words)";
        final String expected = "Unclosed parentheses: (test text (5 words)";

        String actual = processExpression(input);
        verifyResultEquals(expected, actual);
    }

    @Test
    @DisplayName("Non-mathematical expression")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Checks if processor correctly handles non-mathematical expression")
    void testNotMath() {
        final String input = "Non-mathematical expression: test + words";

        String actual = processExpression(input);
        verifyResultEquals(input, actual);
    }

    @Test
    @DisplayName("Expression inside parentheses")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly handles expression inside parentheses")
    void testExpressionInsideParentheses() {
        final String input = "Test expression inside (parentheses)";

        String actual = processExpression(input);
        verifyResultEquals(input, actual);
    }

    @Test
    @DisplayName("Non-mathematical expression inside parentheses")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Checks if processor correctly handles non-mathematical expression inside parentheses")
    void testNonMathematicalExpression() {
        final String input = "Non-mathematical expression: (test + words)";

        String actual = processExpression(input);
        verifyResultEquals(input, actual);
    }

    @Test
    @DisplayName("Multiple expressions")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly handles multiple expressions in text")
    void testMultipleExpressionsInText() {
        final String input = "First: 1 + 2, Second: (2 * 3)";
        final String expected = "First: 3, Second: 6";

        String actual = processExpression(input);
        verifyResultEquals(expected, actual);
    }

    @Test
    @DisplayName("Complex mixed expressions")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly handles Complex mixed expressions in text")
    void testComplexMixedText() {
        final String input = "Area: 3.5 * 2, Perimeter: 2 * (3.5 + 2).";
        final String expected = "Area: 7, Perimeter: 11.";

        String actual = processExpression(input);
        verifyResultEquals(expected, actual);
    }

    @Test
    @DisplayName("Expression without spaces")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Checks if processor correctly handles expressions without spaces")
    void testExpressionWithoutSpaces() {
        final String input = "The value is 10+5.";
        final String expected = "The value is 15.";

        String actual = processExpression(input);
        verifyResultEquals(expected, actual);
    }

    @Test
    @DisplayName("Expression with extra spaces")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Checks if processor correctly handles expressions with extra spaces")
    void testExpressionWithExtraWhitespaces() {
        final String input = "The value is   7   -   2   .";
        final String expected = "The value is   5   .";

        String actual = processExpression(input);
        verifyResultEquals(expected, actual);
    }

    @Test
    @DisplayName("Expression with multiple punctuation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Checks if processor correctly handles expressions with multiple punctuation")
    void testExpressionWithMultiplePunctuation() {
        final String input = "The result is 2 + 2... Amazing!";
        final String expected = "The result is 4... Amazing!";

        String actual = processExpression(input);
        verifyResultEquals(expected, actual);
    }

    @Test
    @DisplayName("No expressions")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly handles no expressions in text")
    void testNoExpressions() {
        final String input = "There is nothing to compute.";

        String actual = processExpression(input);
        verifyResultEquals(input, actual);
    }

    @Test
    @DisplayName("Floating-point precision")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks if processor correctly computes floating-point precision operation")
    void testFloatingPointPrecision() {
        final String input = "Total: (0.1 + 0.2)";

        String actual = processExpression(input);

        Allure.step("Verify that result contains result with floating-point precision ", () -> assertTrue(actual.contains("0.3"), "Result without measurement error"));
    }
}
