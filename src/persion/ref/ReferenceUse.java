package persion.ref;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * description: ReferenceUse <br>
 * date: 2020/3/19 17:57 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 *
 * "C:\Program Files\Java\jdk1.8.0_171\bin\java.exe" -agentlib:jdwp=transport=dt_socket,address=127.0.0.1:61335,suspend=y,server=n -XX:+PrintGCDetails -javaagent:C:\Users\RAYDATA\.IntelliJIdea2018.1\system\captureAgent\debugger-agent.jar=file:/C:/Users/RAYDATA/AppData/Local/Temp/capture.props -Dfile.encoding=UTF-8 -classpath "C:\Program Files\Java\jdk1.8.0_171\jre\lib\charsets.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\deploy.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\access-bridge-64.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\cldrdata.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\dnsns.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\jaccess.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\jfxrt.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\localedata.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\nashorn.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\sunec.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\sunjce_provider.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\sunmscapi.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\sunpkcs11.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\ext\zipfs.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\javaws.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\jce.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\jfr.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\jfxswt.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\jsse.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\management-agent.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\plugin.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\resources.jar;C:\Program Files\Java\jdk1.8.0_171\jre\lib\rt.jar;D:\persion\java-jdk\out\production\java-jdk;C:\Program Files\Java\jdk1.8.0_171\lib\tools.jar;C:\Program Files\JetBrains\IntelliJ IDEA 2018.1.4\lib\idea_rt.jar" persion.ref.ReferenceUse
 * Connected to the target VM, address: '127.0.0.1:61335', transport: 'socket'
 * gc 日志详解
 * ========================
 * Bean{name='nihao ', id='111'}
 * [GC (System.gc()) [PSYoungGen:
 * 3941K（开始前年轻代使用）->815K（开始gc后年轻代使用）(76288K（该内存区域总容量）)]
 *
 * 整个堆区域的使用从3941K到823K
 * 3941K（整个堆使用前）->823K（整个堆回收后）(251392K)堆的总大小为251392K, 0.0011276 secs]
 *
 * [Times: user=0.00 （用户态消耗CPU时间）sys=0.00（内核态消耗cpu时间）, real=0.00 secs（操作从开始到结束经过的墙钟时间）]
 *
 * full 代表 stop -the - word
 * [Full GC (System.gc()（这里是垃圾回收期的类型）) Disconnected from the target VM, address: '127.0.0.1:61335', transport: 'socket'
 * [PSYoungGen: 815K->0K(76288K)] [ParOldGen: 8K->683K(175104K)] 823K->683K(251392K(整个堆空间)), [Metaspace: 3261K->3261K(1056768K)], 0.0041213 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 *
 *
 * Bean{name='nihao3 ', id='1112'}
 * [GC (System.gc()) [PSYoungGen: 1310K->32K(76288K)] 1994K->715K(251392K), 0.0005142 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * [Full GC (System.gc()) [PSYoungGen: 32K->0K(76288K)] [ParOldGen: 683K->681K(175104K)] 715K->681K(251392K), [Metaspace: 3261K->3261K(1056768K)], 0.0019816 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
 * null
 * Heap
 *  PSYoungGen(新生代)      total 76288K, used 1311K [0x000000076b200000, 0x0000000770700000, 0x00000007c0000000)
 *   eden space 65536K, 2%（使用率） used [0x000000076b200000,0x000000076b347cb8,0x000000076f200000)
 *
 *   survive（幸存）
 *   from space 10752K, 0% used [0x000000076fc80000,0x000000076fc80000,0x0000000770700000)
 *   to   space 10752K, 0% used [0x000000076f200000,0x000000076f200000,0x000000076fc80000)
 *  ParOldGen（老年）       total 175104K, used 681K [0x00000006c1600000, 0x00000006cc100000, 0x000000076b200000)
 *   object space 175104K, 0% used [0x00000006c1600000,0x00000006c16aa778,0x00000006cc100000)
 *  Metaspace     （元空间）  used 3268K, capacity 4568K, committed 4864K, reserved 1056768K
 *   class space    used 349K, capacity 392K, committed 512K, reserved 1048576K
 *
 * Process finished with exit code 0
 */
public class ReferenceUse {
    public static void main(String[] args) throws Exception{
        Bean bean = new Bean();
        bean.setId("111");
        bean.setName("nihao ");
        SoftReference t = new SoftReference(bean); //软
        Object o = t.get();
        System.out.println("o:"+o);

        Bean bean1= new Bean();
        bean1.setId("1112");
        bean1.setName("nihao3 ");
        WeakReference w = new WeakReference(bean1); //弱
        bean1 =null;
        System.out.println("o1: " +w.get());
        System.gc();
        System.runFinalization();
        System.out.println("o1o: " +w.get());

        Bean bean3= new Bean();
        bean3.setId("1114");
        bean3.setName("nihaof3 ");
        /**
         * 回收的是 bean3 而不是 p
         * ReferenceQuerue 保存的被回收的引用对象 ， 如果 第二次 仍然要回收 才彻底回收  而且 finlize 只能被系统调用一次
         * 也就是 gc 逃逸只有一次机会
         */
        PhantomReference p = new PhantomReference(bean3,new ReferenceQueue()); //虚 跟踪对象被垃圾回收的状态
        System.gc();
        Object o2 = p.get(); //永远为null
        System.out.println("o2:"+o2);
    }
}
