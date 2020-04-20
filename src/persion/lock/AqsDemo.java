package persion.lock;

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
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();
        reentrantLock.lock();

        condition.await();

        condition.signalAll();
        reentrantLock.unlock();
    }


    /**
     * 读写互斥  写写互斥  读读不互斥
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
     * 读写互斥  写写互斥  读读不互斥
     */
    public  static void test2() throws InterruptedException {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();

        condition.await();

        condition.signalAll();
    }
}
