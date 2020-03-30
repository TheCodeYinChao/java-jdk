package persion.thread;

import persion.threadpool.Demo;

import java.util.HashMap;
import java.util.Map;

/**
 * description: ThreadDemo threadlocal 的探究   他所引起的 内存溢出  value  无法释放被强引用无法释放时会导致内存溢出
 *
 *
 * get 时会清除 或者手动remove
 * date: 2020/3/18 18:40 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 */
public class ThreadDemo {
    public static ThreadLocal<Object> threadLocal  = new ThreadLocal<Object>();

    public static void main(String[] args) {
        ThreadDemo threadDemo = new ThreadDemo();
        Map<ThreadDemo,String> map  = new HashMap();
        map.put(threadDemo,"ndiadadaf dad");
        for (int i= 20000;i<0;i++){
            threadLocal.set(map);
        }
        threadLocal.remove();
        String s = map.get(threadDemo);
        System.out.println(s);
    }

}
