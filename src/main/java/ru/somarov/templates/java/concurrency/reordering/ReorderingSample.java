package ru.somarov.templates.java.concurrency.reordering;

public class ReorderingSample {

    String question;
    int answer;
    int maxAllowedValue;

    public ReorderingSample() {
        this.answer = 42;
        this.question = reverseEngineer(this.answer);
        this.maxAllowedValue = 9000;
    }

    private String reverseEngineer(int answer) {
        return null;
    }
}


/*
* Получится, что тот поток, который первый обнаружит, что data == null, выполнит следующие действия:
Выделит память под новый объект
Вызовет конструктор класса Data
  Запишет значение 42 в поле answer класса Data
  Запишет какую-то строку в поле question класса Data
  Запишет значение 9000 в поле maxAllowedValue класса Data
Запишет только что созданный объект в поле data класса Keeper
Чуете подвох? Ничто не мешает другому потоку увидеть произошедшее в пункте 6 до того,
* как он увидит произошедшее в пунктах 3-5.
* В результате этот поток увидит объект в некорректном состоянии,
* когда его поля ещё не были установлены.
* Такое, разумеется, никого не устроит, и потому есть жёсткий набор правил,
* по которым оптимизатору/компилятору/вашему злому двойнику запрещено выполнять reordering.
* */


/*
public class ReorderExample {
    private int a = 2;
    private boolean flg = false;

    public void method1() {
        a = 1;
        flg = true;
    }

    public void method2() {
        if (flg) {
            //2 might be printed out on some JVM/machines
            System.out.println("a = " + a);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            ReorderExample reorderExample = new ReorderExample();
            Thread thread1 = new Thread(() -> {
                reorderExample.method1();
            });
            Thread thread2 = new Thread(() -> {
                reorderExample.method2();
            });
            thread1.start();
            thread2.start();
        }
    }
}*/


//In above example declaring both variables as volatile won't solve the problem, the reason is volatile just solves
// the visibility problem. We have to use synchronized blocks (locks) to prevent the reordering.