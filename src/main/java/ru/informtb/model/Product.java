package ru.informtb.model;

public class Product extends BaseElement {

    private String name;
    private boolean expirationDate;
    private int bulk;

    public Product(String id, String name, String expirationDate, String bulk) {
        super(id);
        this.name = name;
        this.expirationDate = Boolean.parseBoolean(expirationDate);
        this.bulk = (bulk != null) ? Integer.parseInt(bulk) : 0;
    }

    public Product(String name, String expirationDate, String bulk) {
        this.name = name;
        this.expirationDate = Boolean.parseBoolean(expirationDate);
        this.bulk = (bulk != null) ? Integer.parseInt(bulk) : 0;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(boolean expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getBulk() {
        return bulk;
    }

    public void setBulk(int bulk) {
        this.bulk = bulk;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + getId() + '\'' + " " +
                "name='" + name + '\'' +
                ", expirationDate=" + expirationDate +
                ", bulk=" + bulk +
                '}';
    }
}
