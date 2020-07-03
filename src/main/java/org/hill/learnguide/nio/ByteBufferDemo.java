package org.hill.learnguide.nio;

import java.nio.ByteBuffer;

/**
 * ByteBuffer 使用类说明
 * byte char short int long float double 基本类型除了 Boolean 都有对应的Buffer
 * 0 <= mark <= position <= limit <= capacity
 * 熟悉使用 Buffer 中的 position limit capacity , mark reset rewind clear 方法
 */
public class ByteBufferDemo {

    public static void main(String[] args) {

        test();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        System.out.println("----------allocate()-------");
        printByteBufferInfo(byteBuffer);

        // put 存放数据
        String a = "lynn";
        System.out.println("----------put()------------");
        byteBuffer.put(a.getBytes());
        printByteBufferInfo(byteBuffer);

        // 读写切换
        System.out.println("----------flip()-----------");
        byteBuffer.flip();
        printByteBufferInfo(byteBuffer);

        // 读数据
        System.out.println("-----------get()----------");
        System.out.println((char) byteBuffer.get());
        printByteBufferInfo(byteBuffer);

        byteBuffer.rewind();
        System.out.println("-----------rewind()--------");
        printByteBufferInfo(byteBuffer);

        // 清空缓冲区，但是缓冲区中的数据还在，只是处于被遗忘的状态
        byteBuffer.clear();
        System.out.println("-----------clear()---------");
        printByteBufferInfo(byteBuffer);
    }

    /**
     * 打印byteBuffer 信息
     * @param byteBuffer byteBuffer
     */
    private static void printByteBufferInfo(ByteBuffer byteBuffer) {
        System.out.println("byteBuffer 容量：" + byteBuffer.capacity());
        System.out.println("byteBuffer limit：" + byteBuffer.limit());
        System.out.println("byteBuffer 位置：" + byteBuffer.position());
    }


    private static void test() {
        String str = "guochao";
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        byteBuffer.put(str.getBytes());
        byteBuffer.flip();

        byte[] des = new byte[byteBuffer.limit()];
        byteBuffer.get(des, 0, 2);
        System.out.println(new String(des, 0, 2));
        System.out.println(byteBuffer.position());

        byteBuffer.mark();
        byteBuffer.get(des, 2, 2);
        System.out.println(new String(des, 2, 2));
        System.out.println(byteBuffer.position());

        byteBuffer.reset();
        System.out.println(byteBuffer.position());

        if (byteBuffer.hasRemaining()) {
            System.out.println("byteBuffer 中剩余的数据的长度：" + byteBuffer.remaining());
        }

        System.out.println("############################");
    }
}
