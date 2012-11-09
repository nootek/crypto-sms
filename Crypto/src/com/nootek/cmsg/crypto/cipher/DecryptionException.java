package com.nootek.cmsg.crypto.cipher;

public class DecryptionException extends Exception
{
    public DecryptionException()
    {
        super();
    }

    public DecryptionException(String message)
    {
        super(message);
    }

    public DecryptionException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public DecryptionException(Throwable cause)
    {
        super(cause);
    }

    protected DecryptionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
