package persion.nio;


import java.nio.ByteBuffer;

/**
 * description: BufferDemo <br>
 * date: 2020/4/21 18:25 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 */
public class BufferDemo {
    public static void main(String[] args) {
        test();
    }

    public static void test(){
        ByteBuffer allocate = ByteBuffer.allocate(1024);//字节缓冲区 还有别的等等 代表在内存中开辟一块空间

        byte[] b = "nihao".getBytes();
        allocate.put(b);

        allocate.flip();//移动指针

        byte[] bytes = new byte[b.length];
        allocate.get(bytes);
        System.out.println(new String(bytes));
        allocate.clear();
        allocate.compact();

    }
}
