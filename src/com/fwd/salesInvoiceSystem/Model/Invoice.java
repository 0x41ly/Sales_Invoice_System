package com.fwd.salesInvoiceSystem.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Invoice {
    private int no;
    private LocalDate date;
    private String customerName;
    private float total;
    private ArrayList<InvoiceItem> invoiceItems;


    public Invoice(int no, LocalDate date, String customerName) {
        this.no = no;
        this.date = date;
        this.customerName = customerName;
        this.invoiceItems = new ArrayList<InvoiceItem>();
    }

    public Invoice(String[] invoice) throws Exception {
//
        this(Integer.parseInt(invoice[0]), LocalDate.parse(invoice[1], DateTimeFormatter.ofPattern("dd/MM/yyyy")), invoice[2]);

    }


    public int getNo() {
        return no;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setTotal() {
        float total = 0.0F;
        int i = 0;
        while (i < this.invoiceItems.size()) {
            total += this.invoiceItems.get(i).getItemTotal();
            i++;
        }
        this.total = total;
    }

    public float getTotal() {
        return total;
    }

    public String toString() {
        return no + "," + date.format( DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "," + customerName;
    }

    public Object[] toArray() {
        Object[] obj = {no, date.format( DateTimeFormatter.ofPattern("dd/MM/yyyy")), customerName, total};
        return obj;

    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public ArrayList<InvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(ArrayList<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
        this.setTotal();
    }

    public void addInvoiceItem(InvoiceItem invoiceItem) {
        invoiceItems.add(invoiceItem);
        this.setTotal();
    }
}
