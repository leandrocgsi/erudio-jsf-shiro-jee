package br.com.erudio.util;

import com.google.common.base.Splitter;

public interface Constants {
    String TMP_DIR = "/tmp";
    String REST_USER = "rest@erudio.com.br";
    String BASIC_PASSWORD = "dc70cbc6a54bb92cc4c6c17989a4c78eaea6be6ea3bc95710af9458047c108fe";
    String ERUDIO_MAIL = "mail@erudio.com.br";
    String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";

    String DEFAULT_EMAIL_PREFIX = "@erudio.com.br";
    Splitter COMMA_SPLITTER = Splitter.on(',').trimResults().omitEmptyStrings();
}
