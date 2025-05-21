package ru.informtb.repository;

public interface IWriteStorage<T> {

    void add(T item);
    void update(T item);
    void delete(String id);
}
