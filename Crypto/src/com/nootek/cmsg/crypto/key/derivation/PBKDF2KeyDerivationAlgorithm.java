package com.nootek.cmsg.crypto.key.derivation;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

public final class PBKDF2KeyDerivationAlgorithm implements KeyDerivationAlgorithm
{
    private SecretKeyFactory keyFactory;
    private byte[] salt = new byte[32];
    private int keyLength;
    private int loopCounter;

    public PBKDF2KeyDerivationAlgorithm()
    {
        try
        {
            keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(salt);
            keyLength = 128;
            loopCounter = 64;
        }catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException("PBKDF2KeyDerivationAlgorithm initialization exception");
        }

    }

    public PBKDF2KeyDerivationAlgorithm(int keyLength,byte[] salt,int loopCounter)
    {
        if(salt == null)
        {
            throw new IllegalArgumentException("Salt is null");
        }
        if(loopCounter < 0)
        {
            throw new IllegalArgumentException("LoopCounter < 0:" + loopCounter);
        }

        try
        {
            keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            this.salt = salt.clone();
            this.keyLength = keyLength;
            this.loopCounter = loopCounter;
        }catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException("PBKDF2KeyDerivationAlgorithm initialization exception");
        }

    }

    @Override
    public byte[] deriveKeyFromPassword(String password) throws KeyDerivationException
    {
        char[] passwordChars = password.toCharArray();
        try
        {
            KeySpec spec = new PBEKeySpec(passwordChars,salt,loopCounter,keyLength);
            SecretKey strongPassword = keyFactory.generateSecret(spec);
            return strongPassword.getEncoded();
        }catch (Exception e)
        {
            throw new KeyDerivationException("Can't derive key from password",e);
        }
    }

    public byte[] getSalt()
    {
        return salt.clone();
    }

    public int getKeyLength()
    {
        return keyLength;
    }

    public int getLoopCounter()
    {
        return loopCounter;
    }
}
