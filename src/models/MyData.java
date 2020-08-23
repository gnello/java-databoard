package models;

import interfaces.Data;
import interfaces.User;

import java.util.ArrayList;
import java.util.List;

public class MyData implements Data, Cloneable {
    private final Integer id;

    private final ArrayList<User> likedByList;

    public MyData(Integer id) {
        this.id = id;

        this.likedByList = new ArrayList<>();
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
    public List<User> getLikes() throws CloneNotSupportedException {
        //TODO: specificare nella specifica che è deep copy?
        ArrayList<User> likesList = new ArrayList<>();

        for (User item : this.likedByList) {
            likesList.add(item.clone());
        }

        return likesList;
    }

    @Override
    public void insertLike(String friend) throws NullPointerException {
        if (friend == null) {
            throw new NullPointerException();
        }

        this.likedByList.add(new MyUser(friend));
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
