package com.astra.app.clientchatprocess.client;

import com.astra.app.clientchatprocess.util.JacksonSerializer;
import com.astra.app.clientchatprocess.domain.Message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SendingUtil {

    public static void sendMessage(Message message,
                                   SocketChannel socketChannel,
                                   ByteBuffer sendBuf,
                                   JacksonSerializer<Message> msgSerializer) throws IOException {

        sendBuf.clear();
        sendBuf.put(msgSerializer.objToStr(message).getBytes());
        sendBuf.flip();

        while (sendBuf.hasRemaining()) {
            socketChannel.write(sendBuf);
        }
    }

}
