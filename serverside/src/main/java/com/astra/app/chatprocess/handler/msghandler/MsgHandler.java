package com.astra.app.chatprocess.handler.msghandler;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface MsgHandler {
    void handle(SocketChannel socketChannel);
}
