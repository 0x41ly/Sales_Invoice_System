package com.fwd.salesInvoiceSystem.Model;
import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Database {
    private ArrayList<Invoice> invoicesArrayList;
    private ArrayList<InvoiceItem> invoiceItemsArrayList;

    public ArrayList<Invoice> getInvoicesArrayList() {
        return invoicesArrayList;
    }

    public void setInvoicesArrayList(Object[] objects) throws Exception {
        invoicesArrayList = new ArrayList<>();
        int i = 0;
        while(i < objects.length) {
            Invoice invoice =  new Invoice(objects[i].toString().split(","));
            if (invoicesArrayList.stream().allMatch(x-> x.getNo()!=invoice.getNo() )){
                invoice.setInvoiceItems((ArrayList<InvoiceItem>) (invoiceItemsArrayList.stream().filter(e -> e.getInvoiceNo() == invoice.getNo()).collect(Collectors.toList())));
                invoice.setTotal();
                invoicesArrayList.add(invoice);
            }

            i++;
        }
    }

    public ArrayList<InvoiceItem> getInvoiceItemsArrayList() {
        return invoiceItemsArrayList;

    }

    public void setInvoiceItemsArrayList(Object[] objects) throws Exception {
        invoiceItemsArrayList = new ArrayList<>();
        int i = 0;
        while(i < objects.length) {
            InvoiceItem invoiceItem = new InvoiceItem(objects[i].toString().split(","));
            if (invoiceItemsArrayList.stream().allMatch(x-> x.getNo()!=invoiceItem.getNo() )){
                invoiceItem.setItemTotal();
                invoiceItemsArrayList.add(invoiceItem);
            }
            i++;
        }
    }


    public Database() {
        invoicesArrayList = new ArrayList<>();
        invoiceItemsArrayList = new ArrayList<>();
    }
    public Database(ArrayList invoicesArrayList, ArrayList invoiceItemsArrayList) {
        this.invoicesArrayList = invoicesArrayList;
        this.invoiceItemsArrayList = invoiceItemsArrayList;
    }
    public void addInvoice(Invoice invoice){
        invoicesArrayList.add(invoice);
    }
    public void addInvoiceItem(InvoiceItem invoiceItem){
        invoiceItemsArrayList.add(invoiceItem);
    }
    public void deleteInvoice(int invoiceNo){
        this.getInvoicesArrayList().removeIf(x -> x.getNo() == invoiceNo);
        this.getInvoiceItemsArrayList().removeIf(t -> t.getNo() == invoiceNo);
    }
    public Invoice deleteInvoiceItem(int invoiceItemNo){
        InvoiceItem invoiceItem = this.getInvoiceItemsArrayList().stream().filter(l->l.getNo()==invoiceItemNo).toList().get(0);
        this.getInvoiceItemsArrayList().removeIf(x -> x.getNo() == invoiceItemNo);
        Invoice invoice = this.getInvoicesArrayList().stream().filter(xx -> xx.getNo() == invoiceItem.getInvoiceNo()).collect(Collectors.toList()).get(0);
        invoice.getInvoiceItems().removeIf(t -> t.getNo() == invoiceItemNo);
        invoice.setTotal();
        return invoice;

    }
    public Object[] loadFile(File file) throws IOException {

        Object[] objects;

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            // each lines to array
            objects = bufferedReader.lines().toArray();
            bufferedReader.close();
            return objects;

    }


    public void saveFile(String path, ArrayList arrayList) throws Exception{
            ArrayList<String> data = new ArrayList<>();
            int i = 0;
            while (i < arrayList.size()){
                data.add(arrayList.get(i).toString());
                i++;
            }
            String save_data = String.join("\n",data);
            BufferedWriter  bufferedWriter = new BufferedWriter(new FileWriter(path));
            bufferedWriter.write(save_data);
            bufferedWriter.close();
    }



}
