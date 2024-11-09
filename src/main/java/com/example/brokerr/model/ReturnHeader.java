package com.example.brokerr.model;

public class ReturnHeader {

    public String returnCode;
    public String returnMessage;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    @Override
    public String toString() {
        return "{\n\t" +
                "returnCode='" + returnCode + '\'' +
                ", \n\treturnMessage='" + returnMessage + '\'' + "\n" +
                '}';
    }
}
