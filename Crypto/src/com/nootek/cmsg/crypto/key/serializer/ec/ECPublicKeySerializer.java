package com.nootek.cmsg.crypto.key.serializer.ec;

import com.nootek.cmsg.crypto.key.serializer.KeyDeserializationException;
import com.nootek.cmsg.crypto.key.serializer.KeySerializationException;
import com.nootek.cmsg.crypto.key.serializer.PublicKeySerializer;
import com.nootek.cmsg.crypto.utils.io.StreamCloser;

import java.io.*;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.X509EncodedKeySpec;

public final class ECPublicKeySerializer implements PublicKeySerializer
{
    @Override
    public byte[] serialize(PublicKey key) throws KeySerializationException
    {
        if(key == null)
        {
            throw new IllegalArgumentException("Public key is null");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = null;
        try
        {
            dos = new DataOutputStream(baos);
            byte[] pubKeyBytes = key.getEncoded();
            int pubKeyBytesLength = pubKeyBytes.length;
            dos.writeInt(pubKeyBytesLength);
            dos.write(pubKeyBytes);
        }catch (IOException e)
        {
            throw new KeySerializationException("Error while writing serialized public key data",e);
        }finally {
            StreamCloser.closeStream(dos);
        }

        return baos.toByteArray();
    }

    @Override
    public ECPublicKey deserialize(byte[] data) throws KeyDeserializationException
    {
        if(data == null)
        {
            throw new IllegalArgumentException("data array is null");
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = null;
        ECPublicKey ecPublicKey;
        try
        {
            dis = new DataInputStream(bais);
            int publicKeyLength = dis.readInt();
            byte[] publicKeyBytes = new byte[publicKeyLength];
            dis.read(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("ECDH","BC");
            ecPublicKey = (ECPublicKey)keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        }catch (Exception e)
        {
            throw new KeyDeserializationException("Error while deserializing public key",e);
        }
        finally {
            if(dis != null)
            {
                try
                {
                    dis.close();
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return ecPublicKey;
    }
}
