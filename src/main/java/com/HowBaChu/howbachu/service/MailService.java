package com.HowBaChu.howbachu.service;

public interface MailService {

    void sendMessage(String to);

    boolean certificate(String email, String inputCode);
}
