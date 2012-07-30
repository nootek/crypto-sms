package com.nootek.cmsg.compression.archiver.gzip;

import com.nootek.cmsg.compression.Utils;
import com.nootek.cmsg.compression.archiver.Archiver;
import com.nootek.cmsg.compression.archiver.ArchiverException;
import com.nootek.cmsg.compression.encoding.EncodingData;
import com.nootek.cmsg.compression.encoding.EncodingsEnum;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPArchiver implements Archiver {
    private int archiveType;
    private static final String TAG = GZIPArchiver.class.getName();
    private static final byte[] GZIP_HEADER = new byte[]{
            0x1f, //GZIP_MAGIC,
            (byte) 0x8b, //(GZIP_MAGIC >> 8),
            8, //DEFLATE_CM,
            0, //FLG,
            0, //MTIME,
            0, //MTIME,
            0, //MTIME,
            0, //MTIME,
            0, //XFLG,
            0, //OS
    };


    public GZIPArchiver(int archiveType) {
        this.archiveType = archiveType;
    }

    @Override
    public byte[] compress(@NotNull EncodingData encodingData) throws ArchiverException {
        String stringToCompress = encodingData.getString();
        byte[] stringBytesToCompress = stringToCompress.getBytes();
        int encodingNumber = encodingData.getStringEncoding().getEncodingNumber();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = null;
        try {
            gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(stringBytesToCompress);
            gzipOutputStream.flush();
        } catch (IOException e) {
            throw new ArchiverException("IO error while compressing raw data", e);

        } finally {
            Utils.closeSilently(gzipOutputStream);
        }

        byte[] compressedData = byteArrayOutputStream.toByteArray();
        byte[] compressedDataWithoutHeader = removeGZIPHeaderFromCompressedData(compressedData);
        byteArrayOutputStream.reset();
        try {
            byteArrayOutputStream.write((archiveType << 4) | encodingNumber);
            byteArrayOutputStream.write(compressedDataWithoutHeader);
        } catch (IOException e) {
            throw new ArchiverException("Error while compressing header and input", e);
        }

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public String decompress(@NotNull byte[] compressedData) throws ArchiverException {
        if (compressedData.length == 0) {
            throw new IllegalArgumentException("CompressedData array is empty");
        }
        byte archiveTypeAndEncodingNumber = compressedData[0];
        int encodingType = archiveTypeAndEncodingNumber & 0x0f;
        String encodingName = EncodingsEnum.getEncodingNameByNumber(encodingType);

        byte[] rawCompressedData = new byte[compressedData.length - 1];
        System.arraycopy(compressedData, 1, rawCompressedData, 0, rawCompressedData.length);

        byte[] rawCompressedDataWithHeader = insertGZIPHeaderToCompressedData(rawCompressedData);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPInputStream gzipInputStream = null;

        try {
            gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(rawCompressedDataWithHeader));
            byte[] decompressedData = new byte[1024];
            int read;
            while ((read = gzipInputStream.read(decompressedData)) >= 0) {
                byteArrayOutputStream.write(decompressedData, 0, read);
            }
            byte[] stringBytes = byteArrayOutputStream.toByteArray();
            return new String(stringBytes, encodingName);
        } catch (UnsupportedEncodingException e1) {
            throw new ArchiverException("Can't decompress string,unsupported encoding", e1);
        } catch (IOException e1) {
            throw new ArchiverException("IO error while decompressing", e1);
        } finally {
            Utils.closeSilently(gzipInputStream);
        }

    }

    private static byte[] removeGZIPHeaderFromCompressedData(byte[] compressedData) {
        int compressedDataLengthWithoutHeader = compressedData.length - 10;
        byte[] compressedDataWithoutHeader = new byte[compressedDataLengthWithoutHeader];
        System.arraycopy(compressedData, 10, compressedDataWithoutHeader, 0, compressedDataLengthWithoutHeader);
        return compressedDataWithoutHeader;
    }

    private static byte[] insertGZIPHeaderToCompressedData(@NotNull byte[] compressedData) {
        byte[] compressedDataWithHeader = new byte[compressedData.length + GZIP_HEADER.length];
        System.arraycopy(GZIP_HEADER, 0, compressedDataWithHeader, 0, GZIP_HEADER.length);
        System.arraycopy(compressedData, 0, compressedDataWithHeader, GZIP_HEADER.length, compressedData.length);
        return compressedDataWithHeader;
    }


    public int getArchiveType() {
        return archiveType;
    }
}
