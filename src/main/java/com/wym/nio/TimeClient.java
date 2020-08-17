package com.wym.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class TimeClient implements Runnable {

    public static void main(String[] args) throws IOException {
        TimeClient timeClient = new TimeClient("127.0.0.1", 8080);
        new Thread(timeClient, "TimeClient-001").start();
    }

    private int port;
    private String ip;
    private final Selector selector;
    private volatile boolean stop = false;
    private final SocketChannel sc;

    public TimeClient(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
        selector = Selector.open();
        sc = SocketChannel.open();
        sc.configureBlocking(false);

    }

    @Override
    public void run() {

        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();

                    try {
                        handleInput(key);
                    } catch (IOException e) {
                        if (key != null) {
                            key.cancel();
                        }
                        if (key.channel() != null) {
                            key.channel().close();
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {

        if (key.isValid()) {
            SocketChannel sc = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                if (sc.finishConnect()) {
                    sc.register(selector, SelectionKey.OP_READ);
                    doWrite();
                }
            } else if (key.isReadable()) {

                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int read = sc.read(byteBuffer);
                if (read > 0) {
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    String body = new String(bytes, StandardCharsets.UTF_8);

                    System.out.println("接收到了数据：" + body);
                } else if (read < 0) {
                    key.cancel();
                    sc.close();
                }
            }

        }
    }

    private void doConnect() throws IOException {
        if (sc.connect(new InetSocketAddress(ip, port))) {
            sc.register(selector, SelectionKey.OP_READ);
            doWrite();
        } else {
            sc.register(selector, SelectionKey.OP_CONNECT);
        }

    }

    private void doWrite() throws IOException {

        byte[] bytes = "Query Time".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        sc.write(writeBuffer);
        if (!writeBuffer.hasRemaining()) {
            System.out.println("查询时间消息发送成功...");
        }
    }
}
