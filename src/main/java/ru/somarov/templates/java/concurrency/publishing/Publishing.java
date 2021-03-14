package ru.somarov.templates.java.concurrency.publishing;

import java.util.HashSet;
import java.util.Set;

public class Publishing {

    public static Set<Secret> knownSecrets; // ---- опубликованная переменная. + Опубликованы все ее члены - секреты
    public void initialize() {
        knownSecrets = new HashSet<Secret>();
    }


}
