package persion.thread;

import java.util.concurrent.*;

/**
 * description: ForkJoinDemo <br>
 * date: 2020/3/17 18:19 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 * <a href="https://blog.csdn.net/lh87522/article/details/45973861" > 关于fork join使用时的相关问题 </a>
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
                /**
                 * //坑, 当节点 分配无效时 会报出
                 * Could not initialize class java.util.concurrent.locks.AbstractQueuedSynchronizer$Node
                 * 我这里计算时开始用减法 会导致 start > end 值
                 * 同时递归时可能导致栈的溢出，需要着情 设置我们的栈空间
                 */
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
