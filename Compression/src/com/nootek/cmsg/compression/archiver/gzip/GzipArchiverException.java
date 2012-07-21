package com.nootek.cmsg.compression.archiver.gzip;

import com.nootek.cmsg.compression.archiver.ArchiverException;

public class GzipArchiverException extends ArchiverException
{
    public GzipArchiverException()
    {
        super();
    }

    public GzipArchiverException(String message)
    {
        super(message);
    }

    public GzipArchiverException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
