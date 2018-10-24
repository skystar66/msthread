package com.many.thread;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyBlocingQueue<E> {


    private final List list;
    private final int limit;//有大小限制的

    public MyBlocingQueue(int limit) {
        this.limit = limit;
        this.list = new LinkedList();
    }


    //用wait，notify写的,在list空或者满的时候效率会高一点，因为wait释放锁，然后等待唤醒
    public synchronized void put(E e){
        while (list.size() == limit){
            try {
                wait();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        System.out.println("put list : " + list.toString());
        System.out.println("put : " + e);
        list.add(e);
        notifyAll();
    }
    public synchronized E take() {
        while (list.size() == 0){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("take list : " + list.toString());
        E remove = (E) list.remove(0);
        System.out.println("take : " + remove);
        notifyAll();
        return remove;
    }

    /********************    下面是测试区       *********************************/
    public static void main(String[] args) {
        final MyBlocingQueue<Integer> myBlocingQueue = new MyBlocingQueue(10);
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            exec.execute(new TestRunnable(myBlocingQueue));
        }
        exec.shutdown();
    }

    static class TestRunnable implements Runnable{
        private final MyBlocingQueue<Integer> myBlocingQueue;

        TestRunnable(MyBlocingQueue<Integer> myBlocingQueue) {
            this.myBlocingQueue = myBlocingQueue;
        }

//        @Override
        public void run() {
            Random random = new Random();
            int r = random.nextInt(100);
            //生成随机数,按照一定比率读取或者放入，可以更改！！！
            if (r < 30){
                myBlocingQueue.put(r);
            } else {
                myBlocingQueue.take();
            }
        }
    }




















}
