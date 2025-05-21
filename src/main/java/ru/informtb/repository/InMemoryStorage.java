package ru.informtb.repository;

import ru.informtb.model.Product;

import java.util.ArrayList;
import java.util.List;

public class InMemoryStorage implements IReadStorage<Product>, IWriteStorage<Product> {

    List<Product> storage;

    public InMemoryStorage() {
        this.storage = new ArrayList<>();
    }


    @Override
    public Product get(String id) {
        if (storage.isEmpty()) {
            System.out.println("Данных нет");
            return null;
        }
        Product item = null;
        for (Product element : storage) {
            if (element.getId().equals(id)) {
                item = element;
            }
        }
        return item;
    }

    @Override
    public List<Product> getAll() {
        return storage;
    }

    @Override
    public void add(Product item) {
        if (item != null) {
            storage.add(item);
        } else {
            System.out.println("Элемента нет");
        }
    }

    @Override
    public void update(Product item) {
        if (item == null) return;
        for (Product el : storage) {
            if (el.equals(item)) {
                if (!el.getName().equals(item.getName())) {
                    el.setName(item.getName());
                }
                if (el.isExpirationDate() != item.isExpirationDate()) {
                    el.setExpirationDate(item.isExpirationDate());
                }
                if (el.getBulk() != item.getBulk()) {
                    el.setBulk(item.getBulk());
                }
                break;
            }
        }

    }

    @Override
    public void delete(String id) {
        if (id == null || id.isEmpty()) {
            return;
        }
        for (Product el : storage) {
            if (el.getId().equals(id)) {
                storage.remove(el);
                break;
            }
        }
    }
}
