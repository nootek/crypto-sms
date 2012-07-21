package com.nootek.cmsg.std.error;

public abstract class ReferenceChecker
{
    public static void checkReferenceNotNull(final Object reference) throws NullPointerException
    {
        if(reference == null)
        {
            throw new NullPointerException();
        }
    }

    public static void checkReferenceNotNull(final Object... references)
    {
        for(Object reference : references)
        {
            checkReferenceNotNull(reference);
        }
    }
}
