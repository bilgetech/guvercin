package com.bilgetech.guvercin.model;

import java.util.HashMap;

public class ErrorResponse {

    private Message message;
    private Integer status;

    public Message getMessage() {
        if(message == null) {
            message = new Message();
        }

        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public static class Message {
        private HashMap<String, String> errors;
        private Integer status;

        public HashMap<String, String> getErrors() {
            return errors;
        }

        public Integer getStatus() {
            return status;
        }

        public void setErrors(HashMap<String, String> errors) {
            this.errors = errors;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        @Override
        public String toString() {
            String errorsMessage = "";

            if (errors != null) {
                for (String key : errors.keySet()) {
                    errorsMessage += key + ": " + errors.get(key) + "\n";
                }
            }

            return errorsMessage.trim();
        }
    }
}
