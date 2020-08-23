package models;

import interfaces.Data;

public class MyData implements Data, Cloneable {
    private final Integer id;

    public MyData(Integer id) {
        this.id = id;
    }

    @Override
    public void display() {

    }

    @Override
    public Integer getId() {
        return this.id;
    }

    public MyData clone() throws CloneNotSupportedException {
        return (MyData) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MyData data = (MyData) o;
        return this.getId().equals(data.getId());
    }

    @Override
    /*
     * Sovrascrivi per non violare il general contract di Object.hashCode(),
     */
    public int hashCode() {
        return this.id;
    }
}
