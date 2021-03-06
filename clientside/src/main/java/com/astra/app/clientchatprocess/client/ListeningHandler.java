package com.astra.app.clientchatprocess.client;

import com.astra.app.clientchatprocess.domain.Response;
import com.astra.app.clientchatprocess.domain.ResponseType;
import com.astra.app.clientchatprocess.util.BytesSpliter;
import com.astra.app.clientchatprocess.util.JacksonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class ListeningHandler implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(ListeningHandler.class);

    private Selector selector;
    private ByteBuffer recvBuf;
    private LinkedBlockingQueue<String> chatLog;
    private LinkedBlockingQueue<Response> systeminfo;
    private JacksonSerializer<Response> resSerializer = new JacksonSerializer<>();

    public ListeningHandler(Selector selector,
                            ByteBuffer recvBuf,
                            LinkedBlockingQueue<String> chatLog,
                            LinkedBlockingQueue<Response> systemInfo) {
        this.selector = selector;
        this.recvBuf = recvBuf;
        this.chatLog = chatLog;
        this.systeminfo = systemInfo;
    }

    @Override
    public void run() {
        listening();
    }

    public void listening() {


        try {

            while (true) {

                selector.select();

                for (Iterator<SelectionKey> iterator = selector.selectedKeys().iterator(); iterator.hasNext(); ) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();

                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    if (selectionKey.isReadable()) {

                        recvBuf.clear();
                        if (socketChannel.read(recvBuf) == -1) {
                            socketChannel.close();
                            throw new ServerDownException();
                        } else {
                            while (socketChannel.read(recvBuf) != 0) {
                                continue;
                            }
                        }

                        recvBuf.flip();

                        List<byte[]> resultList = BytesSpliter.splitBytes(recvBuf);

                        for (byte[] eleBytes : resultList) {
                            Response response = resSerializer.bytesToObj(eleBytes, Response.class);

                            logger.info(response.toString());
                            String bodyStr = response.getBody() == null ? "null" : response.getBody();
                            logger.info(bodyStr);

                            // Do not receive chat message
                            if ((response.getResponseHeader().getResponseType() == null)) {
                                systeminfo.add(response);
                            } else {
                                if (response.getResponseHeader().getResponseType().getCode() == ResponseType.FRIEND_MESSAGE.getCode()) {
                                    chatLog.add(ChatLogUtil.formatter(bodyStr, response.getSender(), response.getReceiver()));
                                }
                            }
                        }
                    }

                }
            }

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        logger.warn("The response is null, something must went wrong, connect the server for help");
    }
}
