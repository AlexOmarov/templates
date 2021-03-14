package ru.somarov.templates.java.concurrency;

import ru.somarov.templates.java.concurrency.my_tests.ThreadSafeCollectionDecorator;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<Integer>localNumbers = new ArrayList<>();
        localNumbers.add(1);
        ThreadSafeCollectionDecorator collection = new ThreadSafeCollectionDecorator(localNumbers);

        Integer potentialVulnurability;
        for(int i = 1; i<101;i++){
            collection.add(i);
        }

        Runnable reader = () -> {
            for(int i = 0;i < collection.size(); i++){
                System.out.println("Берем №" + i + "из коллекции. Значение: " + collection.get(i));
            }
        };
        Runnable writer = () -> {
            for(int i = 0;i < collection.size(); i++){
                collection.set(i, i+1);
                System.out.println("Задаем №" + i + "из коллекции. Значение: " + (i+1));

            }
        };
        reader.run();
        writer.run();

    }
}
