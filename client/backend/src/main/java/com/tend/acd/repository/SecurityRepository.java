package com.tend.acd.repository;

import com.tend.acd.Util;
import org.springframework.stereotype.Repository;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@SuppressWarnings("ALL")
@Repository
public class SecurityRepository {
    private static String algorithom = "RSA";
    private static String signatureMethod = "SHA1withRSA";
    private static PrivateKey privateKey;
    private static PublicKey publicKey;
    static {
        try {
            publicKey = readPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String decrypt(String input) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(algorithom);
        cipher.init(Cipher.DECRYPT_MODE,publicKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(input)), StandardCharsets.UTF_8);
    }

    public boolean verify(String input,String sign) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        Signature signature = Signature.getInstance(signatureMethod);
        signature.initVerify(publicKey);
        signature.update(input.getBytes(StandardCharsets.UTF_8));
        byte[] realSig = Base64.getDecoder().decode(sign);
        return signature.verify(realSig);
    }

    private static PublicKey readPublicKey() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        byte[] keyBytes = Util.readResourceFile("keys/publicKey");
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance(algorithom);
        return kf.generatePublic(spec);
    }
}
