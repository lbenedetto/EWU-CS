package com.benedetto.lars.lab3;

import java.util.ArrayList;
import java.util.Collection;

public class Manufacturer {
    private String manufacturer;
    private ArrayList<String> models = new ArrayList<>();

    public Manufacturer(String name, ArrayList<String> models) {
        this.models = models;
        this.manufacturer = name;

    }
    public Manufacturer(String name) {
        this.manufacturer = name;
    }

    public void addModel(String modelName) {
        models.add(modelName);
    }

    public void addAllModels(Collection<String> modelNames) {
        for (String s : modelNames) {
            models.add(s);
        }
    }

    public ArrayList<String> getModels() {
        return models;
    }

    public String getModel(int index) {
        return models.get(index);
    }

    public void deleteModel(int index) {
        models.remove(index);
    }

    public int getSize() {
        return models.size();
    }

    public String getManufacturer() {
        return manufacturer;
    }
}