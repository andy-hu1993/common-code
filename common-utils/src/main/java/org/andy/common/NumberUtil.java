package org.andy.common;

import java.math.BigDecimal;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author huguangsheng
 * @date 18/6/11 下午11:22
 * @version V1.0
 */
public class NumberUtil {

    /**
     * 将数字转化成大写的阿拉伯数字
     */
    public static String transition(String string) {
        StringBuilder builder = new StringBuilder();
        String[] aa = {"", "十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿"};
        String[] bb = {"一", "二", "三", "四", "五", "六", "七", "八", "九"};
        char[] ch = string.toCharArray();
        int maxindex = ch.length;
        // 字符的转换
        // 两位数的特殊转换
        if (maxindex == 2) {
            for (int i = maxindex - 1, j = 0; i >= 0; i--, j++) {
                if (ch[j] != 48) {
                    if (j == 0 && ch[j] == 49) {
                        builder.append(aa[i]);
                    } else {
                        builder.append(bb[ch[j] - 49] + aa[i]);
                    }
                }
            }
            // 其他位数的特殊转换，使用的是int类型最大的位数为十亿
        } else {
            for (int i = maxindex - 1, j = 0; i >= 0; i--, j++) {
                if (ch[j] != 48) {
                    builder.append(bb[ch[j] - 49] + aa[i]);
                }
            }
        }
        return builder.toString();
    }

    /**
     * 用于计算两个数字的除计算
     * @param num
     * @param divideNum
     * @return
     */
    public static Long divide(Long num, Long divideNum) {
        if (divideNum.intValue() == 0) {
            return 0L;
        }
        BigDecimal bigdecimal = new BigDecimal(num);
        bigdecimal = bigdecimal.divide(new BigDecimal(divideNum), 2, BigDecimal.ROUND_HALF_UP);
        Long value = bigdecimal.longValue();
        return value;
    }
    /**
     * 四舍五入
     * @param value
     * @param scale 保留位数
     * @return
     */
    public static Double formatNumerical(Double value, int scale) {
        return BigDecimal.valueOf(value).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
