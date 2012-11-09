package com.nootek.cmsg.crypto.key.serializer.ec;

import com.nootek.cmsg.crypto.key.serializer.KeyDeserializationException;
import com.nootek.cmsg.crypto.key.serializer.KeySerializationException;
import com.nootek.cmsg.crypto.key.serializer.PrivateKeySerializer;

import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public final class ECPrivateKeySerializer implements PrivateKeySerializer
{
    @Override
    public byte[] serialize(PrivateKey privKey) throws KeySerializationException
    {
        if(privKey == null)
        {
            throw new IllegalArgumentException("Private key is null");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos;
        try
        {
            dos = new DataOutputStream(baos);
            byte[] privKeyBytes = privKey.getEncoded();
            int privKeyBytesLength = privKeyBytes.length;
            dos.writeInt(privKeyBytesLength);
            dos.write(privKeyBytes);
        }catch (IOException e)
        {
            throw new KeySerializationException("Error while writing serialized private key data",e);
        }
        finally {
            //TODO:close all streams
        }

        return baos.toByteArray();
    }

    @Override
    public ECPrivateKey deserialize(byte[] data) throws KeyDeserializationException
    {
        if(data == null)
        {
            throw new IllegalArgumentException("data array is null");
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis;
        ECPrivateKey ecPrivateKey;
        try
        {
            dis = new DataInputStream(bais);
            int privKeyLength = dis.readInt();
            byte[] privKeyBytes = new byte[privKeyLength];
            dis.read(privKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("ECDH","BC");
            ecPrivateKey = (ECPrivateKey)keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privKeyBytes));
        }
        catch(Exception e)
        {
            throw new KeyDeserializationException("Error while deserializing private key",e);
        }

        return ecPrivateKey;
    }
}
