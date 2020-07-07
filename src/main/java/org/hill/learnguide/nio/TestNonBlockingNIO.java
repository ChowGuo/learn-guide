package org.hill.learnguide.nio;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Description 非阻塞式NIO的内容
 * @Author 强风拂面
 * @Date 2020-7-6 14:13
 **/
public class TestNonBlockingNIO {

    @Test
    public void testClient() throws Exception {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));
        // 设置非阻塞
        socketChannel.configureBlocking(false);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("hello, this is earth!".getBytes());
        byteBuffer.flip();

        socketChannel.write(byteBuffer);
        socketChannel.shutdownOutput();

        socketChannel.close();
    }

    @Test
    public void testServer() throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 服务端管道非阻塞设置
        serverSocketChannel.configureBlocking(false);
        // 服务端管道绑定端口号
        serverSocketChannel.bind(new InetSocketAddress(8080));

        // 获取选择器
        Selector selector = Selector.open();

        // 将通道注册到选择器, 并且指定“监听事件”
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 轮询式地获取获取选择器上已经就绪的管道的事件
        while (selector.select() > 0) {
            // 获取当前选择器中所注册的选择键（已就绪的监听事件）
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey selectionKey = it.next();
                // 判断具体什么事件准备就绪
                if (selectionKey.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 切换非阻塞模式
                    socketChannel.configureBlocking(false);

                    // 将该通道注册到选择其中
                    socketChannel.register(selector, SelectionKey.OP_READ);

                } else if(selectionKey.isReadable()){
                    // 获取当前选择器中都就绪状态的通道
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int len;
                    while ((len = socketChannel.read(byteBuffer)) != -1) {
                        byteBuffer.flip();
                        System.out.println(new String(byteBuffer.array(), 0, len));
                        byteBuffer.clear();
                    }
                }
                // 取消
                it.remove();
            }

        }

    }
}
