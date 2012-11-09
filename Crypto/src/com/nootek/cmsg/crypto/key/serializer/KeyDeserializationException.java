package com.nootek.cmsg.crypto.key.serializer;

public final class KeyDeserializationException extends Exception
{
    public KeyDeserializationException()
    {
        super();
    }

    public KeyDeserializationException(String message)
    {
        super(message);
    }

    public KeyDeserializationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public KeyDeserializationException(Throwable cause)
    {
        super(cause);
    }

    protected KeyDeserializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
