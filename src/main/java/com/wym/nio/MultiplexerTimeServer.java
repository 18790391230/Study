package com.wym.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

public class MultiplexerTimeServer implements Runnable {


    public static void main(String[] args) throws IOException {
        MultiplexerTimeServer server = new MultiplexerTimeServer(8080);
        new Thread(server, "Nio-TimeServer-001").start();
    }

    private int port;
    private final Selector selector;
    private volatile boolean stop = false;

    public MultiplexerTimeServer(int port) throws IOException {
        this.port = port;
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(port), 1024);
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("开始监听8080端口...");
    }

    public void stop() {
        stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();
                    handleInput(key);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {

        if (key.isValid()) {
            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel socketChannel = ssc.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);
            }
            if (key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int read = sc.read(readBuffer);
                if (read > 0) {
                    readBuffer.flip();
                    byte[] bs = new byte[readBuffer.remaining()];
                    readBuffer.get(bs);
                    String body = new String(bs, StandardCharsets.UTF_8);
                    System.out.println("接收到数据：" + body);
                    doWrite(sc, MessageFormat.format("现在时间是：{0}", LocalDateTime.now()));
                } else if (read < 0) {
                    key.cancel();
                    sc.close();
                }
            }
        }

    }

    private void doWrite(SocketChannel channel, String response) throws IOException {

        if (response != null) {
            byte[] bs = response.getBytes(StandardCharsets.UTF_8);
            ByteBuffer writeBuffer = ByteBuffer.allocate(bs.length);
            writeBuffer.put(bs);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }

    }
}
