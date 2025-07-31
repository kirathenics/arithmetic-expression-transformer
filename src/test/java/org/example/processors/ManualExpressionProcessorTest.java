package org.example.processors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManualExpressionProcessorTest {
    private ManualExpressionProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new ManualExpressionProcessor();
    }

    @Test
    void testSimpleAddition() {
        String input = "The result is (2 + 2).";
        String output = processor.process(input);
        assertEquals("The result is 4.", output);
    }

    @Test
    void testWithMultiplicationAndParentheses() {
        String input = "Expression: (2 + 3 * (4 - 1))";
        String output = processor.process(input);
        assertEquals("Expression: 11", output);
    }

    @Test
    void testNestedParentheses() {
        String input = "Nested: ((1 + 2) * (3 + 4))";
        String output = processor.process(input);
        assertEquals("Nested: 21", output);
    }

    @Test
    void testWithDoubleNumbers() {
        String input = "Price: (3.5 * 2)";
        String output = processor.process(input);
        assertEquals("Price: 7", output);
    }

    @Test
    void testNegativeNumbers() {
        String input = "Balance: (-2 + -3)";
        String output = processor.process(input);
        assertEquals("Balance: -5", output);
    }

    @Test
    void testDivision() {
        String input = "Quotient: (10 / 2)";
        String output = processor.process(input);
        assertEquals("Quotient: 5", output);
    }

    @Test
    void testDivisionByZero() {
        String input = "Failing: (5 / 0)";
        String output = processor.process(input);
        assertTrue(output.contains("[ERROR"));
        assertTrue(output.contains("Division by zero"));
    }

    @Test
    void testUnclosedParentheses() {
        String input = "Bad input: (3 + (4 - 2)";
        String output = processor.process(input);
        assertTrue(output.contains("[ERROR"));
        assertTrue(output.contains("Unclosed bracket"));
    }

    @Test
    void testUnknownOperator() {
        String input = "Bad op: (2 $ 2)";
        String output = processor.process(input);
        assertTrue(output.contains("[ERROR"));
        assertTrue(output.contains("Unknown operator"));
    }

    @Test
    void testMultipleExpressionsInText() {
        String input = "First: (1 + 2), Second: (2 * 3)";
        String output = processor.process(input);
        assertEquals("First: 3, Second: 6", output);
    }

    @Test
    void testNoExpressions() {
        String input = "There is nothing to compute.";
        String output = processor.process(input);
        assertEquals(input, output);
    }

    @Test
    void testFloatingPointPrecision() {
        String input = "Total: (0.1 + 0.2)";
        String output = processor.process(input);
        assertTrue(output.contains("0.3"));
    }
}