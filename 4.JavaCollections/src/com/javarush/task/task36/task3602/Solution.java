package com.javarush.task.task36.task3602;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/*
Найти класс по описанию
*/
public class Solution {
    public static void main(String[] args) {
        System.out.println(getExpectedClass());
    }

    public static Class getExpectedClass() {
        Class[] classes = Collections.class.getDeclaredClasses();
        for (Class clazz : classes) {
            if (!Modifier.isPrivate(clazz.getModifiers()) ||
                    !Modifier.isStatic(clazz.getModifiers()) ||
                    !List.class.isAssignableFrom(clazz))
                continue;

//            for (Constructor constructor : clazz.getDeclaredConstructors()) {
            try {
                Method method = clazz.getDeclaredMethod("get", int.class);
                Constructor constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                method.setAccessible(true);
                method.invoke(constructor.newInstance(), 0);
            } catch (InvocationTargetException e) {
                if (Objects.equals("IndexOutOfBoundsException", e.getCause().getClass().getSimpleName()))
                    return clazz;
            } catch (Exception ex) {}
        }
        return null;
    }
}
