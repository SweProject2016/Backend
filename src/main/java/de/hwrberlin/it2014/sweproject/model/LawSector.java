package de.hwrberlin.it2014.sweproject.model;

public class LawSector {
    private String name;

    public LawSector(final String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setName(final String name){
        this.name=name;
    }
}
