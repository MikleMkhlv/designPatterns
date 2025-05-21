package ru.informtb.repository;

import java.util.List;

public interface IReadStorage<T> {

    T get(String id);
    List<T> getAll();
}
