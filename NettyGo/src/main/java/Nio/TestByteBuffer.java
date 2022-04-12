package Nio;



import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

@Slf4j
public class TestByteBuffer {
    public static void main(String[] args) {
        String path = "C:\\Users\\alisdlyc\\IdeaProjects\\BigData\\NettyGo\\src\\main\\resources\\data.txt";
        try (FileChannel channel = new FileInputStream(path).getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (true) {
                int offset = channel.read(buffer);
                log.debug("offer is {}", offset);
                if(offset == -1) break;
                buffer.flip(); // 切换buffer到读模式
                System.out.println(StandardCharsets.UTF_8.decode(buffer));
                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    System.out.println((char)b);
                }
                buffer.clear(); // 调用clear/compact将buffer切换到写模式
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
