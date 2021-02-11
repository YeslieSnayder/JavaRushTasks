package com.javarush.task.task34.task3403;

/* 
Разложение на множители с помощью рекурсии
*/
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.recurse(132);//2 2 3 11
    }

    public void recurse(int n) {
        if (n <= 1)
            return;

        int m = 2;
        while (true) {
            if (n % m == 0) {
                System.out.print(m + " ");
                if (m == n)
                    return;
                break;
            }
            m++;
        }
        recurse(n / m);
    }
}
