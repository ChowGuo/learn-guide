package org.hill.learnguide.nio;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * @Description 测试管道
 * @Author 强风拂面
 * @Date 2020-7-7 10:20
 **/
public class TestPipe {
    @Test
    public void testPipe() throws Exception{
        // 获取管道
        Pipe pipe = Pipe.open();

        // 分配缓冲区

        // 将缓冲区中的数据写入到管道
        Pipe.SinkChannel sinkChannel = pipe.sink();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("通过sinkChannel写入数据".getBytes());
        byteBuffer.flip();
        sinkChannel.write(byteBuffer);

        // 读取缓冲区中的数据
        Pipe.SourceChannel sourceChannel = pipe.source();
        byteBuffer.flip();
        sourceChannel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array(), 0, byteBuffer.limit()));

        sinkChannel.close();
        sourceChannel.close();
    }
}
