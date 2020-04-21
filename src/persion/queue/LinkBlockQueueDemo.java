package persion.queue;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * description: LinkBlockQueueDemo <br>
 * date: 2020/4/21 14:51 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 */
public class LinkBlockQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingQueue queue = new LinkedBlockingQueue();
        Object poll = queue.take();
//        System.out.println(queue.offer("aa"));

    }
}
