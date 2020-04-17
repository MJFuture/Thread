package com.thread.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.LockSupport;

import com.thread.basics.BizException;

public class Test {

	public static void testBizException() throws BizException {

        System.out.println("throwing BizException from testBizException()");

        throw new BizException("100","哥，我错了");

    }

    public static void main(String[] args) {
//    	CompletableFuture<T> future = new CompletableFuture<>();
        /*try {

            testBizException();

        } catch (BizException e) {

            System.out.println("自己定义的异常");

            e.printStackTrace();

        }*/
        
        /*try (BufferedReader br = new BufferedReader(new FileReader("D:/1.jpg"))) {

            System.out.println(br.readLine());

        } catch (IOException e) {

            e.printStackTrace();
        }*/
    	int DEFAULT_INITIAL_CAPACITY = 1 << 30;
    	System.out.println(DEFAULT_INITIAL_CAPACITY);
    	
    	int size = tableSizeFor(17); //比如我们这里传入的数字是10，那么打印的size会是多少呢？
        System.out.println(size);
        Map<String, String> map = new HashMap<String, String>();
        map.put("a", "1");
        map.put("a", "2");
        System.out.println(map.get("a"));
        
        
        HashMap<String,Integer> hashMap = new HashMap<String,Integer>();
        Map<String, Integer> mapdz = Collections.synchronizedMap(hashMap);
        mapdz.put("ss", 1);
        mapdz.put("ss", 4);
        System.out.println(mapdz.get("ss"));
        
        
        
        
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
    
    
    
    
    static final int MAXIMUM_CAPACITY = 1 << 30;
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

}
