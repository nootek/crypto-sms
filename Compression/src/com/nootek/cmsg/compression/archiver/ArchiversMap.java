package com.nootek.cmsg.compression.archiver;

import com.nootek.cmsg.compression.archiver.femtozip.FemtozipArchiver;
import com.nootek.cmsg.compression.archiver.gzip.GZIPArchiver;
import com.nootek.cmsg.compression.archiver.notcompressing.NotCompressingArchiver;

import java.util.HashMap;
import java.util.Map;

public class ArchiversMap
{
    private final Map<Integer, Class<? extends Archiver>> archivers;

    public ArchiversMap()
    {
        this.archivers = new HashMap<Integer, Class<? extends Archiver>>();
        initArchiversMap();
    }

    private void initArchiversMap()
    {
        archivers.put(0, FemtozipArchiver.class);
        archivers.put(1, GZIPArchiver.class);
        archivers.put(2, NotCompressingArchiver.class);
    }

    public Map<Integer, Class<? extends Archiver>> getArchiversMap()
    {
        return this.archivers;
    }

}
