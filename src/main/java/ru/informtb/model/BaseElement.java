package ru.informtb.model;

import java.util.UUID;

public abstract class BaseElement {
    private final String id;

    public BaseElement() {
        this.id = UUID.randomUUID().toString();
    }

    public BaseElement(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }


}
