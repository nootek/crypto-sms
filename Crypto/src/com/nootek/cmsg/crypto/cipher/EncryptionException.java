package com.nootek.cmsg.crypto.cipher;

public class EncryptionException extends Exception
{
    public EncryptionException()
    {
        super();
    }

    public EncryptionException(String message)
    {
        super(message);
    }

    public EncryptionException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public EncryptionException(Throwable cause)
    {
        super(cause);
    }

    protected EncryptionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
