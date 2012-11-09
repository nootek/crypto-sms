package com.nootek.cmsg.crypto.storage.file;

import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public final class FileStoreUtils
{
    private FileStoreUtils()
    {

    }

    public static String generateFileNameFromParts(char delimiter,String... parts)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0;i < parts.length - 1;i++)
        {
            stringBuilder.append(parts[i]);
            stringBuilder.append(delimiter);
        }
        stringBuilder.append(parts[parts.length - 1]);
        return stringBuilder.toString();
    }

    public static List<File> getFilesWithNameContains(File directory,String... partNames)
    {
        BitSet bitSet = new BitSet(partNames.length);
        List<File> files = new ArrayList<>();
        File[] allFilesInDir = directory.listFiles();
        for(File file : allFilesInDir)
        {
            String fileName = file.getName();
            int counter = 0;
            for(String partName : partNames)
            {

                if(fileName.contains(partName))
                {
                    bitSet.set(counter);
                }else
                {
                   // break;
                }
                counter++;


            }
            if(bitSet.cardinality() == partNames.length)
            {
                files.add(file);
                bitSet.clear();
            }


        }
        return files;
    }

}
