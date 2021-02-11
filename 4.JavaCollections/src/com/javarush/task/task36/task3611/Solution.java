package com.javarush.task.task36.task3611;

import java.util.Set;
import java.util.TreeSet;

/* 
Сколько у человека потенциальных друзей?
*/

public class Solution {
    private boolean[][] humanRelationships;

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.humanRelationships = generateRelationships();

        Set<Integer> allFriendsAndPotentialFriends = solution.getAllFriendsAndPotentialFriends(2, 1);
        System.out.println(allFriendsAndPotentialFriends);                              // Expected: [0, 1, 2, 3, 5, 7]
        Set<Integer> potentialFriends = solution.removeFriendsFromSet(allFriendsAndPotentialFriends, 4);
        System.out.println(potentialFriends);                                           // Expected: [2, 5, 7]
    }

    public Set<Integer> getAllFriendsAndPotentialFriends(int index, int deep) {
        TreeSet<Integer> set = new TreeSet<>();

        // Сначала находим всех друзей человека
        for (int i = 0; i < humanRelationships.length; i++) {
            if (i < index && humanRelationships[index][i])
                set.add(i);
            else if (i > index && humanRelationships[i][index])
                set.add(i);
        }

        set.addAll(getPotentialFriends(set, deep)); // Получаем потенциальных друзей
        set.remove(index);  // Удаляем index

        return set;
    }

    private Set<Integer> getPotentialFriends(TreeSet<Integer> treeSet, int deep) {
        TreeSet<Integer> set = new TreeSet<>();

        for (Integer i : treeSet) {
            for (int x = 0; x < humanRelationships.length; x++) {
                if (x < i && humanRelationships[i][x])
                    set.add(x);
                else if (x > i && humanRelationships[x][i])
                    set.add(x);
            }
        }
        if (--deep > 1)
            set.addAll(getPotentialFriends(set, deep));

        return set;
    }

    // Remove from the set the people with whom you already have a relationship
    public Set<Integer> removeFriendsFromSet(Set<Integer> set, int index) {
        for (int i = 0; i < humanRelationships.length; i++) {
            if ((i < index) && (index < humanRelationships.length) && humanRelationships[index][i]) {
                set.remove(i);
            } else if ((i > index) && humanRelationships[i][index]) {
                set.remove(i);
            }
        }
        return set;
    }

    // Return test data
    private static boolean[][] generateRelationships() {
        return new boolean[][]{
                {true},                                                                 //0
                {true, true},                                                           //1
                {false, true, true},                                                    //2
                {false, false, false, true},                                            //3
                {true, true, false, true, true},                                        //4
                {true, false, true, false, false, true},                                //5
                {false, false, false, false, false, true, true},                        //6
                {false, false, false, true, false, false, false, true}                  //7
        };
    }
}