package com.thread.basics;

import java.util.concurrent.locks.LockSupport;

public class ThreadLockSupport {
	
	
	public static void main(String[] args) throws Exception{
//			new ThreadLockSupport().LockSupportTest();
//			new ThreadLockSupport().LockSupportTest_interrupt();
			new ThreadLockSupport().LockSupportTest_parkNanos();
//			new ThreadLockSupport().park_this();
	}
	
	/**
	 * LockSupport 工具類的使用，主要是將線程掛起與喚醒作用
	 */
	public void LockSupportTest() {
		
		Thread threadA = new Thread(()->{
			System.out.println("threadA--start");
			LockSupport.park();//掛起線程
			System.out.println("threadA--end");
		});
		Thread threadB = new Thread(()->{
			System.out.println("threadB--start");
			LockSupport.unpark(threadA);//喚醒A線程
			LockSupport.park();//掛起線程
			System.out.println("threadB--end");
		});
		Thread threadC = new Thread(()->{
			System.out.println("threadC--start");
			LockSupport.unpark(threadB);//喚醒B線程
			System.out.println("threadC--end");
		});
		
		threadA.start();
		threadB.start();
		threadC.start();
		
	}
	/**
	 * LockSupport 被掛起時，中斷線程就會返回
	 */
	public void LockSupportTest_interrupt() throws Exception{
		
		Thread threadA = new Thread(()->{
			System.out.println("threadA--start");
			//循環線程A 是否被執行只中斷 ，如果沒被中斷，那就掛起自己，知道線程中斷才會退出
			while(!Thread.currentThread().isInterrupted()) {
				System.out.println("threadA--Middle");
				LockSupport.park();//掛起線程
			}
			System.out.println("threadA--end");
		});
	
		
		threadA.start();
		//休眠10s
		Thread.sleep(10000);
		//中斷線程A
		threadA.interrupt();
	}
	/**
	 * LockSupport 類 park(this) parkNanos(long nanos) 方法
	 * 
	 */
	public void LockSupportTest_parkNanos() throws Exception{
		
		Thread threadA = new Thread(()->{
			System.out.println("threadA--start");
			LockSupport.parkNanos(20000);
			System.out.println("threadA--end");
		});
		Thread threadB = new Thread(()->{
			System.out.println("threadB--start");
			LockSupport.park(threadA);
//			LockSupport.unpark(threadA);//喚醒A線程
			System.out.println("threadB--end");
		});
		
		threadA.start();
		threadB.start();
		//休眠10s
		Thread.sleep(10000);
	}
	/**
	 * LockSupport.park(this); 作用 
	 * 掛起線程  在日誌打印信息時比較詳細，就知道那個線程被阻塞了
	 * 使用 命令 jstack pid 查看同之處 
	 */
	public void park_this() {
		
//		LockSupport.park();
		LockSupport.park(this);
	}
}
