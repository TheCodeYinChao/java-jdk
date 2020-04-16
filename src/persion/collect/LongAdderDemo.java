package persion.collect;

import java.util.concurrent.atomic.LongAdder;

/**
 * description: LongAdderDemo <br>
 * date: 2020/4/16 14:52 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 *
 *分段统计
 *
 * 根据竞争的激烈程度逐渐升级
 *
 *
 * base 基础数据
 * 竞争不激烈直接 操作base
 *
 * 竞争激烈 初始化cell 长度为2 的分段
 *
 * 操作cell时通过cas 加锁自选 0 1
 *
 * 如果还激烈的竞争
 *
 * 则扩展cell 的长度 并cas加锁操作
 *
 *
 * 最后 base 加各个cell的值就是总的数据量
 */
public class LongAdderDemo {
    public static void main(String[] args) {

        LongAdder longAdder = new LongAdder();
        longAdder.add(1);
        System.out.println(longAdder.sum());
    }
}
