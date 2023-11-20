package com.woniu.zerenliandemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @description 这是一个用来让你更加熟悉parallelStream的原理的实力
 * @date 2016年10月11日18:26:55
 * @version v1.0
 * @author wangguangdong 
 */
public class App {
    public static void main(String[] args) throws Exception {
        int num = 100000;
        // 构造一个10000个元素的集合
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(i);
        }
        // 统计并行执行list的线程
        Set<Thread> threadSet = new CopyOnWriteArraySet<>();
        ArrayList<Integer> result = new ArrayList<>(num);
        // 并行执行
        AtomicReference<Integer> i2 = new AtomicReference<>(0);
        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
        list.parallelStream().forEach(integer -> {
            Thread thread = Thread.currentThread();
            threadSet.add(thread);
            result.add(integer);
            i2.getAndSet(i2.get() + 1);
            copyOnWriteArrayList.add(integer);
        });
        System.out.println("i= " + i2.get());
        System.out.println("threadSet一共有" + threadSet.size() + "个线程");
        System.out.println("系统一个有"+Runtime.getRuntime().availableProcessors()+"个cpu");
        System.out.println("result有" + result.size() + "个元素");
        System.out.println("CopyOnWriteList有" + copyOnWriteArrayList.size() + "个元素");
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            list1.add(i);
            list2.add(i);
        }
        Set<Thread> threadSetTwo = new CopyOnWriteArraySet<>();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Thread threadA = new Thread(() -> {
            list1.parallelStream().forEach(integer -> {
                Thread thread = Thread.currentThread();
                // System.out.println("list1" + thread);
                threadSetTwo.add(thread);
            });
            countDownLatch.countDown();
        });
        Thread threadB = new Thread(() -> {
            list2.parallelStream().forEach(integer -> {
                Thread thread = Thread.currentThread();
                // System.out.println("list2" + thread);
                threadSetTwo.add(thread);
            });
            countDownLatch.countDown();
        });

        threadA.start();
        threadB.start();
        countDownLatch.await();
        System.out.print("threadSetTwo一共有" + threadSetTwo.size() + "个线程");

        System.out.println("---------------------------");
        System.out.println(threadSet);
        System.out.println(threadSetTwo);
        System.out.println("---------------------------");
        threadSetTwo.addAll(threadSet);
        System.out.println(threadSetTwo);
        System.out.println("threadSetTwo一共有" + threadSetTwo.size() + "个线程");
        System.out.println("系统一个有"+Runtime.getRuntime().availableProcessors()+"个cpu");
    }
}

