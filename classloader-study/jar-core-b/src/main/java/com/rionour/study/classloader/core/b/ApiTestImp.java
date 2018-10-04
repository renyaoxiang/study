package com.rionour.study.classloader.core.b;


import com.rionour.study.classloader.api.ApiTest;

public class ApiTestImp implements ApiTest {
    @Override
    public void apply() {
        System.out.println("b");
    }
}
