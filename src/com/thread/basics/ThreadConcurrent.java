package com.thread.basics;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

import sun.misc.Unsafe;

/**
 * 线程并发处理
 * @author mjun
 *
 */
public class ThreadConcurrent {

	private static java.util.concurrent.atomic.AtomicLong   atomicLong = new AtomicLong();
	
	private static Integer[] a= new Integer[] {1,5,5,68,1,20,485,6,2,54,6,2,0,4,50};
	private static Integer[] b= new Integer[] {1,5,6,68,1,20,60,65,2,55,6,2,0,4,50};
	public static void main(String[] args) throws Exception{
//		Unsafe.getUnsafe();
	}
	/**
	 * Random 和 ThreadLocalRandom 理解
	 * Random ：單線程下生成隨機數:是拿舊數據和新數據計算生成新的數據，最後通過CAS新舊值更新；併發性能低
	 * 			多線程下：生成新的數據，多調線程拿舊數據可能是同一個值，故在最後CAS操作時，只有一個更新成功，不成功的通過自旋的方式重新獲取舊的數據再和新的數據生成隨機數，再CAS操作，這導致降低的併發性能。
	 * 
	 * ThreadLocalRandom ：讓每一個線程都持有一個本地的數據變量，該數據變量只有在生產隨機數的時候才被初始化。在多線程下計算新的種子 變量是根據自己線程內的種子進行更新，從而避免競爭，達到併發性能最高
	 * 多線程併發生成隨機數時
	 */
	public void function_ThreadLocalRandom() {
		//獲取一個隨機數生成器
		ThreadLocalRandom random = ThreadLocalRandom.current();
		for (int i = 0; i < 6; i++) {
			System.out.println(random.nextInt(10000));
		}
	}
	/**
	 * AtomicLong 的原子性  適合同步
	 * 雖然提供CAS非阻塞原子性，但是在高併發的情況下 大量線程會去競爭同一個原子變量，但只能有一個成功，其他線程會自旋嘗試CAS 操作，所以導致ＣＰＵ資源的浪費
	 * 
	 */
	public void atomicLong() throws Exception{
		
		Thread thread_A = new Thread(()-> {
			for (Integer i : a) {
				if(i==0) {
					//遞增
					atomicLong.incrementAndGet();
				}
			}
		});
		Thread thread_B = new Thread(()-> {
			for (Integer i : b) {
				if(i==0) {
					//遞增
					atomicLong.incrementAndGet();
					//遞減
//					atomicLong.decrementAndGet();
					// 
//					atomicLong.getAndIncrement();
				}
			}
		});
		thread_A.start();
		thread_B.start();
		
		
		thread_A.join();
		thread_B.join();
		
		System.out.println(atomicLong.get());
	}
}
