package com.rionour.study.classloader.core.a;

import com.rionour.study.classloader.api.ApiTest;

public class ApiTestImp implements ApiTest {
    @Override
    public void apply() {
        System.out.println("a");
    }
}
