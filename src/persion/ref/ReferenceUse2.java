package persion.ref;

/**
 * description: ReferenceUse <br>
 * date: 2020/3/19 17:57 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 */
public class ReferenceUse2 {
    public static void main(String[] args) throws Exception{

        MyRefrece my = new MyRefrece(new People("121","zyc"));
        System.gc();  //还是不能gc掉
        System.runFinalization();

        System.runFinalization();
        System.out.println(my.get());
    }
}
