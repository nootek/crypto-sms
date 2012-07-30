package com.nootek.cmsg.compression.encoding;

import com.nootek.cmsg.std.error.ReferenceChecker;

public class EncodingData
{
    private final String string;
    private final EncodingsEnum stringEncoding;

    public EncodingData(final String string,final EncodingsEnum stringEncoding)
    {
        ReferenceChecker.checkReferenceNotNull(string, stringEncoding);

        this.string = string;
        this.stringEncoding = stringEncoding;
    }

    public String getString()
    {
        return this.string;
    }

    public EncodingsEnum getStringEncoding()
    {
        return this.stringEncoding;
    }
}
