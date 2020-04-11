package persion.basetype;

import java.io.Console;
import java.util.Scanner;

/**
 * description: SystemDemo <br>
 * date: 2020/3/24 17:51 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 */
public class SystemDemo {
    public static void main(String[] args)throws Exception {

        Scanner in = new Scanner(System.in);
        while (in.hasNext()){
            String next = in.next();
            System.out.println(next);
        }

        Console console = System.console();
    }
}
