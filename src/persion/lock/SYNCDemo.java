package persion.lock;

/**
 * description: SYNCDemo <br>
 * date: 2020/3/31 16:40 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 *     锁 ：对象监视器（Object Monitor）。
 *      当synchronized作用在实例方法时，监视器锁（monitor）便是对象实例（this）；
 *
 *       当synchronized作用在静态方法时，监视器锁（monitor）便是对象的Class实例，
 *       因为Class数据存在于永久代，因此静态方法锁相当于该类的一个全局锁；
 *       当synchronized作用在某一个对象实例时，监视器锁（monitor）便是括号括起来的对象实例；
 *
 *      monitor.monitorenter
 *      monitor.monitorexit
 *
 *      ACC_SYNCHRONIZED  标识符实现
 *
 *      <a href="https://www.jianshu.com/p/e62fa839aa41">关于sync的原理分析</a>
 *      <a href="https://www.jianshu.com/p/e2054351bd95">实现原理</a>
 *      <a href="https://www.jianshu.com/p/e62fa839aa41">实现原理</a>
 *
 */
public class SYNCDemo {
    public static void main(String[] args) {


    }

    //方法加锁   ower  entry set  waitset
    public static synchronized void test(){ //ACC_SYNCHRONIZED  标识  最后 还是落到 monitor 上monitorenter  monitorexit

    }

    //细化锁的颗粒度
    public  void test1(){
        synchronized (this.getClass()){ //monitor.monitorenter
                                        //monitor.monitorexit

        }

    }


}
