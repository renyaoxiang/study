package com.rionour.study.classloader.main;


import com.google.common.collect.Lists;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class App {

    public static void main(String[] args) {
        File rootFile = new File("testData");
        File[] files = rootFile.getAbsoluteFile().listFiles();

        List<URL> jars = Arrays.asList(files).stream().map(it -> {
            try {
                return it.toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(it -> it != null).collect(Collectors.toList());

        URL[] jarsURL = jars.toArray(new URL[jars.size()]);
        ClassLoader classLoader = new URLClassLoader(jarsURL,
                ClassLoader.getSystemClassLoader().getParent());
        String listsName = Lists.class.getName();
        Thread thread = new Thread(() -> {
            try {
                Class<?> instanceClazz = Thread.currentThread().getContextClassLoader()
                        .loadClass("com.rionour.study.classloader.core.a.ApiTestImp");
                Method method = instanceClazz.getMethod("apply");
                method.invoke(instanceClazz.newInstance());
                System.out.println(classLoader.getParent().getClass());
                Thread.currentThread().setContextClassLoader(classLoader);
                Thread.currentThread().setUncaughtExceptionHandler(($0, $1) -> {
                    $1.printStackTrace();
                });
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }


        });
        thread.setUncaughtExceptionHandler((thread_, e_) -> {
            e_.printStackTrace();
        });
        thread.setContextClassLoader(classLoader);
        thread.start();
    }
}
