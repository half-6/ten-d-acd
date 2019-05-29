package com.tend.acd.server.repository;

import com.tend.acd.server.Util;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Repository;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@SuppressWarnings("Duplicates")
@Repository
public class SecurityRepository {
    private static String algorithom = "RSA";
    private static String signatureMethod = "SHA1withRSA";
    private static PrivateKey privateKey;
    private static PublicKey publicKey;
    static {
        try {
            privateKey = readPrivateKey();
            publicKey = readPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithom);
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }
    public void generateKeyPairToFile(Path dir) throws NoSuchAlgorithmException, IOException {
        File f = dir.toFile();
        if(!f.exists()){
            f.mkdirs();
        }
        KeyPair pair = generateKeyPair();
        Util.logger.trace("generate key file to {}",dir.toString());
        FileUtils.writeByteArrayToFile(Paths.get(dir.toString(),"privateKey").toFile(),pair.getPrivate().getEncoded());
        FileUtils.writeByteArrayToFile(Paths.get(dir.toString(),"publicKey").toFile(),pair.getPublic().getEncoded());
    }
    public String encrypt(String input) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(algorithom);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        Util.logger.trace("encrypt {}",input);
        return Base64.getEncoder().encodeToString(cipher.doFinal(input.getBytes(StandardCharsets.UTF_8)));
    }

    public String decrypt(String input) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(algorithom);
        cipher.init(Cipher.DECRYPT_MODE,publicKey);
        Util.logger.trace("decrypt {}",input);
        return new String(cipher.doFinal(Base64.getDecoder().decode(input)), StandardCharsets.UTF_8);
    }

    public String signature(String input) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        SecureRandom secureRandom = new SecureRandom();
        Signature signature = Signature.getInstance(signatureMethod);
        signature.initSign(privateKey,secureRandom);
        signature.update(input.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signature.sign());
    }
    public boolean verify(String input,String sign) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        Signature signature = Signature.getInstance(signatureMethod);
        signature.initVerify(publicKey);
        signature.update(input.getBytes(StandardCharsets.UTF_8));
        byte[] realSig = Base64.getDecoder().decode(sign);
        return signature.verify(realSig);
    }

    private static PrivateKey readPrivateKey() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        byte[] keyBytes = Util.readResourceFile("keys/privateKey");
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance(algorithom);
        return kf.generatePrivate(spec);
    }

    private static PublicKey readPublicKey() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        byte[] keyBytes = Util.readResourceFile("keys/publicKey");
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance(algorithom);
        return kf.generatePublic(spec);
    }
}
