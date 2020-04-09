package persion.collect;

import javax.sql.rowset.serial.SQLOutputImpl;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description: MapDemo  <br>
 * date: 2020/4/8 17:53 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 * <a  href="https://www.sohu.com/a/320372210_120176035">关联博文</a>
 */
public class MapDemo {
    public static void main(String[] args) {
//        test();

        tableSizeFor();
    }

    public static void test(){
        ConcurrentHashMap <String,Object> concurrentHashMap = new ConcurrentHashMap<>(Integer.MAX_VALUE-5);

        concurrentHashMap.put("a","你好");


        concurrentHashMap.get("a");

        concurrentHashMap.remove("a");
    }

    /**
     * 如何做到的 2的幂次方？
     *  把 末尾字节置于0 其他 全为 1
     *  通过位移  1 2 4 8 16 （32）int 最大字节32 亦或操作已达最大
     * 为什么要转？
     *  只有当数组长度为2的幂次方时，h&(length-1)才等价于h%length，即实现了key的定位，2的幂次方也可以减少冲突次数，提高HashMap的查询效率
     */
    public static void tableSizeFor(){
        int c= Integer.MAX_VALUE-2372;
        int n = c - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        int i = (n < 0) ? 1 : (n >= 1 << 30) ? 1 << 30 : n + 1;
        System.out.println(i);
    }
}
