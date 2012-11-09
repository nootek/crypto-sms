package com.nootek.cmsg.crypto.key.serializer;

import java.security.PrivateKey;

public interface PrivateKeySerializer
{
    byte[] serialize(PrivateKey key) throws KeySerializationException;
    PrivateKey deserialize(byte[] data) throws KeyDeserializationException;
}
