import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class NonBlockingClient {
    private Selector selector;
    private SocketChannel clientChannel;

    public NonBlockingClient(String host, int port) throws IOException {
        selector = Selector.open();
        clientChannel = SocketChannel.open(new InetSocketAddress(host, port));
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
    }

    public void start() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Masukkan Pesan: ");
            String message = scanner.nextLine();
            sendMessage(message);

            selector.select();

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isReadable()) {
                    readResponse(key);
                }
            }
        }
    }

    private void sendMessage(String message) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        clientChannel.write(buffer);
        System.out.println("Mengirim Pesan: " + message);
    }

    private void readResponse(SelectionKey key) throws IOException {
        SocketChannel serverChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(256);
        
        int bytesRead = serverChannel.read(buffer);
        if (bytesRead > 0) {
            String response = new String(buffer.array()).trim();
            System.out.println("Menerima: " + response);
        }
    }

    public static void main(String[] args) throws IOException {
        NonBlockingClient client = new NonBlockingClient("localhost", 5000);
        client.start();
    }
}