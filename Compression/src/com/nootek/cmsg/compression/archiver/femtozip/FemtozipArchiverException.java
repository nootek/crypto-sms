package com.nootek.cmsg.compression.archiver.femtozip;

import com.nootek.cmsg.compression.archiver.ArchiverException;

public class FemtozipArchiverException extends ArchiverException
{
    public FemtozipArchiverException()
    {
        super();
    }

    public FemtozipArchiverException(String message)
    {
        super(message);
    }

    public FemtozipArchiverException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
