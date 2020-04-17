package com.thread.basics;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
/**
 * 信號量 內部是遞增 與  CyclicBarrier， CountDownLatch
 * 
 * 
 * 
 * Semaphore 只有3个操作：初始化， 增加，减少
 * 
 * @author mjun
 *
 */
public class ThreadSemaphore {

    public static void main(String[] args) {
    	
        ExecutorService executorService = Executors.newCachedThreadPool();
        System.out.println("是否啓動線程池   "+executorService.isShutdown());
        //信号量，只允许 3个线程同时访问
        Semaphore semaphore = new Semaphore(3);

        for (int i=0;i<10;i++){
            final long num = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        //获取许可
                        semaphore.acquire();
                        //执行
                        System.out.println("Accessing: " + num);
                        Thread.sleep(new Random().nextInt(5000)); // 模拟随机执行时长
                        //释放
                        semaphore.release();
                        System.out.println("Release..." + num);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executorService.shutdown();
//        executorService.shutdownNow();
        System.out.println("關閉的線程"+executorService.isShutdown());
    }

}
