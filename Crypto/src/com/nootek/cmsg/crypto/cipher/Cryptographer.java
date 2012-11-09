package com.nootek.cmsg.crypto.cipher;

public interface Cryptographer
{
    byte[] encrypt(byte[] data,Key key) throws EncryptionException;
    byte[] decrypt(byte[] data,Key key) throws DecryptionException;
}
