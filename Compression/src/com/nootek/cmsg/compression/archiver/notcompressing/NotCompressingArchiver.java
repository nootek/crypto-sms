package com.nootek.cmsg.compression.archiver.notcompressing;

import com.nootek.cmsg.compression.archiver.Archiver;
import com.nootek.cmsg.compression.encoding.EncodingData;
import com.nootek.cmsg.compression.encoding.EncodingsEnum;
import com.nootek.cmsg.std.error.ReferenceChecker;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class NotCompressingArchiver extends Archiver
{
    private int archiveType;

    public NotCompressingArchiver()
    {

    }

    public NotCompressingArchiver(int archiveType)
    {
        this.archiveType = archiveType;
    }

    @Override
    public byte[] compress(final EncodingData encodingData) throws NotCompressingArchiverException
    {
        ReferenceChecker.checkReferenceNotNull(encodingData);
        final String stringToCompress = encodingData.getString();
        final byte[] stringBytesToCompress = stringToCompress.getBytes();
        final int encodingNumber = encodingData.getStringEncoding().getEncodingNumber();

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try
        {
            dataOutputStream.write((archiveType << 4) | encodingNumber);
            dataOutputStream.write(stringBytesToCompress);
        }catch (IOException e)
        {
            throw new NotCompressingArchiverException("IO error while compressing",e);
        }

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public String decompress(final byte[] compressedData) throws NotCompressingArchiverException
    {
        ReferenceChecker.checkReferenceNotNull(compressedData);
        if(compressedData.length == 0)
        {
            throw new IllegalArgumentException("CompressedData array is empty");
        }
        final byte archiveTypeAndEncodingNumber = compressedData[0];
        final int encodingNumber = archiveTypeAndEncodingNumber & 0x0f;
        final String encodingName = EncodingsEnum.getEncodingNameByNumber(encodingNumber);
        final byte[] rawCompressedData = new byte[compressedData.length - 1];

        System.arraycopy(compressedData,1,rawCompressedData,0,rawCompressedData.length);
        String string = null;
        try
        {
            string = new String(rawCompressedData,encodingName);
        }catch (UnsupportedEncodingException e)
        {
            throw new NotCompressingArchiverException("Can't decompress string,unsupported encoding",e);
        }
        return string;
    }

    public int getArchiveType()
    {
        return archiveType;
    }
}
