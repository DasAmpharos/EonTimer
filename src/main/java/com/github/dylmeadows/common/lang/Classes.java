package com.github.dylmeadows.common.lang;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Classes {

    private Classes() {
    }

    public static Field[] getAllFields(Class clazz) {
        List<Field> fields = new ArrayList<>();
        Collections.addAll(fields, clazz.getDeclaredFields());
        if (!clazz.getSuperclass().equals(Object.class))
            Collections.addAll(fields, getAllFields(clazz.getSuperclass()));
        return fields.toArray(new Field[0]);
    }

    public static Method[] getAllMethods(Class clazz) {
        List<Method> methods = new ArrayList<>();
        Collections.addAll(methods, clazz.getDeclaredMethods());
        if (!clazz.getSuperclass().equals(Object.class))
            Collections.addAll(methods, getAllMethods(clazz.getSuperclass()));
        return methods.toArray(new Method[0]);
    }
}
