package com.tarabd.tara;

public class Inbox {
    private String OrderId;
    private String Shopname;
    private String CustomerName;
    private String CustomerNumber;
    private String CustomerAddress;
    private String ProductCode;
    private String ProductPrice;
    private String OrderDate;

    public String getOrderId() {
        return OrderId;
    }

    public String getShopname() {
        return Shopname;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public String getCustomerAddress() {
        return CustomerAddress;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public Inbox(String orderId, String shopname, String customerName, String customerNumber, String customerAddress, String productCode, String productPrice, String orderDate) {
        OrderId = orderId;
        Shopname = shopname;
        CustomerName = customerName;
        CustomerNumber = customerNumber;
        CustomerAddress = customerAddress;
        ProductCode = productCode;
        ProductPrice = productPrice;
        OrderDate = orderDate;
    }
}
