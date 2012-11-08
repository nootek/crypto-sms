package com.nootek.cmsg.crypto.certs;

import com.nootek.cmsg.crypto.agreement.PrivateKeyWithID;
import com.nootek.cmsg.crypto.cipher.EncryptionAlgorithmIDs;

import java.security.PrivateKey;

public final class PrivateCertificate
{
    private final int id;
    private final String phone;
    private final PrivateKeyWithID keyWithID;
    private final byte encryptionAlgortihmID = EncryptionAlgorithmIDs.AES_VERSION_1;

    public PrivateCertificate(int id,String phone,PrivateKeyWithID keyWithID)
    {
        if(keyWithID == null)
        {
            throw new IllegalArgumentException("PrivateKeyWithID is null");
        }
        this.id = id;
        this.phone = phone;
        this.keyWithID = keyWithID;
    }

    public PrivateKeyWithID getPrivateKeyWithID()
    {
        return keyWithID;
    }

    public int getID()
    {
        return id;
    }

    public String getPhone()
    {
        return phone;
    }

    public byte getEncryptionAlgortihmID()
    {
        return encryptionAlgortihmID;
    }
}
