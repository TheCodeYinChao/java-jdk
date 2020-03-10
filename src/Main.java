import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Admin on 2020/3/10.
 */
public class Main {
    public static void main(String[] args) {
        Map map = new ConcurrentHashMap();

        map.put("a","a");


        System.out.println(map.toString());
    }
}
