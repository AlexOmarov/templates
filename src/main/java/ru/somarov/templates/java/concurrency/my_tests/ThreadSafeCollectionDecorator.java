package ru.somarov.templates.java.concurrency.my_tests;

import java.util.List;

public class ThreadSafeCollectionDecorator {
    //COLLECTION IS NOT THREAD SAFE - it's elements got published, but doesn't have any synch policy
    //To make it thread safe we need to
    private final List<Integer> numbers;
    public ThreadSafeCollectionDecorator(List<Integer> numbers){
        this.numbers = numbers;
    }

    public synchronized Integer get(Integer index){
        return numbers.get(index);
    }

    public synchronized Integer set(Integer index, Integer element){
        return numbers.set(index, element);
    }

    public synchronized void add(Integer element){
        numbers.add(element);
    }

    public Integer size(){
        return numbers.size();
    }
}
