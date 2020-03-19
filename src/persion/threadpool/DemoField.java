package persion.threadpool;

/**
 * description: DemoField <br>
 * date: 2020/3/17 17:27 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 */
public class DemoField {
    static Integer a=1;

    public static void main(String[] args) {
        //很蛋疼的写法
        for(Integer b = null, q = a, s; q != null; q = s){
            System.out.println(q);
            System.out.println(a);
            s = q;
            System.out.println(s);
        }

    }

}
