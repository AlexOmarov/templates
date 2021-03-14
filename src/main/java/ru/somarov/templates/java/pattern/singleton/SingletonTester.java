package ru.somarov.templates.java.pattern.singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.*;


class EventStorage<T> {
    //Consumer is an actual event, which is performed every time we call all the events for a particular argument
    // (in our case it is a query)
    private int index = 0;
    private HashMap<Integer, Consumer<T>> events = new HashMap<>();


    //this adds new event to storage
    public int subscribe(Consumer<T> event) {
        events.put(index++,event);
        return index;
    }

    public void unsubscribe(int index){
        events.remove(index);
    }

    //This method invokes all of the events to a query (and ths events change query as they are meant to)
    public void fireAllEvents(T something) {
        for (Consumer<T> event : events.values()){
            event.accept(something);
        }
    }

}

class Game {
    //Here we define storage for events which will be fired to a query objects
    public EventStorage<Query<Creature, Creature.Attribute>> eventsForCreatures = new EventStorage<>();
    public ArrayList<Creature> creatures = new ArrayList<>();
}

interface Creature {

     int getBaseAttack();
     void setBaseAttack(int attack);
    int getAttack();
    int getDefense();
    int getBaseDefense();
    void setBaseDefense(int attack);
     String getName();
     void setName(String name);

    enum Attribute {
        ATTACK, DEFENSE
    }
}

abstract class MagicCreature implements Creature {


    protected int baseAttack, baseDefense;
    protected Game game;
    protected String name;
    protected ArrayList<Integer> modifierIndexes = new ArrayList<>();

    public ArrayList<Integer> getModifierIndexes() {
        return modifierIndexes;
    }

    public void setModifierIndexes(ArrayList<Integer> modifierIndexes) {
        this.modifierIndexes = modifierIndexes;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    @Override
    public void setBaseAttack(int attack) {
        this.baseAttack = attack;
    }

    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public int getBaseDefense() {
        return baseDefense;
    }

    @Override
    public void setBaseDefense(int defense) {
        this.baseDefense = defense;
    }

    @Override
    public int getAttack(){
        Query<Creature, Attribute> creatureQuery = new Query<>(this, Attribute.ATTACK);
        creatureQuery.setResult(baseAttack);
        game.eventsForCreatures.fireAllEvents(creatureQuery);
        return creatureQuery.getResult();
    }
    @Override
    public int getDefense(){
        Query<Creature, Attribute> creatureQuery = new Query<>(this, Attribute.DEFENSE);
        creatureQuery.setResult(baseDefense);
        game.eventsForCreatures.fireAllEvents(creatureQuery);
        return creatureQuery.getResult();
    }

    @Override
    public String toString() // avoid printing Game
    {
        return "Creature{" +
                "name='" + name + '\'' +
                ", attack=" + getAttack() + // !!!
                ", defense=" + getDefense() +
                '}';
    }

}

interface GoblinFolk extends Creature {
    void addGoblinModifier();
}

class Goblin extends MagicCreature implements GoblinFolk {

    public Goblin(Game game) {
        this.baseAttack = 1;
        this.baseDefense = 1;
        this.name = "Goblin";
        this.game = game;
    }
    @Override
    public void addGoblinModifier() {
        GoblinModifier goblinModifier = new GoblinModifier(game, GoblinFolk.class, this);
    }

}

class GoblinKing extends MagicCreature implements GoblinFolk {

    public GoblinKing(Game game) {
        this.baseAttack = 3;
        this.baseDefense = 3;
        this.game = game;
        this.name = "GoblinKing";
    }

    private void addGoblinKingModifier() {
        GoblinKingModifier goblinKingModifier = new GoblinKingModifier(game, Goblin.class);
    }

    @Override
    public void addGoblinModifier() {
        GoblinModifier goblinModifier = new GoblinModifier(game, GoblinFolk.class, this);
    }
}

class Query<T, V> {

    private T entity;
    private V attribute;
    private int result;

    public Query(T entity, V attribute){
        this.entity = entity;
        this.attribute = attribute;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public V getAttribute() {
        return attribute;
    }

    public void setAttribute(V attribute) {
        this.attribute = attribute;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}

interface EventCreator<T> extends AutoCloseable {

    public T getReason();
    public void setReason(T reason);
}

class GoblinKingModifier implements EventCreator<Creature> {

    private Game game;
    private int index;
    private Creature reason;

    public GoblinKingModifier(Game game, Class<? extends Creature> clazz) {
        this.game = game;
        this.index = game.eventsForCreatures.subscribe(q -> {
            if(clazz.isAssignableFrom(q.getEntity().getClass())  && q.getAttribute() == Creature.Attribute.ATTACK){
                q.setResult(q.getResult() + 1);
            }
        });
    }

    @Override
    public void close() {
        game.eventsForCreatures.unsubscribe(index);
    }

    @Override
    public Creature getReason() {
        return reason;
    }

    @Override
    public void setReason(Creature reason) {
        this.reason = reason;
    }
}


class GoblinModifier implements EventCreator<Creature> {

    private Game game;
    private int index;
    private Creature reason;

    public GoblinModifier(Game game, Class<? extends Creature> clazz, Creature reason) {
        this.game = game;
        this.index = game.eventsForCreatures.subscribe(q -> {
            if(clazz.isAssignableFrom(q.getEntity().getClass())  &&
                    q.getAttribute() == Creature.Attribute.DEFENSE &&
            q.getEntity() != reason){
                q.setResult(q.getResult() + 1);
            }
        });
    }

    @Override
    public void close() {
        game.eventsForCreatures.unsubscribe(index);
    }

    @Override
    public Creature getReason() {
        return reason;
    }

    @Override
    public void setReason(Creature reason) {
        this.reason = reason;
    }
}


class BrokerChainDemo
{
    public static void main(String[] args)
    {

        // modifiers can be piled up
       Game game = new Game();
       Creature goblin = new Goblin(game);
       game.creatures.add(goblin);
        System.out.println(goblin.toString());
       Creature goblin2 = new Goblin(game);
        System.out.println(goblin.toString());
        System.out.println(goblin2.toString());
       Creature goblinKing = new GoblinKing(game);
        System.out.println(goblin.toString());
        System.out.println(goblin2.toString());
        System.out.println(goblinKing.toString());
    }
}


