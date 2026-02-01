package com.ejo.util.lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class QueueableArrayList<E> extends ArrayList<E> {

    private final ArrayList<E> additionQueue;
    private final ArrayList<E> removalQueue;

    public QueueableArrayList() {
        this.additionQueue = new ArrayList<>();
        this.removalQueue = new ArrayList<>();
    }

    public void forEachQueued(Action<E> runnable) {
        for (E e : this) runnable.run(e);
        cycleQueuedElements();
    }

    public void forIQueued(ActionI<E> runnable) {
        for (int i = 0; i < size(); i++) runnable.run(get(i),i);
        cycleQueuedElements();
    }


    public void queueAdd(E e) {
        additionQueue.add(e);
    }

    public void queueAddAll(Collection<E> collection) {
        additionQueue.addAll(collection);
    }

    public void queueAddAll(E... e) {
        additionQueue.addAll(Arrays.asList(e));
    }


    public void queueRemove(E e) {
        removalQueue.add(e);
    }

    public void queueRemovalAll(Collection<E> collection) {
        removalQueue.removeAll(collection);
    }

    public void queueRemovalAll(E... e) {
        removalQueue.removeAll(Arrays.asList(e));
    }


    public void cycleQueuedElements() {
        addAll(additionQueue);
        additionQueue.clear();

        removeAll(removalQueue);
        removalQueue.clear();
    }


    public boolean isRemovalQueued(E e) {
        return removalQueue.contains(e);
    }

    public boolean isAdditionQueued(E e) {
        return additionQueue.contains(e);
    }

    public boolean isQueued(E e) {
        return isAdditionQueued(e) && isRemovalQueued(e);
    }


    public ArrayList<E> getAdditionQueue() {
        return additionQueue;
    }

    public ArrayList<E> getRemovalQueue() {
        return removalQueue;
    }


    @FunctionalInterface
    public interface Action<E> {
        void run(E e);
    }

    @FunctionalInterface
    public interface ActionI<E> {
        void run(E e, int i);
    }
}
