package ru.java.fun.util;

import java.util.function.Supplier;

public class Lazy<T> {

    private volatile T value;
    private final Object lock = new Object();
    private final Supplier<T> supplier;

    public Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        if (value == null) {
            synchronized (lock) {
                if (value == null) {
                    value = supplier.get();
                }
            }
        }
        return value;
    }

}
