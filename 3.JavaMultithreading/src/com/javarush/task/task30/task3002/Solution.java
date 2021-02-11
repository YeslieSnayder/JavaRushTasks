package com.javarush.task.task30.task3002;

/* 
Осваиваем методы класса Integer
*/
public class Solution {

    public static void main(String[] args) {
        System.out.println(convertToDecimalSystem("0x16")); //22
        System.out.println(convertToDecimalSystem("012"));  //10
        System.out.println(convertToDecimalSystem("0b10")); //2
        System.out.println(convertToDecimalSystem("62"));   //62
    }

    public static String convertToDecimalSystem(String s) {
        //напишите тут ваш код
        Integer i;
        if (s.substring(0, 1).equals("0")) {
            if (s.substring(0, 2).equals("0x")) {
                s = s.substring(0, 1) + s.substring(2);
                i = Integer.parseInt(s, 16);
            }
            else if (s.substring(0, 2).equals("0b")) {
                s = s.substring(0, 1) + s.substring(2);
                i = Integer.parseInt(s, 2);
            }
            else
                i = Integer.parseInt(s, 8);
        } else
            i = Integer.parseInt(s);
        return i.toString();
    }
}
