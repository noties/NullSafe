# NullSafe

Null safe flow for Java.

Java8:

```java
// `country` can be null, meaning that no mapping will occur and "Unknown" will be returned
final String name = NullSafe.create(country)
        .map(Country::city) // map function will never call Supplier with null argument
        .map(City::street)
        .map(Street::name)
        .get("Unknown");
```

```java

final City capitalCity = capitalCity();

final String name = NullSafe.create(country)
        .map(capitalCity, Country::city) // allows to specify def values (cannot be null)
        .map(City::street)
        .map(Street::name)
        .get("Unknown");
```


```java

// NullSafe instance can be used to split the flow (safe to reference intermediate state)

final NullSafe<City> nullSafe = NullSafe.create(country)
        .map(capitalCity, Country::city);

final String name = nullSafe
        .map(City::street)
        .map(Street::name)
        .get(); // <- returns nullable if no def value was specified

final House house = nullSafe
        .map(City::street)
        .map(Street::house)
        .get();
```

This utility class is **not** thread-safe