package com.ejo.ui.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//How does this class work?
// A removal queue is a list of lists, each with their own removal queue.
// When queued, the item is added to EVERY queued removal list for each original list
// When the cycle method is called at a safe location after a loop, it is removed from the original list
// then it is removed from the queue that is tied to that original list.
// This is set up with multiple queues to ensure we can remove one element from multiple lists safely in different parts
public class RemovalQueueManager<T> {

    private final List<ArrayList<T>> lists;
    private final List<ArrayList<T>> removalQueues;

    public RemovalQueueManager(ArrayList<T>... lists) {
        this.lists = Arrays.asList(lists);

        this.removalQueues = new ArrayList<>();
        for (int i = 0; i < lists.length; i++) {
            removalQueues.add(new ArrayList<>());
        }
    }

    public void queueRemoval(T obj) {
        for (ArrayList<T> removalQueue : removalQueues)
            removalQueue.add(obj);
    }

    public <I> void cycleQueuedItems(ArrayList<I> list) {
        int index = lists.indexOf(list);
        lists.get(index).removeAll(removalQueues.get(index));
        removalQueues.get(index).clear();
    }
}
