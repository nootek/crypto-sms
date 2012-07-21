package com.nootek.cmsg.compression.encoding;

import com.nootek.cmsg.std.error.ReferenceChecker;

import java.util.HashSet;
import java.util.Set;

public enum EncodingsEnum
{
    ISO8859_1("ISO-8859-1",0), ISO8859_2("ISO-8859-2",1),
    ISO8859_3("ISO-8859-3",2), ISO8859_4("ISO-8859-4",3),
    ISO8859_5("ISO-8859-5",4), ISO8859_6("ISO-8859-6",5),
    ISO8859_7("ISO-8859-7",6), ISO8859_8("ISO-8859-8",7),
    ISO8859_9("ISO-8859-9",8), ISO8859_11("ISO-8859-11",9),
    ISO8859_13("ISO-8859-13",10),ISO8859_15("ISO-8859-15",11),
    UTF_8("UTF-8",12);

    public static final int ENCODINGS_COUNT = EncodingsEnum.values().length;
    private final String encodingName;
    private final int encodingNumber;

    static
    {
        if(containsRepeatingNumbers())
        {
            throw new RuntimeException("EncodingsEnum contains encodings with equal numbers");
        }
    }

    EncodingsEnum(final String encodingName,int encodingNumber)
    {
        ReferenceChecker.checkReferenceNotNull(encodingName);

        this.encodingName = encodingName;
        this.encodingNumber = encodingNumber;
    }

    public static EncodingsEnum[] getOneByteEncodings()
    {
        final EncodingsEnum[] oneByteEncodings = {
            ISO8859_1,ISO8859_2,
            ISO8859_3,ISO8859_4,
            ISO8859_5,ISO8859_6,
            ISO8859_7,ISO8859_8,
            ISO8859_9,ISO8859_11,
            ISO8859_13,ISO8859_15

        };

        return oneByteEncodings;
    }

    public static String getEncodingNameByNumber(int number)
    {
        for(EncodingsEnum encoding : EncodingsEnum.values())
        {
            if(encoding.encodingNumber == number)
            {
                return encoding.getEncodingName();
            }
        }
        throw new RuntimeException("Encoding with this number isn't supported:" + number);

    }

    public static int getEncodingNumberByName(final String name)
    {
        for(EncodingsEnum encoding : EncodingsEnum.values())
        {
            if(encoding.encodingName.equals(name))
            {
                return encoding.getEncodingNumber();
            }
        }
        throw new RuntimeException("Encoding with this name isn't supported:" + name);
    }

    public static EncodingsEnum getEncodingByNumber(int number)
    {
        for (EncodingsEnum encoding : EncodingsEnum.values())
        {
            if(encoding.encodingNumber == number)
            {
                return encoding;
            }
        }
        throw new RuntimeException("Encoding with this number isn't supported:" + number);
    }

    private static boolean containsRepeatingNumbers() {
        final Set<Integer> numbers = new HashSet<Integer>();

        for (final EncodingsEnum encoding : EncodingsEnum.values()) {
            numbers.add(encoding.encodingNumber);
        }

        return !(numbers.size() == EncodingsEnum.values().length);
    }

    public String getEncodingName()
    {
        return this.encodingName;
    }

    public int getEncodingNumber()
    {
        return this.encodingNumber;
    }
}


