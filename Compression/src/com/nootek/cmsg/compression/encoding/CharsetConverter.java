package com.nootek.cmsg.compression.encoding;

import com.nootek.cmsg.std.error.ReferenceChecker;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CharsetConverter
{
    private static final short[] SUPPORTED_CHARS = new short[1024 * 64];
    private static final EncodingsEnum[] oneByteEncodings = EncodingsEnum.getOneByteEncodings();

    public static List<EncodingData> convertStringToOneByteEncoding(final String inputString)
    {
        ReferenceChecker.checkReferenceNotNull(inputString);
        List<EncodingData> stringsInOneByteEncodings = new ArrayList<EncodingData>();


        initSupportedCharsArray();

        short tmp = -1;
        int len = inputString.length();
        for(int i=0; i<len; i++){
            char c = inputString.charAt(i);
            tmp &= SUPPORTED_CHARS[c];
        }
        if(tmp != 0)
        {
            for(EncodingsEnum oneByteEncoding : oneByteEncodings)
            {
                if((tmp & (1 << oneByteEncoding.getEncodingNumber())) != 0)
                {
                    String encodingName = oneByteEncoding.getEncodingName();
                    try
                    {
                        byte[] bytes = inputString.getBytes(encodingName);
                        EncodingData encodingData = new EncodingData(new String(bytes),oneByteEncoding);
                        stringsInOneByteEncodings.add(encodingData);
                    }catch (UnsupportedEncodingException e)
                    {
                        System.err.printf("Can't convert string from UTF-16 to %s:%s",encodingName,e.getMessage());
                    }
                }
            }
        }
        else
        {
            try
            {
                byte[] bytes = inputString.getBytes(EncodingsEnum.UTF_8.getEncodingName());
                EncodingData encodingData = new EncodingData(new String(bytes),EncodingsEnum.UTF_8);
                stringsInOneByteEncodings.add(encodingData);
            }catch (UnsupportedEncodingException e)
            {
                System.err.println("Can't convert string from UTF-16 to UTF-8:" + e.getMessage());
            }
        }

       return stringsInOneByteEncodings;
    }

    private static void initSupportedCharsArray()
    {
        byte[] bytes  = new byte[256];
        for(int i=0; i<256; i++){
            bytes[i] = (byte)i;
        }

        for (EncodingsEnum oneByteEncoding : oneByteEncodings)
        {
            try
            {
                String allCharsInCurrentEncoding = new String(bytes,oneByteEncoding.getEncodingName());
                for (int j = 0; j < bytes.length; j++)
                {
                    char c = allCharsInCurrentEncoding.charAt(j);
                    SUPPORTED_CHARS[c] |= (1 << oneByteEncoding.getEncodingNumber());
                }
            }catch (UnsupportedEncodingException e)
            {
                System.err.println("Unsupported encoding error:" + e.getMessage());
            }
        }

    }

}
