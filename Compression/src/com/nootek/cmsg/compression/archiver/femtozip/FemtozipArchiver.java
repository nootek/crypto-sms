package com.nootek.cmsg.compression.archiver.femtozip;

import com.nootek.cmsg.compression.archiver.Archiver;
import com.nootek.cmsg.compression.encoding.EncodingData;
import com.nootek.cmsg.compression.encoding.EncodingsEnum;
import com.nootek.cmsg.std.error.ReferenceChecker;
import com.sun.xml.internal.ws.message.ByteArrayAttachment;
import org.toubassi.femtozip.CompressionModel;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class FemtozipArchiver extends Archiver
{
    private Map<EncodingsEnum,Map<Byte,CompressionModel>> dictionaries;
    private int archiveType;

    public FemtozipArchiver()
    {

    }

    public FemtozipArchiver(int archiveType)
    {
        dictionaries = Dictionaries.getEncodingsToDictionariesMap();
        this.archiveType = archiveType;
    }

    @Override
    public byte[] compress(final EncodingData encodingData) throws FemtozipArchiverException
    {
        final String stringToCompress = encodingData.getString();
        final byte[] stringBytesToCompress = stringToCompress.getBytes();
        final int encodingNumber = encodingData.getStringEncoding().getEncodingNumber();
        final EncodingsEnum encoding = EncodingsEnum.getEncodingByNumber(encodingNumber);
        final List<byte[]> archiveDataList = new ArrayList<byte[]>();
        final Map<Byte,CompressionModel> numberToCompressionModel = dictionaries.get(encoding);
        if(numberToCompressionModel != null)
        {
            for(Map.Entry<Byte,CompressionModel> numberToCompressionModelEntry : numberToCompressionModel.entrySet())
            {
                final CompressionModel compressionModel = numberToCompressionModelEntry.getValue();
                final byte[] rawCompressedData = compressionModel.compress(stringBytesToCompress);

                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

                try
                {
                    dataOutputStream.write((archiveType << 4) | encodingNumber);
                    dataOutputStream.write(numberToCompressionModelEntry.getKey().byteValue());
                    dataOutputStream.write(rawCompressedData);
                }catch (IOException e)
                {
                    throw new FemtozipArchiverException("IO error while compressing data",e);
                }
                final byte[] compressedData = byteArrayOutputStream.toByteArray();
                archiveDataList.add(compressedData);
            }
            final byte[] minimumArchiveData = Collections.min(archiveDataList,new Comparator<byte[]>()
            {
                @Override
                public int compare(byte[] o1, byte[] o2)
                {
                   return o1.length - o2.length;
                }
            });

            return minimumArchiveData;
        }
        else
        {
            throw new FemtozipArchiverException("No dictionaries found for that encoding:" + encoding.getEncodingName());
        }
    }

    @Override
    public String decompress(final byte[] compressedData) throws FemtozipArchiverException
    {
        ReferenceChecker.checkReferenceNotNull(compressedData);
        if(compressedData.length == 0)
        {
            throw new IllegalArgumentException("CompressedData array is empty");
        }
        final byte archiveTypeAndEncodingNumber = compressedData[0];
        final int encodingNumber = archiveTypeAndEncodingNumber & 0x0f;
        final EncodingsEnum encoding = EncodingsEnum.getEncodingByNumber(encodingNumber);
        final String encodingName = EncodingsEnum.getEncodingNameByNumber(encodingNumber);
        final byte compressionModelNumber = compressedData[1];
        final byte[] rawCompressedData = new byte[compressedData.length - 2];
        System.arraycopy(compressedData,2,rawCompressedData,0,rawCompressedData.length);
        final Map<Byte,CompressionModel> numberToCompressionModel = dictionaries.get(encoding);
        if(numberToCompressionModel != null)
        {
            final CompressionModel compressionModel = numberToCompressionModel.get(compressionModelNumber);
            if(compressionModel != null)
            {
                final byte[] decompressedData = compressionModel.decompress(rawCompressedData);
                String string = null;
                try
                {
                    string = new String(decompressedData,encodingName);
                }catch (UnsupportedEncodingException e)
                {
                     throw new FemtozipArchiverException("Can't decompress string,unsupported encoding",e);
                }
                return string;
            }
            else
            {
                throw new FemtozipArchiverException("No suitable compression module found for that model number:" + compressionModelNumber);
            }
        }
        else
        {
            throw new FemtozipArchiverException("No dictionaries found for that encoding:" + encoding.getEncodingName());
        }
    }

    public int getArchiveType()
    {
        return archiveType;
    }

}
