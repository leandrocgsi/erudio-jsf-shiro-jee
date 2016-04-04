package br.com.tecsinapse.util;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.joda.time.LocalDateTime;

import java.math.BigInteger;
import java.security.SecureRandom;

import static java.util.Objects.requireNonNull;

public class Token {
    private static final SecureRandom random = new SecureRandom();

    public static String generateToken() {
        return new BigInteger(130, random).toString(32);
    }

    public static String generatePassword() {
        return generateToken().substring(0, 6);
    }

    public static String generateHash() {
        return generateToken().substring(0, 6);
    }

    public static String sha256(String plainText) {
        requireNonNull(plainText);
        return new Sha256Hash(plainText).toHex();
    }

    public static String generateCadastrarNovaSenha() {
        return generateToken().substring(0, 12)
                + new LocalDateTime().toString("hhddyyyyMMss");
    }

    public static String generateCodigoTv() {
        return generateToken().substring(0, 6);
    }
}
