package com.thread.basics;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 此類記錄線關鍵函數的使用
 * @author mjun
 * 
 * yeid() 与 sleep() 区别在于  
 * 当线程调用sleep 方法时调用线程会被阻塞挂起指定时间，在这期间线程调度器不会去调度该线程。
 * 而调用 yieid 方法时，线程只是让出自己剩余的时间片，并没有被阻塞挂起，而是处于就绪状态，线程调度器下一次调度室就有可能调度到当前线程执行。
 */
public class ThreadFunctionUse {
	//模拟死锁操作变量
	private static String strA= "A";
	private static String strB= "B";
	//多线程访问资源问题   所以创建一个线程变量
	private static ThreadLocal<String> local = new ThreadLocal<>();
	//创建线程变量
	private static ThreadLocal<String> threadLocal = new InheritableThreadLocal<String>();
	//创建一个独占锁
	private static final Lock lock = new ReentrantLock();
	
	public static void main(String[] args) throws Exception{
//		new ThreadFunctionUse().lock();
//		new ThreadFunctionUse().yieid();
//		new ThreadFunctionUse().interruptedException();
//		new ThreadFunctionUse().DeadLock();
//		new ThreadFunctionUse().Daemon();
//		new ThreadFunctionUse().ThreadLocal();
//		new ThreadFunctionUse().ThreadLocal_two();
//		new ThreadFunctionUse().inheritableThreadLocal();
//		new ThreadFunctionUse().ThreadVolatile();
		new ThreadFunctionUse().ThreadPark();
	}
	/**
	 * 简单的独占锁的使用
	 */
	public void lock() {
		//创建线程A
		Thread thread_A = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//获取独占锁
				lock.lock();
				System.out.println("thread_A 获取到 锁");
				//释放锁
				lock.unlock();
			}
		});
		//创建线程B
		Thread thread_B = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//获取独占锁
				lock.lock();
				System.out.println("thread_B 获取到 锁");
				//释放锁
				lock.unlock();
			}
		});
		//创建线程C
		Thread thread_C= new Thread(()->{
			lock.lock();
			System.out.println("thread_C 获取到 锁");
			//释放锁
			lock.unlock();
		}); 
		Thread thread_D= new Thread(()->{
			lock.lock();
			System.out.println("thread_D 获取到 锁");
			//释放锁
			lock.unlock();
		}); 
			
		thread_A.start();
		thread_B.start();
		thread_C.start();
		thread_D.start();
		
	}
	/**
	 * 让出cpu执行权的 yieid()方法
	 * 当使用yieid 时 当前线程让出cpu执行权，然后处于就绪状态。线程调度器会从线程就绪队列中获取一个线程优先级最高的线程执行。
	 * -
	 */
	public void yieid() {
		Thread thread= new Thread(()->{
			//当i=0 时让出cpu执行权，放弃时间片，进行下一轮调度
			for (int i = 0; i < 5; i++) {
				if((i % 5)== 0) {
					System.out.println(Thread.currentThread()+ "  thread   yieid cpu ");
					//当前线程让出cpu执行权，放弃时间片，进行下一轮调度
					Thread.yield();
				}
			}
			System.out.println(Thread.currentThread()+ " thread   is over");
		}); 
		Thread thread_A= new Thread(()->{
			//当i=0 时让出cpu执行权，放弃时间片，进行下一轮调度
			for (int i = 0; i < 5; i++) {
				if((i % 5)== 0) {
					System.out.println(Thread.currentThread()+"thread_A   yieid cpu ");
					//当前线程让出cpu执行权，放弃时间片，进行下一轮调度
					Thread.yield();
				}
			}
			System.out.println(Thread.currentThread()+"thread_A   is over");
		}); 
		Thread thread_B= new Thread(()->{
			//当i=0 时让出cpu执行权，放弃时间片，进行下一轮调度
			for (int i = 0; i < 5; i++) {
				if((i % 5)== 0) {
					System.out.println(Thread.currentThread()+"  thread_B   yieid cpu ");
					//当前线程让出cpu执行权，放弃时间片，进行下一轮调度
					Thread.yield();
				}
			}
			System.out.println(Thread.currentThread()+"   thread_B   is over");
		}); 
		thread.start();
		thread_A.start();
		thread_B.start();
	}
	
	/**
	 * 线程的中断
	 */
	public void interruptedException() throws Exception{
		
		Thread thread = new Thread(()->{
			// isInterrupted() 如果已中断 返回 true 否则返回  false
			while (!Thread.currentThread().isInterrupted()) {
					System.out.println("interrupted()---"+Thread.currentThread().interrupted()+"      isInterrupted()---"+Thread.currentThread().isInterrupted()+"--线程未中断");
			}
		});
		//启动子线程
		thread.start();
		//主线程休眠 1s
		Thread.sleep(1000);
		//中断子线程
		System.out.println("开始中断线程");
		thread.interrupt();
		//等待子线程执行完毕
		thread.join();
		System.out.println("线程已中断");
		
	}
	/**
	 * 死锁
	 * 如何避免死锁，必须保证资源的有序分配
	 * @throws Exception
	 */
	public void DeadLock() throws Exception{
		
		Thread threadA = new Thread(()->{
			synchronized (strA) {
				System.out.println(" threadA  获取得资源A");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			synchronized (strB) {
				System.out.println("获取资源B");
			}
		});
		Thread threadB = new Thread(()->{
			synchronized (strA) {
				System.out.println("threadB  获取得资源B");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			synchronized (strB) {
				System.out.println("获取资源A");
			}
		});
		threadA.start();
		threadB.start();
	}
	/**
	 * 守护线程
	 * 当主线程结束时，子线程也跟着结束，不管子线程有没有处理完事务
	 */
	public void Daemon() {
		
		Thread thread = new Thread(()->{
			for(;;) {System.out.println("线程在运行");}
		});
		//守护线程
		thread.setDaemon(true);
		//启动子线程
		thread.start();
		System.out.println("------线程结束-------");
	}
	/**
	 * ThreadLocal 的使用
	 * 多线程访问
	 * @throws Exception
	 * ThreadLocal 
	 */
	public void ThreadLocal() throws Exception{
//		创建  ThreadLocal local 作用是每个线程都会复制一个变量到自己的本地内存
		//就是每个线程访问同一个资源时，
		Thread threadA = new Thread(()->{
			local.set("1111");
			System.out.println("threadA get   "+ local.get());
			local.remove();
			System.out.println("threadA   "+ local.get());
			
		});
		Thread threadB = new Thread(()->{
			local.set("2222");
			System.out.println("threadB get   "+ local.get());
			local.remove();
			System.out.println("threadB remove   "+ local.get());
			
		});
		threadA.start();
		threadB.start();
		
		
//		local.set("333");
//		System.out.println("threadB remove   "+ local.get());
	}
	/**
	 * ThreadLocal 不支持继承性  
	 * 以下列子 threadA 是获取不到 ThreadLocaltwo主线程所以设置的变量
	 * 因为是2个不同的线程
	 * @throws Exception
	 */
	public void ThreadLocal_two() throws Exception{
//		创建  ThreadLocal local 作用是每个线程都会复制一个变量到自己的本地内存
		//就是每个线程访问同一个资源时，
		local.set("333");
		Thread threadA = new Thread(()->{
			System.out.println("threadA get   "+ local.get());
		});
		threadA.start();
		System.out.println("ThreadLocaltwo get   "+ local.get());
	}
	/**
	 * inheritableThreadLocal 类 可以让子线程访问父线程
	 * @throws Exception
	 */
	public void inheritableThreadLocal() throws Exception{
//		创建  ThreadLocal local 作用是每个线程都会复制一个变量到自己的本地内存
		//就是每个线程访问同一个资源时，
		threadLocal.set("123456");
		Thread threadA = new Thread(()->{
			System.out.println("threadA get   "+ threadLocal.get());
		});
		threadA.start();
		System.out.println("inheritableThreadLocal get   "+ threadLocal.get());
	}
	
	/**
	 * Volatile 的使用
	 * 如果不在变量前加 关键字 volatile 会出现线程安全问题
	 * @throws Exception
	 */
	private static boolean ready = false;
	private static volatile int num = 0;
	public void ThreadVolatile() throws Exception{
		Thread threadA = new Thread(()->{
			while(!Thread.currentThread().isInterrupted()) {
				if(ready) {
					System.out.println(num+num);
				}
			}
		});
		Thread threadB = new Thread(()->{
			num=2;
			ready=true;
		});
		threadA.start();
		threadB.start();
		Thread.sleep(10);
		threadA.interrupt();
		System.out.println("ThreadVolatile  exit");
	}
	
	/**
	 * park方法和unpark方法相信看过LockSupport类的都不会陌生，
	 * 这两个方法主要用来挂起和唤醒线程。LockSupport中的park和unpark方法正是通过Unsafe来实现的
	 * 线程阻塞的使用
	 * @throws Exception
	 */
	public void ThreadPark() throws Exception{
		Thread threadA = new Thread(()->{
			int i = 0;
			LockSupport.park();//开始阻塞
			while(!Thread.currentThread().isInterrupted()) {//如果线程没中断就继续循环
					System.out.println(i++);
			}
		});
		threadA.start();
		//休眠5秒、
		Thread.sleep(5000);
		//唤醒线程
		LockSupport.unpark(threadA);
		//休眠5秒、
		Thread.sleep(5000);
		threadA.interrupt();
		System.out.println("inheritableThreadLocal get   "+ threadLocal.get());
	}
	/**
	 * 公平锁与非公平锁  ReentrantLock 类的使用
	 * @throws Exception
	 * 公平锁与非公平锁 区别 
	 * 公平锁:先到先得
	 * 非公平锁：先到不一定先得
	 * 
	 */
	public void ThreadReentrantLock() throws Exception{
		
		ReentrantLock lock = new ReentrantLock();//默认非公平锁
		ReentrantLock lock2 = new ReentrantLock(true);//公平锁
		
		Thread threadA = new Thread(()->{
			synchronized (lock2) {
				for(;;) {}
			}
		});
		Thread threadB = new Thread(()->{
			for(;;) {}
		});
		Thread threadC = new Thread(()->{
			for(;;) {}
		});
		
		threadA.start();
		threadB.start();
		threadC.start();
	}
	/**
	 * ReadWriteLock  读写锁 允许一个资源可以被多线程同时进行读取操作
	 * @throws Exception
	 */
	public void ThreadReadWriteLock() throws Exception{
	}
	/** 
	 * 锁可以分为独占锁和共享锁
	 * 独占锁和共享锁的区别
	 * 独占锁其实就是一种悲观锁：每次访问资源都会加上互斥锁，这就导致了并发性。而且独占锁同一时间只能有一个现成读取数据，其他线程必须等待当前线程锁的释放才可进行读取操作
	 * 
	 * 共享锁就是一个乐观锁：允许多个线程进行读取操作
	 * 
	 * synchronized 与 volatile 区别
	 * synchronized：即可保证内存的可以性又保证原子性  比较重
	 * volatile ： 主要保证内存的可见性  比较灵活
	 * 
	 * 
	 * 什么事CAS 即  compareAndSwapLong 
	 */
}
