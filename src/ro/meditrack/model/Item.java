package ro.meditrack.model;

/**
 * Created by motan on 2/23/14.
 */
public class Item {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Item(String name) {

        this.name = name;
    }
}