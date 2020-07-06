package org.hill.learnguide.nio;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @description 非阻塞NIO操作
 * @author  强风拂面
 * @date 2020-7-6 10:58
 **/
public class TestBlockingNIO2 {

    @Test
    public void testClient() throws Exception{
        // 连接服务器管道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",8080));

        // 本地文件读取管道
        FileChannel fileChannel = FileChannel.open(Paths.get("1.txt"), StandardOpenOption.READ);

        // 分配缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 将本地文件通过缓冲区写入到服务器管道
        while (fileChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        // 关闭连接到服务器的管道
        socketChannel.shutdownOutput();

        // 获取服务端发送的消息
        int len;
        while ((len = socketChannel.read(byteBuffer)) != -1) {
            byteBuffer.flip();
            System.out.println(new String(byteBuffer.array(), 0, len));
            byteBuffer.clear();
        }

        // 关闭管道
        fileChannel.close();
        socketChannel.close();
    }

    @Test
    public void testServer() throws Exception{
        // 创建服务器端的管道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));

        // 服务器接受客户端上传文件管道
        FileChannel fileChannel = FileChannel.open(Paths.get("5.txt"),
                StandardOpenOption.READ,
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE);

        // 服务器接受客户端练接管道
        SocketChannel socketChannel = serverSocketChannel.accept();

        // 分配缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 通过缓冲区读取客户端上传文件信息
        while (socketChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            fileChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        // 接受文件后返回客户端信息
        byteBuffer.put("服务器接收完毕！".getBytes());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);

        // 关闭管道
        socketChannel.close();
        serverSocketChannel.close();
        fileChannel.close();
    }
}
