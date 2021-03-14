package ru.somarov.templates.java.pattern.memento;

import java.util.ArrayList;
import java.util.List;

public class MementoTest {
}

class Token
{
    public int value = 0;

    public Token(int value)
    {
        this.value = value;
    }

    public Token deepCopy(){
        return new Token(value);
    }
}

class Memento
{
    public List<Token> tokens = new ArrayList<>();
}

class TokenMachine
{
    public List<Token> tokens = new ArrayList<>();

    public List<Token> deepCopy(){

        List<Token> newTokens = new ArrayList<>();
        tokens.forEach(t -> newTokens.add(t.deepCopy()));
        return newTokens;
    }

    public Memento addToken(int value)
    {
        Memento memento = new Memento();
        tokens.add(new Token(value));
        memento.tokens = deepCopy();
        return memento;
    }

    public Memento addToken(Token token)
    {
        Memento memento = new Memento();
        tokens.add(token);
        memento.tokens = deepCopy();
        return memento;
    }

    public void revert(Memento m)
    {
        tokens = m.tokens;
    }
}

