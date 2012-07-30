package com.nootek.cmsg.compression.archiver.femtozip;

import com.nootek.cmsg.compression.encoding.EncodingsEnum;
import org.toubassi.femtozip.CompressionModel;
import org.toubassi.femtozip.models.FemtoZipCompressionModel;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

abstract class Dictionaries
{
    private static final String PATH_TO_DICTIONARIES = "Compression/res/";
    private static Map<EncodingsEnum,Map<Byte,CompressionModel>> encodingsToDictionariesMap = new HashMap<EncodingsEnum, Map<Byte, CompressionModel>>();

    public static Map<EncodingsEnum,Map<Byte,CompressionModel>> getEncodingsToDictionariesMap()
    {
        initDictionaries();
        return encodingsToDictionariesMap;
    }

    private static void initDictionaries()
    {
        try
        {
            addDictionary("dic_iso8859_1_0.dat",(byte)0,EncodingsEnum.ISO8859_1);
            addDictionary("dic_iso8859_5_0.dat",(byte)1,EncodingsEnum.ISO8859_5);
        }catch (FileNotFoundException e)
        {
            System.err.println("No dictionary with this name found:" + e.getMessage());
        }
    }

    private static void addDictionary(final String dictionaryName,final byte dictionaryNumber,final EncodingsEnum encoding) throws FileNotFoundException
    {
        final FileInputStream fileInputStream = new FileInputStream(PATH_TO_DICTIONARIES + dictionaryName);
        final BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        final DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);

        final CompressionModel compressionModel = new FemtoZipCompressionModel();
        try
        {
            compressionModel.load(dataInputStream);
            final Map<Byte,CompressionModel> numberToCompressionModel = new HashMap<Byte, CompressionModel>();
            numberToCompressionModel.put(dictionaryNumber,compressionModel);
            encodingsToDictionariesMap.put(encoding,numberToCompressionModel);
        }catch (IOException e)
        {
            System.err.println("IO error while loading femtozip dictionary:" + dictionaryName);
        }
    }
}
