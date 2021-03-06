package com.astra.app.chatprocess.util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class BytesSpliter {

    public static List<byte[]> splitBytes(ByteBuffer byteBuffer) {

        int length;
        List<byte[]> resultList = new ArrayList<>();

        while(byteBuffer.position() < byteBuffer.limit()){
            length = byteBuffer.getInt();
            byte[] resByte = new byte[length];
            byteBuffer.get(resByte,0, length);
            resultList.add(resByte);
        }

        return resultList;
    }

}
