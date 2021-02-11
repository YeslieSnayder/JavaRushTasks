package com.javarush.task.task36.task3601;

import java.util.List;

public class Controller {

    private Model model;

    public List<String> onShowDataList() {
        model = new Model();
        return model.getStringDataList();
    }
}
