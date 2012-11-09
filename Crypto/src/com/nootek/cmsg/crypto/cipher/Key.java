package com.nootek.cmsg.crypto.cipher;

public class Key
{
    private byte[] key;

    public Key(byte[] key)
    {
        this.key = key;
    }

    public byte[] getBytes()
    {
        return key.clone();
    }
}
