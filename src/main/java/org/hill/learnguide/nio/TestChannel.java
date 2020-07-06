package org.hill.learnguide.nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

/**
 * 1.通道：用于节点与目标节点的连接。在javaNIO中负责缓冲中数据的传输。
 * Channel 不存储数据， 需要配置缓冲区才能完成传输。
 * 2. 通道的实现类：java.nio.channels.Channel
 * FileChannel
 * SocketChannel
 * ServerSocketChannel
 * DatagramChannel
 * 3.获取通道
 * 1.java针对通道类提供了 getChannel()方法
 * 本地IO：FileInputStream/FileOutputStream/RandomAccessFile
 * 网络IO: Socket ServerSocket DatagramSocket
 * 2. 在jdk1.7中的NIO.2针对各个通道提供了方法open()
 * 3. 在jdk1.7中的NIO.2的 Files工具类的 newByteChannel()
 * <p>
 * 通道之间数据的传输
 * transferFrom()
 * transferTo()
 *
 * 五：分散与聚集 scatter gather
 * 分散读取（scattering reads）：将通道中的数据分散到多个缓冲区中
 *
 * 六：字符集
 * 编码：将字符串转换成字节数据
 * 解码：将字节数据转换成字符串
 */
public class TestChannel {
    @Test
    public void copyFile() {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            fileInputStream = new FileInputStream("1.txt");
            fileOutputStream = new FileOutputStream("2.txt");

            inChannel = fileInputStream.getChannel();
            outChannel = fileOutputStream.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            while (inChannel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                outChannel.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (outChannel != null) {
            try {
                outChannel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (inChannel != null) {
            try {
                inChannel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (fileOutputStream != null) {
            try {
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void copyFileWithDirectMemory() throws IOException {
        FileChannel inChannel = FileChannel.open(Paths.get("1.txt"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("2.txt"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        MappedByteBuffer inBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outBuffer = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

        byte[] bytes = new byte[inBuffer.limit()];
        inBuffer.get(bytes);
        outBuffer.put(bytes);

        inChannel.close();
        outChannel.close();
    }

    @Test
    public void channelTransfer() throws IOException {
        FileChannel inChannel = FileChannel.open(Paths.get("1.txt"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("2.txt"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        //inChannel.transferTo(0, inChannel.size(), outChannel);
        outChannel.transferFrom(inChannel, 0, inChannel.size());
        inChannel.close();
        outChannel.close();
    }

    @Test
    public void gatheringAndScatteringTest() throws IOException{
        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        ByteBuffer b1 = ByteBuffer.allocate(30);
        ByteBuffer b2 = ByteBuffer.allocate(1022);

        ByteBuffer [] bs = {b1, b2};
        channel.read(bs);

        for (ByteBuffer b : bs) {
            b.flip();
        }

        System.out.println(new String(bs[0].array(), 0, bs[0].limit()));
        System.out.println(new String(bs[1].array(), 0, bs[1].limit()));

        RandomAccessFile randomAccessFile1 = new RandomAccessFile("3.txt", "rw");
        FileChannel fileChannel = randomAccessFile1.getChannel();
        fileChannel.write(bs);
    }

    @Test
    public void charsetTest() throws IOException{
        /*Map<String, Charset> map = Charset.availableCharsets();

        for (Map.Entry entry: map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }*/

        // 指定编码或解码的字符集类型
        Charset c1 = Charset.forName("GBK");
        // 获取编码器
        CharsetEncoder charsetEncoder = c1.newEncoder();
        // 获取解码器
        CharsetDecoder charsetDecoder = c1.newDecoder();

        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put("强风拂面");
        charBuffer.flip();

        // 编码
        ByteBuffer byteBuffer = charsetEncoder.encode(charBuffer);

        for (int i = 0; i < 8; i++) {
            System.out.println(byteBuffer.get());
        }

        byteBuffer.flip();

        CharBuffer charBuffer1 = charsetDecoder.decode(byteBuffer);
        System.out.println(charBuffer1.toString());
    }
}
