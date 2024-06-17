package CentralSync.demo.util;

import CentralSync.demo.model.ItemOrder;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.stereotype.Component;
import java.io.FileNotFoundException;

@Component
public class PdfGenerator {

    public void generateOrderPdf(String filePath, ItemOrder itemOrder) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Adding company name
        document.add(new Paragraph().setTextAlignment(TextAlignment.CENTER)
                .add(new Text("CentralSync").setItalic().setFontSize(8)));

        // Adding a heading
        Paragraph heading = new Paragraph("Order Details")
                .setFontSize(16)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(heading);

        // Adding order details
        document.add(new Paragraph("Item Name: " + itemOrder.getItemName()));
        document.add(new Paragraph("Brand: " + itemOrder.getBrandName()));
        document.add(new Paragraph("Quantity: " + itemOrder.getQuantity()));

        document.close();
    }

}
