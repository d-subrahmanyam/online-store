package io.subbu.estore.contracts;

public interface DtoI<T, E> {
    T createEntity();

    E createDtoFromEntity(T t);
}
