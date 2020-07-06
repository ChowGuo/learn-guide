package org.hill.learnguide.nio;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @Description 测试阻塞式NIO
 * @Author 强风拂面
 * @Date 2020-7-6 10:35
 *
 * 1. 通道 Channel：负责连接
 *      java.nio.channels.Channel
 *          |-- SelectableChannel
 *              |-- socketChannel
 *              |-- ServerSocketChannel
 *              |-- DatagramSocketChannel
 *
 *              |-- pipe.sinkChannel
 *              |-- Pipe.SourceChannel
 *
 * 2. 缓冲区
 *
 * 3. 选择器（Selector): selectableChannel 的多路选择器。用于监控
 *
 **/
public class TestBlockingNIO {

    @Test
    public void testClientSocket() throws Exception{
        // 监听"127.0.0.1", 8848 网络通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8848));

        // 本地文件通道
        FileChannel inChannel = FileChannel.open(Paths.get("1.txt"), StandardOpenOption.READ);

        // 分配缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 通过本地文件通道，读取文件内容到网络通道
        while (inChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        }



        inChannel.close();
        socketChannel.close();
    }

    @Test
    public void testServerSocketChannel() throws Exception {
        // 获取服务端通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 创建本地文件通道
        FileChannel fileChannel = FileChannel.open(Paths.get("4.txt"), StandardOpenOption.READ,
                StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);

        // 服务端通道绑定端口号
        serverSocketChannel.bind(new InetSocketAddress(8848));

        // 获取socket通道
        SocketChannel socketChannel = serverSocketChannel.accept();

        // 分配缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 从socket通道中获取数据，并将数据写入到文件通道中
        while (socketChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            fileChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        socketChannel.close();
        fileChannel.close();
        serverSocketChannel.close();
    }


}
