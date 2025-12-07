package com.ejo.util.event;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Event {

    private final ArrayList<EventAction> eventActions = new ArrayList<>();

    //What are the points of the queues? So we can remove/subscribe an action to the action list from an
    // active action to prevent a concurrent modification exception
    private final ArrayList<EventAction> additionQueue = new ArrayList<>();
    private final ArrayList<EventAction> removalQueue = new ArrayList<>();

    private Object[] args = new Object[]{};

    public void post(Object... args) {
        this.args = args;

        try {
            updateSubscribeQueue();
        } catch (ConcurrentModificationException e) {
            System.out.println("Error Queueing Event Action Subscription Change");
        }

        ArrayList<EventAction> actionsCopy = (ArrayList<EventAction>) getActions().clone();
        for (EventAction event : actionsCopy) event.run();

    }

    private void updateSubscribeQueue() {
        removalQueue.forEach((action) -> getActions().remove(action));
        removalQueue.clear();
        additionQueue.forEach((action) -> getActions().add(action));
        additionQueue.clear();
    }

    public boolean subscribeAction(EventAction action) {
        if((getActions().contains(action))) return false; //Don't subscribe the same action multiple times
        action.subscribed = true;
        return additionQueue.add(action);
    }


    public boolean unsubscribeAction(EventAction action) {
        action.subscribed = false;
        return removalQueue.add(action);
    }

    public boolean unsubscribeAllActions() {
        for (EventAction action : getActions()) unsubscribeAction(action);
        return true;
    }


    public Object[] getArgs() {
        return args;
    }

    public ArrayList<EventAction> getActions() {
        return eventActions;
    }

}
