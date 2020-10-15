package com.example.pocketalert;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.UUID;

public class Message {
    public String command;
    public String argument;
    private String messageId;

    public Message(String messageId) {
        this.messageId = messageId;
    }

    public Message() {
        messageId = UUID.randomUUID().toString();
    }

    public static Message fromString(String text) {
        Message message = new Message();
        int index = text.indexOf('\n');

        if (index == -1) {
            message.command = text;
            Log.e("Message", "Received a message without messageId: " + text);
            return message;
        }

        message.command = text.substring(0, index);
        text = text.substring(index + 1);
        index = text.indexOf('\n');

        if (index == -1) {
            message.messageId = text;
            return message;
        }

        message.messageId = text.substring(0, index);
        message.argument = text.substring(index + 1);
        return message;
    }

    public String getMessageId() {
        return messageId;
    }

    @NonNull
    @Override
    public String toString() {
        return command + "\n" + messageId + "\n" + argument;
    }
}
