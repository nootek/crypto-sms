package com.nootek.cmsg.compression.archiver;

import java.lang.reflect.Constructor;
import java.util.Map;

public class SimpleArchiverFactory
{
    private final ArchiversMap archiversMap;

    public SimpleArchiverFactory(ArchiversMap archiversMap)
    {
        this.archiversMap = archiversMap;
    }

    public Archiver createArchiverForType(int type) throws ArchiverFactoryException
    {
        final Map<Integer, Class<? extends Archiver>> archivers = archiversMap.getArchiversMap();
        final Class<? extends Archiver> archiverClass = archivers.get(type);
        Archiver archiver = null;
        if (archiverClass != null)
        {
            Constructor<?> archiverConstructor = null;

            try
            {
                archiverConstructor = archiverClass.getConstructor(int.class);
            } catch (NoSuchMethodException e)
            {
                final String exceptionMessage = String.format("Class %s doesn't have public constructor with int number argument\n",archiverClass.getSimpleName());
                throw new ArchiverFactoryException(exceptionMessage);
            }


            if (archiverConstructor != null)
            {
                try
                {
                    archiver = (Archiver) archiverConstructor.newInstance(type);
                } catch (Exception e)
                {
                    throw new ArchiverFactoryException("Can't create archiver with number:" + type);
                }
            }

            return archiver;
        } else
        {
            throw new ArchiverFactoryException("No archiver with this number found:" + type);
        }

    }
}
