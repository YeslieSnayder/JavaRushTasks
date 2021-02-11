package com.javarush.task.task35.task3507;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/* 
ClassLoader - что это такое?
*/
public class Solution {
    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Set<? extends Animal> allAnimals = getAllAnimals(
                Solution.class.getProtectionDomain().getCodeSource().getLocation().getPath() +
                        Solution.class.getPackage().getName().replaceAll("[.]", "/") + "/data");
        System.out.println(allAnimals);
    }

    public static Set<? extends Animal> getAllAnimals(String pathToAnimals) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        HashSet<Animal> animals = new HashSet<>();

        File[] fileList = new File(pathToAnimals).listFiles();
        if (fileList == null || fileList.length == 0) return animals;

        for (File file : fileList) {
            if (file.getName().endsWith(".class")) {
                MyClassLoader loader = new MyClassLoader();
                Class clazz = loader.loadClass(file.getAbsolutePath());
                if (!Animal.class.isAssignableFrom(clazz))
                    continue;

                Constructor[] constructors = clazz.getConstructors();
                for (Constructor c : constructors) {
                    if (c.getParameterCount() == 0 && c.getModifiers() == Modifier.PUBLIC) {
                        animals.add((Animal) c.newInstance());
                    }
                }
            }
        }
        return animals;
    }

    private static class MyClassLoader extends ClassLoader {
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            try {
                byte[] bytes = Files.readAllBytes(Paths.get(name));
                return defineClass(null, bytes, 0, bytes.length);

            } catch (IOException e) { }

            return super.findClass(name);
        }
    }
}
