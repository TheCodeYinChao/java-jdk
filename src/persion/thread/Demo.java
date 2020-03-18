package persion.thread;

import java.util.concurrent.*;

/**
 * Created by Admin on 2020/3/12.
 * 执行参数问题  核心线程数   队列长度  最大线程数  先填充核心线程数  ----当 满了之后 填充对列长度 ---- 当对列满啦 增加工作线程至最大线程数
 *
 * <a href="https://www.cnblogs.com/trust-freedom/p/6693601.html"> 链接博客</a>
 */
public class Demo {
    public static void main(String[] args) {
        threadPool();
    }

    public static void threadPool(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                executorService.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(10000);
//                            System.out.println("t1");
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        },"t1").start();

                Future<Object> future = executorService.submit(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        Thread.sleep(100000);
                        return "这是返回值";
                    }
                });
                new  Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Object o = future.get();
                            System.out.println( o +"");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                },"ct1").start();
                new  Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Object o = future.get();
                            System.out.println( o);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                },"ct2").start();

        executorService.shutdown();

    }

}
