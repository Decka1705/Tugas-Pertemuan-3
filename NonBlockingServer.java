import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NonBlockingServer {
    private Selector selector;
    private ServerSocketChannel serverChannel;

    public NonBlockingServer(String host, int port) throws IOException {
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();

        serverChannel.configureBlocking(false);
        
        serverChannel.bind(new InetSocketAddress(host, port));
        
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        
        System.out.println("Server listening on port " + port);
    }

    public void start() throws IOException {
        while (true) {
            selector.select();


            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()) {
                    acceptConnection(key);
                } else if (key.isReadable()) {
                    readData(key);
                }
            }
        }
    }

    private void acceptConnection(SelectionKey key) throws IOException {

        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        

        clientChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("Accepted connection from " + clientChannel.getRemoteAddress());
    }

    private void readData(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(256);
        
        int bytesRead = clientChannel.read(buffer);
        if (bytesRead == -1) {
            clientChannel.close();
            System.out.println("Client disconnected.");
            return;
        }
        
        String data = new String(buffer.array()).trim();
        System.out.println("Menerima: " + data);
        
        buffer.flip();
        clientChannel.write(ByteBuffer.wrap(("" + data).getBytes()));
    }

    public static void main(String[] args) throws IOException {
        NonBlockingServer server = new NonBlockingServer("localhost", 5000);
        server.start();
    }
}
