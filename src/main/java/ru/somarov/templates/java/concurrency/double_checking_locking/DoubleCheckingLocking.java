package ru.somarov.templates.java.concurrency.double_checking_locking;

public class DoubleCheckingLocking {
    // Broken multithreaded version
// "Double-Checked Locking" idiom
        private Helper helper; // --- Если здесь будет volatile, то все будет норм - чтение всегда будет только после полной записи всех полей объекта, -> мы не заэскейпим не полностью инициализированный объект

        public Helper getHelper() {
            if (helper == null) {
                synchronized (this) {
                    if (helper == null) {
                        helper = new Helper();
                    }
                }
            }
            return helper; //--- Тут можно получить недоинициализированный другим потоком объект!
        }

        // other functions and members...
    }
