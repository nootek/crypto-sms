package com.nootek.cmsg.crypto.cipher.aes;

import com.nootek.cmsg.crypto.cipher.Cryptographer;
import com.nootek.cmsg.crypto.cipher.DecryptionException;
import com.nootek.cmsg.crypto.cipher.EncryptionException;
import com.nootek.cmsg.crypto.cipher.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

public final class AESCryptographer implements Cryptographer
{
    private Cipher cipher;
    private IvParameterSpec ivParameterSpec;
    private byte[] iv = new byte[16];

    public AESCryptographer(byte[] iv)
    {
        if(iv == null)
        {
            throw new IllegalArgumentException("iv is null");
        }

        try
        {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            this.iv = iv.clone();
            this.ivParameterSpec = new IvParameterSpec(this.iv);
        }catch (Exception e)
        {
            //TODO:define exception to throw
            throw new RuntimeException("AESCryptographer initialization error");
        }
    }

    public AESCryptographer()
    {
        try
        {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);
            ivParameterSpec = new IvParameterSpec(iv);
        }catch (Exception e)
        {
            //TODO:define exception to throw
            throw new RuntimeException("AESCryptographer initialization error");
        }

    }

    @Override
    public byte[] encrypt(byte[] data,Key key) throws EncryptionException
    {
        byte[] encryptedData = null;
        try
        {
            byte[] keyBytes = key.getBytes();
            SecretKey secret = new SecretKeySpec(keyBytes,"AES");
            cipher.init(Cipher.ENCRYPT_MODE,secret,ivParameterSpec);
            encryptedData = cipher.doFinal(data);
        }catch (Exception e)
        {
            throw new EncryptionException("Can't encrypt data",e);
        }
        return encryptedData;
    }

    @Override
    public byte[] decrypt(byte[] data,Key key) throws DecryptionException
    {
        byte[] decryptedData = null;
        try
        {
            byte[] keyBytes = key.getBytes();
            SecretKey secret = new SecretKeySpec(keyBytes,"AES");
            cipher.init(Cipher.DECRYPT_MODE,secret,ivParameterSpec);
            decryptedData = cipher.doFinal(data);
        }catch (Exception e)
        {
            throw new DecryptionException("Can't decrypt data",e);
        }
        return decryptedData;
    }

    public byte[] getIV()
    {
        return iv.clone();
    }
}
