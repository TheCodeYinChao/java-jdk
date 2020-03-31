package persion.obj;

/**
 * description: ObjDemo <br>
 * date: 2020/3/30 19:23 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 */
public class ObjDemo {
    public static void main(String[] args) {

        Object o = new Object();
        System.out.println(o.hashCode());
        Object o1= new Object();
        System.out.println(o1.hashCode());
        String s = new String();

        System.out.println(s.hashCode());
//        s.equals()
    }
}
