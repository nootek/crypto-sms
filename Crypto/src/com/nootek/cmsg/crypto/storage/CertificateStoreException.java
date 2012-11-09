package com.nootek.cmsg.crypto.storage;

public final class CertificateStoreException extends Exception
{
    public CertificateStoreException()
    {
        super();
    }

    public CertificateStoreException(String message)
    {
        super(message);
    }

    public CertificateStoreException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public CertificateStoreException(Throwable cause)
    {
        super(cause);
    }

    protected CertificateStoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
