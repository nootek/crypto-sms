package com.nootek.cmsg.compression.archiver.femtozip;

import android.util.Log;
import com.nootek.cmsg.compression.encoding.EncodingsEnum;
import org.toubassi.femtozip.CompressionModel;
import org.toubassi.femtozip.models.FemtoZipCompressionModel;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

abstract class Dictionaries {
    private static final String TAG = Dictionaries.class.getName();

    private static final String PATH_TO_DICTIONARIES = "Compression/res/";
    private static final Map<EncodingsEnum, Map<Byte, CompressionModel>> encodingsToDictionariesMap = new HashMap<EncodingsEnum, Map<Byte, CompressionModel>>();

    public static Map<EncodingsEnum, Map<Byte, CompressionModel>> getEncodingsToDictionariesMap() {
        return encodingsToDictionariesMap;
    }

    static {
        try {
            addDictionary("dic_iso8859_1_0.dat", (byte) 0, EncodingsEnum.ISO8859_1);
            addDictionary("dic_iso8859_5_0.dat", (byte) 1, EncodingsEnum.ISO8859_5);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "No dictionary with this name found:" + e.getMessage());
        }
    }

    private static void addDictionary(String dictionaryName, byte dictionaryNumber, EncodingsEnum encoding) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(PATH_TO_DICTIONARIES + dictionaryName);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);

        CompressionModel compressionModel = new FemtoZipCompressionModel();
        try {
            compressionModel.load(dataInputStream);
            Map<Byte, CompressionModel> numberToCompressionModel = new HashMap<Byte, CompressionModel>();
            numberToCompressionModel.put(dictionaryNumber, compressionModel);
            encodingsToDictionariesMap.put(encoding, numberToCompressionModel);
        } catch (IOException e) {
            Log.e(TAG, "IO error while loading femtozip dictionary:" + dictionaryName);
        }
    }
}
