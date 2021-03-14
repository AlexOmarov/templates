package ru.somarov.templates.java.concurrency.thread_confinement.stack_confinement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

public class ThreadConfinementOfPrimitives {

    public boolean loadTheArk(Collection<Animal> candidates) {
        SortedSet<Animal> animals;
        //THIS VARIABLE IS ALWAYS THREAD CONFINED BECAUSE ALL PRIMITIVE TYPES ARE SO
        int numPairs = 0;
        Animal candidate = null;
        // animals confined to method, don't let them escape!
        animals = new TreeSet<>(new SpeciesGenderComparator());
        animals.addAll(candidates);
        for (Animal a : animals) {
            if (candidate == null || !candidate.isPotentialMate(a))
                candidate = a;
            else {
                ArrayList<AnimalPair> ark = new ArrayList<AnimalPair>();
                ark.add(new AnimalPair(candidate, a));
                ++numPairs;
                candidate = null;
            }
        }
        return numPairs == animals.size();
    }

}
