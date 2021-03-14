package ru.somarov.templates.java.pattern.builder;

import java.util.HashMap;


class CodeBuilder
{
    ClassToBuild classToBuild;

    public CodeBuilder(String className)
    {
        classToBuild = new ClassToBuild(className, new HashMap<>());
    }

    public CodeBuilder addField(String name, String type)
    {
        classToBuild.getFields().put(name, type);
        return this;
    }

    @Override
    public String toString()
    {
        return classToBuild.toString();
    }
}

class ClassToBuild {
    private String name;
    private HashMap<String, String> fields;

    ClassToBuild(String name, HashMap<String, String> fields) {
        this.name = name;
        this.fields = fields;
    }

    public HashMap<String, String> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        return "public class " + name + "\n{" + formatFieldsForInput() + "\n}";
    }

    private String formatFieldsForInput(){
        StringBuilder sb = new StringBuilder();
        fields.forEach((k,v) -> sb.append("\n  public ").append(v).append(" ").append(k).append(";"));
        return sb.toString();
    }
}

class Demo {
    public static void main(String[] args) {
        CodeBuilder cb = new CodeBuilder("Person").addField("name","String").addField("age","int");
        System.out.println(cb);
    }
}
