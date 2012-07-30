package com.nootek.cmsg.compression.archiver.notcompressing;

import com.nootek.cmsg.compression.archiver.Archiver;
import com.nootek.cmsg.compression.archiver.ArchiverException;
import com.nootek.cmsg.compression.encoding.EncodingData;
import com.nootek.cmsg.compression.encoding.EncodingsEnum;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class NotCompressingArchiver implements Archiver {
    private int archiveType;


    public NotCompressingArchiver(int archiveType) {
        this.archiveType = archiveType;
    }

    @Override
    public byte[] compress(@NotNull EncodingData encodingData) throws ArchiverException {
        String stringToCompress = encodingData.getString();
        byte[] stringBytesToCompress = stringToCompress.getBytes();
        int encodingNumber = encodingData.getStringEncoding().getEncodingNumber();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.write((archiveType << 4) | encodingNumber);
            dataOutputStream.write(stringBytesToCompress);
        } catch (IOException e) {
            throw new ArchiverException("IO error while compressing", e);
        }

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public String decompress(@NotNull byte[] compressedData) throws ArchiverException {
        byte archiveTypeAndEncodingNumber = compressedData[0];
        int encodingNumber = archiveTypeAndEncodingNumber & 0x0f;
        String encodingName = EncodingsEnum.getEncodingNameByNumber(encodingNumber);
        try {
            return new String(compressedData, 1, compressedData.length - 1, encodingName);
        } catch (UnsupportedEncodingException e) {
            throw new ArchiverException("Can't decompress value,unsupported encoding", e);
        }
    }

    public int getArchiveType() {
        return archiveType;
    }
}
