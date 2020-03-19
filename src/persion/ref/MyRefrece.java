package persion.ref;

import java.lang.ref.WeakReference;

/**
 * description: MyRefrece <br>
 * date: 2020/3/19 18:57 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 */
public class MyRefrece extends WeakReference<People> {
    public MyRefrece(People referent) {
        super(referent);
    }
}
