package com.nootek.cmsg.crypto.storage;

public class CertificateLoadException extends Exception
{
    public CertificateLoadException()
    {
        super();
    }

    public CertificateLoadException(String message)
    {
        super(message);
    }

    public CertificateLoadException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public CertificateLoadException(Throwable cause)
    {
        super(cause);
    }

    protected CertificateLoadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
