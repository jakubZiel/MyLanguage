package Lexer;

public enum TokenType {
    PAREN_L,
    PAREN_R,
    CURLY_L,
    CURLY_R,
    ANGLE_L,
    ANGLE_R,
    SQUARE_L,
    SQUARE_R,
    ARROW,
    DOT,
    COMA,
    SEMICOLON,
    ASSIGN,
    EOF,

    WHILE,
    IF,
    ELSE,
    RETURN,

    LIST,
    INT,
    DOUBLE,
    STRING_T,

    EQUAL,
    N_EQUAL,
    GREATER_OR_EQUAL,
    LESS_OR_EQUAL,
    OR,
    AND,

    ADD,
    SUBTRACT,
    DIVIDE,
    MULTIPLY,
    MODULO,

    NULL,

    IDENTIFIER,
    NUMBER_T,
}
