package com.astra.app.clientchatprocess.clienttest;

import com.astra.app.clientchatprocess.client.ListeningHandler;
import com.astra.app.clientchatprocess.client.SendingUtil;
import com.astra.app.clientchatprocess.client.ServerDownException;
import com.astra.app.clientchatprocess.domain.Message;
import com.astra.app.clientchatprocess.domain.Response;
import com.astra.app.clientchatprocess.domain.ResponseCode;
import com.astra.app.clientchatprocess.enums.ByteBufferSetting;
import com.astra.app.clientchatprocess.enums.InnetAddressSetting;
import com.astra.app.clientchatprocess.util.JacksonSerializer;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

public class ClientTest implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(ClientTest.class);

    private String nickName;
    private Selector selector;
    private SocketChannel socketChannel = null;
    private LinkedBlockingQueue<String> chatLog = new LinkedBlockingQueue<>(20);
    private LinkedBlockingQueue<Response> systemInfo = new LinkedBlockingQueue<>(20);

    // Each client assume 5 thread is enough
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    private InputHandlerTest inputHandler;
    private ListeningHandler listeningHandler;
    private ByteBuffer recvBuf = ByteBuffer.allocate(ByteBufferSetting.DEFAULT.getSize());
    private ByteBuffer sendBuf = ByteBuffer.allocate(ByteBufferSetting.DEFAULT.getSize());
    private JacksonSerializer<Message> msgSerializer = new JacksonSerializer<>();

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private CyclicBarrier cyclicBarrier;

    public ClientTest(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    private void init() {

        try {
            InetAddress inetAddress = InetAddress.getByName(InnetAddressSetting.DEFAULT_ADDRESS.localhost);
            InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, InnetAddressSetting.DEFAULT_ADDRESS.port);
            socketChannel = SocketChannel.open(inetSocketAddress);
            logger.info("Connect to: " + socketChannel.getRemoteAddress());
            socketChannel.configureBlocking(false);
            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_READ);

        } catch (UnknownHostException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void sendCheckName() {

        while (true) {
            this.nickName = String.valueOf(RandomUtils.nextInt(300));
            Message message = Message.msgNowLogin(this.nickName);

            try {
                SendingUtil.sendMessage(message, socketChannel, sendBuf, msgSerializer);
            } catch (ClosedChannelException e) {
                logger.error(e.toString());
            } catch (IOException e) {
                logger.error(e.toString());
            }

            while (systemInfo.size() == 0) {
                continue;
            }

            if (systemInfo.poll().getResponseHeader().getReponseCode().getCode() == ResponseCode.SUCCESS.getCode()) {
                System.out.println("Login success");
                break;
            } else {
                System.out.println("your name is repeat, please choose another name");
                continue;
            }
        }
    }

    private void sendingMsg() {
        this.inputHandler = new InputHandlerTest(nickName, socketChannel, sendBuf, chatLog);
        executorService.execute(inputHandler);
    }

    private void listeningMsg() {
        this.listeningHandler = new ListeningHandler(selector, recvBuf, chatLog, systemInfo);
        executorService.execute(listeningHandler);
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            socketChannel.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void run() {
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } catch (BrokenBarrierException e) {
            logger.error(e.getMessage(), e);
        }

        init();
        // after listening msg begin to run, sendCheckName and sendingMsg can begin
        try {
            listeningMsg();
        } catch (ServerDownException e) {
            logger.info("The remote server is down: ");
            logger.info("closing right now...");
            shutdown();
        }

        sendCheckName();
        sendingMsg();
    }
}
