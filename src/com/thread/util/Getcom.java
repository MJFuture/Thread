package com.jpush.utlis;

import com.alibaba.fastjson.JSONPath;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Getcom {

    public static void main(String[] are){
        int corePoolSize = 10;//核心线程池大小
        int maximumPoolSize = 100000;//最大线程池大小
        long keepAliveTime = 100;//线程最大空闲时间
        TimeUnit unit = TimeUnit.SECONDS;//时间单位
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(5000);//线程等待队列
        ThreadFactory threadFactory = new NameTreadFactory();//线程创建工厂
        RejectedExecutionHandler handler = new MyIgnorePolicy();//拒绝策略

        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
                workQueue, threadFactory, handler);
        executor.prestartAllCoreThreads(); // 预启动所有核心线程


        List<String> listStr = getListString();
        for (int i = 1; i <= listStr.size(); i++) {
            MyTask task = new MyTask(listStr.get(i),String.valueOf(i));
            executor.execute(task);
        }
//        while (true) {
//    		System.out.println();
//
//    		int queueSize = executor.getQueue().size();
//    		System.out.println("当前排队线程数：" + queueSize);
//
//    		int activeCount = executor.getActiveCount();
//    		System.out.println("当前活动线程数：" + activeCount);
//
//    		long completedTaskCount = executor.getCompletedTaskCount();
//    		System.out.println("执行完成线程数：" + completedTaskCount);
//
//    		long taskCount = executor.getTaskCount();
//    		System.out.println("总线程数：" + taskCount);
//
//    		Thread.sleep(3000);
//    		if(queueSize==0){
//    			executor.shutdownNow();
//    			break;
//    		}
//    	}
//        System.in.read(); //阻塞主线程

    }
    //獲取24個字母
    public static List<String> getListString(){
        String s = "qwertyuiopasdfghjklzxcvbnm";
        StringBuffer result = new StringBuffer();
        List<String> stringList = new ArrayList<String>();
        for(int i = 0; i < s.length(); i++){
            for(int j = 0; j < s.length(); j++){
                for(int k = 0; k < s.length(); k++){
                    for(int m = 0; m < s.length(); m++){
                        stringList.add(s.charAt(i)+""+s.charAt(j)+""+s.charAt(k)+""+s.charAt(m));
                    }
                }

            }
        }
        stringList = stringList.stream().distinct().collect(Collectors.toList());
        return stringList;
    }



    public  static void  writeContent(String data){
        BufferedWriter bw=null;
        try{
            File file =new File("Bcomment.txt");
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(file.getName(),true);
            bw= new BufferedWriter(fileWritter);
            bw.write(data);
            bw.newLine();
            System.out.println("Done");
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            try {
                if(bw!=null){
                    bw.flush();
                    bw.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }




    static class NameTreadFactory implements ThreadFactory {
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

    public static class MyIgnorePolicy implements RejectedExecutionHandler {

        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            doLog(r, e);
        }

        private void doLog(Runnable r, ThreadPoolExecutor e) {
            // 可做日志记录等
            System.err.println( r.toString() + " rejected+拒絕執行");
//          System.out.println("completedTaskCount: " + e.getCompletedTaskCount());
        }
    }
    static class MyTask implements Runnable {
        private String name;
        private String theradnum;
        public MyTask(String name,String theradnum) {
            this.name = name;
            this.theradnum = theradnum;
        }

        @Override
        public void run() {
            try {
                try {
                    getMessage(name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(this.toString() + " is running! 正在執行");
                Thread.sleep(1000); //让任务执行慢点
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public String getName() {
            return name;
        }

        public String getTheradnum() {
            return theradnum;
        }

        @Override
        public String toString() {
            return "MyTask [theradnum=" + theradnum + ",  name="+ name +"]";
        }


        private static void getMessage( String str) throws IOException {
//        String  dataUrl="https://member.bilibili.com/x/web/replies?order=ctime&filter=-1&is_hidden=0&type=1&pn="+cursor+"&ps="+limit;

//            String str="asdasdgggggg";
            String token = "Ybc17fc8e05368a01cece211ce888b65f";
            String callback= "jsonp_1587113264924_78742";
            String TMP_COOKIES ="cna=y+lQEil69R4CAXjqD4KvuyEy; cnz=LFNXEyMpaFkCAYIP6nhKHPac; _ga=GA1.2.1178027096.1523516121; login_aliyunid_pk=1822472630798961; UM_distinctid=1707f9cdf18c9-02a8c7f6e470cd-32365f08-ffc00-1707f9cdf19288; aliyun_choice=CN; _gid=GA1.2.647364645.1587092181; ping_test=true; t=478bd5ef5297adbe2a0f88168e01a158; _tb_token_=f8a665871bb73; cookie2=19658cef79fc90babba94026d048bfd2; _samesite_flag_=true; _hvn_login=6; login_aliyunid_ticket=XZqg4XFWQfyKpeu*0vCmV8s*MT5tJl3_1$$w*r*oy2bnGF4GzYE*cWD9PTEE0Yij_eYwLz7QnnJkvof_MNpoU_BOTwChTBoNM1ZJeedfK9zxYnbN5hossqIZCr6t7SGxRigm2Cb4fGaCdBZWIzmgdHs60; hssid=1u8TMzVtSUMHpEN2dyFgJiQ1; hsite=6; aliyun_country=CN; aliyun_site=CN; aliyun_lang=zh; login_aliyunid_luid=BG+D03H13J50a6fd5aa3da2b467f8ff498ee37cf09e+eNjaCvLMtua7raFJp8qU6WVFLtKQgNSPXQLAE/zq; login_aliyunid_pks=BG+1WM+q0anS9kPpjkbtGScP7ulfrZjk5rUYECsy+6ZCPo=; login_aliyunid_abi=BG+a1A4vNuj74456272d598e1b31dcd73a6c8a6c22c+ga5dkRrNL0qjS7k4w9Ly35LqYRSLA3bxWKCTAfcbgP3bi/aAlQE=; login_aliyunid=al****; FECS-XSRF-TOKEN=9690ea56-8c80-4cbd-b9dc-98dc6183868b; FECS-UMID=%7B%22token%22%3A%22Yf2004c03235baafb88b370f48554cae7%22%2C%22timestamp%22%3A%2212607469505E5F40564D6374%22%7D; console_base_assets_version=3.9.3; __yunlog_session__=1587092782437; XSRF-TOKEN=f049d53e-8ed4-4a5a-a538-237491089ed7; aliyunMerakSource=WyJvc3NiYWcuMTI4OTg4OS4yMjY4NDU5Mjkubm9uLm5vbi5ub24ubm9uIiwiZG9tYWluX18uNTkwNTkzMi43MTUubm9uLm5vbi5ub24ubm9uIiwibm9uLjU5MDU5MzIuNzE2Lm5vbi5ub24ubm9uLm5vbiJd; aliyunAliothSource=eyJzcG0iOiIwLjAuMC5kMTc5Iiwic2NtIjoiMjAxNDA3MjIuMTM3MS40LjEzOTQifQ==; iaGreyFlag=1587107841377; SESSIONX=6c0d19f1-5982-4ff8-ba9d-663607dbe004; login_aliyunid_csrf=_csrf_tk_1216387107889507; csg=298bc2fe; channel=Afl4nTTmSL01muC5v4hvvA%2BqN4d2Hdm8%2B%2BD96yOMxh4%3D; JSESSIONID=14FFA4BC3A8635F2E57AB19BC31B8617; l=eBTRZu2PqfJvCdRhBO5wdm7y1l7TiIRXCsPrglsgMIHcB6mjOFpIKP-QLQxqeRt5WhQNnscXf3PF8cjzBlL8hyUIh1-UPFirU0HmRdYO.; isg=BJCQz83m-oopwKXeL1pTIAHGYd7iWXSjpc3AMoph1eu3xT1vJm3TM1GznY0lFSx7";
            String dataUrl=" https://tm-api.aliyun.com/trademarksearch/stat.jsonp?keyword="+str+"&searchType=0&statType=3&token="+token+"&ua=&currency=&site=&bid=&_csrf_token=&callback="+callback;
            HttpClient httpClient = new HttpClient();
            PostMethod postMethod=new PostMethod();
            GetMethod getMethod = new GetMethod(dataUrl);
            getMethod.setRequestHeader("cookie", TMP_COOKIES);
            postMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
            postMethod.setRequestHeader("accept","*");
            postMethod.setRequestHeader("accept-encoding","gzip, deflate, br");
            postMethod.setRequestHeader("accept-language:","zh-CN,zh;q=0.9,en;q=0.8");
            httpClient.executeMethod(getMethod);
            String text = getMethod.getResponseBodyAsString();
            System.out.println(text);
//            JSONObject jsonObject=JSONObject.parseObject(text);
//            for (int i = 0; i <10 ; i++) {
//                try {
//                    String content= JSONPath.eval(jsonObject,"$.data["+i+"].message").toString();
//                    String title=JSONPath.eval(jsonObject,"$.data["+i+"].title").toString();
//                    String replier=JSONPath.eval(jsonObject,"$.data["+i+"].replier").toString();
//                    // if(content.indexOf("根据指定关键字抓取评论")>0)
//                    // {
//                    writeContent(text);
//                    System.out.println("评论:"+title);
//                    // }
//                }catch (Exception e){
//                    System.out.println(e.getMessage());
//                }
//            }
        }
    }





}
