package persion.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description: RegexDemo <br>
 * date: 2020/3/31 15:02 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 */
public class RegexDemo {
    public static void main(String[] args) {
        Pattern compile = Pattern.compile("^1[1-9]1$");

        Matcher matcher = compile.matcher("");

        if(matcher.find()){

        }
        //应对有逗号 如 1,2,3,4
        String group = matcher.group(1);  //1

    }
}
