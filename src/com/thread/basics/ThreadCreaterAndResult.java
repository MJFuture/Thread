package com.thread.basics;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
/**
 * 本類介紹線程的創建和線程帶有返回結果2中方式
 * @author mjun
 *
 */
public class ThreadCreaterAndResult {

	
	public static void main(String[] s) {
		new myThread().run();
		new myThread2().start();
		//如果 是繼承 Runnable 接口 還可以這麼啓動 可以啓動多個
		myThread thread = new myThread();
		new Thread(thread).start();
		new Thread(thread).start();
		new Thread(thread).start();
		
		/**帶有返回結果的使用 start**/
		//1.創建異步任務
		FutureTask<String> futureTask = new FutureTask<>(new CallerTask());
		//2.啓動線程
		new Thread(futureTask).start();
		//3.等待返回結果
		try {
			String result = futureTask.get();
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**帶有返回結果的使用 end**/
		
				
	}
	/**
	 * 第一種方式創建線程
	 * @author mjun
	 *
	 */
	public static class myThread implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("Runnable 方式 線程啓動了");
		}
	}
	/**
	 * 第二種方式創建線程
	 * @author mjun
	 *
	 */
	public static class myThread2 extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("Thread 方式 線程啓動了");
		}
	}
	
	/**
	 * 帶有返回的線程
	 * @author mjun
	 *
	 */
	public static class CallerTask implements Callable<String>{

		@Override
		public String call() throws Exception {
			// TODO Auto-generated method stub
			String msg ="我是帶有返回值（返回結果） 的線程";
			return msg;
		}
		
	}
	/**
	 * 創建線程也要設置線程名稱
	 */
	public static void demo() {
		Thread threadA = new Thread(()->{
			System.out.println("threadA--start");
			throw new NullPointerException();
		},"threadA");
		Thread threadB = new Thread(()->{
			System.out.println("threadB--start");
		},"threadB");
		
		threadA.start();
		threadB.start();
	}
}
