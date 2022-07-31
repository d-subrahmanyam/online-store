package io.subbu.estore.contracts;

import java.util.List;

public interface ControllerI<T> {

    /**
     * This method retrieves all the
     * entities from the repo for the
     * given type T
     * @return List<T>
     */
    List<T> all();

    /**
     * This method retrieves a single
     * entity of type T for the given
     * uuid from the repo
     * @param uuid
     * @return T
     */
    T get(String uuid);

    /**
     * This method saves the given entity
     * of type T to the repo
     * @param t
     */
    void save(T t);

    /**
     * This method deletes the entity
     * of type T from the repo for the
     * given uuid
     * @param uuid
     */
    void delete(String uuid);
}
