package com.javarush.task.task36.task3601;

public class View {

    private Model model;
    private Controller controller;

    public View() {
        model = new Model();
        controller = new Controller();
    }

    public void fireShowDataEvent() {
        System.out.println(controller.onShowDataList());
    }
}
