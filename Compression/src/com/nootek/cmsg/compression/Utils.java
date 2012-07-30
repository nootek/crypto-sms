package com.nootek.cmsg.compression;

import java.io.Closeable;
import java.io.IOException;

/**
 * User: nikolay
 * Date: 7/31/12
 */
public class Utils {

    public static void closeSilently(Closeable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (IOException e) {
            //ignore
        }
    }
}
