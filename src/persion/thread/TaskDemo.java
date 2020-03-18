package persion.thread;

import java.util.concurrent.RecursiveTask;

/**
 * Created by Admin on 2020/3/17.
 */
public class TaskDemo extends RecursiveTask<Integer> {
    private int start = 0;
    private int end = 0;

    public TaskDemo(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if((end-start)<100){
            int sum =0;
            for(int i = start; i <= end; i++){
                sum += i;
            }
            return sum;
        }else{
            int i = (end - start) / 2;
            TaskDemo t1 = new TaskDemo(start,i);
            TaskDemo t2 = new TaskDemo(i,end);

            invokeAll();
            return t1.join() + t2.join();
        }
    }
}