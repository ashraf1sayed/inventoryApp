package com.example.ashra.inventory.data;

/**
 * Created by ashra on 2/12/2018.
 */
public class ListItems {
    private final String NameEdit;
    private final String priceEdit;
    private final int quantityEdit;
    private final String supplierNameEdit;
    private final String supplierPhoneEdit;
    private final String supplierEmailEdit;
    private final String imageView;

    public ListItems(String NameEdit, String priceEdit, int quantityEdit, String supplierNameEdit, String supplierPhoneEdit, String supplierEmailEdit, String imageView) {
        this.NameEdit = NameEdit;
        this.priceEdit = priceEdit;
        this.quantityEdit = quantityEdit;
        this.supplierNameEdit = supplierNameEdit;
        this.supplierPhoneEdit = supplierPhoneEdit;
        this.supplierEmailEdit = supplierEmailEdit;
        this.imageView = imageView;
    }

    public String getProductName() {
        return NameEdit;
    }

    public String getPrice() {
        return priceEdit;
    }

    public int getQuantity() {
        return quantityEdit;
    }

    public String getSupplierName() {
        return supplierNameEdit;
    }

    public String getSupplierPhone() {
        return supplierPhoneEdit;
    }

    public String getSupplierEmail() {
        return supplierEmailEdit;
    }

    public String getImage() {
        return imageView;
    }
    @Override
    public String toString() {
        return "StockItem{" +
                "productName='" + NameEdit + '\'' +
                ", price='" + priceEdit + '\'' +
                ", quantity=" + quantityEdit +
                ", supplierName='" + supplierNameEdit + '\'' +
                ", supplierPhone='" + supplierPhoneEdit + '\'' +
                ", supplierEmail='" + supplierEmailEdit + '\'' +
                '}';
    }
}
