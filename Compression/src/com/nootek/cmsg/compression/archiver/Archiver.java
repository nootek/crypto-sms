package com.nootek.cmsg.compression.archiver;

import com.nootek.cmsg.compression.encoding.EncodingData;

public interface Archiver {
    /**
     * @param encodingData
     * @return
     * @throws ArchiverException
     */
    byte[] compress(EncodingData encodingData) throws ArchiverException;

    /**
     * @param compressedData
     * @return
     * @throws ArchiverException
     */
    String decompress(byte[] compressedData) throws ArchiverException;

    /**
     * @return
     */
    int getArchiveType();
}
