package com.ejo.util.setting;
/**
 * This class is a container class for a single value. This value can be changed and retrieved. This class is very
 * useful for using outside variables within lambda statements.
 */
public class Container<T> {

    private T value;

    public Container(T value) {
        this.value = value;
    }

    public Container<T> set(T value) {
        this.value = value;
        return this;
    }

    public T get() {
        return value;
    }


    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return "[Container: " + get() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Container<?> container)) return false;
        return container.get().equals(get());
    }
}
