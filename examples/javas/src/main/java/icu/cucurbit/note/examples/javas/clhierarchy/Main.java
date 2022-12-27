package icu.cucurbit.note.examples.javas.clhierarchy;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <h1> Main </h1>
 *
 * @author yuwen
 * @since 2022-12-27
 */
public class Main {

    public static void main(String[] args) throws Throwable {
        String klass = "icu.cucurbit.note.examples.javas.clhierarchy.ToLoad";

        Class<?> firstLoad = Class.forName(klass);
        byte[] binaryRepresentation;

        String internalClassName = firstLoad.getName().replace('.', '/') + ".class";
        try (InputStream is = firstLoad.getClassLoader().getResourceAsStream(internalClassName)) {
            if (is == null) {
                throw new IOException(internalClassName + " not found.");
            }
            int size =is.available();
            binaryRepresentation = new byte[size];
            new DataInputStream(is).readFully(binaryRepresentation);
        }

        Map<String, byte[]> typeDefinitions = new HashMap<>();
        typeDefinitions.put(klass, binaryRepresentation);

        ChildFirstByteArrayClassLoader myLoader = new ChildFirstByteArrayClassLoader(
                firstLoad.getClassLoader(), typeDefinitions);
        Class<?> secondLoad = myLoader.loadClass(klass);

        System.out.println(secondLoad == firstLoad);
        System.out.println(secondLoad.getName());
    }

    public static class ChildFirstByteArrayClassLoader extends ClassLoader {

        private final Map<String, byte[]> typeDefinitions;

        public ChildFirstByteArrayClassLoader(ClassLoader parent, Map<String, byte[]> typeDefinitions) {
            super(parent);
            this.typeDefinitions = typeDefinitions;
        }


        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            synchronized (getClassLoadingLock(name)) {
                Class<?> loaded = findLoadedClass(name);
                if (loaded != null) {
                    return loaded;
                }

                try {
                    Class<?> type = findClass(name);
                    if (resolve) {
                        resolveClass(type);
                    }
                    return type;
                } catch (ClassNotFoundException notFound) {
                    return super.loadClass(name, resolve);
                }
            }
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] binaryRepresentation = typeDefinitions.remove(name);
            if (binaryRepresentation == null) {
                throw new ClassNotFoundException(name);
            }

            int idx = name.lastIndexOf('.');
            if (idx != -1) {
                String pkgName = name.substring(0, idx);
                Package pkg = getDefinedPackage(pkgName);
                if (pkg == null) {
                    definePackage(name, null, null, null, null, null, null, null);
                }
            }
            return defineClass(name, binaryRepresentation, 0, binaryRepresentation.length, null);
        }
    }
}
