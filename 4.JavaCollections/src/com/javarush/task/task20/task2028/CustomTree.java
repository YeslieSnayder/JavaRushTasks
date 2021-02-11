package com.javarush.task.task20.task2028;

import java.io.Serializable;
import java.util.*;

/* 
Построй дерево(1)
*/
public class CustomTree extends AbstractList<String> implements Cloneable, Serializable {

    Entry<String> root;
    private int size;

    public CustomTree() {
        this(String.valueOf(0));
    }

    public CustomTree(String elementName) {
        root = new Entry<>(elementName);
        size = 0;
    }

    public boolean add(String s) {
        if (s.isEmpty()) return false;

        Entry<String> current = new Entry<>(s);
        Entry<String> parent = null;
        try {
            parent = searchAvailableElement();
        } catch (UnsupportedOperationException e) {
            return false;
        }

        if (parent == null) {
            root = current;
            size++;
        } else {
            if (parent.addChild(current))
                size++;
            else
                return false;
        }

        return true;
    }

    public boolean remove(Object o) {
        if (!(o instanceof String))
            throw new UnsupportedOperationException();
        Entry<String> element = search((String) o);
        if (element.hasChild()) {
            removeHierarchyOfElement(element);
            size++;
        }
        element.parent.removeChild(element);
        size--;
        return true;
    }

    private Entry<String> removeHierarchyOfElement(Entry<String> element) {
        if (element == null) return null;

        while (element.hasChild()) {
            element.leftChild = removeHierarchyOfElement(element.leftChild);
            element.rightChild = removeHierarchyOfElement(element.rightChild);
        }
        size--;
        return null;
    }

    public String getParent(String s) {
        if (s.isEmpty()) return null;
        Entry<String> element = search(s);
        return element != null ? element.parent.elementName : null;
    }

    public Entry<String> search(String s) {
        if (root == null)
            throw new UnsupportedOperationException("Tree haven't elements for search");
        if (root.elementName.equals(s))
            return root;
        Entry<String> element = root;
        Queue<Entry> queue = new LinkedList<>();
        boolean lastChance = false;

        do {
            if (element.leftChild != null) {
                if (!element.leftChild.elementName.equals(s))
                    queue.add(element.leftChild);
                else
                    return element.leftChild;
            }
            if (element.rightChild != null) {
                if (!element.rightChild.elementName.equals(s))
                    queue.add(element.rightChild);
                else
                    return element.rightChild;
            }

            element = queue.poll();
            if (queue.isEmpty() && !lastChance) lastChance = true;
            else if (queue.isEmpty() && lastChance) break;
        } while (true);

        return null;
    }

    @Override
    public int size() {
        return size;
    }

    public String set(int index, String element) {
        throw new UnsupportedOperationException();
    }

    public void add(int index, String element) {
        throw new UnsupportedOperationException();
    }

    public String remove(int index) {
        throw new UnsupportedOperationException();
    }

    public List<String> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(int index, Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String get(int index) {
        throw new UnsupportedOperationException();
    }

    /**
     * Search element that available to add children.
     * @return element that available to add children.
     * @throws UnsupportedOperationException When an unexpected error occurs.
     */
    private Entry<String> searchAvailableElement() {
        if (root == null)
            return null;

        Entry<String> element = root;
        Queue<Entry> queue = new LinkedList<>();

        do {
            if (element != null) {
                if (!element.availableToAddLeftChildren)
                    queue.add(element.leftChild);
                else
                    return element;

                if (!element.availableToAddRightChildren)
                    queue.add(element.rightChild);
                else
                    return element;
            }

            element = queue.poll();
        } while (!queue.isEmpty());

        restartAvailabilityElements();
        return searchAvailableElement();
    }

    private void restartAvailabilityElements() {
        Entry<String> element = root;
        Queue<Entry> queue = new LinkedList<>();

        do {
            if (element.leftChild == null)
                element.availableToAddLeftChildren = true;
            else
                queue.add(element.leftChild);

            if (element.rightChild == null)
                element.availableToAddRightChildren = true;
            else
                queue.add(element.rightChild);

            element = queue.poll();
        } while (!queue.isEmpty());
    }

    static class Entry<T> implements Serializable {

        String elementName;
        boolean availableToAddLeftChildren, availableToAddRightChildren;
        Entry<T> parent, leftChild, rightChild;

        public Entry(String elementName) {
            this.elementName = elementName;
            availableToAddLeftChildren = true;
            availableToAddRightChildren = true;
        }

        public boolean isAvailableToAddChildren() {
            return availableToAddLeftChildren || availableToAddRightChildren;
        }

        public boolean hasChild() {
            return leftChild != null || rightChild != null;
        }

        public boolean addChild(Entry<T> child) {
            if (availableToAddLeftChildren) {
                addLeftChild(child);
                return true;
            } else if (availableToAddRightChildren) {
                addRightChild(child);
                return true;
            }
            return false;
        }

        public void removeChild(Entry<T> child) {
            if (leftChild == child)
                leftChild = null;
            else if (rightChild == child)
                rightChild = null;
        }

        private void addLeftChild(Entry<T> child) {
            leftChild = child;
            availableToAddLeftChildren = false;
            leftChild.setParent(this);
        }

        private void addRightChild(Entry<T> child) {
            rightChild = child;
            availableToAddRightChildren = false;
            rightChild.setParent(this);
        }

        public void setParent(Entry<T> parent) {
            this.parent = parent;
        }
    }
}
