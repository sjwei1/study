package bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    public static void main(String[] args) throws IOException {
        System.out.println("服务端启动");
        ServerSocket serverSocket = new ServerSocket(9000);
        while (true){
        final Socket clientSocket = serverSocket.accept();
//            handler(clientSocket);
            System.out.println("有客户端连接了");
            new Thread(new Runnable() {
                public void run() {
                    try {
                        handler(clientSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private static void handler(Socket clientSocket) throws IOException {
        byte[] bytes = new byte[1024];
        int read = clientSocket.getInputStream().read(bytes);
        if(read!=-1){
            System.out.println("接受到的客户端请求"+new String(bytes,0,read));
        }
        clientSocket.getOutputStream().write("HelloClient".getBytes());
        clientSocket.getOutputStream().flush();
    }


}
