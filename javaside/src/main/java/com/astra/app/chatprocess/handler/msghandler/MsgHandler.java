package com.astra.app.charprocess.handler.msghandler;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface MsgHandler {
    void handle(SocketChannel socketChannel);
}
