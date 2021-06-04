package com.astra.app.chatprocess.enums;

import com.astra.app.chatprocess.domain.MessageType;
import com.astra.app.chatprocess.handler.msghandler.ChatHandler;
import com.astra.app.chatprocess.handler.msghandler.LoginHandler;
import com.astra.app.chatprocess.handler.msghandler.LogoutHandler;
import com.astra.app.chatprocess.handler.msghandler.MsgWrongTypeHandler;


public enum HandlerMap {

    CHAT_HANDLER(MessageType.CHAT.getCode(), ChatHandler.class.getName()),
    LOGIN_HANDLER(MessageType.LOGIN.getCode(), LoginHandler.class.getName()),
    LOGOUT_HANDLER(MessageType.LOGOUT.getCode(), LogoutHandler.class.getName()),
    MSGWRONGTYPE_HANDLER(-1, MsgWrongTypeHandler.class.getName());

    private int messageTypeCode;
    private String packageName;

    HandlerMap(int messageTypeCode, String packageName) {
        this.messageTypeCode = messageTypeCode;
        this.packageName = packageName;
    }

    public static String getClassName(int msgType){
        for (HandlerMap handlerMap : HandlerMap.values()){
            if (handlerMap.messageTypeCode == msgType){
                return handlerMap.packageName;
            }
        }
        return HandlerMap.MSGWRONGTYPE_HANDLER.packageName;
    }

    public static void main(String[] args) {

        System.out.println(getClassName(1));

    }

}
