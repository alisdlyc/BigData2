package Nio;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class SplitBao {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.put("Hello,World\nI'm qwq\nHo".getBytes(StandardCharsets.UTF_8));
        split(buffer);
        buffer.put("w are u?\n".getBytes(StandardCharsets.UTF_8));
        split(buffer);
    }
    private static void split(ByteBuffer buffer) {
        buffer.flip();
        for (int i = 0; i < buffer.limit(); i++) {
            if(buffer.get(i) == '\n') {
                int len = i + 1 - buffer.position();
                ByteBuffer target = ByteBuffer.allocate(len);
                for (int j = 0; j < len; j++) {
                    target.put(buffer.get());
                }
                target.flip();
                System.out.println(StandardCharsets.UTF_8.decode(target));
            }
        }
        buffer.compact(); // 将buffer中剩余的部分放到buffer头部
    }
}
