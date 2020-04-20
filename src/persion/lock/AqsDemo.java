package persion.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * description: AqsDemo <br>
 * date: 2020/3/31 16:40 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 */
public class AqsDemo {

    public static void main(String[] args) throws Exception{
        test();

    }

    public  static void test()throws Exception{
        /**
         * 现在看其实就是一个同步队列 一个 等待队列的实现  貌似也不难  这里面涉及到 排他模式
         */
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();
        reentrantLock.lock();

        condition.await();

        condition.signalAll();
        reentrantLock.unlock();
    }


    /**
     * 读写互斥  写写互斥  读读不互斥
     *
     * 这里有共享模式
     *
     * 其实就是同步队列中的 节点模式决定的
     *
     *
     * 队里面有 排他节点 则 读 写都阻塞
     *
     * 执行节点即获取到锁的节点 是共享节点 则 其他可以获取到锁
     *
     *
     */
    public  static void test1(){
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

        ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
        writeLock.lock();
        writeLock.unlock();

        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
        readLock.lock();
        readLock.unlock();
    }


    /**
     * 一切就已经明了
     * @throws Exception
     */
    public  static void test2()throws Exception{
        CountDownLatch cdl= new CountDownLatch(3);
        cdl.await();//获取结果阻塞

        cdl.countDown();// 执行结果 countDown释放一个 释放一个 释放到state == 0则全部执行

    }

}
