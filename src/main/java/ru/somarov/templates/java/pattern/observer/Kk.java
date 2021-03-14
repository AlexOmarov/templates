package ru.somarov.templates.java.pattern.observer;


import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Kk {
}

class Game implements Observer<Rat>
{
    List<Rat> rats = new ArrayList<>();
    @Override
    public void handle(Rat obj, EventType type) {
        switch (type) {

            case ENTER: if(!rats.contains(obj)) {rats.add(obj);}
                break;
            case DIE: if(rats.remove(obj)) {obj.attack = 1;}
                break;
            default:
                break;
        }
        rats.forEach(r -> r.attack = rats.size());
    }
}

interface Observer<T> {
    void handle(T obj, EventType type);
}


class Observable<T> {
    List<Observer<T>> observers = new ArrayList<>();

    public void subscribe(Observer<T> obs){
        observers.add(obs);
    }

    public boolean unsubscribe(Observer<T> obs){
        return observers.remove(obs);
    }
    protected void fireEvent(T obj, EventType type){
        observers.forEach(obs -> obs.handle(obj, type));
    }
}

class Rat extends Observable<Rat> implements Closeable
{
    private Game game;
    public int attack = 1;

    public Rat(Game game)
    {
        subscribe(game);
        fireEvent(this, EventType.ENTER);
    }

    @Override
    public void close() throws IOException
    {
        fireEvent(this, EventType.DIE);
    }
}

enum EventType {
    ENTER, DIE
}
