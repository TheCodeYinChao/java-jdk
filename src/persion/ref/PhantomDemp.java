package persion.ref;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * Created by Admin on 2020/3/19.
 * PhantomReference 类似强引用，它不会自动根据内存情况自动对目标对象回收，
 * 所以这里在 Heap 里不断开辟新空间，当达到 2m 阈值时，系统报出 OutOfMemoryError 异常。
 */
public class PhantomDemp {
    public static void main(String[] args) {
        ReferenceQueue<String> refQueue = new ReferenceQueue<String>();
        PhantomReference<String> referent = new PhantomReference<String>(
                new String("T"), refQueue);
        System.out.println(referent.get());// null

        System.gc();
        System.runFinalization();

        System.out.println(refQueue.poll() == referent); //true
    }
}
