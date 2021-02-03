package com.urise.webapp;

import com.urise.webapp.model.Resume;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainReflection {
    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Resume resume = new Resume("Asr");
        Field[] fields = resume.getClass().getDeclaredFields();
        Field uuid = fields[0];
        uuid.setAccessible(true);
        System.out.println(uuid.get(resume));
        uuid.set(resume, "new_uuid");
        System.out.println(uuid.get(resume));

        Method method = resume.getClass().getDeclaredMethod("toString");
        System.out.println("toString( ) method invocation: " + method.invoke(resume));
    }
}
