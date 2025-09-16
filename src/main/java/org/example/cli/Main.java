package org.example.cli;

import org.example.io.SimpleFileReader;
import org.example.io.SimpleFileWriter;
import org.example.expressions.processors.ExpressionProcessor;
import org.example.expressions.processors.RegexExpressionProcessor;
import org.example.expressions.processors.ManualExpressionProcessor;
import org.example.expressions.core.ExpressionConverter;
import org.example.expressions.core.ExpressionEvaluator;
import org.example.expressions.core.ExpressionParser;
import org.example.expressions.core.ExpressionValidator;

public class Main {

    private static final int EXPECTED_ARGUMENT_COUNT = 3;

    public static void main(String[] args) {
        if (args.length != EXPECTED_ARGUMENT_COUNT) {
            System.out.println("""
                Usage: java -jar procedural.jar <inputFile> <outputFile> <mode>
                Modes:
                  manual  - implementation without RegEx
                  regex   - implementation with RegEx
                """);
            return;
        }

        String inputPath = args[0];
        String outputPath = args[1];
        String mode = args[2];

        ExpressionProcessor processor;

        switch (mode.toLowerCase()) {
            case "manual" -> processor = new ManualExpressionProcessor(new ExpressionEvaluator(new ExpressionParser(), new ExpressionConverter()), new ExpressionValidator(new ExpressionParser()));
            case "regex"  -> processor = new RegexExpressionProcessor(new ExpressionEvaluator(new ExpressionParser(), new ExpressionConverter()), new ExpressionValidator(new ExpressionParser()));
            default -> {
                System.err.println("Unknown mode: " + mode);
                return;
            }
        }

        try {
            String content = SimpleFileReader.read(inputPath);
            String result = processor.process(content);
            SimpleFileWriter.write(outputPath, result);
            System.out.println("File processed using mode '" + mode + "'");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
