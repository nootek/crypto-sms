package com.nootek.cmsg.crypto.utils.io;

import java.io.Closeable;
import java.io.IOException;

public class StreamCloser
{
    public static void closeStream(Closeable closeable)
    {
        try
        {
            if(closeable != null)
            {
                closeable.close();
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
