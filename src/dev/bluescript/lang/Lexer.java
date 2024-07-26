package dev.bluescript.lang;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Lexer {
    private final String input;
    private int position;
    private char currentChar;

    private static final Set<String> KEYWORDS = Set.of(
            "var", "new", "try", "except", "for", "in", "as", "print"
    );

    @Contract(pure = true)
    public Lexer(@NotNull String input) {
        this.input = input;
        this.position = 0;
        this.currentChar = input.charAt(0);
    }

    private void advance() {
        position++;
        if (position >= input.length()) {
            currentChar = '\0'; // End of input
        } else {
            currentChar = input.charAt(position);
        }
    }

    private void skipWhitespace() {
        while (currentChar != '\0' && Character.isWhitespace(currentChar)) {
            advance();
        }
    }

    @Contract(" -> new")
    private @NotNull Token string() {
        StringBuilder result = new StringBuilder();
        advance(); // Skip opening "
        while (currentChar != '\0' && currentChar != '"') {
            result.append(currentChar);
            advance();
        }
        advance(); // Skip closing "
        return new Token(TokenType.STRING_LITERAL, result.toString());
    }

    @Contract(" -> new")
    private @NotNull Token number() {
        StringBuilder result = new StringBuilder();
        while (currentChar != '\0' && Character.isDigit(currentChar)) {
            result.append(currentChar);
            advance();
        }
        return new Token(TokenType.NUMBER_LITERAL, result.toString());
    }

    private @NotNull Token identifier() {
        StringBuilder result = new StringBuilder();
        while (currentChar != '\0' && Character.isLetterOrDigit(currentChar)) {
            result.append(currentChar);
            advance();
        }
        String text = result.toString();

        if (KEYWORDS.contains(text)) {
            return new Token(TokenType.valueOf(text.toUpperCase()), text);
        }

        return new Token(TokenType.IDENTIFIER, text);
    }
    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (currentChar != '\0') {
            if (Character.isWhitespace(currentChar)) {
                skipWhitespace();
                continue;
            }
            if (currentChar == '"') {
                tokens.add(string());
                continue;
            }
            if (Character.isDigit(currentChar)) {
                tokens.add(number());
                continue;
            }
            if (Character.isLetter(currentChar)) {
                tokens.add(identifier());
                continue;
            }
            switch (currentChar) {
                case '(': tokens.add(new Token(TokenType.LPAREN, "(")); break;
                case ')': tokens.add(new Token(TokenType.RPAREN, ")")); break;
                case '{': tokens.add(new Token(TokenType.LBRACE, "{")); break;
                case '}': tokens.add(new Token(TokenType.RBRACE, "}")); break;
                case '[': tokens.add(new Token(TokenType.LBRACKET, "[")); break;
                case ']': tokens.add(new Token(TokenType.RBRACKET, "]")); break;
                case ',': tokens.add(new Token(TokenType.COMMA, ",")); break;
                case '.': tokens.add(new Token(TokenType.DOT, ".")); break;
                case ':': tokens.add(new Token(TokenType.COLON, ":")); break;
                case ';': tokens.add(new Token(TokenType.SEMICOLON, ";")); break;
                case '=': tokens.add(new Token(TokenType.ASSIGN, "=")); break;
                default:
                    throw new RuntimeException("Unknown character: " + currentChar);
            }
            advance();
        }
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }
}
