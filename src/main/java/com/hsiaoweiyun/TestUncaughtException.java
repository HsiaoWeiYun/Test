package com.hsiaoweiyun;

public class TestUncaughtException {

    private static Thread.UncaughtExceptionHandler uncaughtExceptionHandler = (thread, throwable)->{
        System.out.printf("Thread UncaughtExceptionHandler %s: %s%n", thread.getName(), throwable.getMessage());
    };

    public static void main(String[] args) {

        ThreadGroup threadGroup = new ThreadGroup("G1"){
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.printf("ThreadGroup UncaughtExceptionHandler %s: %s%n", t.getName(), e.getMessage());
            }
        };

        Thread t1 = new Thread(()->{
            throw new RuntimeException("T1 RuntimeException");
        }, "T1");

        t1.setUncaughtExceptionHandler(uncaughtExceptionHandler);

        Thread t2 = new Thread(threadGroup, ()->{
            throw new RuntimeException("T2 RuntimeException");
        }, "T2");

        Thread t3 = new Thread(threadGroup, ()->{
            throw new RuntimeException("T3 RuntimeException");
        }, "T3");

        t3.setUncaughtExceptionHandler(uncaughtExceptionHandler);

        Thread t4 = new Thread(()->{
            throw new RuntimeException("T4 RuntimeException");
        }, "T4");

        Thread t5 = new Thread(()->{
            throw new ThreadDeath();
        }, "T5");


        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        /*
         *   uncaughtException處理順序為
         *   1. ThreadGroup
         *   2. Thread
         *   3. 假若以上均無設定, 則檢查例外是否為ThreadDeath, 是的話不予理會, 否則執行Exception的printStackTrace()
         */
    }
}
