package ru.somarov.templates.java.pattern.composite;


import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collector;

interface ValueContainer extends Iterable<Integer> {}

class CompositePattern {


}

class SingleValue implements ValueContainer
{
    public int value;

    // please leave this constructor as-is
    public SingleValue(int value)
    {
        this.value = value;
    }

    @Override
    public Iterator<Integer> iterator() {
        return Collections.singleton(value).iterator();
    }

    @Override
    public void forEach(Consumer<? super Integer> action) {

        action.accept(value);
    }

    @Override
    public Spliterator<Integer> spliterator() {
        return Collections.singleton(value).spliterator();
    }
}

class ManyValues extends ArrayList<Integer> implements ValueContainer
{
}


class MyList extends ArrayList<ValueContainer>
{
    // please leave this constructor as-is
    public MyList(Collection<? extends ValueContainer> c)
    {
        super(c);
    }

    public int sum()
    {
        return this.stream().collect(Collector.of(
                () -> new int[1],
                (result, val) -> {
                    for(Integer i: val){
                        result[0] += i;
                    }
                },
                (result1, result2) -> {
                    result1[0] += result2[0];
                    return result1;
                },
                total -> total[0]

        ));
    }
}
