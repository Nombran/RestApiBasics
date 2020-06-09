package com.epam.esm;

import java.util.List;
import java.util.Optional;

public interface CrudService<T> {
    boolean create(T t);

    boolean update(T t);

    boolean delete(long id);

    Optional<T> find(long id);

    List<T> findAll();
}
