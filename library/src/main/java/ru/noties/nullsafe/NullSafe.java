package ru.noties.nullsafe;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class NullSafe<T> {

    public interface Supplier<T, R> {

        @Nullable
        R supply(@NonNull T t);
    }

    @NonNull
    public static <T> NullSafe<T> create(@Nullable T t) {
        return new Impl<>(t);
    }

    @Nullable
    public abstract T get();

    @NonNull
    public abstract T get(@NonNull T def);

    @NonNull
    public abstract <R> NullSafe<R> map(@NonNull Supplier<T, R> supplier);

    @NonNull
    public abstract <R> NullSafe<R> map(@NonNull R def, @NonNull Supplier<T, R> supplier);


    private static class Impl<T> extends NullSafe<T> {

        private final T value;

        Impl(@Nullable T value) {
            this.value = value;
        }

        @Nullable
        @Override
        public T get() {
            return value;
        }

        @NonNull
        @Override
        public T get(@NonNull T def) {
            return value == null
                    ? def
                    : value;
        }

        @NonNull
        @Override
        public <R> NullSafe<R> map(@NonNull Supplier<T, R> supplier) {
            final NullSafe<R> nullSafe;
            if (value == null) {
                nullSafe = new Impl<>(null);
            } else {
                nullSafe = new Impl<>(supplier.supply(value));
            }
            return nullSafe;
        }

        @NonNull
        @Override
        public <R> NullSafe<R> map(@NonNull R def, @NonNull Supplier<T, R> supplier) {
            final NullSafe<R> nullSafe;
            if (value == null) {
                nullSafe = new Impl<>(def);
            } else {
                nullSafe = new Impl<>(supplier.supply(value));
            }
            return nullSafe;
        }
    }
}
