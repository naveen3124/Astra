package com.astra.app.charprocess.client;

import com.astra.app.charprocess.domain.Response;
import com.astra.app.charprocess.domain.ResponseCode;

public class SystemInfoUtil {

    public static boolean nameCheckPass(Response response){

        if ((response == null) || (response.getResponseHeader() == null) || (response.getResponseHeader().getReponseCode() == null)){
            return false;
        }

        if (response.getResponseHeader().getReponseCode().getCode() == ResponseCode.SUCCESS.getCode()){
            return true;
        }

        return false;

    }
}
