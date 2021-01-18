package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NioServer {

    static List<SocketChannel> channelList = new ArrayList<SocketChannel>();

    public static void main(String[] args) throws IOException {

        // 创建 NIO ServerSocketChannel,与BIO的serverSocket类似
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(9000));
        // 设置非阻塞
        serverSocket.configureBlocking(false);
        while (true) {
            SocketChannel accept = serverSocket.accept();
            if (accept != null) {
                System.out.println("连接成功");
                accept.configureBlocking(false);
                channelList.add(accept);
            }
            // 遍历连接获取数据
            Iterator<SocketChannel> iterator = channelList.iterator();
            while (iterator.hasNext()) {
                SocketChannel next = iterator.next();
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(128);
                // 非阻塞模式read方法不会阻塞,否则阻塞
                int read = next.read(byteBuffer);
                // 如果有数据,打印出来
                if (read > 0) {
                    System.out.println("接收到的数据" + new String(byteBuffer.array()));
                } else if (read != -1) {
                    iterator.remove();
                    System.out.println("客户端断开连接");
                }
            }
        }
    }
}
