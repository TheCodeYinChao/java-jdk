package persion.thread;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by Admin on 2020/3/12.
 */
public class FutureTaskDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

/*
        FutureTask<String> aa = new FutureTask<String>(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("ryn");
            }
        }, "aa");
        aa.run();
        System.out.println(aa.get());*/

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        FutureTask f = new FutureTask(new Runnable() {
            @Override
            public void run() {
                System.out.println("future tak  --- runable --run ");
            }
        },"default value ");
        for(int i =0 ;i<10 ;i++){
            executorService.submit(f);
            System.out.println((String) f.get());
        }


    }
}
