package ru.clevertec.check.service.impl;

import ru.clevertec.check.annotations.log.LogMe;
import ru.clevertec.check.dao.DBController;
import ru.clevertec.check.dao.Repository;
import ru.clevertec.check.entities.Card;
import ru.clevertec.check.observer.Publisher;
import ru.clevertec.check.observer.listeners.Listener;
import ru.clevertec.check.service.Check;
import ru.clevertec.check.exception.ProductException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import ru.clevertec.check.parameters.ProductParameters;

import javax.mail.MessagingException;
import java.util.List;
import java.io.*;
import java.util.Date;
import java.util.Map;

import static ru.clevertec.check.observer.entity.State.*;
import static ru.clevertec.check.service.CheckConstants.*;

public class CheckImpl implements Check {

    private Publisher publisher = new Publisher(CHECK_WAS_PRINTED_IN_PDF, CHECK_WAS_PRINTED_IN_TXT, CHECK_HAS_NOT_PRINTED);
    private Map<String, Integer> map;
    private DBController database = new DBController();
    private Repository repository = Repository.getInstance(database);

    public CheckImpl(Map<String, Integer> map) {
        this.map = map;
    }

    @LogMe
    @Override
    public StringBuilder showCheck(List<ProductParameters> list) {
        StringBuilder sb = new StringBuilder();
        Date date = new Date();
        Card card = new Card(0);
        sb.append("\n\n              CASH RECEIPT\n\n").
                append("       supermarket 'The Two Geese' \n").
                append("      " + date + "\n\n").
                append("QTY  DESCRIPTION            PRICE   TOTAL \n");
        int key;
        int quantity;
        double totalPriceProduct;
        double totalPrice = 0;
        double discount = 0;
        String line;
        int repositorySize = repository.getSize();
        for (Map.Entry<String, Integer> entry : map.entrySet()
        ) {
            if (!entry.getKey().equals(CARD)) {
                for (int i = 0; i <= list.size() - 1; i++) {
                    key = Integer.parseInt(entry.getKey());
                    if (list.get(i).getItemId() == key) {
                        quantity = entry.getValue();
                        if (list.get(i).isStock() && quantity >= repositorySize) {
                            totalPriceProduct = list.get(i).getCost() * quantity * PERCENT90;
                        } else {
                            totalPriceProduct = list.get(i).getCost() * quantity;
                        }
                        line = String.format("%-3d  %-18s %8.2f %8.2f \n", quantity,
                                list.get(i).getName(), list.get(i).getCost(), totalPriceProduct);
                        totalPrice += totalPriceProduct;
                        discount += list.get(i).getCost() * quantity - totalPriceProduct;
                        sb.append(line);
                    }
                }
            } else {
                card = new Card(entry.getValue());
            }
        }
        sb.append(TRANFER);
        if (card.getNumber() == 0) {
            sb.append(" \nNo Discount Card ");
            sb.append(String.format("\nYour Discount   %25.2f", discount));
            sb.append(String.format("\nTotal Price   %27.2f", totalPrice));
        } else {

            if (card.getNumber() > 0 && card.getNumber() < 100) {
                sb.append(" \nYour Card with 3% Discount № " + card.getNumber());
                sb.append(String.format("\nYour Discount   %25.2f", discount + totalPrice * PERCENT3));
                sb.append(String.format("\nTotal Price   %27.2f", totalPrice * PERCENT97));
            }
            if (card.getNumber() > 99 && card.getNumber() < 1000) {
                sb.append(" \nYour Card with 4% Discount № " + card.getNumber());
                sb.append(String.format("\nYour Discount   %25.2f", discount + totalPrice * PERCENT4));
                sb.append(String.format("\nTotal Price   %27.2f", totalPrice * PERCENT96));
            }
            if (card.getNumber() > 999) {
                sb.append(" \nYour Card with 5% Discount № " + card.getNumber());
                sb.append(String.format("\nYour Discount   %25.2f", discount + totalPrice * PERCENT5));
                sb.append(String.format("\nTotal Price   %27.2f", totalPrice * PERCENT95));
            }
        }
        return sb;

    }

    @Override
    public StringBuilder htmlCheck(List<ProductParameters> list) {
        StringBuilder sb = new StringBuilder();
        Date date = new Date();
        Card card = new Card(0);
        sb.append("<html>").
                append("<head><h2><pre>        CASH RECEIPT</pre></h2></head>").
                append("<pre>        supermarket 'The Two Geese'</pre> ").
                append(String.format("<pre>       %s </pre>    ", date.toString())).
                append("<h3><pre>QTY DESCRIPTION          PRICE   TOTAL </pre></h3>");
        int key;
        int quantity;
        double totalPriceProduct;
        double totalPrice = 0;
        double discount = 0;
        String line;
        for (Map.Entry<String, Integer> entry : map.entrySet()
        ) {
            if (!entry.getKey().equals(CARD)) {
                for (int i = 0; i <= list.size() - 1; i++) {
                    key = Integer.parseInt(entry.getKey());
                    if (list.get(i).getItemId() == key) {
                        quantity = entry.getValue();
                        if (list.get(i).isStock() && quantity >= 5) {
                            totalPriceProduct = list.get(i).getCost() * quantity * PERCENT90;
                        } else {
                            totalPriceProduct = list.get(i).getCost() * quantity;
                        }
                        line = String.format("<pre>%-3d  %-18s   %8.2f %8.2f </pre>", quantity,
                                list.get(i).getName(), list.get(i).getCost(), totalPriceProduct);
                        totalPrice += totalPriceProduct;
                        discount += list.get(i).getCost() * quantity - totalPriceProduct;
                        sb.append(line);
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
                sb.append(String.format("<pre>Your Discount     %25.2f</pre>", discount + totalPrice * PERCENT3));
                sb.append(String.format("<h4><pre><h2>Total Price %17.2f</pre></h4>", totalPrice * PERCENT97));
            }
            if (card.getNumber() > 99 && card.getNumber() < 1000) {
                sb.append("<pre>Your Card with 4% Discount: </pre>" + card.getNumber());
                sb.append(String.format("<pre>Your Discount     %25.2f</pre>", discount + totalPrice * PERCENT4));
                sb.append(String.format("<h4><pre><h2>Total Price %17.2f</pre></h4>", totalPrice * PERCENT96));
            }
            if (card.getNumber() > 999) {
                sb.append("<pre>Your Card with 5% Discount: </pre>" + card.getNumber());
                sb.append(String.format("<pre>Your Discount     %25.2f</pre>", discount + totalPrice * PERCENT5));
                sb.append(String.format("<h4><pre><h2>Total Price %17.2f</pre></h4>", totalPrice * PERCENT95));
            }
        }
        sb.append("</html>");
        return sb;

    }

    @Override
    public void printCheck(StringBuilder sb) throws ProductException {
        File file = new File(CHECKFILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(sb.toString());
            writer.close();
            publisher.notify(CHECK_WAS_PRINTED_IN_TXT, "Check was printed in TXT file");
        } catch (IOException | MessagingException e) {
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
            publisher.notify(CHECK_WAS_PRINTED_IN_PDF, "Check was printed in PDF file");
        } catch (DocumentException e) {
            throw new ProductException("Document problem");
        } catch (FileNotFoundException e) {
            throw new ProductException("Cant find file");
        } catch (IOException | MessagingException e) {
            throw new ProductException("IO problem");
        }
    }

    @Override
    public Publisher getPublisher() {
        return publisher;
    }

}
