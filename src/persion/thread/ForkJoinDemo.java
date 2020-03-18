package persion.thread;

import java.util.concurrent.*;

/**
 * description: ForkJoinDemo <br>
 * date: 2020/3/17 18:19 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 */
public class ForkJoinDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        TaskDemo taskDemo = new TaskDemo(1,100);
//        ForkJoinTask<Integer> fork = taskDemo.fork();
//        Integer integer = fork.get();
//        System.out.println(integer);

        ForkJoinPool f = new ForkJoinPool();
        ForkJoinTask<Integer> submit = f.submit(new TaskDemo(1, 100));
        Integer integer = submit.get();
        System.out.println(integer);

    }

}
