package persion.collect;

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
        ConcurrentHashMap <String,Object> concurrentHashMap = new ConcurrentHashMap<>();

        concurrentHashMap.put("a","你好");


        concurrentHashMap.get("a");


        concurrentHashMap.remove("a");


    }

}
