package com.fwd.salesInvoiceSystem.Model;

import java.lang.reflect.Array;

public class InvoiceItem {
    private int no;
    private int invoiceNo;
    private String itemName;
    private float itemPrice;
    private int itemCount;
    private float itemTotal;


    public InvoiceItem(int no, int invoiceNo ,String itemName, float itemPrice, int itemCount) {
        this.no = no;
        this.invoiceNo= invoiceNo;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemCount = itemCount;
    }
    public InvoiceItem(String[] invoiceItem) throws Exception{
        this(Integer.parseInt(invoiceItem[0]),Integer.parseInt(invoiceItem[1]), invoiceItem[2],Float.parseFloat(invoiceItem[3]),Integer.parseInt(invoiceItem[4]));
    }
    public String toString(){
        return no + "," +invoiceNo+","+ itemName + "," + itemPrice + ","+ itemCount;
    }
    public Object[] toArray(){
        Object[] obj = {no ,itemName, itemPrice, itemCount, itemTotal};
        return  obj;

    }

    public int getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(int invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public void setItemTotal() {
        itemTotal = itemCount * itemPrice;
    }
    public float getItemTotal(){
        return itemTotal;
    }


}
