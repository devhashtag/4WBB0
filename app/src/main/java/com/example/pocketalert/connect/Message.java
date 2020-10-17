package com.example.pocketalert.connect;

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

    public static Message fromString(String text) throws IllegalArgumentException {
        // Get ID
        int index = text.indexOf('\n');

        if (index == -1) {
            Log.e("Message", "Received a message without messageId: " + text);
            throw new IllegalArgumentException("Message must contain an Id");
        }

        Message message = new Message(text.substring(0, index));

        // Get command
        text = text.substring(index + 1);
        index = text.indexOf('\n');

        if (index == -1) {
            message.command = text;
            return message;
        }

        // Get argument
        message.command = text.substring(0, index);
        message.argument = text.substring(index + 1);

        return message;
    }

    public String getMessageId() {
        return messageId;
    }

    @NonNull
    @Override
    public String toString() {
        return argument == null
                ? messageId + "\n" + command
                : messageId + "\n" + command + "\n" + argument;
    }
}
