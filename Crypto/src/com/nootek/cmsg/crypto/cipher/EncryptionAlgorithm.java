package com.nootek.cmsg.crypto.cipher;

public interface EncryptionAlgorithm
{
    byte[] encrypt(byte[] data,String password) throws EncryptionException;
    byte[] decrypt(byte[] data,String password) throws DecryptionException;
}
