package com.comp.iitb.vialogue.dataStructures;

import java.util.Stack;

/**
 * Created by jeffrey on 8/3/17.
 */

public class LIFOSet<E> extends Stack<E> {
    private Stack backSequence;
    private Stack forwardSequence;

    public LIFOSet() {
        backSequence = new Stack();
            forwardSequence = new Stack();
    }

    public E push(E value) {
        super.push(value);
     backSequence.push("push");
        forwardSequence.clear();
        return value;
    }

    public E pop() {
        E value = super.pop();
        backSequence.push(value);
        backSequence.push("pop");
        forwardSequence.clear();
        return value;
    }

    public boolean canIRedo() {
        return !forwardSequence.isEmpty();
    }

    public boolean canIUndo() {
        return !backSequence.isEmpty();
    }

    public void undo() {
        if (!canIUndo()) {
            throw new IllegalStateException();
        }
        Object action = backSequence.pop();
        if (action.equals("push")) {
            E value = super.pop();
            forwardSequence.push(value);
            forwardSequence.push("push");
        } else {
            E value = (E) backSequence.pop();
            super.push(value);
            forwardSequence.push("pop");
        }
    }


    public void redo() {
        if (!canIRedo()) {
            throw new IllegalStateException();
        }
        Object action = forwardSequence.pop();
        if (action.equals("push")) {
            E value = (E) forwardSequence.pop();
            super.push(value);
            backSequence.push("push");
        } else {
            E value = super.pop();
            backSequence.push(value);
            backSequence.push("pop");
        }
    }
}
