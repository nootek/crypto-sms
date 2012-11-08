package com.nootek.cmsg.crypto.agreement;

import java.security.PublicKey;

public final class PublicKeyWithID
{
    private final PublicKey key;
    private final byte keyGenerationAlgorithmID;

    public PublicKeyWithID(PublicKey key,byte keyGenerationAlgorithmID)
    {
        if(key == null)
        {
            throw new IllegalArgumentException("Public key is null");
        }
        this.key = key;
        this.keyGenerationAlgorithmID = keyGenerationAlgorithmID;
    }

    public PublicKey getPublicKey()
    {
        return key;
    }

    public byte getKeyGenerationAlgorithmID()
    {
        return keyGenerationAlgorithmID;
    }
}
