package com.javarush.task.task37.task3707;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AmigoSet<E> extends AbstractSet<E> implements Set<E>, Serializable, Cloneable {
    private static final Object PRESENT = new Object();
    private transient HashMap<E, Object> map;

    public AmigoSet() {
        map = new HashMap<>();
    }

    public AmigoSet(Collection<? extends E> collection) {
        int capacity = Math.max(16, (int) Math.floor(collection.size()/.75f) + 1);
        map = new HashMap<>(capacity);
        addAll(collection);
    }

    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) != null;
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean mod = false;
        for (E e : c) {
            if (add(e))
                mod = true;
        }
        return mod;
    }

    @Override
    public Object clone() {
        try {
            AmigoSet<E> copy = new AmigoSet<>();
            copy.map = (HashMap<E, Object>) map.clone();
            return copy;
        } catch (Throwable e) {
            throw new InternalError();
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    public void forEach(Consumer<? super E> action) {

    }

    public <T> T[] toArray(IntFunction<T[]> generator) {
        return null;
    }

    public boolean removeIf(Predicate<? super E> filter) {
        return false;
    }

    public Spliterator<E> spliterator() {
        return null;
    }

    public Stream<E> stream() {
        return null;
    }

    public Stream<E> parallelStream() {
        return null;
    }

    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(map.size());
        out.writeFloat(HashMapReflectionHelper.<Float>callHiddenMethod(map, "loadFactor"));
        for (E e : map.keySet()) {
            out.writeObject(e);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        int capacity = in.readInt();
        float loadFactor = in.readFloat();
        map = new HashMap<>(capacity, loadFactor);
        for (int i = 0; i < capacity; i++) {
            map.put((E) in.readObject(), PRESENT);
        }
    }

//    public static void main(String[] args) throws Exception {
//        AmigoSet<Integer> initialAmigoSet = new AmigoSet<>();
//
//        for (int i = 0; i < 10; i++) {
//            initialAmigoSet.add(i);
//        }
//        for (int i : initialAmigoSet) {
//            System.out.println("Initial element = " + i);
//        }
//
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//        objectOutputStream.writeObject(initialAmigoSet);
//
//        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
//        AmigoSet<Integer> loadedAmigoSet = (AmigoSet<Integer>) objectInputStream.readObject();
//
//        System.out.println("Initial Size = " + initialAmigoSet.size());
//
//        for (int i : loadedAmigoSet) {
//            System.out.println("Load element = " + i);
//        }
//        System.out.println("Loaded Size = " + loadedAmigoSet.size());
//    }
}
