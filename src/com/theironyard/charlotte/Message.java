package com.theironyard.charlotte;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by jenniferchang on 8/24/16.
 */
public class Message {
    String subject;
    String date;
    String body;

    public Message(String subject, String date, String body) {
        this.subject = subject;
        this.date = date;
        this.body = body;
    }
}
