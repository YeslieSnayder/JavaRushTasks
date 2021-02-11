package com.javarush.task.task30.task3012;

/* 
Получи заданное число
*/

public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.createExpression(74);
    }

    public void createExpression(int number) {
        int[] ints = {1, 3, 9, 27, 81, 243, 729, 2187};
        System.out.print(number + " = ");

        int num = number;
        StringBuilder s = new StringBuilder();

        while (num > 0) {
            if (num % 3 == 0) {
                s.append("0");
                num /= 3;
            } else if (num % 3 == 1) {
                s.append("+");
                num /= 3;
            } else if (num % 3 == 2) {
                s.append("-");
                num /= 3;
                num++;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '+') {
                sb.append(" + ");
                sb.append(ints[i]);
            } else if (c == '-') {
                sb.append(" - ");
                sb.append(ints[i]);
            }
        }
        System.out.println(sb.toString() + Integer.toString(ints[0], 2));
    }
}