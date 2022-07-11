package com.fwd.salesInvoiceSystem.View;

import javax.swing.*;

public class NewInvoiceItemPanel extends javax.swing.JPanel {

    private JLabel invoiceItemNoLabel;
    private JFormattedTextField invoiceItemNoValue;

    /**
     * Creates new form NewInvoiceItemPanel
     */
    public NewInvoiceItemPanel() {
        initComponents();
    }


    private void initComponents() {

        invoiceNoLabel = new javax.swing.JLabel();
        itemNameValue = new javax.swing.JTextField();
        itemNameLabel = new javax.swing.JLabel();
        itemCountLabel = new javax.swing.JLabel();
        itemPriceLabel = new javax.swing.JLabel();
        invoiceNoValue = new javax.swing.JFormattedTextField();
        itemCountValue = new javax.swing.JFormattedTextField();
        itemPriceValue = new javax.swing.JFormattedTextField();
        invoiceItemNoLabel = new javax.swing.JLabel();
        invoiceItemNoValue = new javax.swing.JFormattedTextField();

        invoiceNoLabel.setText("Invoice Number:");


        itemNameLabel.setText("Item Name :");

        itemCountLabel.setText("Item Count  :");

        itemPriceLabel.setText("Item Price  :");

        invoiceNoValue.setEditable(false);
        invoiceNoValue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        invoiceNoValue.setEnabled(false);


        itemCountValue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));


        itemPriceValue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        invoiceItemNoLabel.setText("No :");

        invoiceItemNoValue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(itemCountLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(invoiceNoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(itemNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(itemPriceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(invoiceItemNoLabel))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(invoiceItemNoValue)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(itemNameValue)
                                                .addComponent(invoiceNoValue)
                                                .addComponent(itemCountValue)
                                                .addComponent(itemPriceValue, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(33, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(invoiceItemNoLabel)
                                        .addComponent(invoiceItemNoValue))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(invoiceNoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(invoiceNoValue))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(itemNameLabel)
                                        .addComponent(itemNameValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(itemCountLabel)
                                        .addComponent(itemCountValue))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(itemPriceLabel)
                                        .addComponent(itemPriceValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(29, 29, 29))
        );
    }

    public void setInvoiceNoValue(String invoiceNoValue) {
        this.invoiceNoValue.setText(invoiceNoValue);
    }

    public String getInvoiceNoValue() {
        return invoiceNoValue.getText();
    }

    public String getInvoiceItemNoValue() {
        return invoiceItemNoValue.getText();
    }


    public String getItemCountValue() {
        return itemCountValue.getText();
    }

    public String getItemNameValue() {
        return itemNameValue.getText();
    }

    public String getItemPriceValue() {
        return itemPriceValue.getText();
    }

    // Variables declaration - do not modify
    private javax.swing.JLabel invoiceNoLabel;
    private javax.swing.JFormattedTextField invoiceNoValue;
    private javax.swing.JLabel itemCountLabel;
    private javax.swing.JFormattedTextField itemCountValue;
    private javax.swing.JLabel itemNameLabel;
    private javax.swing.JTextField itemNameValue;
    private javax.swing.JLabel itemPriceLabel;
    private javax.swing.JFormattedTextField itemPriceValue;
    // End of variables declaration
}
