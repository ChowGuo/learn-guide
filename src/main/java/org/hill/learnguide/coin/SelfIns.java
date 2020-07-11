package org.hill.learnguide.coin;

/**
 * @Description 自增计算的面试题
 * @Author 强风拂面
 * @Date 2020-7-10 13:53
 **/
public class SelfIns {
    /**
     * 通过操作数栈的执行来推理i的值
     * 1. 赋值 = 最后运算
     * 2. = 右边是从左到右来加载值到操作数栈中
     * 3. 实际先算哪个？看运算符优先级
     * 4. 自增自减都是直接修改变量的值，不经过操作数栈
     * 5. 最后赋值之前，临时结果都是放在操作数栈中的
     * @param args 参数
     */
    public static void main(String[] args) {
        int i = 1;
        i = i++;
        int j = i++;
        int k = i + ++i * i++;
        System.out.println("i的值：" + i);
        System.out.println("j的值：" + j);
        System.out.println("k的值：" + k);
    }
}
