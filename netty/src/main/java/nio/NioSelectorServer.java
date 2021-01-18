package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class NioSelectorServer {
    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(9000));
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        // 把serverSocketChannel注册到selector上,并且selec对客户端accept连接操作感兴趣
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器启动");

        while (true) {
            // 阻塞等待需要处理的时间发生
            selector.select();
            // 获取selector中注册的全部时间SelectionKey 实例
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                if (next.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) next.channel();
                    SocketChannel accept = channel.accept();
                    accept.configureBlocking(false);
                    // 这里只注册了读事件,如果客户端给客户端发送数据就可以注册写事件
                    accept.register(selector, SelectionKey.OP_READ);
                    System.out.println("客户端连接成功");

                } else if (next.isReadable()) {
                    SocketChannel channel = (SocketChannel) next.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(128);
                    int read = channel.read(byteBuffer);
                    if (read > 0) {
                        System.out.println("接收到的数据:" + new String(byteBuffer.array()));
                    } else if (read == -1) {
                        System.out.println("服务端断开连接");
                        channel.close();
                    }
                }
            }
        }
    }
}
