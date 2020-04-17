package com.thread.util;

public class Testep {
	
	
	/**
	 * 1.mian 主線程 先執行完 同時  isRun = false 所以 子線程不會死循輸出
	 * 2.子線程先執行有
	 */
	static	boolean isRun = true;
	public static void main(String[] args) throws Exception{
//	    new Thread(() ->{
//	      while (isRun){
//	        System.out.println("1");
//	      }
//	    }).start();
//	    Thread.sleep(1000);
//	    //測試時
//	    isRun = false;
	    testJoin();
	}
	
	
	static void testJoin() {
        Thread t1 = new Thread(()->{
            for(int i=0; i<10; i++) {
                System.out.println("A" + i);
                try {
                    Thread.sleep(500);
                    //TimeUnit.Milliseconds.sleep(500)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(()->{

            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for(int i=0; i<10; i++) {
                System.out.println("B" + i);
                try {
                    Thread.sleep(500);
                    //TimeUnit.Milliseconds.sleep(500)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();
    }

}
