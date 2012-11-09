package com.nootek.cmsg.crypto.key.derivation;

public class KeyDerivationException extends Exception
{
    public KeyDerivationException()
    {
        super();
    }

    public KeyDerivationException(String message)
    {
        super(message);
    }

    public KeyDerivationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public KeyDerivationException(Throwable cause)
    {
        super(cause);
    }

    protected KeyDerivationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
