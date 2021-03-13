package ru.clevertec.check.model.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import ru.clevertec.check.annotations.log.LogMe;
import ru.clevertec.check.entities.parameters.ProductParameters;
import ru.clevertec.check.entities.product.Card;
import ru.clevertec.check.entities.product.Order;
import ru.clevertec.check.entities.product.SingleProduct;
import ru.clevertec.check.exception.DaoException;
import ru.clevertec.check.exception.ServiceException;
import ru.clevertec.check.model.dao.ProductDao;
import ru.clevertec.check.model.dao.impl.ProductDaoImpl;
import ru.clevertec.check.model.service.CheckService;
import ru.clevertec.check.observer.Publisher;

import javax.mail.MessagingException;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static ru.clevertec.check.exception.ProductExceptionConstants.*;
import static ru.clevertec.check.model.service.CheckConstants.*;
import static ru.clevertec.check.observer.entity.State.*;

public class CheckServiceImpl implements CheckService {

    private Publisher publisher = new Publisher(CHECK_WAS_PRINTED_IN_PDF, CHECK_WAS_PRINTED_IN_TXT, CHECK_HAS_NOT_PRINTED);
    private Map<String, Integer> map;
    private ProductDao repository = ProductDaoImpl.getInstance();

    public CheckServiceImpl(Map<String, Integer> map) {
        this.map = map;
    }

    @LogMe
    @Override
    public StringBuilder showCheck(List<ProductParameters> list) {
        StringBuilder sb = new StringBuilder();
        Date date = new Date();
        Card card = new Card(CARD_RANGE_0);
        sb.append("\n\n              CASH RECEIPT\n\n").
                append("       supermarket 'The Two Geese' \n").
                append("      " + date + "\n\n").
                append("QTY  DESCRIPTION            PRICE   TOTAL \n");
        int key;
        int quantity;
        double totalPriceProduct;
        double totalPrice = ZERO_INT;
        double discount = ZERO_INT;
        String line;
        for (Map.Entry<String, Integer> entry : map.entrySet()
        ) {
            if (!entry.getKey().equals(CARD)) {
                for (int i = ZERO_INT; i < list.size(); i++) {
                    key = Integer.parseInt(entry.getKey());
                    if (list.get(i).getItemId() == key) {
                        quantity = entry.getValue();
                        if (list.get(i).isStock() && quantity >= PRODUCT_NUMBER) {
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
        sb.append(TRANSFER);
        sb.append(getCardTXT(card, totalPrice, discount));
        return sb;
    }

    @Override
    public StringBuilder getHTML(List<ProductParameters> list) {
        StringBuilder stringBuilder = new StringBuilder();
        Date date = new Date();
        Card card = null;
        stringBuilder.append(HTML_OPEN).
                append("<head><h2><pre>        CASH RECEIPT</pre></h2></head>").
                append("<pre>        supermarket 'The Two Geese'</pre> ").
                append(String.format("<pre>       %s </pre>    ", date.toString())).
                append("<h3><pre>QTY DESCRIPTION          PRICE   TOTAL </pre></h3>");
        int key;
        int quantity;
        String line;
        double totalPriceProduct;
        double totalPrice = ZERO_INT;
        double discount = ZERO_INT;
        for (Map.Entry<String, Integer> entry : map.entrySet()
        ) {
            if (!entry.getKey().equals(CARD)) {
                for (int i = 0; i < list.size(); i++) {
                    key = Integer.parseInt(entry.getKey());
                    if (list.get(i).getItemId() == key) {
                        quantity = entry.getValue();
                        if (quantity != ZERO_INT) {
                            if (list.get(i).isStock() && quantity >= 10) {
                                totalPriceProduct = list.get(i).getCost() * quantity * PERCENT90;
                            } else {
                                totalPriceProduct = list.get(i).getCost() * quantity;
                            }
                            line = String.format("<pre>%-3d  %-18s   %8.2f %8.2f </pre>", quantity,
                                    list.get(i).getName(), list.get(i).getCost(), totalPriceProduct);
                            totalPrice += totalPriceProduct;
                            discount += list.get(i).getCost() * quantity - totalPriceProduct;
                            stringBuilder.append(line);
                        }
                    }
                }
            } else {
                card = new Card(entry.getValue());
            }
        }
        stringBuilder.append(TRANSFER_HTML).
                append(getCard(card, totalPrice, discount)).
                append(HTML_CLOSE);
        return stringBuilder;
    }

    @Override
    public void printCheck(StringBuilder sb) throws ServiceException {
        File file = new File(CHECKFILETXT);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(sb.toString());
            writer.close();
            publisher.notify(CHECK_WAS_PRINTED_IN_TXT, CHECK_TXT);
        } catch (IOException | MessagingException e) {
            throw new ServiceException(IOEXCEPTION);
        }
    }

    @Override
    public void printPDFCheck(StringBuilder sb) throws ServiceException {
        try {
            FileOutputStream file = new FileOutputStream(CHECKFILEPDF);
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, file);
            document.open();
            document.newPage();
            document.add(new Paragraph(THRIPLE_INDENT));
            document.add(new Paragraph(sb.toString()));
            PdfReader reader = new PdfReader(new FileInputStream(PDFTEMPLATE));
            PdfImportedPage page = writer.getImportedPage(reader, PAGENUMBER);
            PdfContentByte pdfContentByte = writer.getDirectContentUnder();
            pdfContentByte.addTemplate(page, COORD_X, COORD_Y);
            document.close();
            publisher.notify(CHECK_WAS_PRINTED_IN_PDF, CHECK_PDF);
        } catch (DocumentException e) {
            throw new ServiceException(DOCUMENT_EXCEPTION);
        } catch (FileNotFoundException e) {
            throw new ServiceException(NO_FILE_EXCEPTION);
        } catch (IOException | MessagingException e) {
            throw new ServiceException(IOEXCEPTION);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public StringBuilder getPDF(List<ProductParameters> list) {
        StringBuilder sb = new StringBuilder();
        Date date = new Date();
        Card card = new Card(CARD_RANGE_0);
        sb.append(DOUBLE_INDENT + TRANSPORT + TRANSPORT_HALF + "      CASH RECEIPT" + DOUBLE_INDENT).
                append(TRANSPORT + TRANSPORT_HALF + "  supermarket 'The Two Geese' \n").
                append(TRANSPORT + TRANSPORT_HALF + date + DOUBLE_INDENT).
                append(TRANSPORT + "QTY        DESCRIPTION            PRICE         TOTAL \n");
        int key;
        int quantity;
        String line;
        double totalPriceProduct;
        double totalPrice = ZERO_INT;
        double discount = ZERO_INT;
        for (Map.Entry<String, Integer> entry : map.entrySet()
        ) {
            if (!entry.getKey().equals(CARD)) {
                for (int i = ZERO_INT; i < list.size(); i++) {
                    key = Integer.parseInt(entry.getKey());
                    if (list.get(i).getItemId() == key && list.get(i).getItemId() != 0) {
                        quantity = entry.getValue();
                        if (list.get(i).isStock() && quantity >= 5) {
                            totalPriceProduct = list.get(i).getCost() * quantity * PERCENT90;
                        } else {
                            totalPriceProduct = list.get(i).getCost() * quantity;
                        }
                        line = String.format(" %43d  %18s %27.2f  %14.2f \n", quantity,
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
        sb.append(TRANSPORT + TRANSFER_PDF);
        sb.append(getCardPDF(card, totalPrice, discount));
        return sb;
    }

    public List<SingleProduct> getSingleProducts(List<ProductParameters> list) throws ServiceException {
        SingleProduct singleProduct;
        double totalPriceProduct;
        List<SingleProduct> products = new ArrayList<>();
        try {
            int repositorySize = repository.getProductsCount();
            for (Map.Entry<String, Integer> entry : map.entrySet()
            ) {
                if (!entry.getKey().equals(CARD)) {
                    for (int i = ZERO_INT; i <= repositorySize; i++) {
                        int key = Integer.parseInt(entry.getKey());
                        if (key == i + ONE_INT) {
                            int quantity = entry.getValue();
                            totalPriceProduct = list.get(i).getCost() * quantity;
                            singleProduct = new SingleProduct(quantity, list.get(i).getName(),
                                    list.get(i).getCost(), totalPriceProduct, repository.getId(i + 1));
                            if (singleProduct.getProduct().getName() != null)
                                products.add(singleProduct);
                        }
                    }
                }
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return getStockPrice(products);
    }

    @Override
    public Order getOrder(List<ProductParameters> list) throws ServiceException {
        List<SingleProduct> products = getSingleProducts(list);
        Card card = null;
        double totalPriceWODiscount = ZERO_INT;
        double cardPercent = ZERO_INT;
        double totalPrice = ZERO_INT;
        double totalDiscount;
        for (Map.Entry<String, Integer> entry : map.entrySet()
        ) {
            if (entry.getKey().equals(CARD)) {
                card = new Card(entry.getValue());
                cardPercent = getPercent(card);
            } else {
                card = new Card(CARD_RANGE_0);
                cardPercent = ONE_INT;
            }
        }
        for (SingleProduct product : products
        ) {
            totalPriceWODiscount += product.getQuantity() * product.getPrice();
            totalPrice += product.getTotalPrice();
        }
        totalPrice = totalPrice * cardPercent;
        totalDiscount = totalPriceWODiscount - totalPrice;
        return new Order(products, card, cardPercent, totalDiscount, totalPrice);
    }

    @Override
    public Publisher getPublisher() {
        return publisher;
    }

    private double getPercent(Card card) {
        int number = card.getNumber();
        if (number == CARD_RANGE_0) {
            return ONE_INT;
        } else {
            if (number > CARD_RANGE_0 && number < CARD_RANGE_100) {
                return PERCENT97;
            } else if (number >= CARD_RANGE_100 && number < CARD_RANGE_1000) {
                return PERCENT96;
            } else if (number >= CARD_RANGE_1000) {
                return PERCENT95;
            }
        }
        return ONE_INT;
    }

    private List<SingleProduct> getStockPrice(List<SingleProduct> list) {
        List<SingleProduct> discountedProducts = new ArrayList<>();
        for (SingleProduct product : list) {
            if (product.getQuantity() >= 5 && product.getProduct().isStock()) {
                product.setTotalPrice(product.getTotalPrice() * PERCENT90);
                discountedProducts.add(product);
            } else {
                discountedProducts.add(product);
            }
        }
        return discountedProducts;
    }

    private StringBuilder getCard(Card card, double totalPrice, double discount) {
        StringBuilder stringBuilder = new StringBuilder();
        if (card.getNumber() == CARD_RANGE_0) {
            stringBuilder.append("<pre>No Discount Card </pre>").
                    append(String.format(DISCOUNT_HTML, discount)).
                    append(String.format(PRICE_HTML, totalPrice));
        } else {
            if (card.getNumber() > CARD_RANGE_0 && card.getNumber() < CARD_RANGE_100) {
                stringBuilder.append("<pre>Your Card with 3% Discount: </pre>" + card.getNumber()).
                        append(String.format(DISCOUNT_HTML, discount + totalPrice * PERCENT3)).
                        append(String.format(PRICE_HTML, totalPrice * PERCENT97));
            } else if (card.getNumber() >= CARD_RANGE_100 && card.getNumber() < CARD_RANGE_1000) {
                stringBuilder.append("<pre>Your Card with 4% Discount: </pre>" + card.getNumber()).
                        append(String.format(DISCOUNT_HTML, discount + totalPrice * PERCENT4)).
                        append(String.format(PRICE_HTML, totalPrice * PERCENT96));
            } else if (card.getNumber() >= CARD_RANGE_1000) {
                stringBuilder.append("<pre>Your Card with 5% Discount: </pre>" + card.getNumber()).
                        append(String.format(DISCOUNT_HTML, discount + totalPrice * PERCENT5)).
                        append(String.format(PRICE_HTML, totalPrice * PERCENT95));
            }
        }
        return stringBuilder;
    }

    private StringBuilder getCardTXT(Card card, double totalPrice, double discount) {
        StringBuilder sb = new StringBuilder();
        if (card.getNumber() == CARD_RANGE_0) {
            sb.append(NO_CARD);
            sb.append(String.format(DISCOUNT, discount));
            sb.append(String.format(PRICE, totalPrice));
        } else {
            if (card.getNumber() > CARD_RANGE_0 && card.getNumber() < CARD_RANGE_100) {
                sb.append(CARD3 + card.getNumber());
                sb.append(String.format(DISCOUNT, discount + totalPrice * PERCENT3));
                sb.append(String.format(PRICE, totalPrice * PERCENT97));
            }
            if (card.getNumber() >= CARD_RANGE_100 && card.getNumber() < CARD_RANGE_1000) {
                sb.append(CARD4 + card.getNumber());
                sb.append(String.format(DISCOUNT, discount + totalPrice * PERCENT4));
                sb.append(String.format(PRICE, totalPrice * PERCENT96));
            }
            if (card.getNumber() >= CARD_RANGE_1000) {
                sb.append(CARD5 + card.getNumber());
                sb.append(String.format(DISCOUNT, discount + totalPrice * PERCENT5));
                sb.append(String.format(PRICE, totalPrice * PERCENT95));
            }
        }
        return sb;
    }

    private StringBuilder getCardPDF(Card card, double totalPrice, double discount) {
        double priceDiscount = ZERO_INT;
        StringBuilder sb = new StringBuilder();
        if (card.getNumber() == CARD_RANGE_0) {
            priceDiscount = discount;
            sb.append(INDENT + TRANSPORT + NO_CARD);
            sb.append(String.format(DISCOUNT_PDF_FORMAT, DISCOUNT_PDF, priceDiscount));
            sb.append(String.format(PRICE_PDF_FORMAT, TOTAL_PRICE_PDF, totalPrice + discount - priceDiscount));
        } else {
            if (card.getNumber() > CARD_RANGE_0 && card.getNumber() < CARD_RANGE_100) {
                priceDiscount = discount + totalPrice * PERCENT3;
                sb.append(INDENT + TRANSPORT + CARD3 + card.getNumber());
            }
            if (card.getNumber() >= CARD_RANGE_100 && card.getNumber() < CARD_RANGE_1000) {
                priceDiscount = discount + totalPrice * PERCENT4;
                sb.append(INDENT + TRANSPORT + CARD4 + card.getNumber());
            }
            if (card.getNumber() >= CARD_RANGE_1000) {
                priceDiscount = discount + totalPrice * PERCENT5;
                sb.append(INDENT + TRANSPORT + CARD5 + card.getNumber());
            }
            sb.append(String.format(DISCOUNT_PDF_FORMAT, DISCOUNT_PDF, priceDiscount));
            sb.append(String.format(PRICE_PDF_FORMAT, TOTAL_PRICE_PDF, totalPrice + discount - priceDiscount));
        }
        return sb;
    }
}
