package com.javarush.task.task36.task3601;

import java.util.List;

public class Model {

    private Service service;

    public Model() {
        service = new Service();
    }

    public List<String> getStringDataList() {
        return service.getData();
    }
}
