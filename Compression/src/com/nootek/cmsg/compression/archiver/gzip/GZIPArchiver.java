package com.nootek.cmsg.compression.archiver.gzip;

import com.nootek.cmsg.compression.archiver.Archiver;
import com.nootek.cmsg.compression.encoding.EncodingData;
import com.nootek.cmsg.compression.encoding.EncodingsEnum;
import com.nootek.cmsg.std.error.ReferenceChecker;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPArchiver extends Archiver
{
    private int archiveType;

    public GZIPArchiver()
    {

    }

    public GZIPArchiver(int archiveType)
    {
        this.archiveType = archiveType;
    }

    @Override
    public byte[] compress(final EncodingData encodingData)  throws GzipArchiverException
    {
        ReferenceChecker.checkReferenceNotNull(encodingData);
        final String stringToCompress = encodingData.getString();
        final byte[] stringBytesToCompress = stringToCompress.getBytes();
        final int encodingNumber = encodingData.getStringEncoding().getEncodingNumber();

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = null;
        try
        {
            gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(stringBytesToCompress);
            gzipOutputStream.flush();
        } catch (IOException e)
        {
            throw new GzipArchiverException("IO error while compressing raw data",e);

        } finally
        {
            if (gzipOutputStream != null)
            {
                try
                {
                    gzipOutputStream.close();
                } catch (IOException e)
                {
                    System.err.println("IO error while closing compression stream:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        byte[] compressedData = byteArrayOutputStream.toByteArray();
        byte[] compressedDataWithoutHeader = removeGZIPHeaderFromCompressedData(compressedData);
        byteArrayOutputStream.reset();
        try
        {
            byteArrayOutputStream.write((archiveType << 4) | encodingNumber);
            byteArrayOutputStream.write(compressedDataWithoutHeader);
        }catch (IOException e)
        {
            throw new GzipArchiverException("Error while compressing header and input",e);
        }

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public String decompress(final byte[] compressedData) throws GzipArchiverException
    {
        ReferenceChecker.checkReferenceNotNull(compressedData);
        if(compressedData.length == 0)
        {
            throw new IllegalArgumentException("CompressedData array is empty");
        }
        final byte archiveTypeAndEncodingNumber = compressedData[0];
        final int encodingType = archiveTypeAndEncodingNumber & 0x0f;
        final String encodingName = EncodingsEnum.getEncodingNameByNumber(encodingType);

        final byte[] rawCompressedData = new byte[compressedData.length -1];
        System.arraycopy(compressedData,1,rawCompressedData,0,rawCompressedData.length);

        final byte[] rawCompressedDataWithHeader = insertGZIPHeaderToCompressedData(rawCompressedData);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPInputStream gzipInputStream = null;

        try
        {
            gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(rawCompressedDataWithHeader));
            final int size = 8192;
            byte[] decompressedData = new byte[size];
            int read = 0;
            while((read = gzipInputStream.read(decompressedData,0,size)) >= 0)
            {
                byteArrayOutputStream.write(decompressedData, 0, read);
            }
            final byte[] stringBytes = byteArrayOutputStream.toByteArray();
            final String string = new String(stringBytes, encodingName);
            return string;
        }catch (UnsupportedEncodingException e1)
        {
            throw new GzipArchiverException("Can't decompress string,unsupported encoding",e1);
        }catch (IOException e1)
        {
            throw new GzipArchiverException("IO error while decompressing",e1);
        }
        finally {
              if(gzipInputStream != null)
              {
                  try
                  {
                      gzipInputStream.close();
                  }catch (IOException e)
                  {
                      System.err.println("IO error while closing decompression stream:" + e.getMessage());
                  }
              }

        }

    }

    private static byte[] removeGZIPHeaderFromCompressedData(byte[] compressedData)
    {
        int compressedDataLengthWithoutHeader = compressedData.length - 10;
        byte[] compressedDataWithoutHeader = new byte[compressedDataLengthWithoutHeader];
        System.arraycopy(compressedData,10,compressedDataWithoutHeader,0,compressedDataLengthWithoutHeader);
        return compressedDataWithoutHeader;
    }

    private static byte[] insertGZIPHeaderToCompressedData(byte[] compressedData)
    {
        ReferenceChecker.checkReferenceNotNull(compressedData);

        byte[] header = createGZIPHeader();
        byte[] compressedDataWithHeader = new byte[compressedData.length + header.length];
        for(int i = 0;i < header.length;i++)
        {
            compressedDataWithHeader[i] = header[i];
        }

        System.arraycopy(compressedData,0,compressedDataWithHeader,header.length,compressedData.length);
        return compressedDataWithHeader;
    }

    private static byte[] createGZIPHeader()
    {
        final int GZIP_MAGIC = 0x8b1f;
        final int DEFLATE_CM = 8;
        final int FLG = 0;
        final int MTIME = 0;
        final int XFLG = 0;
        final int OS = 0;

        final byte[] header = new byte[] {
                (byte)GZIP_MAGIC,
                (byte)(GZIP_MAGIC >> 8),
                (byte)DEFLATE_CM,
                FLG,
                MTIME,
                MTIME,
                MTIME,
                MTIME,
                XFLG,
                OS
        };

        return header;
    }

    public int getArchiveType()
    {
        return archiveType;
    }
}
