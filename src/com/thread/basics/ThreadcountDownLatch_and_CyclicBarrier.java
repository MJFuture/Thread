package com.thread.basics;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * CountDownLatch 與  CyclicBarrier 區別
 * @author mjun
 * 
 * CountDownLatch 是個計數器，線程完成一個記錄一個，計數器遞減，且只能使用一次
 * CyclicBarrier 的計數器是個閥門，需要達到一定數量的線程才去執行。計數器遞增，且提供Reset（重置）功能，可以多次使用 
 *
 */
public class ThreadcountDownLatch_and_CyclicBarrier {
	//countDownLatch这个类使一个线程等待其他线程各自执行完毕后再执行。
	private static CountDownLatch latch = new CountDownLatch(2);

	
	public static void main(String[] args) throws Exception{
//		common();
//		bingfa();
		ThreadCyclicBarrier();
	}

	/**
	 * 循環柵欄 CyclicBarrier
	 * @throws Exception
	 */
	public static void ThreadCyclicBarrier() throws Exception{
		int Parties = 3; //固定達到多少線程
		int num = 3;//有多少條線程
		
		//到達多少條線程就有執行以下線程
		CyclicBarrier barrier = new CyclicBarrier(Parties, new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						System.out.println("恭喜通關~~");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
		for (int i = 0; i < num; i++) {
			new taskRunnable(barrier,i+1).start();
//			new taskThread(barrier).start();
		}
		//重置
		barrier.reset();
		//獲取達到線程數多少執行通關操作 --3
		System.out.println(barrier.getParties());
			
	}
	static class taskRunnable extends Thread {
		CyclicBarrier barrier;
		int i;
		public taskRunnable(CyclicBarrier b,int i) {
			this.barrier = b;
			this.i= i;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(1000);
				System.out.println("奧特曼打怪獸第 " +i +"關");
				barrier.await();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
	}
	static class taskThread extends Thread {
		CyclicBarrier barrier;
		public taskThread(CyclicBarrier b) {
			this.barrier = b;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(1000);
				System.out.println("奧特曼打怪獸第 1關");
				barrier.await();
				Thread.sleep(1000);
				System.out.println("奧特曼打怪獸第 2關");
				barrier.await();
				Thread.sleep(1000);
				System.out.println("奧特曼打怪獸第3關");
				barrier.await();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
	}
	
	
	
	
	/**
	 * 普通線程
	 */
	public static void common() throws Exception{
		 ExecutorService pool = Executors.newSingleThreadExecutor();
		 pool.submit(new Runnable() {
			 
			 @Override
			 public void run() {
				try {
					Thread.sleep(3000);
				} catch (Exception e) {
					// TODO: handle exception
				}finally {
					latch.countDown();
				}
				System.out.println("線程1執行完整");
				 
			 }
		 });
		 pool.submit(new Runnable() {
			 
			 @Override
			 public void run() {
				 try {
						Thread.sleep(3000);
				 } catch (Exception e) {
					 // TODO: handle exception
				 }finally {
					 latch.countDown();
				 }
				 System.out.println("線程2執行完整");
				 
			 }
		 });
		 //等待線程全部執行完成
		 latch.await();
		 //停止線程池
		 pool.shutdown();
		 System.out.println("線程池停止");
	}
		
	/**
	 * 模擬並發
	 */
	public static void bingfa() {
	    ExecutorService pool = Executors.newCachedThreadPool();
        CountDownLatch cdl = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            CountRunnable runnable = new CountRunnable(cdl);
            pool.execute(runnable);
        }
	}
	
	static class CountRunnable implements Runnable {
	    private CountDownLatch countDownLatch;
	    public CountRunnable(CountDownLatch countDownLatch) {
	        this.countDownLatch = countDownLatch;
	    }
	    @Override
	    public void run() {
	        try {
	            synchronized (countDownLatch) {
	                /*** 每次减少一个容量*/
	                countDownLatch.countDown();
	                System.out.println("thread counts = " + (countDownLatch.getCount()));
	                System.out.println("concurrency counts = " + (100 - countDownLatch.getCount()));
	            }
	            countDownLatch.await();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
	}
}
