package org.hill.learnguide.nio;

import java.nio.ByteBuffer;

/**
 * ByteBuffer 使用类说明
 */
public class ByteBufferDemo {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        printByteBufferInfo(byteBuffer);

        String a = "lynn";
        byteBuffer.put(a.getBytes());
        printByteBufferInfo(byteBuffer);

        // 读写切换
        byteBuffer.flip();
        printByteBufferInfo(byteBuffer);

    }

    private static void printByteBufferInfo(ByteBuffer byteBuffer) {
        System.out.println("byteBuffer 容量：" + byteBuffer.capacity());
        System.out.println("byteBuffer limit：" + byteBuffer.limit());
        System.out.println("byteBuffer 位置：" + byteBuffer.position());
    }

}
