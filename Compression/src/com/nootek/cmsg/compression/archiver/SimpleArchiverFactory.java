package com.nootek.cmsg.compression.archiver;

import com.nootek.cmsg.compression.archiver.femtozip.FemtozipArchiver;
import com.nootek.cmsg.compression.archiver.gzip.GZIPArchiver;
import com.nootek.cmsg.compression.archiver.notcompressing.NotCompressingArchiver;

import java.util.Iterator;


public class SimpleArchiverFactory implements Iterable<Archiver> {
    private static final Archiver ARCHIVERS[] = {
            new FemtozipArchiver(0),
            new GZIPArchiver(1),
            new NotCompressingArchiver(2)};

    public SimpleArchiverFactory() {
    }

    public Archiver createArchiverForType(int type) throws ArchiverFactoryException {
        if (type < 0) {
            throw new ArchiverFactoryException("Negative type: " + type);
        }

        if (type > ARCHIVERS.length) {
            throw new ArchiverFactoryException("Unknown type: " + type);
        }
        return ARCHIVERS[type];
    }

    @Override
    public Iterator<Archiver> iterator() {
        return new Iterator<Archiver>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < ARCHIVERS.length;
            }

            @Override
            public Archiver next() {
                return ARCHIVERS[i++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
