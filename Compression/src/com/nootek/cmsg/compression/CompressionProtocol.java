package com.nootek.cmsg.compression;

import android.util.Log;
import com.nootek.cmsg.compression.archiver.Archiver;
import com.nootek.cmsg.compression.archiver.SimpleArchiverFactory;
import com.nootek.cmsg.compression.encoding.CharsetConverter;
import com.nootek.cmsg.compression.encoding.EncodingData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CompressionProtocol {
    private static final String TAG = CompressionProtocol.class.getName();
    public static final Comparator<byte[]> CMP = new Comparator<byte[]>() {
        @Override
        public int compare(byte[] archiveOne, byte[] archiveTwo) {
            return archiveOne.length - archiveTwo.length;
        }
    };


    public static byte[] compress(final String message) {
        final List<EncodingData> stringsInOneByteEncodings = CharsetConverter.convertStringToOneByteEncoding(message);
        final List<byte[]> archiveDataToCompare = new ArrayList<byte[]>();
        final SimpleArchiverFactory archiverFactory = new SimpleArchiverFactory();
        for (EncodingData encodingData : stringsInOneByteEncodings) {
            for (Archiver archiver : archiverFactory) {
                final byte[] archiveData;
                try {
                    archiveData = archiver.compress(encodingData);
                } catch (Exception e) {
                    Log.e(TAG, "ignore type: " + archiver.getArchiveType(), e);
                    continue;
                }
                archiveDataToCompare.add(archiveData);
            }

        }
        return Collections.min(archiveDataToCompare, CMP);
    }


}
