package com.thread.basics;

import java.util.concurrent.CompletableFuture;

import org.junit.Test;

public class ThreadCompletableFuture {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	/**
	 * supplyAsync 方法内定义要执行的任务
	 */
	@Test
	public void supplyAsync(){
	    CompletableFuture.supplyAsync(()->{
	        //Thread.sleep(5000);
	        return "执行任务线程ajsjd";
	    }).thenAccept(v ->{
	        System.out.println("放假放假的 " + v);
	    });
	}

	/**
	 *
	 * 整合多个线程返回结果
	 */
	@Test
	public void thenCombine(){
	    String  str = CompletableFuture.supplyAsync(()->{
	        try {
	            Thread.sleep(2000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        return "11";
	    }).thenCombine(CompletableFuture.supplyAsync(()->{
	        try {
	            Thread.sleep(5000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        return "2222";
	    }),(a,b)->{ return  a +"**"+ b;}).join();

	    System.out.println("str=== " + str);
	}

	/**
	 * 消费之前线程结果
	 */
	@Test
	public void thenAccept(){

	}

	/**
	 * 优先返回执行完成的结果
	 */
	@Test
	public void applyToEither() throws Exception{
	    String  str = CompletableFuture.supplyAsync(()->{
	        try {
	            Thread.sleep(5000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        return "11";
	    }).applyToEither(CompletableFuture.supplyAsync(()->{
	        try {
	            Thread.sleep(1000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        return "2222";
	    }),(s)->{ return  s;}).join();

	    System.out.println("str=== " + str);

	}
}
