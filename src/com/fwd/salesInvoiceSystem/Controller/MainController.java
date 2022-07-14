package com.fwd.salesInvoiceSystem.Controller;

import com.fwd.salesInvoiceSystem.Model.Database;
import com.fwd.salesInvoiceSystem.Model.Invoice;
import com.fwd.salesInvoiceSystem.Model.InvoiceItem;
import com.fwd.salesInvoiceSystem.View.MainFrame;
import com.fwd.salesInvoiceSystem.View.NewInvoiceItemPanel;
import com.fwd.salesInvoiceSystem.View.NewInvoicePanel;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import java.io.File;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.exit;
import static java.lang.System.in;

public class MainController {
    private File invoicesFile;
    private File invoiceItemsFile;
    private Database database;
    private MainFrame mainFrame;

    public MainController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.database = new Database();
        this.mainFrame.exitMenuItem(e -> {
                    exit(0);
                }
        );


        this.mainFrame.loadMenuItem(e -> {
            Object[] invoicesItems = new Object[0];
            Object[] invoices = new Object[0];
            try {
                invoicesFile = fileChooser("Specify Invoices File ");
                if (invoicesFile != null) {
                    invoiceItemsFile = fileChooser("Specify InvoiceItems File");
                    if (invoiceItemsFile != null) {


                        try {
                            invoicesItems = database.loadFile(invoiceItemsFile);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(this.mainFrame
                                    , "File Not Found"
                                    , "File Not Found",
                                    JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }


                        try {
                            database.setInvoiceItemsArrayList(invoicesItems);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this.mainFrame
                                    , "Invoice Items file is corrupted.\n Reload it again."
                                    , "Corrupted File",
                                    JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }


                    }

                    try {

                        invoices = database.loadFile(invoicesFile);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this.mainFrame
                                , "File Not Found"
                                , "File Not Found",
                                JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                    try {
                        database.setInvoicesArrayList(invoices);
                    } catch (DateTimeParseException ex) {
                        JOptionPane.showMessageDialog(this.mainFrame
                                , "Wrong date format found "
                                , "Date Parse Error",
                                JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this.mainFrame
                                , "Invoices file is corrupted.\n Reload it again."
                                , "Corrupted File",
                                JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this.mainFrame
                        , "Only CSV files are allowed"
                        , "File Format Error.",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

            this.mainFrame.setInvoicesTableInfo(database.getInvoicesArrayList());
        });

        this.mainFrame.selectRowFromInvoicesTable(e -> {
            int selectedRow = this.mainFrame.getInvoicesTable().getSelectedRow();
            if (selectedRow != -1) {
                int invoiceNo = (Integer) (this.mainFrame.getInvoicesTable().getValueAt(selectedRow, 0));
                Invoice invoice = this.database.getInvoicesArrayList().stream()
                        .filter(x -> x.getNo() == invoiceNo).collect(Collectors.toList()).get(0);
                this.mainFrame.InvoiceInfo(invoice);
            }

        });
        this.mainFrame.createInvoice(e -> {
            int invoiceNo =0;
            try {
                invoiceNo = database.getInvoicesArrayList().get(database.getInvoicesArrayList().size() - 1).getNo() + 1;
            }catch (Exception exception){}
            NewInvoicePanel newInvoicePanel = new NewInvoicePanel();
            newInvoicePanel.setInvoiceNoValue(String.valueOf(invoiceNo));
            int x = JOptionPane.showConfirmDialog(this.mainFrame
                    , newInvoicePanel
                    , "Add New Invoice"
                    , JOptionPane.OK_CANCEL_OPTION
                    , JOptionPane.PLAIN_MESSAGE);
            if (x == 0) {
                try {
                    Invoice invoice = new Invoice(Integer.parseInt(newInvoicePanel.getInvoiceNoValue())
                            , LocalDate.parse(newInvoicePanel.getInvoiceDateValue()
                            , DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            , newInvoicePanel.getCustNameValue());
                    database.addInvoice(invoice);
                    DefaultTableModel defaultTableModel = (DefaultTableModel) (this.mainFrame.getInvoicesTable().getModel());
                    defaultTableModel.addRow(invoice.toArray());
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(this.mainFrame
                            , "You have to fill all input fields"
                            , "Insertion Error",
                            JOptionPane.ERROR_MESSAGE);
                    exception.printStackTrace();
                }
            }
        });
        this.mainFrame.addInvoiceItem(e -> {
            int selectedRow = this.mainFrame.getInvoicesTable().getSelectedRow();
            if (selectedRow != -1) {
                int invoiceNo = (int) (this.mainFrame.getInvoicesTable().getValueAt(selectedRow, 0));
                int invoiceItemNo = 0;
                try {
                    invoiceItemNo = database.getInvoiceItemsArrayList().get(
                            database.getInvoiceItemsArrayList().size() - 1).getNo() + 1;
                }catch (Exception exception){}
                NewInvoiceItemPanel invoiceItemPanel = new NewInvoiceItemPanel();
                invoiceItemPanel.setInvoiceItemNoValue(String.valueOf(invoiceItemNo));
                invoiceItemPanel.setInvoiceNoValue(String.valueOf(invoiceNo));
                int x = JOptionPane.showConfirmDialog(this.mainFrame
                        , invoiceItemPanel
                        , "Add New Invoice"
                        , JOptionPane.OK_CANCEL_OPTION
                        , JOptionPane.PLAIN_MESSAGE);
                if (x == 0) {

                    try {
                        InvoiceItem invoiceItem = new InvoiceItem(invoiceItemNo , invoiceNo
                                , invoiceItemPanel.getItemNameValue()
                                , Float.parseFloat(invoiceItemPanel.getItemPriceValue())
                                , Integer.parseInt(invoiceItemPanel.getItemCountValue()));
                        invoiceItem.setItemTotal();
                        List<Invoice> invoices = this.database.getInvoicesArrayList().stream()
                                .filter(xx -> xx.getNo() == invoiceNo).collect(Collectors.toList());
                        if (invoices.size() != 0) {
                            Invoice invoice = invoices.get(0);
                            invoice.addInvoiceItem(invoiceItem);

                            database.addInvoiceItem(invoiceItem);
                            mainFrame.InvoiceInfo(invoice);
                            mainFrame.setInvoicesTableInfo(database.getInvoicesArrayList());
                        }
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(this.mainFrame
                                , "You have to fill all input fields"
                                , "Insertion Error",
                                JOptionPane.ERROR_MESSAGE);
                        exception.printStackTrace();
                    }
                }


            }


        });
        this.mainFrame.deleteInvoice(e ->

        {
            int selectedRow = this.mainFrame.getInvoicesTable().getSelectedRow();
            if (selectedRow != -1) {
                int invoiceNo = (Integer) (this.mainFrame.getInvoicesTable().getValueAt(selectedRow, 0));
                database.deleteInvoice(invoiceNo);
                mainFrame.setInvoicesTableInfo(database.getInvoicesArrayList());
                mainFrame.resetInvoiceInfo();
            }
        });
        this.mainFrame.deleteInvoiceItem(e ->

        {
            int selectedRow = this.mainFrame.getInvoiceItemsTable().getSelectedRow();
            if (selectedRow != -1) {
                JTable invoiceItemsTable = this.mainFrame.getInvoiceItemsTable();
                int invoiceItemNo = (Integer) (invoiceItemsTable.getValueAt(selectedRow, 0));
                mainFrame.InvoiceInfo(database.deleteInvoiceItem(invoiceItemNo));
                mainFrame.setInvoicesTableInfo(database.getInvoicesArrayList());

            }
        });
        this.mainFrame.saveChanges(e ->

        {
            this.saveChanges();
        });
        this.mainFrame.saveMenuItem(e ->

        {
            this.saveChanges();
        });
        this.mainFrame.cancelChanges(e ->

        {
            if (invoiceItemsFile != null) {
                try {
                    this.database.setInvoiceItemsArrayList(this.database.loadFile(invoiceItemsFile));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } else {
                try {
                    this.database.setInvoiceItemsArrayList(new Object[0]);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            this.mainFrame.resetInvoiceInfo();
            if (invoicesFile != null) {
                try {
                    this.database.setInvoicesArrayList(this.database.loadFile(invoicesFile));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                    this.database.setInvoicesArrayList(new Object[0]);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            this.mainFrame.setInvoicesTableInfo(this.database.getInvoicesArrayList());
        });
        this.mainFrame.editInvoicesTableCell(e -> {
            JTable invoicesTable = mainFrame.getInvoicesTable();
            if(invoicesTable.isEditing()){
                if (invoicesTable.getSelectedRow() != -1){
                   Invoice invoice = database.getInvoicesArrayList()
                           .stream()
                           .filter(i ->
                                   i.getNo() ==
                                   (Integer) (invoicesTable.getValueAt(invoicesTable.getSelectedRow(),0))
                           ).collect(Collectors.toList()).get(0);
                    try{
                        LocalDate date = LocalDate.parse(
                                (String)(invoicesTable.getValueAt(invoicesTable.getSelectedRow(),1))
                                , DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        invoice.setDate(date);
                    }catch (Exception exception){
                        JOptionPane.showMessageDialog(this.mainFrame
                                , "Date Format should be dd/MM/yyyy"
                                , "Date format Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    String name = (String)(invoicesTable.getValueAt(invoicesTable.getSelectedRow(),2));
                    invoice.setCustomerName(name);
                    invoicesTable.clearSelection();
                    mainFrame.setInvoicesTableInfo(database.getInvoicesArrayList());
                    mainFrame.InvoiceInfo(invoice);
                }
            }
        });
        this.mainFrame.editInvoiceItemsTableCell(e -> {
            JTable invoiceItemsTable = mainFrame.getInvoiceItemsTable();
            if(invoiceItemsTable.isEditing()){
                if (invoiceItemsTable.getSelectedRow() != -1){
                    InvoiceItem invoiceitem = database.getInvoiceItemsArrayList()
                            .stream()
                            .filter(i ->
                                    i.getNo() ==
                                            (Integer) (invoiceItemsTable.getValueAt(invoiceItemsTable.getSelectedRow(),0))
                            ).collect(Collectors.toList()).get(0);
                    Invoice invoice = database.getInvoicesArrayList()
                            .stream()
                            .filter(in ->
                                    in.getNo() == invoiceitem.getInvoiceNo()
                            ).collect(Collectors.toList()).get(0);
                    int index = invoice.getInvoiceItems().indexOf(invoiceitem);
                    try{
                        int count =  Integer.parseInt((invoiceItemsTable.getValueAt(invoiceItemsTable.getSelectedRow(),3)).toString());
                        invoiceitem.setItemCount(count);

                    }catch (Exception exception){
                        JOptionPane.showMessageDialog(this.mainFrame
                                , "Count field should be INT"
                                , "Format Error",
                                JOptionPane.ERROR_MESSAGE);

                    }
                    try{
                        Float price = Float.parseFloat((invoiceItemsTable.getValueAt(invoiceItemsTable.getSelectedRow(),2)).toString());
                        invoiceitem.setItemPrice(price);
                    }catch (Exception exception){
                        JOptionPane.showMessageDialog(this.mainFrame
                                , "Price field should be A number"
                                , "Format Error",
                                JOptionPane.ERROR_MESSAGE);

                    }
                    String name = (String)(invoiceItemsTable.getValueAt(invoiceItemsTable.getSelectedRow(),1));
                    invoiceitem.setItemName(name);
                    invoice.getInvoiceItems().set(index,invoiceitem);
                    invoice.setTotal();
                    invoiceItemsTable.clearSelection();
                    mainFrame.InvoiceInfo(invoice);

                }


            }
        });
    }

    public void saveChanges() {
        try {
            if (invoicesFile != null) {

                this.database.saveFile(invoicesFile.getPath(), this.database.getInvoicesArrayList());
            } else {
                invoicesFile = fileSaveDialog("Specify File To Save Invoices");
                if (invoicesFile != null) {
                    this.database.saveFile(invoicesFile.getPath(), this.database.getInvoicesArrayList());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {

            if (invoiceItemsFile != null) {

                this.database.saveFile(invoiceItemsFile.getPath(), this.database.getInvoiceItemsArrayList());
            } else {
                invoiceItemsFile = fileSaveDialog("Specify File To Save InvoiceItems");
                if (invoiceItemsFile != null) {
                    this.database.saveFile(invoiceItemsFile.getPath(), this.database.getInvoiceItemsArrayList());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public File fileSaveDialog(String title) {
        JFileChooser filesChooser = new JFileChooser();
        filesChooser.setDialogTitle(title);
        filesChooser.setFileFilter(new FileFilter() {

            public String getDescription() {
                return "CSV files (*.csv)";
            }

            public boolean accept(File f) {

                String filename = f.getName().toLowerCase();
                return filename.endsWith(".csv");

            }
        });
        filesChooser.setAcceptAllFileFilterUsed(false);
        filesChooser.setCurrentDirectory(new java.io.File(
                "src\\com\\fwd\\salesInvoiceSystem\\data"));
        filesChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (filesChooser.showSaveDialog(this.mainFrame) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = filesChooser.getSelectedFile();
            return fileToSave;
        }
        return null;
    }

    public File fileChooser(String title) throws Exception {
        File file = null;
        JFileChooser filesChooser = new JFileChooser();
        filesChooser.setDialogTitle(title);
        filesChooser.setFileFilter(new FileFilter() {

            public String getDescription() {
                return "CSV files (*.csv)";
            }

            public boolean accept(File f) {

                String filename = f.getName().toLowerCase();
                return filename.endsWith(".csv");

            }
        });
        filesChooser.setAcceptAllFileFilterUsed(false);
        filesChooser.setCurrentDirectory(new java.io.File(
                "src\\com\\fwd\\salesInvoiceSystem\\data"));
        filesChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (filesChooser.showOpenDialog(this.mainFrame) == JFileChooser.OPEN_DIALOG) {
            file = filesChooser.getSelectedFile();
        }
        if (file == null) {
            return null;
        }
        if (!file.getName().endsWith(".csv")) {
            throw new Exception();
        }
        return file;
    }

}
