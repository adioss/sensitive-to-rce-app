package com.adioss.hack.utils;

import java.io.*;

public class Serializer {
    public static byte[] serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream(); ObjectOutputStream o = new ObjectOutputStream(b)) {
            o.writeObject(obj);
            return b.toByteArray();
        }
    }

    public static Object deserialize(String type, byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream b = new ByteArrayInputStream(bytes); ObjectInputStream o = new ObjectInputStream(b)) {
            return Class.forName(type).cast(o.readObject());
        }
    }
}
