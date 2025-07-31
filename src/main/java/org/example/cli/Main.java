package org.example.cli;

import org.example.io.SimpleFileReader;
import org.example.io.SimpleFileWriter;
import org.example.processors.ExpressionProcessor;
import org.example.processors.RegexExpressionProcessor;
import org.example.processors.ManualExpressionProcessor;

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
            case "manual" -> processor = new ManualExpressionProcessor();
            case "regex"  -> processor = new RegexExpressionProcessor();
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
