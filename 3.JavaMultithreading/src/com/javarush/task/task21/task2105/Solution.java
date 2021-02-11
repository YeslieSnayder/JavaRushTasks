package com.javarush.task.task21.task2105;

import java.util.HashSet;
import java.util.Set;

/* 
Исправить ошибку. Сравнение объектов
*/
public class Solution {
    private final String first, last;

    public Solution(String first, String last) {
        this.first = first;
        this.last = last;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Solution))
            return false;
        Solution n = (Solution) o;
        if (n.first == null ||
            n.last == null ||
            first == null ||
            last == null) return false;

        return hashCode() == n.hashCode();
    }

    public int hashCode() {
        if (first == null || last == null) return 0;
        return first.hashCode() + last.hashCode();
    }

    public static void main(String[] args) {
        Set<Solution> s = new HashSet<>();
        s.add(new Solution("Mickey", "A"));
        System.out.println(s.contains(new Solution("Mickey", "A")));
    }
}
