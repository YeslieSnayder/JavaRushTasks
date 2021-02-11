package com.javarush.task.task37.task3701;

import java.util.ArrayList;
import java.util.ListIterator;

/* 
Круговой итератор
*/
public class Solution<T> extends ArrayList<T> {
    public static void main(String[] args) {
        Solution<Integer> list = new Solution<>();
        list.add(1);
        list.add(2);
        list.add(3);

        int count = 0;
        for (Integer i : list) {
            //1 2 3 1 2 3 1 2 3 1
            System.out.print(i + " ");
            count++;
            if (count == 10) {
                break;
            }
        }
    }

    public RoundIterator<T> iterator() {
        return new RoundIterator<T>();
    }

    public class RoundIterator<T> implements ListIterator {
        ListIterator iterator = Solution.super.listIterator();

        @Override
        public boolean hasNext() {
            return Solution.super.size() > 0;
        }

        @Override
        public Object next() {
            try {
                return iterator.next();
            } catch (Exception e) {
                iterator = Solution.super.listIterator();
                return iterator.next();
            }
        }

        @Override
        public boolean hasPrevious() {
            return iterator.hasPrevious();
        }

        @Override
        public Object previous() {
            return iterator.previous();
        }

        @Override
        public int nextIndex() {
            return iterator.nextIndex();
        }

        @Override
        public int previousIndex() {
            return iterator.previousIndex();
        }

        @Override
        public void remove() {
            iterator.remove();
            iterator = Solution.super.listIterator();
        }

        @Override
        public void set(Object o) {
            iterator.set(o);
        }

        @Override
        public void add(Object o) {
            iterator.add(o);
        }
    }
}
