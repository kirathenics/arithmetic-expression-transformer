# Arithmetic Expression Transformer CLI Application

This is a Java application designed to read input files, process arithmetic expressions contained within, and write the results to output files. 

## Features

- Processes arithmetic expressions with correct operator precedence and parentheses.
- Multiple expression processing modes:
  - Manual parsing (without regular expressions).
- Unit tested with JUnit 5 + Allure Framework for beautiful test reports.

## TODO Features

- Supports multiple file formats: plain text, XML, JSON, YAML, HTML (planned).
- Supports file compression and encryption (planned).
- Multiple expression processing modes:
  - Regular expression-based parsing.
- Modular design to easily switch between different processing strategies.

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven

### Running the Application

Use the command line to run the application with parameters:
java -jar arithmetic-expression-transformer.jar <input-file-path> <output-file-path> <mode> 


Where:

- `<input-file-path>` — path to the input file
- `<output-file-path>` — path to the output file
- `<mode>` — processing mode: `manual` or `regex`


### Example

```bash
java -jar arithmetic-expression-transformer.jar input.txt output.txt manual
```

### Running Tests with Allure Reports
This project uses JUnit 5 and Allure Framework for test reporting.

Run Tests
```bash
mvn clean test
```

Generate Allure Report
```bash
mvn allure:report
```

Open Allure Report in Browser
```bash
mvn allure:serve
```