package com.nootek.cmsg.compression.encoding;

import android.util.Log;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CharsetConverter {
    private static final String TAG = CharsetConverter.class.getName();

    private static final short[] SUPPORTED_CHARS = new short[1024 * 64];
    private static final EncodingsEnum[] oneByteEncodings = EncodingsEnum.getOneByteEncodings();

    public static List<EncodingData> convertStringToOneByteEncoding(@NotNull String inputString) {
        List<EncodingData> stringsInOneByteEncodings = new ArrayList<EncodingData>();

        short tmp = -1;
        int len = inputString.length();
        for (int i = 0; i < len; i++) {
            char c = inputString.charAt(i);
            tmp &= SUPPORTED_CHARS[c];
        }
        if (tmp != 0) {
            for (EncodingsEnum oneByteEncoding : oneByteEncodings) {
                if ((tmp & (1 << oneByteEncoding.getEncodingNumber())) != 0) {
                    String encodingName = oneByteEncoding.getEncodingName();
                    try {
                        byte[] bytes = inputString.getBytes(encodingName);
                        EncodingData encodingData = new EncodingData(new String(bytes), oneByteEncoding);
                        stringsInOneByteEncodings.add(encodingData);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Can't convert string from UTF-16 to " + encodingName, e);
                    }
                }
            }
        } else {
            try {
                byte[] bytes = inputString.getBytes(EncodingsEnum.UTF_8.getEncodingName());
                EncodingData encodingData = new EncodingData(new String(bytes), EncodingsEnum.UTF_8);
                stringsInOneByteEncodings.add(encodingData);
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "Can't convert string from UTF-16 to UTF-8:" + e.getMessage());
            }
        }

        return stringsInOneByteEncodings;
    }

    static {
        byte[] bytes = new byte[256];
        for (int i = 0; i < 256; i++) {
            bytes[i] = (byte) i;
        }

        for (EncodingsEnum oneByteEncoding : oneByteEncodings) {
            try {
                String allCharsInCurrentEncoding = new String(bytes, oneByteEncoding.getEncodingName());
                for (int j = 0; j < bytes.length; j++) {
                    char c = allCharsInCurrentEncoding.charAt(j);
                    SUPPORTED_CHARS[c] |= (1 << oneByteEncoding.getEncodingNumber());
                }
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "Unsupported encoding error", e);
            }
        }

    }

}
