package persion.threadpool;

import java.util.concurrent.*;

/**
 * Created by Admin on 2020/3/12.
 * 执行参数问题  核心线程数   队列长度  最大线程数  先填充核心线程数  ----当 满了之后 填充对列长度 ---- 当对列满啦 增加工作线程至最大线程数
 *
 * <a href="https://www.cnblogs.com/trust-freedom/p/6693601.html"> 链接博客</a>
 *
 * 线程的关闭 类推到所有的其他关闭问题  比如
 * 1 直接关闭  不管有没有任务在执行都给你关了
 * 2 优雅的关闭1  我等正在执行的任务执行完啦我再关
 * 3 优雅的关闭2 我等正在执行的和已经提交的任务都完事啦我再关闭
 *
 */
public class Demo {
    public static void main(String[] args) {
        threadPool();
    }

    public static void threadPool(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
//        scheduledExecutorService.schedule()
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

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
