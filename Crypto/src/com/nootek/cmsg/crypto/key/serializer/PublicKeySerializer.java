package com.nootek.cmsg.crypto.key.serializer;

import java.security.PublicKey;

public interface PublicKeySerializer
{
    byte[] serialize(PublicKey key) throws KeySerializationException;
    PublicKey deserialize(byte[] data) throws KeyDeserializationException;
}
