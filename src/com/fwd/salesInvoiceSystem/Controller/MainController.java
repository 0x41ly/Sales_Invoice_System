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
            NewInvoicePanel newInvoicePanel = new NewInvoicePanel();
            int x = JOptionPane.showConfirmDialog(this.mainFrame
                    , newInvoicePanel
                    , "Add New Invoice"
                    , JOptionPane.OK_CANCEL_OPTION
                    , JOptionPane.PLAIN_MESSAGE);
            if (x == 0) {
                Invoice invoice = new Invoice(Integer.parseInt(newInvoicePanel.getInvoiceNoValue())
                        , LocalDate.parse(newInvoicePanel.getInvoiceDateValue()
                        , DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        , newInvoicePanel.getCustNameValue());
                database.addInvoice(invoice);
                DefaultTableModel defaultTableModel = (DefaultTableModel) (this.mainFrame.getInvoicesTable().getModel());
                defaultTableModel.addRow(invoice.toArray());
            }
        });
        this.mainFrame.addInvoiceItem(e -> {
            int selectedRow = this.mainFrame.getInvoicesTable().getSelectedRow();
            if (selectedRow != -1) {
                int invoiceNo = (int) (this.mainFrame.getInvoicesTable().getValueAt(selectedRow, 0));
                NewInvoiceItemPanel invoiceItemPanel = new NewInvoiceItemPanel();
                invoiceItemPanel.setInvoiceNoValue(String.valueOf(invoiceNo));
                int x = JOptionPane.showConfirmDialog(this.mainFrame
                        , invoiceItemPanel
                        , "Add New Invoice"
                        , JOptionPane.OK_CANCEL_OPTION
                        , JOptionPane.PLAIN_MESSAGE);
                if (x == 0) {

                    InvoiceItem invoiceItem = new InvoiceItem(Integer.parseInt(invoiceItemPanel.getInvoiceItemNoValue()),invoiceNo
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
                    } else {
                        JOptionPane.showMessageDialog(this.mainFrame
                                , "Associated Invoice does not exist "
                                , "Associated Invoice is does not  exist",
                                JOptionPane.ERROR_MESSAGE);

                    }


                }
            }

        });
        this.mainFrame.deleteInvoice(e -> {
            int selectedRow = this.mainFrame.getInvoicesTable().getSelectedRow();
            if (selectedRow != -1) {
                int invoiceNo = (Integer) (this.mainFrame.getInvoicesTable().getValueAt(selectedRow, 0));
                database.deleteInvoice(invoiceNo);
                mainFrame.setInvoicesTableInfo(database.getInvoicesArrayList());
                mainFrame.resetInvoiceInfo();
            }
        });
        this.mainFrame.deleteInvoiceItem(e -> {
            int selectedRow = this.mainFrame.getInvoiceItemsTable().getSelectedRow();
            if (selectedRow != -1) {
                JTable invoiceItemsTable = this.mainFrame.getInvoiceItemsTable();
                int invoiceItemNo = (Integer) (invoiceItemsTable.getValueAt(selectedRow, 0));
                mainFrame.InvoiceInfo(database.deleteInvoiceItem(invoiceItemNo));
                mainFrame.setInvoicesTableInfo(database.getInvoicesArrayList());

            }
        });
        this.mainFrame.saveChanges(e -> {
            this.saveChanges();
        });
        this.mainFrame.saveMenuItem(e -> {
            this.saveChanges();
        });
        this.mainFrame.cancelChanges(e -> {
            if (invoiceItemsFile !=null){
                try {
                    this.database.setInvoiceItemsArrayList(this.database.loadFile(invoiceItemsFile));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
            else{
                try {
                    this.database.setInvoiceItemsArrayList(new Object[0]);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            this.mainFrame.resetInvoiceInfo();
            if (invoicesFile != null){
                try {
                    this.database.setInvoicesArrayList(this.database.loadFile(invoicesFile));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else{
                try {
                    this.database.setInvoicesArrayList(new Object[0]);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            this.mainFrame.setInvoicesTableInfo(this.database.getInvoicesArrayList());
        });
    }

    public void saveChanges(){
        try {
            if (invoicesFile != null) {

                this.database.saveFile(invoicesFile.getPath(), this.database.getInvoicesArrayList());
            }
            else{
                File file = fileSaveDialog("Specify File To Save Invoices");
                if (file !=null){
                    this.database.saveFile(file.getPath(), this.database.getInvoicesArrayList());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {

            if (invoiceItemsFile != null) {

                this.database.saveFile(invoiceItemsFile.getPath(), this.database.getInvoiceItemsArrayList());
            }
            else{
                File file = fileSaveDialog("Specify File To Save InvoiceItems");
                if (file !=null){
                    this.database.saveFile(file.getPath(), this.database.getInvoiceItemsArrayList());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public File fileSaveDialog(String title){
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

        if (filesChooser.showSaveDialog(this.mainFrame) == JFileChooser.APPROVE_OPTION ){
            File fileToSave = filesChooser.getSelectedFile();
            return  fileToSave;
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
