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
public class ElementRemovalManager<T> {

    //TODO: Goal is to find a way to pair the lists with an index in a map kinda double list.
    // Have when checked, use the list instead of the index to prevent confusion (like the bottom methods) but still have it
    // check an index rather than call .contains()
    private final List<ArrayList<T>> lists;
    private final List<ArrayList<T>> removalQueues;

    @SafeVarargs
    public ElementRemovalManager(ArrayList<T>... lists) {
        this.lists = Arrays.asList(lists);

        this.removalQueues = new ArrayList<>();
        for (int i = 0; i < lists.length; i++) {
            removalQueues.add(new ArrayList<>());
        }
    }

    public void queueElement(T obj) {
        for (ArrayList<T> removalQueue : removalQueues)
            removalQueue.add(obj);
    }

    public void cycleQueuedElements(int listIndex) {
        lists.get(listIndex).removeAll(removalQueues.get(listIndex));
        removalQueues.get(listIndex).clear();
    }

    public boolean isQueued(int listIndex, T obj) {
        return removalQueues.get(listIndex).contains(obj);
    }

    public ArrayList<T> getRemovalQueue(int listIndex) {
        return removalQueues.get(listIndex);
    }



    //These might and DO cause concurrent modification exceptions. Avoid using them. It defeats the whole purpose
    public <I> void cycleQueuedElements(ArrayList<I> list) {
        cycleQueuedElements(lists.indexOf(list));
    }

    public <I> ArrayList<T> getRemovalQueue(ArrayList<I> list) {
        return getRemovalQueue(lists.indexOf(list));
    }

    public <I> boolean isQueued(ArrayList<I> list, T obj) {
        return removalQueues.get(lists.indexOf(list)).contains(obj);
    }
}
