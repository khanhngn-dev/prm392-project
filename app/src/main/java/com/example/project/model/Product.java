package com.example.project.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import utils.https.types.response.ProductDescriptionResponse;

public class Product implements Serializable {
    private int id;
    private String name;
    private String description;
    private int price;
    private int stock;
    private String imageUrl;
    private String type = "Laptop";
    private Double rate = 4.9;

    public Product() {
    }


    public Product(int id, String name, String description, int price, int stock, String imageUrl, String type, Double rate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.type = type;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        Map<String, String> map = new HashMap<>();
        String[] pairs = new Gson().fromJson(description, String[].class);
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            map.put(keyValue[0], keyValue[1]);
        }
        String json = new Gson().toJson(map);
        ProductDescriptionResponse productDescriptionResponse = new Gson().fromJson(json, ProductDescriptionResponse.class);
        ProductDescription productDescription = productDescriptionResponse.toModel();

        return productDescription.getCpu() + " - " + productDescription.getRam();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
