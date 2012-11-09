package com.nootek.cmsg.crypto.key.serializer;

public final class KeySerializationException extends Exception
{
    public KeySerializationException()
    {
        super();
    }

    public KeySerializationException(Throwable cause)
    {
        super(cause);
    }

    public KeySerializationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public KeySerializationException(String message)
    {
        super(message);
    }
}
