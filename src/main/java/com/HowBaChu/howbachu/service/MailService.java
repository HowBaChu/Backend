package com.HowBaChu.howbachu.service;

import com.HowBaChu.howbachu.domain.constants.ResponseCode;

public interface MailService {

    void sendMessage(String to);

    ResponseCode certificate(String email, String inputCode);
}
