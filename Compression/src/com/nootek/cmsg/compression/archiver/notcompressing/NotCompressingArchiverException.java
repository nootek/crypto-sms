package com.nootek.cmsg.compression.archiver.notcompressing;

import com.nootek.cmsg.compression.archiver.ArchiverException;

public class NotCompressingArchiverException extends ArchiverException
{
    public NotCompressingArchiverException()
    {
        super();
    }

    public NotCompressingArchiverException(String message)
    {
        super(message);
    }

    public NotCompressingArchiverException(String message,Throwable cause)
    {
        super(message,cause);
    }

}
