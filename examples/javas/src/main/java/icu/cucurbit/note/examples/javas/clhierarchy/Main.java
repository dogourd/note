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
        v2();
    }

    public static void v1() throws Throwable {
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

        Class<?> thirdLoad = myLoader.loadClass(klass);

        System.out.println("first class loader: " + firstLoad.getClassLoader());
        System.out.println("second class loader: " + secondLoad.getClassLoader());
        System.out.println("third class loader: " + thirdLoad.getClassLoader());
        System.out.println("class, first == second? " + (secondLoad == firstLoad));
        System.out.println("class, second == third? " + (secondLoad == thirdLoad));
        System.out.println(secondLoad.getName());
    }

    public static void v2() throws Throwable {
        String klass = "icu.cucurbit.note.examples.javas.clhierarchy.ToLoad";
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();


        byte[] binaryRepresentation;
        String internalClassName = klass.replace('.', '/') + ".class";
        try (InputStream is = contextClassLoader.getResourceAsStream(internalClassName)) {
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
                contextClassLoader, typeDefinitions);

        Class<?> firstLoad = myLoader.loadClass(klass);

        Class<?> secondLoad = myLoader.loadClass(klass);

        Class<?> thirdLoad = Class.forName(klass, true, contextClassLoader);

        System.out.println("first class loader: " + firstLoad.getClassLoader());
        System.out.println("second class loader: " + secondLoad.getClassLoader());
        System.out.println("third class loader: " + thirdLoad.getClassLoader());
        System.out.println("class, first == second? " + (secondLoad == firstLoad));
        System.out.println("class, second == third? " + (secondLoad == thirdLoad));
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
