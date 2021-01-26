package ru.clevertec.check.entity.impl;

import ru.clevertec.check.annotations.LogMe;
import ru.clevertec.check.entity.Card;
import ru.clevertec.check.entity.Check;
import ru.clevertec.check.entity.Product;
import ru.clevertec.check.exception.ProductException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import ru.clevertec.check.parameters.ProductParameters;

import java.util.List;
import java.io.*;
import java.util.Date;
import java.util.Map;

public class CheckImpl implements Check {
    Map<String, Integer> map;
    private final String CHECKFILE = "D:\\check.txt";
    private final String CHECKFILEPDF = "D:\\check.pdf";
    private final String PDFTEMPLATE = "C:\\Users\\Stealth\\IdeaProjects\\NewCheck\\src\\main\\resources\\templates\\Clevertec_Template.pdf";
    private final String INDENT = "\n" + "\n" + "\n";
    private final int Y = 0;
    private final int X = 0;

    public CheckImpl(Map<String, Integer> map) {
        this.map = map;
    }
    @LogMe
    @Override
    public StringBuilder showCheck(List<ProductParameters> list){
        StringBuilder sb = new StringBuilder();
        Date date = new Date();
        Card card = new Card(0);
        sb.append("\n\n              CASH RECEIPT\n\n");
        sb.append("       supermarket 'The Two Geese' \n");
        sb.append("      " + date + "\n\n");
        sb.append("QTY  DESCRIPTION            PRICE   TOTAL \n");
        int key;
        int quantity;
        double totalPriceProduct;
        double totalPrice = 0;
        double discount = 0;
        String m;
        String c = "card";
        for (Map.Entry<String, Integer> entry : map.entrySet()
        ) {
            if (!entry.getKey().equals(c)) {
                for (int i=0; i<= list.size()-1; i++) {
                    key = Integer.parseInt(entry.getKey());
                    if (list.get(i).getItemId() == key) {
                        quantity = entry.getValue();
                        if (list.get(i).isStock() && quantity >= 5) {
                            totalPriceProduct = list.get(i).getCost() * quantity * 0.9;
                        } else {
                            totalPriceProduct = list.get(i).getCost() * quantity;
                        }
                        m = String.format("%-3d  %-18s %8.2f %8.2f \n", quantity,
                                list.get(i).getName(), list.get(i).getCost(), totalPriceProduct);
                        totalPrice += totalPriceProduct;
                        discount += list.get(i).getCost() * quantity - totalPriceProduct;
                        sb.append(m);
                    }
                }
            } else {
                card = new Card(entry.getValue());
            }
        }
        sb.append("\n========================================= \n");
        if (card.getNumber() == 0) {
            sb.append(" \nNo Discount Card ");
            sb.append(String.format("\nYour Discount   %25.2f                     ", discount));
            sb.append(String.format("\nTotal Price   %27.2f                     ", totalPrice));
        } else {

            if (card.getNumber() > 0 && card.getNumber() < 100) {
                sb.append(" \nYour Card with 3% Discount № " + card.getNumber());
                sb.append(String.format("\nYour Discount   %25.2f                     ", discount + totalPrice * 0.03));
                sb.append(String.format("\nTotal Price   %27.2f                     ", totalPrice * 0.97));
            }
            if (card.getNumber() > 99 && card.getNumber() < 1000) {
                sb.append(" \nYour Card with 4% Discount № " + card.getNumber());
                sb.append(String.format("\nYour Discount   %25.2f                     ", discount + totalPrice * 0.04));
                sb.append(String.format("\nTotal Price   %27.2f                     ", totalPrice * 0.96));
            }
            if (card.getNumber() > 999) {
                sb.append(" \nYour Card with 5% Discount № " + card.getNumber());
                sb.append(String.format("\nYour Discount   %25.2f                     ", discount + totalPrice * 0.05));
                sb.append(String.format("\nTotal Price   %27.2f                     ", totalPrice * 0.95));
            }
        }
        return sb;

    }

    @Override
    public StringBuilder htmlCheck(List<ProductParameters> list){
        StringBuilder sb = new StringBuilder();
        Date date = new Date();
        Card card = new Card(0);
        sb.append("<html>");
        sb.append("<head><h2><pre>        CASH RECEIPT</pre></h2></head>");
        sb.append("<pre>        supermarket 'The Two Geese'</pre> ");
        sb.append(String.format("<pre>       %s </pre>    ",date.toString()));
        sb.append("<h3><pre>QTY DESCRIPTION          PRICE   TOTAL </pre></h3>");
        int key;
        int quantity;
        double totalPriceProduct;
        double totalPrice = 0;
        double discount = 0;
        String m;
        String c = "card";
        for (Map.Entry<String, Integer> entry : map.entrySet()
        ) {
            if (!entry.getKey().equals(c)) {
                for (int i=0; i<= list.size()-1; i++) {
                    key = Integer.parseInt(entry.getKey());
                    if (list.get(i).getItemId() == key) {
                        quantity = entry.getValue();
                        if (list.get(i).isStock() && quantity >= 5) {
                            totalPriceProduct = list.get(i).getCost() * quantity * 0.9;
                        } else {
                            totalPriceProduct = list.get(i).getCost() * quantity;
                        }
                        m = String.format("<pre>%-3d  %-18s   %8.2f %8.2f </pre>", quantity,
                                list.get(i).getName(), list.get(i).getCost(), totalPriceProduct);
                        totalPrice += totalPriceProduct;
                        discount += list.get(i).getCost() * quantity - totalPriceProduct;
                        sb.append(m);
                    }
                }
            } else {
                card = new Card(entry.getValue());
            }
        }
        sb.append("===================================");
        if (card.getNumber() == 0) {
            sb.append("<pre>No Discount Card </pre>");
            sb.append(String.format("<pre>Your Discount     %25.2f</pre>", discount));
            sb.append(String.format("<h4><pre><h2>Total Price %17.2f</pre></h4>", totalPrice));
        } else {
            if (card.getNumber() > 0 && card.getNumber() < 100) {
                sb.append("<pre>Your Card with 3% Discount: </pre>" + card.getNumber());
                sb.append(String.format("<pre>Your Discount     %25.2f</pre>", discount + totalPrice * 0.03));
                sb.append(String.format("<h4><pre><h2>Total Price %17.2f</pre></h4>", totalPrice * 0.97));
            }
            if (card.getNumber() > 99 && card.getNumber() < 1000) {
                sb.append("<pre>Your Card with 4% Discount: </pre>" + card.getNumber());
                sb.append(String.format("<pre>Your Discount     %25.2f</pre>", discount + totalPrice * 0.04));
                sb.append(String.format("<h4><pre><h2>Total Price %17.2f</pre></h4>", totalPrice * 0.96));
            }
            if (card.getNumber() > 999) {
                sb.append("<pre>Your Card with 5% Discount: </pre>" + card.getNumber());
                sb.append(String.format("<pre>Your Discount     %25.2f</pre>", discount + totalPrice * 0.05));
                sb.append(String.format("<h4><pre><h2>Total Price %17.2f</pre></h4>", totalPrice * 0.95));
            }
        }sb.append("</html>");
        return sb;

    }

    @Override
    public void printCheck(StringBuilder sb) throws ProductException {
        File file = new File(CHECKFILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            throw new ProductException("Cant find filepath");
        }
    }

    @Override
    public void printPDFCheck(StringBuilder sb) throws ProductException {
        try {
            FileOutputStream file = new FileOutputStream(CHECKFILEPDF);
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, file);
            document.open();
            document.newPage();
            document.add(new Paragraph(INDENT));
            document.add(new Paragraph(sb.toString()));
            PdfReader reader = new PdfReader(new FileInputStream(PDFTEMPLATE));
            PdfImportedPage page = writer.getImportedPage(reader, 1);
            PdfContentByte pdfContentByte = writer.getDirectContentUnder();
            pdfContentByte.addTemplate(page, X, Y);
            document.close();
        } catch (DocumentException e) {
            throw new ProductException("Document problem");
        } catch (FileNotFoundException e) {
            throw new ProductException("Cant find file");
        } catch (IOException e) {
            throw new ProductException("IO problem");
        }
    }
    }
