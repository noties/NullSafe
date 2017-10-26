package ru.noties.nullsafe;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NullSafeTest {

    @Test
    public void null_safe_never_null() {

        // all operations on `NullSafe` never return null

        assertNotNull(NullSafe.create(null));
        assertNotNull(NullSafe.create(null).map(t -> null));
        assertNotNull(NullSafe.create(null).map(t -> null).map(t -> null));
        assertNotNull(NullSafe.create(null).map(t -> null).map(t -> null).map(t -> null));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void supplier_never_called_with_null_argument() {

        final NullSafe.Supplier supplier = mock(NullSafe.Supplier.class);

        when(supplier.supply(any()))
                .thenThrow(new AssertionError());

        final Object o = NullSafe.create(null)
                .map(supplier)
                .map(supplier)
                .map(supplier)
                .map(supplier)
                .get();

        assertNull(o);
    }

    @Test
    public void no_default_returns_null() {
        assertNull(NullSafe.create(null).get());
    }

    @Test
    public void with_default_returns_default_value() {
        final Object o = new Object();
        assertEquals(o, NullSafe.create(null).get(o));
    }

    @Test
    public void normal_flow() {

        final Object o = new Object();

        final int expected = o.toString().length();

        final Integer actual = NullSafe.create(o)
                .map(Object::toString)
                .map(String::length)
                .get();

        //noinspection ConstantConditions
        assertEquals(expected, actual.intValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void def_value_for_map() {

        final NullSafe.Supplier supplier = mock(NullSafe.Supplier.class);

        when(supplier.supply(any()))
                .thenThrow(new AssertionError());

        final String def = "::default";

        final String s = (String) NullSafe.create(null)
                .map(def, supplier)
                .get();

        assertEquals(def, s);
    }

    @Test
    public void nested_def_for_map() {

        final Boolean bool = NullSafe.create((Boolean) null)
                .map(b -> null)
                .map(b -> null)
                .map(b -> null)
                .map(b -> null)
                .map(Boolean.TRUE, b -> null)
                .get();

        assertEquals(Boolean.TRUE, bool);
    }

    @Test
    public void split_flow() {

        final NullSafe<String> nullSafe = NullSafe.create(Boolean.TRUE)
                .map(Object::toString);

        final Integer length = nullSafe
                .map(String::length)
                .get();

        final Boolean bool = nullSafe
                .map(Boolean::valueOf)
                .get();

        //noinspection ConstantConditions
        assertEquals(4, (int) length);

        assertEquals(Boolean.TRUE, bool);
    }
}
