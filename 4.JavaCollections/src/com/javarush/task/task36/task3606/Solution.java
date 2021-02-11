package com.javarush.task.task36.task3606;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/* 
Осваиваем ClassLoader и Reflection
*/
public class Solution {
    private List<Class> hiddenClasses = new ArrayList<>();
    private String packageName;

    public Solution(String packageName) {
        this.packageName = packageName;
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Solution solution = new Solution(Solution.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "com/javarush/task/task36/task3606/data/second");
        solution.scanFileSystem();
        System.out.println(solution.getHiddenClassObjectByKey("secondhiddenclassimpl"));
        System.out.println(solution.getHiddenClassObjectByKey("firsthiddenclassimpl"));
        System.out.println(solution.getHiddenClassObjectByKey("packa"));
    }

    public void scanFileSystem() throws ClassNotFoundException {
        String sep = System.getProperty("file.separator");
        String pathName = packageName;
        if(!(packageName.endsWith(sep))){
            pathName = pathName.concat(sep);
        }

        File folder = new File(pathName);
        String[] files = folder.list((folder1, name) -> name.endsWith(".class"));

        MyClassLoader myLoader = new MyClassLoader(ClassLoader.getSystemClassLoader(), pathName);
        for (String f : files) {
            hiddenClasses.add(myLoader.findClass(f.replace(".class","")));
        }
    }

    private boolean checkClass(Class clazz) {
        boolean isRight = false;
        Constructor[] constructors = clazz.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            if (constructor.getParameterTypes().length == 0)
                isRight = true;
        }
        if (!isRight) return false;
        isRight = false;

        Class[] interfaces = clazz.getInterfaces();
        for (Class i : interfaces) {
            if (i == HiddenClass.class)
                isRight = true;
        }

        return isRight;
    }

    public HiddenClass getHiddenClassObjectByKey(String key) {
        HiddenClass clazz = null;

        for (Class c : hiddenClasses) {
            if (!checkClass(c)) continue;
            String className = c.getSimpleName().toLowerCase();
            if (className.startsWith(key.toLowerCase())) {
                try {
                    Constructor constructor = c.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    clazz = (HiddenClass) constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) { }
                break;
            }
        }
        return clazz;
    }

    private class MyClassLoader extends ClassLoader {
        String pathName;

        public MyClassLoader(ClassLoader parent, String pathName) {
            super(parent);
            this.pathName = pathName;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            File file = new File(pathName + name + ".class");
            try(FileInputStream inputStream = new FileInputStream(file)) {
                byte[] bytes = new byte[(int) file.length()];
                inputStream.read(bytes);
                return defineClass(null, bytes, 0, bytes.length);
            } catch (IOException e) {
                return super.findClass(name);
            }
        }
    }
}

