package org.hill.learnguide.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;

/**
 * @Description 测试UDP连接发送消息 NIO
 * @Author 强风拂面
 * @Date 2020-7-7 10:10
 **/
public class TestDatagramSend {
    public static void main(String[] args) throws Exception{
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(false);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String str = scanner.next();
            byteBuffer.put(str.getBytes());
            byteBuffer.flip();
            datagramChannel.send(byteBuffer, new InetSocketAddress("127.0.0.1", 8080));
            byteBuffer.clear();
        }

        datagramChannel.close();
    }
}
