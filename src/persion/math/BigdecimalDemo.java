package persion.math;

import java.math.BigDecimal;

/**
 * description: BigdecimalDemo <br>
 * date: 2020/3/31 11:40 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 */
public class BigdecimalDemo {
    public static void main(String[] args) {

        BigDecimal bDouble = new BigDecimal( 2.3d);
        System.out.println(bDouble); //2.29999999999999982236431605997495353221893310546875

        //坑1 //double 是不建议使用的构造方法 参数类型为double的构造方法的结果有一定的不可预知性。有人可能认为在Java中写入newBigDecimal(0.1)所创建的BigDecimal正好等于 0.1（非标度值 1，其标度为 1），但是它实际上等于0.1000000000000000055511151231257827021181583404541015625。这是因为0.1无法准确地表示为 double（或者说对于该情况，不能表示为任何有限长度的二进制小数）。这样，传入到构造方法的值不会正好等于 0.1（虽然表面上等于该值）。
        // 可以是用 BigDecimal.valueof(double)转换

        /**
         * MathContext  构建  精度和舍入模式
         */

        /**
         * RoundingMode 舍入模式
         *
         * ROUND_CEILING    //向正无穷方向舍入
         *  ROUND_DOWN    //向零方向舍入
         *  ROUND_FLOOR    //向负无穷方向舍入
         *  ROUND_HALF_DOWN    //向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，向下舍入, 例如1.55 保留一位小数结果为1.5
         *  ROUND_HALF_EVEN    //向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，如果保留位数是奇数，使用ROUND_HALF_UP，如果是偶数，使用ROUND_HALF_DOWN
         *  ROUND_HALF_UP    //向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，向上舍入, 1.55保留一位小数结果为1.6
         *  ROUND_UNNECESSARY    //计算结果是精确的，不需要舍入模式
         *  ROUND_UP    //向远离0的方向舍入
         *
         *
         */

    }
}
