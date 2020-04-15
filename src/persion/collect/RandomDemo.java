package persion.collect;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * description: RandomDemo <br>
 * date: 2020/4/15 15:17 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 */
public class RandomDemo {
    public static void main(String[] args) {
        Random random = new Random(); //多线程并发问题 竞争问题
        System.out.println(random.nextInt());


        ThreadLocalRandom current = ThreadLocalRandom.current(); //解决多线程并发竞争自旋导致的效率下降
        System.out.println(current.nextInt());

    }
}
