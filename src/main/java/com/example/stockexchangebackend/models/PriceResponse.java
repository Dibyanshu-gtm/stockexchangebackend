package com.example.stockexchangebackend.models;

public class PriceResponse {
    String label;
    float price;

    public PriceResponse(){

    }
    public PriceResponse(String label, float price) {
        this.label = label;
        this.price = price;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
