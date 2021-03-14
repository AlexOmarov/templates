package ru.somarov.templates.java.pattern.mediator;

import java.util.ArrayList;
import java.util.List;


public class MediatorTest {
}



class Participant
{

    public int value = 0;
    private Mediator mediator;
    public Participant(Mediator mediator)
    {
        mediator.subscribe(this);
        this.mediator = mediator;
    }

    public void say(int n)
    {
        mediator.processMessage(n, this);
    }
}

class Mediator
{
    private List<Participant> people = new ArrayList<>();

    public void subscribe(Participant p) {
        people.add(p);
    }

    public void processMessage(int n, Participant participant) {
        people.forEach(p-> {if(p != participant) p.value = n; });
    }
}

class Person
{
    public String name;
    public ChatRoom room;
    private List<String> chatLog = new ArrayList<>();

    public Person(String name)
    {
        this.name = name;
    }

    public void receive(String sender, String message)
    {
        String s = sender + ": '" + message + "'";
        System.out.println("[" + name + "'s chat session] " + s);
        chatLog.add(s);
    }

    public void say(String message)
    {
        room.broadcast(name, message);
    }

    public void privateMessage(String who, String message)
    {
        room.message(name, who, message);
    }
}

class ChatRoom
{
    private List<Person> people = new ArrayList<>();

    public void broadcast(String source, String message)
    {
        for (Person person : people)
            if (!person.name.equals(source))
                person.receive(source, message);
    }

    public void join(Person p)
    {
        String joinMsg = p.name + " joins the chat";
        broadcast("room", joinMsg);

        p.room = this;
        people.add(p);
    }

    public void message(String source, String destination, String message)
    {
        people.stream()
                .filter(p -> p.name.equals(destination))
                .findFirst()
                .ifPresent(person -> person.receive(source, message));
    }
}

class ChatRoomDemo
{
    public static void main(String[] args)
    {
        Mediator m = new Mediator();
        Participant p = new Participant(m);
        Participant p1 = new Participant(m);
        Participant p2 = new Participant(m);
        p.say(3);
        System.out.println("p: " + p.value + ", p1: " + p1.value + ", p2: " + p2.value);
        p1.say(4);
        System.out.println("p: " + p.value + ", p1: " + p1.value + ", p2: " + p2.value);
    }
}