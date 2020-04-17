package com.thread.basics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
/**
 * ConcurrentHashMap 引發併發問題 舉例
 * @author mjun
 * put与putIfAbsent区别:

	put在放入数据时，如果放入数据的key已经存在与Map中，最后放入的数据会覆盖之前存在的数据，
	
	而putIfAbsent在放入数据时，如果存在重复的key，那么putIfAbsent不会放入值。
	
	putIfAbsent   如果传入key对应的value已经存在，就返回存在的value，不进行替换。
					如果不存在，就添加key和value，返回null
 */
public class ThreadConcurrentHashMap {
	static ConcurrentHashMap<String, List<String>> map = new ConcurrentHashMap<>();
	public static void main(String[] args) throws Exception {
		NoProblem();

		
		
//		problem();
		/*運行結果可見 topic 少了數據
		{topic2=[c1, c2], topic=[b1, b2]}
		{topic2=[c1, c2], topic=[b1, b2]}
		{topic2=[c1, c2], topic=[b1, b2]}*/
	}
	
	/**
	 *  有問題代碼 
	 *  
	 */
	public static void problem() throws Exception {
		Thread thread_A= new Thread(()->{
			List<String> list = new ArrayList<>();
			list.add("a1");
			list.add("a2");
			map.put("topic", list);
			System.out.println(map.toString());
		}); 
		Thread thread_B= new Thread(()->{
			List<String> list = new ArrayList<>();
			list.add("b1");
			list.add("b2");
			map.put("topic", list);
			System.out.println(map.toString());
		}); 
		Thread thread_C= new Thread(()->{
			List<String> list = new ArrayList<>();
			list.add("c1");
			list.add("c2");
			map.put("topic2", list);
			System.out.println(map.toString());
		}); 
			
		thread_A.start();
		thread_B.start();
		thread_C.start();
		
	}
	/**
	 *  改進的代碼
	 *  利用 putIfAbsent 原子性判斷 如果不存在，就添加key和value，返回null
	 *  
	 */
	public static void NoProblem() throws Exception {
		Thread thread_A= new Thread(()->{
			List<String> list = new ArrayList<>();
			list.add("a1");
			list.add("a2");
			List<String> oldList = map.putIfAbsent("topic", list);
			if(null != oldList) {
				oldList.addAll(list);
			}
			System.out.println(map.toString());
			
		}); 
		Thread thread_B= new Thread(()->{
			List<String> list = new ArrayList<>();
			list.add("b1");
			list.add("b2");
			List<String> oldList = map.putIfAbsent("topic", list);
			if(null != oldList) {
				oldList.addAll(list);
			}
			System.out.println(map.toString());
		}); 
		Thread thread_C= new Thread(()->{
			List<String> list = new ArrayList<>();
			list.add("c1");
			list.add("c2");
			List<String> oldList = map.putIfAbsent("topic2", list);
			if(null != oldList) {
				oldList.addAll(list);
			}
			System.out.println(map.toString());
		}); 
		
		thread_A.start();
		thread_B.start();
		thread_C.start();
		
	}
}
