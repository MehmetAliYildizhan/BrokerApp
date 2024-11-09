package com.example.brokerr.util;

import com.example.brokerr.model.ReturnHeader;

public class ReturnMessageHandler {

    public ReturnHeader getReturnHeader(String code){

        ReturnHeader rh = new ReturnHeader();
        if(code.equals("00")){
            rh.setReturnCode(code);
            rh.setReturnMessage("Succsessfull!!");
        }
        if(code.equals("01")){
            rh.setReturnCode(code);
            rh.setReturnMessage("Wrong username/password.");
        }
        if(code.equals("02")){
            rh.setReturnCode(code);
            rh.setReturnMessage("Username/password CANNOT be empty.");
        }
        if(code.equals("03")){
            rh.setReturnCode(code);
            rh.setReturnMessage("Account blocked due to failed login attempt count.");
        }
        if(code.equals("04")){
            rh.setReturnCode(code);
            rh.setReturnMessage("");
        }
        if(code.equals("05")){
            rh.setReturnCode(code);
            rh.setReturnMessage("");
        }
        if(code.equals("06")){
            rh.setReturnCode(code);
            rh.setReturnMessage("");
        }

        return rh;

    }
}
