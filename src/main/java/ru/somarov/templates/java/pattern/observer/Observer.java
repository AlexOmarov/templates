/*
package ru.somarov.javatest.patterns.observer;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Observer {
}

class Game
{
    List<Rat> rats = new ArrayList<>();

    public void fire(Rat rat, RatEvent event){
        switch (event) {

            case ENTER: if(!rats.contains(rat)) {rats.add(rat); rats.forEach(r -> r.attack++);}
                break;
            case DIE: if(rats.remove(rat)) rats.forEach(r -> r.attack--);
                break;
                default:

        }
    }


    // todo
}

class Rat implements Closeable
{
    private Game game;
    public int attack = 1;

    public Rat(Game game)
    {
        this.game = game;
        game.fire(this, RatEvent.ENTER);
    }

    @Override
    public void close() */
/*throws IOException*//*

    {
        game.fire(this, RatEvent.DIE);
    }

}

enum RatEvent {
    ENTER, DIE
}


class Demo {

    public static void main(String[] args) {
        Game game = new Game();
        Rat one = new Rat(game);
        Rat two = new Rat(game);
        Rat three = new Rat(game);
        System.out.println(three.attack + ": " + two.attack + ": " + one.attack);
        one.close();
        System.out.println(three.attack + ": " + two.attack + ": " + one.attack);
    }
}*/
