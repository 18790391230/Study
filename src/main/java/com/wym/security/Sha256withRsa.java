package com.wym.security;

import org.apache.tomcat.util.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.security.*;

public class Sha256withRsa {

    private static final String alg = "SHA256withRSA";
//    private static final String alg = "SHA256withECDSA";


    public static void main(String[] args) throws Exception{
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        final KeyPair keyPair = keyPairGenerator.generateKeyPair();
        final PrivateKey priv = keyPair.getPrivate();
        final PublicKey pub = keyPair.getPublic();
        System.out.println("公钥=" + Base64.encodeBase64String(pub.getEncoded()));
        System.out.println("私钥=" + Base64.encodeBase64String(priv.getEncoded()));

        //签名签名的消息
        final byte[] message = "hello world1111111111111".getBytes(StandardCharsets.UTF_8);

        final Signature signature = Signature.getInstance(alg);
        signature.initSign(priv);
        signature.update(message);
        final byte[] sign = signature.sign();
        System.out.println(Base64.encodeBase64String(sign));

        final Signature verify = Signature.getInstance(alg);
        verify.initVerify(pub);
        verify.update(message);
        System.out.println(verify.verify(sign));

    }
}
