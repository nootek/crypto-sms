package com.nootek.cmsg.crypto.certs;

import com.nootek.cmsg.crypto.agreement.PublicKeyWithID;

import java.security.PublicKey;
import java.util.Date;

public final class PublicCertificate
{

    private final int id;
    private final String phone;
    private final PublicKeyWithID publicKeyWithID;
    private Date storeDate;

    public PublicCertificate(int id,String phone,PublicKeyWithID publicKeyWithID)
    {
        if(phone == null)
        {
            throw new IllegalArgumentException("Phone is null");
        }
        if(publicKeyWithID == null)
        {
            throw new IllegalArgumentException("PublicKeyWithId is null");
        }
        this.id = id;
        this.phone = phone;
        this.publicKeyWithID = publicKeyWithID;
        this.storeDate = new Date(System.currentTimeMillis());
    }

    public PublicCertificate(int id, String phone, PublicKeyWithID publicKeyWithID, Date storeDate)
    {
        this(id,phone,publicKeyWithID);
        this.storeDate = new Date(storeDate.getTime());
    }

    public int getID()
    {
        return id;
    }

    public String getPhone()
    {
        return phone;
    }

    public PublicKeyWithID getPublicKeyWithID()
    {
        return publicKeyWithID;
    }

    public Date getStoreDate()
    {
        return new Date(storeDate.getTime());
    }
}
