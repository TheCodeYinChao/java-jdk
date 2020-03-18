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
        TaskDemo taskDemo = new TaskDemo(0,100);
        ForkJoinTask<Integer> fork = taskDemo.fork();
        Integer integer = fork.join();
        System.out.println(integer);

//        ForkJoinPool f = new ForkJoinPool();
//        ForkJoinTask<Integer> submit = f.submit(new TaskDemo(1, 100));
//        Integer integer = submit.get();
//        System.out.println(integer);

    }


    static class TaskDemo extends RecursiveTask<Integer> {
        private int start = 0;
        private int end = 0;

        public TaskDemo(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if((end-start)<10){
                int sum =0;
                for(int i = start; i <= end; i++){
                    sum += i;
                }
                return sum;
            }else{
                int i = (end + start) / 2;
                TaskDemo t1 = new TaskDemo(start,i);
                TaskDemo t2 = new TaskDemo(i,end);
                    t1.fork();
                    t2.fork();
                return t1.join() + t2.join();
            }
        }
    }
}
