package ru.clevertec.check.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.check.dao.ProductDao;
import ru.clevertec.check.entities.product.Card;
import ru.clevertec.check.entities.product.Order;
import ru.clevertec.check.entities.product.Product;
import ru.clevertec.check.entities.product.SingleProduct;
import ru.clevertec.check.exception.ServiceException;
import ru.clevertec.check.service.CheckService;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

import static ru.clevertec.check.exception.ProductExceptionConstants.*;
import static ru.clevertec.check.service.CheckConstants.*;

@AllArgsConstructor
@Service
public class CheckServiceImpl implements CheckService {

    private final ProductDao productDao;

    @Override
    public StringBuilder showCheck(Map<String, Integer> map) {
        StringBuilder sb = new StringBuilder();
        Order order = getOrder(map);
        sb.append("\n\n              CASH RECEIPT\n\n").
                append("       supermarket 'The Two Geese' \n").
                append("      " + new Date().toString() + "\n\n").
                append("QTY  DESCRIPTION            PRICE   TOTAL \n");
        sb.append(buildSingleProducts(map, OUTPUT_TXT));
        sb.append(TRANSFER);
        sb.append(getCardTXT(order.getCard(), order.getTotalPrice(), order.getDiscount()));
        return sb;
    }

    @Override
    public StringBuilder getHTML(Map<String, Integer> map) {
        Order order = getOrder(map);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HTML_OPEN).
                append("<head><h2><pre>        CASH RECEIPT</pre></h2></head>").
                append("<pre>        supermarket 'The Two Geese'</pre> ").
                append(String.format("<pre>       %s </pre>    ", new Date().toString())).
                append("<h3><pre>QTY DESCRIPTION          PRICE   TOTAL </pre></h3>");
        stringBuilder.append(buildSingleProducts(map, OUTPUT_HTML));
        stringBuilder.append(TRANSFER_HTML).
                append(getCardHTML(order.getCard(), order.getTotalPrice(), order.getDiscount())).
                append(HTML_CLOSE);
        return stringBuilder;
    }

    @Override
    public StringBuilder getPDF(Map<String, Integer> map) {
        Order order = getOrder(map);
        StringBuilder sb = new StringBuilder();
        sb.append(DOUBLE_INDENT + TRANSPORT + TRANSPORT_HALF + "      CASH RECEIPT" + DOUBLE_INDENT).
                append(TRANSPORT + TRANSPORT_HALF + "  supermarket 'The Two Geese' \n").
                append(TRANSPORT + TRANSPORT_HALF + new Date().toString() + DOUBLE_INDENT).
                append(TRANSPORT + "QTY        DESCRIPTION            PRICE         TOTAL \n");
        sb.append(buildSingleProducts(map, OUTPUT_PDF));
        sb.append(TRANSPORT + TRANSFER_PDF);
        sb.append(getCardPDF(order.getCard(), order.getTotalPrice(), order.getDiscount()));
        return sb;
    }

    public StringBuilder buildSingleProducts(Map<String, Integer> map, String format) {
        List<Product> list = productDao.findAll();
        StringBuilder sb = new StringBuilder();
        String key;
        int quantity;
        String line;
        double totalPriceProduct;
        double totalPrice = ZERO_INT;
        double discount = ZERO_INT;
        Card card;
        for (Map.Entry<String, Integer> entry : map.entrySet()
        ) {
            if (!entry.getKey().equals(CARD)) {
                for (int i = ZERO_INT; i < list.size(); i++) {
                    key = entry.getKey();
                    if (list.get(i).getName().equals(key)) {
                        quantity = entry.getValue();
                        if (list.get(i).isStock() && quantity >= PRODUCT_NUMBER) {
                            totalPriceProduct = list.get(i).getCost() * quantity * PERCENT90;
                        } else {
                            totalPriceProduct = list.get(i).getCost() * quantity;
                        }
                        line = String.format(format, quantity,
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
        return sb;
    }

    @Override
    public Order getOrder(Map<String, Integer> map) {
        List<SingleProduct> products = getSingleProducts(map);
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
    public void printCheck(StringBuilder sb) throws ServiceException {
        File file = new File(CHECKFILETXT);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(sb.toString());
            writer.close();
//            publisher.notify(CHECK_WAS_PRINTED_IN_TXT, CHECK_TXT);
        } catch (IOException e) {
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
        } catch (DocumentException e) {
            throw new ServiceException(DOCUMENT_EXCEPTION);
        } catch (FileNotFoundException e) {
            throw new ServiceException(NO_FILE_EXCEPTION);
        } catch (IOException e) {
            throw new ServiceException(IOEXCEPTION);
        }
    }

    @Override
    public Map<String, Integer> selectProducts(HttpServletRequest request){
        List<Product> products = productDao.findAll();
        Map<String, Integer> purchaseParameters = new HashMap<>();
        for (Product item : products) {
            if (!request.getParameter(item.getName()).isBlank()) {
                purchaseParameters.put(item.getName(), Integer.parseInt(request.getParameter(item.getName())));
            }
        }
        if (!request.getParameter(CARD).isBlank()) {
            purchaseParameters.put(CARD, Integer.parseInt(request.getParameter(CARD)));
        } else {
            purchaseParameters.put(CARD, ZERO_INT);
        }
        return purchaseParameters;
    }

    private List<SingleProduct> getSingleProducts(Map<String, Integer> map) {
        List<Product> list = productDao.findAll();
        SingleProduct singleProduct;
        double totalPriceProduct;
        List<SingleProduct> products = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()
        ) {
            if (!entry.getKey().equals(CARD) && entry.getValue() != ZERO_INT) {
                for (int i = ZERO_INT; i < list.size(); i++) {
                    String key = entry.getKey();
                    if (key.equals(list.get(i).getName())) {
                        int quantity = entry.getValue();
                        totalPriceProduct = list.get(i).getCost() * quantity;
                        singleProduct = new SingleProduct(quantity, list.get(i).getName(),
                                list.get(i).getCost(), totalPriceProduct, list.get(i));
                        if (singleProduct.getProduct().getName() != null)
                            products.add(singleProduct);
                    }
                }
            }
        }

        return getStockPrice(products);
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

    private StringBuilder getCardHTML(int card, double totalPrice, double discount) {
        StringBuilder stringBuilder = new StringBuilder();
        if (card == CARD_RANGE_0) {
            stringBuilder.append("<pre>No Discount Card </pre>");
        } else {
            if (card > CARD_RANGE_0 && card < CARD_RANGE_100) {
                stringBuilder.append("<pre>Your Card with 3% Discount: </pre>" + card);
            } else if (card >= CARD_RANGE_100 && card < CARD_RANGE_1000) {
                stringBuilder.append("<pre>Your Card with 4% Discount: </pre>" + card);
            } else if (card >= CARD_RANGE_1000) {
                stringBuilder.append("<pre>Your Card with 5% Discount: </pre>" + card);
            }
        }
        stringBuilder.append(String.format(DISCOUNT_HTML, discount)).
                append(String.format(PRICE_HTML, totalPrice));
        return stringBuilder;
    }

    private StringBuilder getCardTXT(int card, double totalPrice, double discount) {
        StringBuilder sb = new StringBuilder();
        if (card == CARD_RANGE_0) {
            sb.append(NO_CARD);
        } else {
            if (card > CARD_RANGE_0 && card < CARD_RANGE_100) {
                sb.append(CARD3 + card);
            }
            if (card >= CARD_RANGE_100 && card < CARD_RANGE_1000) {
                sb.append(CARD4 + card);
            }
            if (card >= CARD_RANGE_1000) {
                sb.append(CARD5 + card);
            }
        }
        sb.append(String.format(DISCOUNT, discount));
        sb.append(String.format(PRICE, totalPrice));
        return sb;
    }

    private StringBuilder getCardPDF(int card, double totalPrice, double discount) {
        StringBuilder sb = new StringBuilder();
        if (card == CARD_RANGE_0) {
            sb.append(INDENT + TRANSPORT + NO_CARD);
        } else {
            if (card > CARD_RANGE_0 && card < CARD_RANGE_100) {
                sb.append(INDENT + TRANSPORT + CARD3 + card);
            }
            if (card >= CARD_RANGE_100 && card < CARD_RANGE_1000) {
                sb.append(INDENT + TRANSPORT + CARD4 + card);
            }
            if (card >= CARD_RANGE_1000) {
                sb.append(INDENT + TRANSPORT + CARD5 + card);
            }
        }
        sb.append(String.format(DISCOUNT_PDF_FORMAT, DISCOUNT_PDF, discount));
        sb.append(String.format(PRICE_PDF_FORMAT, TOTAL_PRICE_PDF, totalPrice));
        return sb;
    }
}
