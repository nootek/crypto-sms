package com.nootek.cmsg.compression.encoding;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public enum EncodingsEnum {
    ISO8859_1("ISO-8859-1", 0),
    ISO8859_2("ISO-8859-2", 1),
    ISO8859_3("ISO-8859-3", 2),
    ISO8859_4("ISO-8859-4", 3),
    ISO8859_5("ISO-8859-5", 4),
    ISO8859_6("ISO-8859-6", 5),
    ISO8859_7("ISO-8859-7", 6),
    ISO8859_8("ISO-8859-8", 7),
    ISO8859_9("ISO-8859-9", 8),
    ISO8859_11("ISO-8859-11", 9),
    ISO8859_13("ISO-8859-13", 10),
    ISO8859_15("ISO-8859-15", 11),
    UTF_8("UTF-8", 12);

    public static final int ENCODINGS_COUNT = EncodingsEnum.values().length;

    private static class All {
        private static final Map<Integer, EncodingsEnum> ALL = new HashMap<Integer, EncodingsEnum>();
    }

    private final String encodingName;
    private final int encodingNumber;

    EncodingsEnum(@NotNull String encodingName, int encodingNumber) {
        if (All.ALL.containsKey(encodingNumber)) {
            throw new RuntimeException("Duplicate encoding: " + encodingNumber + "/" + encodingName + "; conflict with " + All.ALL.get(encodingNumber));
        }
        All.ALL.put(encodingNumber, this);
        this.encodingName = encodingName;
        this.encodingNumber = encodingNumber;
    }

    public static EncodingsEnum[] getOneByteEncodings() {
        final EncodingsEnum[] oneByteEncodings = {
                ISO8859_1, ISO8859_2,
                ISO8859_3, ISO8859_4,
                ISO8859_5, ISO8859_6,
                ISO8859_7, ISO8859_8,
                ISO8859_9, ISO8859_11,
                ISO8859_13, ISO8859_15
        };
        return oneByteEncodings;
    }

    public static String getEncodingNameByNumber(int number) {
        return getEncodingByNumber(number).encodingName;
    }

    public static EncodingsEnum getEncodingByNumber(int number) {
        if (All.ALL.containsKey(number)) {
            return All.ALL.get(number);
        }
        throw new RuntimeException("Encoding with this number isn't supported:" + number);
    }


    public String getEncodingName() {
        return this.encodingName;
    }

    public int getEncodingNumber() {
        return this.encodingNumber;
    }
}


