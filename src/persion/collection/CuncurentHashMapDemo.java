package persion.collection;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Admin on 2020/4/7.
 */
public class CuncurentHashMapDemo {

    public static void main(String[] args) {
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();


        concurrentHashMap.put("a","a");
        concurrentHashMap.put("b","a");
        concurrentHashMap.put("c","a");
        concurrentHashMap.get("a");

        concurrentHashMap.remove("a");
        concurrentHashMap.size();


    }
}
