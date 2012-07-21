package com.nootek.cmsg.std.error.number;

public abstract class ByteUtilities
{
    public static boolean isBitSet(byte b, int bitNumber)
    {
        if(bitNumber < 0 || bitNumber > 7)
        {
            throw new IllegalArgumentException("Bit number not in range [0,7]:" + bitNumber);
        }
        return (b & (1 << bitNumber)) != 0;
    }

    public static byte setBit(byte b,int bitNumber)
    {
        if(bitNumber < 0 || bitNumber > 7)
        {
            throw new IllegalArgumentException("Bit number not in range [0,7]:" + bitNumber);
        }
        b |= 1 << bitNumber;
        return   b;
    }

    public static byte unsetBit(byte b,int bitNumber)
    {
        if(bitNumber < 0 || bitNumber > 7)
        {
            throw new IllegalArgumentException("Bit number not in range [0,7]:" + bitNumber);
        }
        b &= ~(1 << bitNumber);
        return   b;
    }
}
