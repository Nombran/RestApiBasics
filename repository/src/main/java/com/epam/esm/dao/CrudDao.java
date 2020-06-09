package com.epam.esm.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<T> {

    boolean create(T t);

    boolean update(T t);

    boolean delete(long id);

    Optional<T> find(long id);

    List<T> findAll();
}
