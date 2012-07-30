package com.nootek.cmsg.compression.archiver.femtozip;

import com.nootek.cmsg.compression.CompressionProtocol;
import com.nootek.cmsg.compression.archiver.Archiver;
import com.nootek.cmsg.compression.archiver.ArchiverException;
import com.nootek.cmsg.compression.encoding.EncodingData;
import com.nootek.cmsg.compression.encoding.EncodingsEnum;
import org.jetbrains.annotations.NotNull;
import org.toubassi.femtozip.CompressionModel;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FemtozipArchiver implements Archiver {
    private Map<EncodingsEnum, Map<Byte, CompressionModel>> dictionaries;
    private final int archiveType;


    public FemtozipArchiver(int archiveType) {
        dictionaries = Dictionaries.getEncodingsToDictionariesMap();
        this.archiveType = archiveType;
    }

    @Override
    public byte[] compress(EncodingData encodingData) throws ArchiverException {
        String stringToCompress = encodingData.getString();
        byte[] stringBytesToCompress;
        try {
            stringBytesToCompress = stringToCompress.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ArchiverException("", e);
        }
        int encodingNumber = encodingData.getStringEncoding().getEncodingNumber();
        EncodingsEnum encoding = EncodingsEnum.getEncodingByNumber(encodingNumber);
        List<byte[]> archiveDataList = new ArrayList<byte[]>();
        Map<Byte, CompressionModel> numberToCompressionModel = dictionaries.get(encoding);
        if (numberToCompressionModel != null) {
            for (Map.Entry<Byte, CompressionModel> numberToCompressionModelEntry : numberToCompressionModel.entrySet()) {
                CompressionModel compressionModel = numberToCompressionModelEntry.getValue();
                byte[] rawCompressedData = compressionModel.compress(stringBytesToCompress);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

                try {
                    dataOutputStream.write((archiveType << 4) | encodingNumber);
                    dataOutputStream.write(numberToCompressionModelEntry.getKey().byteValue());
                    dataOutputStream.write(rawCompressedData);
                } catch (IOException e) {
                    throw new ArchiverException("IO error while compressing data", e);
                }
                byte[] compressedData = byteArrayOutputStream.toByteArray();
                archiveDataList.add(compressedData);
            }
            return Collections.min(archiveDataList, CompressionProtocol.CMP);
        } else {
            throw new ArchiverException("No dictionaries found for that encoding:" + encoding.getEncodingName());
        }
    }

    @Override
    public String decompress(@NotNull byte[] compressedData) throws ArchiverException {
        if (compressedData.length < 0) {
            throw new IllegalArgumentException("CompressedData array is empty");
        }
        byte archiveTypeAndEncodingNumber = compressedData[0];
        int encodingNumber = archiveTypeAndEncodingNumber & 0x0f;
        EncodingsEnum encoding = EncodingsEnum.getEncodingByNumber(encodingNumber);
        String encodingName = EncodingsEnum.getEncodingNameByNumber(encodingNumber);
        byte compressionModelNumber = compressedData[1];
        byte[] rawCompressedData = new byte[compressedData.length - 2];
        System.arraycopy(compressedData, 2, rawCompressedData, 0, rawCompressedData.length);
        Map<Byte, CompressionModel> numberToCompressionModel = dictionaries.get(encoding);
        if (numberToCompressionModel != null) {
            CompressionModel compressionModel = numberToCompressionModel.get(compressionModelNumber);
            if (compressionModel != null) {
                byte[] decompressedData = compressionModel.decompress(rawCompressedData);
                try {
                    return new String(decompressedData, encodingName);
                } catch (UnsupportedEncodingException e) {
                    throw new ArchiverException("Can't decompress string, unsupported encoding: " + encoding, e);
                }
            } else {
                throw new ArchiverException("No suitable compression module found for that model number:" + compressionModelNumber);
            }
        } else {
            throw new ArchiverException("No dictionaries found for that encoding:" + encoding.getEncodingName());
        }
    }

    public int getArchiveType() {
        return archiveType;
    }

}
