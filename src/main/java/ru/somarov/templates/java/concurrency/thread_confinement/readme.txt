Thread confinement - привязка к потоку какой-то переменной. Не равно thread-safe! Но любая перенменная, которая конфайнт
автоматически thread safe.
Stack conf - обьект доступен только через локальные переменные. Такие переменные находятся в стэке потока и недоступны
для других потоков.
Ad-Hoc - делаем привязку ручками. Ответственность вся на разработчике
ThreadLocal - A more formal means of maintaining thread confinement is ThreadLocal, which allows you to associate a
per-thread value with a value-holding object.
Thread-local variables are often used to prevent sharing in designs based on mutable Singletons or global
variables. For example, a single-threaded application might maintain a global database connection that is
initialized at startup to avoid having to pass a Connection to every method. Since JDBC connections may
not be thread-safe, a multithreaded application that uses a global connection without additional
coordination is
not thread-safe either. By using a ThreadLocal to store the JDBC connection, as in
ConnectionHolder in Listing 3.10, each thread will have its own connection.