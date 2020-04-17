package com.thread.basics;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 創建帶名稱的線程池。以方便錯誤查詢
 * 指定業務名相關的名稱
 * @author mjun
 *
 */
public class ThreadPoolExecutorSetName {
	static	BlockingQueue<Runnable> workQueue;
	static  ThreadPoolExecutor executor = 
			new ThreadPoolExecutor(2, 2, 1, TimeUnit.MINUTES, workQueue,new CustomThreadFactory("測試線程池名稱"));
	static  ThreadPoolExecutor executors = 
			new ThreadPoolExecutor(2, 2, 1, TimeUnit.MINUTES, workQueue,new CustomThreadFactory("測試線程池名稱"));
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("線程1111");
				throw new NullPointerException(); 
			}
		});
		executors.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("線程1111");
			}
		});
		executor.shutdown();
		executors.shutdown();
	}

    /**
     * The Custom thread factory
     * 這是自定義 線程池模
     */
    static class CustomThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        CustomThreadFactory(String name) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                                  Thread.currentThread().getThreadGroup();
            if(null == name || name.isEmpty()) {
            	name="pool";
            }
            namePrefix = name + " - " + poolNumber.getAndIncrement() + "-thread-";
            
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                                  namePrefix + threadNumber.getAndIncrement(),
                                  0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
	
}
