package ro.meditrack.model;

import java.io.Serializable;

/**
 * Created by motan on 3/8/14.
 */
public class Medicament implements Serializable, ItemInterface {

    private int id;
    private String name;
    private String descirere;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Medicament(String name, String descirere) {
        this.name = name;
        this.descirere = descirere;
    }

    public Medicament() {
    }


    public String getItemDescription() {
        return name;
    }

    public String getDescirere() {
        return descirere;
    }

    public void setDescirere(String descirere) {
        this.descirere = descirere;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
