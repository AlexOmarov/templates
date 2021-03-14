package ru.somarov.templates.java.pattern.singleton;

import java.util.*;
import java.util.stream.Collectors;


public class Testing {
}

class ExpressionProcessor
{
    public Map<Character, Integer> variables = new HashMap<>();

    public StringBuilder sb = new StringBuilder();
    public int calculate(String expression)
    {
        sb = new StringBuilder();
        variables.put('x',5);
        Element result = parse(lex(expression), null);
        System.out.println(expression + " = " + result.eval());
        return result.eval();
    }

    private List<Token> lex(String expression) {
        List<Token> tokens = new ArrayList<>();
        for(int i = 0; i < expression.length(); i++){
            char currentChar = expression.charAt(i);
            switch (currentChar){
                case '+':
                    tokens.add(new Token(Token.Type.PLUS,"+"));
                    break;
                case '-':
                    tokens.add(new Token(Token.Type.MINUS,"-"));
                    break;
                default:
                    if(tokens.size() != 0 && tokens.get(tokens.size() - 1).type == Token.Type.NUMBER){
                        return null;
                    }
                    StringBuilder sb = new StringBuilder("");
                    Integer variableValue = variables.get(currentChar);
                    if(variableValue != null) {
                        tokens.add(new Token(Token.Type.NUMBER, variableValue.toString()));
                        break;
                    }
                    for (int j = i; j < expression.length(); j++) {
                        if(Character.isDigit(expression.charAt(j))) {
                            sb.append(expression.charAt(j));
                            if (expression.length() - j == 1) {
                                tokens.add(new Token(Token.Type.NUMBER,sb.toString()));
                                break;
                            }
                            i++;
                        } else {
                            tokens.add(new Token(Token.Type.NUMBER,sb.toString()));
                            i--;
                            break;
                        }
                    }
                    break;
            }
        }
        return tokens;
    }
    private Element parse(List<Token> tokens, Element leftPart) {

        BinaryOperation result = new BinaryOperation();
        boolean haveLHS = false;
        if(leftPart != null) {
            result.left = leftPart;
            haveLHS = true;
        }
        if(tokens == null){
            return new Digit(0);
        }
        for(int i = 0; i < tokens.size();i++){
            Token token = tokens.get(i);
            switch (token.type) {
                case NUMBER:
                    Digit digit = new Digit(Integer.parseInt(token.text));
                    sb.append(token.text);
                    System.out.println(sb.toString());
                    if(!haveLHS) {
                        result.left = digit;
                        haveLHS = true;
                    } else {
                        result.right = digit;
                        return parse(tokens.stream().skip(i+1).collect(Collectors.toList()), result);
                    }
                    break;
                case PLUS:
                    sb.append(token.text);
                    System.out.println(sb.toString());
                    result.type = BinaryOperation.Type.ADDITION;
                    break;
                case MINUS:
                    sb.append(token.text);
                    System.out.println(sb.toString());
                    result.type = BinaryOperation.Type.SUBTRACTION;
                    break;
            }
        }
        result.type = BinaryOperation.Type.LAST;
        return result;
    }
}


class Demo {
    public static void main(String[] args) {
        int result = new ExpressionProcessor().calculate("1+x");
        System.out.println(result);
    }
}
interface Element {
    int eval();
}

class Digit implements Element {

    int value;

    public Digit(int value) {
        this.value = value;
    }

    @Override
    public int eval() {
        return value;
    }
}

class BinaryOperation implements Element {

    public enum Type {
        ADDITION,
        SUBTRACTION,
        LAST
    }
    public Type type;
    public Element left, right;
    @Override
    public int eval() {
        switch (type) {
            case ADDITION: return left.eval() + right.eval();
            case SUBTRACTION: return left.eval() - right.eval();
            default: return left.eval();
        }
    }
}


class Token {

    enum Type {
        NUMBER,
        PLUS,
        MINUS
    }


    public Token(Type type, String text) {
        this.type = type;
        this.text = text;
    }

    public Type type;
    public String text;

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", text='" + text + '\'' +
                '}';
    }
}