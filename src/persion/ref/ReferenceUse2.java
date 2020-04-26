package persion.ref;

/**
 * description: ReferenceUse <br>
 * date: 2020/3/19 17:57 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 *
 *     ThreadLocal失去强引用，
 *     生命周期只能存活到下次gc前，此时ThreadLocalMap中就会出现key为null的Entry，
 *     当前线程无法结束，这些key为null的Entry的value就会一直存在一条强引用链，造成内存泄露。
 *
 *     注意线程池中要慎用
 */
public class ReferenceUse2 {
    public static void main(String[] args) throws Exception{

        MyRefrece my = new MyRefrece(new People("121","zyc"));

        People people = my.get();
        System.out.println(people);

        System.gc();  //还是不能gc掉

        System.runFinalization();
        System.out.println(my.get());
    }
}
