package com.nootek.cmsg.compression.archiver;

import com.nootek.cmsg.compression.archiver.femtozip.FemtozipArchiverException;
import com.nootek.cmsg.compression.archiver.gzip.GzipArchiverException;
import com.nootek.cmsg.compression.archiver.notcompressing.NotCompressingArchiverException;
import com.nootek.cmsg.compression.encoding.EncodingData;

import java.io.IOException;

public abstract class Archiver
{
    public abstract byte[] compress(final EncodingData encodingData) throws FemtozipArchiverException, GzipArchiverException, NotCompressingArchiverException;
    public abstract String decompress(final byte[] compressedData) throws NotCompressingArchiverException, FemtozipArchiverException, GzipArchiverException;
}
