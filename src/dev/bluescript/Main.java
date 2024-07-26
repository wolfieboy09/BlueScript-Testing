package dev.bluescript;

import dev.bluescript.lang.Interpreter;
import dev.bluescript.lang.Lexer;
import dev.bluescript.lang.Parser;
import dev.bluescript.lang.Token;
import dev.bluescript.lang.ast.Script;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            String scriptText = readFileAsString("scripts/some_script.bs");
            Lexer lexer = new Lexer(scriptText);
            List<Token> tokens = lexer.tokenize();
            Parser parser = new Parser(tokens);
            Script script = parser.parse();
            Interpreter interpreter = new Interpreter(script);
            interpreter.interpret();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readFileAsString(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}

