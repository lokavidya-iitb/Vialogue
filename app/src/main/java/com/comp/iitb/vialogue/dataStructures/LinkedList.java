package com.comp.iitb.vialogue.dataStructures;

import java.util.ArrayList;

/**
 * Created by ironstein on 17/02/17.
 */


public class LinkedList<T> {

    private class Node {

        T mObject;
        int mPreviousIndex;
        int mNextIndex;

        public Node(T object, int previousIndex, int nextIndex) {
            mObject = object;
            mPreviousIndex = previousIndex;
            mNextIndex = nextIndex;
        }

        public T getObject() {
            return mObject;
        }

        public void setObject(T object) {
            mObject = object;
        }

        public int getPreviousIndex() {
            return mPreviousIndex;
        }

        public void setPreviousIndex(int previousIndex) {
            mPreviousIndex = previousIndex;
        }

        public int getNextIndex() {
            return mNextIndex;
        }

        public void setNextIndex(int nextIndex) {
            mNextIndex = nextIndex;
        }

    }

    private ArrayList<Node> mList;
    private Node mLastNode;

    public LinkedList() {
        mList = new ArrayList<Node>();
        // add the root node
        put(null);
    }

    public LinkedList(ArrayList<T> list) {
        this();
        for(int i=0; i<list.size(); i++) {
            put(list.get(i));
        }
    }

    public void put(T object) {
//        Node n = new Node(object, mLastNode)
//        Node n = new Node(object, mLargestIndex);
//        mList.add(n);
    }

    public Node getNode(int index) {
        // displace by 1 because of the root node
        index += 1;

        int nextIndex = mList.get(0).getNextIndex();
        int count = 0;
        while (count < index - 1) {
            // try {
            nextIndex = mList.get(nextIndex).getNextIndex();
            // } catch (java.lang.IndexOutOfBoundsException e) {
            // e.printStackTrace();
            // TODO throw new exception
            // }
            count += 1;
        }

        return mList.get(nextIndex);
    }

    public T get(int index) {
        return getNode(index).getObject();
    }

    public void remove(int index) {
        // displace by 1 because of the root node
        index += 1;

        Node n1 = mList.get(index - 1);
        Node n2 = mList.get(index);
        n1.setNextIndex(n2.getNextIndex());
        n2.setObject(null);
    }

    public void moveElement(int initialPosition, int finalPosition) {
        // displace by 1 because of the root node
        initialPosition += 1;
        finalPosition += 1;

        int max, min;
        Node maxNode = new Node(null, 0, 0);
        Node minNode = new Node(null, 0, 0);
        if(initialPosition < finalPosition) {
            max = finalPosition;
            min = initialPosition;
        } else if(initialPosition > finalPosition) {
            max = initialPosition;
            min = finalPosition;
        } else {
            // initial and final positions are the same, do nothing
            return;
        }

        int nextIndex = mList.get(0).getNextIndex();
        int count = 0;

        while (count < max - 1) {
            if(count == min - 2) {
//                beforeMinNode = mList.get(nextIndex);
            }
            if(count == min - 1) {
                minNode = mList.get(nextIndex);
            }
            if(count == min - 2)
                // try {
                nextIndex = mList.get(nextIndex).getNextIndex();
            // } catch (java.lang.IndexOutOfBoundsException e) {
            // e.printStackTrace();
            // TODO throw new exception
            // }
            count += 1;
        }

        maxNode = mList.get(nextIndex);

        Node initialNode, finalNode;
        if(initialPosition < finalPosition) {
            initialNode = minNode;
            finalNode = maxNode;

        } else {
            initialNode = maxNode;
            finalNode = minNode;
        }

        System.out.println(initialNode.getObject());
        System.out.println(finalNode.getObject());

        if (finalPosition < initialPosition) {
            mList.get(initialPosition - 1).setNextIndex(mList.get(initialPosition).getNextIndex());
            mList.get(finalPosition - 1).setNextIndex(initialPosition);
            mList.get(initialPosition).setNextIndex(finalPosition+1);
        } else {
            // initial and final positions are the same, do nothing
        }

    }

    public ArrayList<T> toList() {

        ArrayList<T> returnArray = new ArrayList<T>();
        int nextIndex = mList.get(0).getNextIndex();
        while (true) {
            Node n;
            try {
                n = mList.get(nextIndex);
            } catch (java.lang.IndexOutOfBoundsException e) {
                break;
            }
            returnArray.add(n.getObject());
            nextIndex = n.getNextIndex();
        }
        return returnArray;
    }

    public static void main(String[] args) {
        LinkedList l = new LinkedList<Integer>();
        for(int i=0; i<10; i++) {
            l.put(i+1);
        }
        // System.out.println(l.toList());
        // l.remove(7);
        // System.out.println(l.toList());
        // l.put(233);
        // System.out.println(l.toList());
        // l.moveElement(1, 0);
        // System.out.println(l.toList());
        System.out.println(l.get(2));
        System.out.println(l.toList());
        l.remove(3);
        System.out.println(l.toList());
        System.out.println(l.get(5));
        l.moveElement(0, 8);

    }
}
