package com.nootek.cmsg.compression;

import com.nootek.cmsg.compression.archiver.Archiver;
import com.nootek.cmsg.compression.archiver.ArchiverException;
import com.nootek.cmsg.compression.archiver.SimpleArchiverFactory;
import org.jetbrains.annotations.NotNull;

public class DecompressionProtocol {
    public static String decompress(@NotNull byte[] compressedData)
            throws ArchiverException {
        if (compressedData.length == 0) {
            throw new IllegalArgumentException("CompressedData array is empty");
        }
        byte archiveTypeAndEncodingNumber = compressedData[0];
        int archiveType = archiveTypeAndEncodingNumber >> 4;

        SimpleArchiverFactory simpleArchiverFactory = new SimpleArchiverFactory();
        try {
            Archiver archiver = simpleArchiverFactory.createArchiverForType(archiveType);
            return archiver.decompress(compressedData);
        } catch (Exception e) {
            throw new ArchiverException("", e);
        }
    }
}
