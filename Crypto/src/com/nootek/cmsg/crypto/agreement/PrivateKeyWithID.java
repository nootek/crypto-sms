package com.nootek.cmsg.crypto.agreement;

import java.security.PrivateKey;

public final class PrivateKeyWithID
{
    private final PrivateKey key;
    private final byte keyGenerationAlgorithmID;

    public PrivateKeyWithID(PrivateKey key,byte keyGenerationAlgorithmID)
    {
        if(key == null)
        {
            throw new IllegalArgumentException("Private key is null");
        }
        this.key = key;
        this.keyGenerationAlgorithmID = keyGenerationAlgorithmID;
    }

    public PrivateKey getPrivateKey()
    {
        return key;
    }

    public byte getKeyGenerationAlgorithmID()
    {
        return keyGenerationAlgorithmID;
    }
}
