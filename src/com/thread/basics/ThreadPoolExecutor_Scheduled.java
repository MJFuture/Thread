package com.thread.basics;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.StampedLock;


public class ThreadPoolExecutor_Scheduled {
	//JDK8
	private final StampedLock sl = new StampedLock();
	
	
	
	private static volatile CountDownLatch downLatch = new CountDownLatch(2);
    static ThreadFactory threadFactory = new NameTreadFactory();//线程创建工厂
    static RejectedExecutionHandler handler = new MyIgnorePolicy();//拒绝策略
	/**
	 * 最大的特色是能够周期性执行异步任务，当调用schedule,scheduleAtFixedRate和scheduleWithFixedDelay方法时，
	 * 实际上是将提交的任务转换成的ScheduledFutureTask类，
	 */
	private static ScheduledThreadPoolExecutor executors =  new ScheduledThreadPoolExecutor(2, threadFactory, handler);
	//达到给定的延时时间后，执行任务。这里传入的是实现Runnable接口的任务，
	//因此通过ScheduledFuture.get()获取结果为null
//	public ScheduledFuture<?> schedule(Runnable command,long delay, TimeUnit unit);
	
	
	//达到给定的延时时间后，执行任务。这里传入的是实现Callable接口的任务，
	//因此，返回的是任务的最终计算结果
//	 public <V> ScheduledFuture<V> schedule(Callable<V> callable,long delay, TimeUnit unit);

	//是以上一个任务开始的时间计时，period时间过去后，
	//检测上一个任务是否执行完毕，如果上一个任务执行完毕，
	//则当前任务立即执行，如果上一个任务没有执行完毕，则需要等上一个任务执行完毕后立即执行
//	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
//	                                                  long initialDelay,
//	                                                  long period,
//	                                                  TimeUnit unit);
	//当达到延时时间initialDelay后，任务开始执行。上一个任务执行结束后到下一次
	//任务执行，中间延时时间间隔为delay。以这种方式，周期性执行任务。
//	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
//	                                                     long initialDelay,
//	                                                     long delay,
//	                                                     TimeUnit unit);
	
	public static void main(String[] args) throws Exception{
		executors.prestartAllCoreThreads();// 预启动所有核心线程
		for (int i = 0; i < 10; i++) {
			  MyTask task = new MyTask(String.valueOf(i));
			 executors.execute(task);
		}
		
		/**
		 * 2s後開始執行
		 */
		/*executors.schedule(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("schedule---執行");
			}
		}, 2, TimeUnit.SECONDS);*/
		/**
		 * schedule 的 使用
		 * 返回的結果
		 * 2s 線程執行.
		 */
		 /*RunnableScheduledFuture<String> t = (RunnableScheduledFuture<String>) executors.schedule(new Callable<String>() {
			@Override
			public String call() throws Exception {
				// TODO Auto-generated method stub
				return "sss";
			}
		}, 2, TimeUnit.SECONDS);//2s 執行線程
		 //返回結果
		System.out.println(t.get());*/
		
		
		/**
		 * scheduleWithFixedDelay 的使用
		 * 2s 線程執行. 等線程執行完後每個 5s 再次執行線程 能爲<=0s
		 */
		/*long s = System.currentTimeMillis();
		executors.scheduleWithFixedDelay(new Runnable() {
			 private final AtomicInteger mThreadNum = new AtomicInteger(1);
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("scheduleWithFixedDelay--在執行--執行次數："+mThreadNum.getAndIncrement());
				System.out.println(System.currentTimeMillis()-s);
			}
		}, 2, 5, TimeUnit.SECONDS); // 2s 線程執行. 等線程執行完後每個 5s 再次執行線程 能爲<=0s
		*/
		
		/**
		 * scheduleAtFixedRate 
		 * 
		 * 2s 執行後  2s+2s執行
		 */
		/*long ss = System.currentTimeMillis();
		executors.scheduleAtFixedRate(new Runnable() {
			 private final AtomicInteger mThreadNum = new AtomicInteger(1);
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("scheduleAtFixedRate--在執行--執行次數："+mThreadNum.getAndIncrement());
				System.out.println(System.currentTimeMillis()-ss);
				
			}
		}, 2, 2, TimeUnit.SECONDS);*/
		
	}
	
	
	
	
	
	
	/**
	 * 拒绝策略
	 * @author mjun
	 *
	 */
    public static class MyIgnorePolicy implements RejectedExecutionHandler {

        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            doLog(r, e);
        }

        private void doLog(Runnable r, ThreadPoolExecutor e) {
            // 可做日志记录等
            System.err.println( r.toString() + " rejected+拒絕執行");
          System.out.println("completedTaskCount: " + e.getCompletedTaskCount());
        }
    }
    /**
     *线程创建工厂 
     * @author mjun
     *
     */
    public static class NameTreadFactory implements ThreadFactory {
    	/*
    	 * 是一个提供原子操作的Integer类，通过线程安全的方式操作加减。
    	 * 提供原子操作来进行Integer的使用，因此十分适合高并发情况下的使用。
    	 */
        private final AtomicInteger mThreadNum = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "my-thread-" + mThreadNum.getAndIncrement());
            System.out.println(t.getName() + " has been created 正在創建");
            return t;
        }
    }
    /**
     * The Custom thread factory
     * 這是自定義 線程池模
     */
    public static class CustomThreadFactory implements ThreadFactory {
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
    
    static class MyTask implements Runnable {
        private String name;

        public MyTask(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                System.out.println(this.toString() + " is running! 正在執行");
                Thread.sleep(3000); //让任务执行慢点
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "MyTask [name=" + name + "]";
        }
    }
}
