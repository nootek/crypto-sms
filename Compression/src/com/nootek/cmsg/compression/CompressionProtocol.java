package com.nootek.cmsg.compression;

import com.nootek.cmsg.compression.archiver.*;
import com.nootek.cmsg.compression.encoding.CharsetConverter;
import com.nootek.cmsg.compression.encoding.EncodingData;

import java.util.*;

public class CompressionProtocol
{
    public static byte[] compress(final String message)
    {
        final List<EncodingData> stringsInOneByteEncodings = CharsetConverter.convertStringToOneByteEncoding(message);
        final List<byte[]> archiveDataToCompare = new ArrayList<byte[]>();
        final ArchiversMap archivers = new ArchiversMap();
        final SimpleArchiverFactory archiverFactory = new SimpleArchiverFactory(archivers);
        final Map<Integer, Class<? extends Archiver>> archiversMap = archivers.getArchiversMap();
        byte[] minimumArchiveData = null;
        for (EncodingData encodingData : stringsInOneByteEncodings)
        {
            for (Map.Entry<Integer, Class<? extends Archiver>> archiverMapEntry : archiversMap.entrySet())
            {
                try
                {
                    Archiver archiver = archiverFactory.createArchiverForType(archiverMapEntry.getKey());
                    final byte[] archiveData = archiver.compress(encodingData);
                    archiveDataToCompare.add(archiveData);
                } catch (ArchiverFactoryException e1)
                {
                    System.err.println("Error while creating archiver:" + e1.getMessage());
                    e1.printStackTrace();
                } catch (ArchiverException e2)
                {
                    System.err.println("Error while compressing:" + e2.getMessage());
                    e2.printStackTrace();
                }

            }

        }
        minimumArchiveData = findMinimumArchiveData(archiveDataToCompare);
        return minimumArchiveData;
    }

    private static byte[] findMinimumArchiveData(List<byte[]> archiveData)
    {

        return Collections.min(archiveData, new Comparator<byte[]>()
        {
            @Override
            public int compare(byte[] archiveOne, byte[] archiveTwo)
            {
                return archiveOne.length - archiveTwo.length;
            }
        });

    }

}
