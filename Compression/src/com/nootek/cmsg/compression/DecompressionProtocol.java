package com.nootek.cmsg.compression;

import com.nootek.cmsg.compression.archiver.*;
import com.nootek.cmsg.std.error.ReferenceChecker;

public class DecompressionProtocol
{
    public static String decompress(final byte[] compressedData)
    {
        ReferenceChecker.checkReferenceNotNull(compressedData);
        if(compressedData.length == 0)
        {
            throw new IllegalArgumentException("CompressedData array is empty");
        }
        final byte archiveTypeAndEncodingNumber = compressedData[0];
        final int encodingNumber = archiveTypeAndEncodingNumber & 0x0f;
        final int archiveType = archiveTypeAndEncodingNumber >> 4;

        final ArchiversMap archiversMap = new ArchiversMap();
        final SimpleArchiverFactory simpleArchiverFactory = new SimpleArchiverFactory(archiversMap);
        try
        {
            final Archiver archiver = simpleArchiverFactory.createArchiverForType(archiveType);
            final String string = archiver.decompress(compressedData);
            return string;
        }catch (ArchiverFactoryException e)
        {
            System.err.println("Can't create archiver for type:" + archiveType);
        }catch (ArchiverException e)
        {
            System.err.println("Can't decompress data:" + e.getMessage());
        }
        return new String();
    }
}
