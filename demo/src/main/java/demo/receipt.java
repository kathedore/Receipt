package demo;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet (name = "print", urlPatterns = "/receipt")
public class receipt extends HttpServlet {

    public static final String IMAGE1 = "/Users/ketikakhniauri/IdeaProjects/demo/src/BOG.jpg";
    public static final String FONT[] = {"/Users/ketikakhniauri/IdeaProjects/demo/src/sylfaen.ttf"};
    public static final String DATA [][]= {
            {"24/08/2020", "107.57"},
            {"24/09/2020", "106.98"},
            {"10/09/2020", "-0.20"}
    };

    //Initialization
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PdfDocument pdfDoc = new PdfDocument(new PdfWriter(baos));
    Document doc = new Document(pdfDoc);

    //columnWidth for grids
    float[] columnWidths = {2, 6, 1,1,1};

    public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException {

    }

    public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException {

        //Setting font
        PdfFont freeUnicode = PdfFontFactory.createFont(FONT[0], PdfEncodings.IDENTITY_H, true);
        doc.setFont(freeUnicode);

        //Adding image at the top right corner
        Image image = new Image(ImageDataFactory.create(IMAGE1),400,730,150);
        doc.add(image);

        //Adding title
        doc.add(new Paragraph("ანგარიშიდან ამონაწერი").setBold().setFontSize(16));

        //Getting current time and date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        DateTimeFormatter dtf2= DateTimeFormatter.ofPattern("dd/MM/yyyy");

        //DateTimeFormatter period= DateTimeFormatter.ofPattern("dd"+String.valueOf(Integer.parseInt("MM")-1)+"yyyy");
        LocalDateTime now = LocalDateTime.now();

        //Creating First table for personal information
        Table table = new Table(UnitValue.createPercentArray(1));
        table.setWidth(UnitValue.createPercentValue(65));
        table.setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 1));
        Cell cell = new Cell();

        Paragraph p = new Paragraph("ანგარიშის მფლობელი                  "+req.getParameter("name")+" "+req.getParameter("surname"))
                .setFixedLeading(20);
        cell.add(p);

        System.out.println("ანგარიშის ნომერი"); //Asking for the card number
        p = new Paragraph("ანგარიშის ნომერი:                  "+ req.getParameter("Accnum"))
                .setFixedLeading(20);
        cell.add(p);

        System.out.println("ბარათის ტიპი"); //Asking the card type
        p = new Paragraph("ბარათი                  "+req.getParameter("cardType"))
                .setFixedLeading(20);
        cell.add(p);

        p = new Paragraph("ბეჭდვის თარიღი                  "+dtf.format(now))
                .setFixedLeading(20);
        cell.add(p);

        p = new Paragraph("ამონაწერის პერიოდი                  "+dtf2.format(now))
                .setFixedLeading(20);
        cell.add(p);

        table.addCell(cell);
        table.setMarginTop(20f);
        doc.add(table);
        
        grid0();
        grid1();
        doc.close();

        //getting out info
        OutputStream os = res.getOutputStream();
        baos.writeTo(os);
        os.flush();
        os.close();
    }

    private void grid0(){

        //adding first grid
        Table grid = new Table(UnitValue.createPercentArray(columnWidths));

        Cell c = new Cell(1, 1)
                .add(new Paragraph("თარიღი"))
                .setFontSize(13)
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER);

        grid.addHeaderCell(c);

        for (int i = 0; i < 1; i++) {
            Cell[] headerFooter = new Cell[]{
                    new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("           ")),
                    new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("GEL").setTextAlignment(TextAlignment.CENTER)),
                    new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("USD")),
                    new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("EUR"))
            };

            for (Cell hfCell : headerFooter) {
                if (i == 0) {
                    grid.addHeaderCell(hfCell);
                } else {
                    grid.addFooterCell(hfCell);
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 1; j++) {
                grid.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(String.valueOf(DATA[i][j]))));
                grid.addCell(new Cell().setTextAlignment(TextAlignment.LEFT).add(new Paragraph("საწყისი ნაშთი")));
                grid.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(String.valueOf(DATA[i][j+1]))));
                grid.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(" ")));
                grid.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(" ")));
            }
        }
        grid.setMarginTop(50f);
        doc.add(grid);
    }

    private void grid1(){

        Table grid1 = new Table(UnitValue.createPercentArray(columnWidths));

        Cell c1 = new Cell(1, 1)
                .add(new Paragraph("თარიღი"))
                .setFontSize(13)
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER);

        grid1.addHeaderCell(c1);

        for (int i = 0; i < 1; i++) {
            Cell[] headerFooter = new Cell[]{
                    new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("დანიშნულება").setTextAlignment(TextAlignment.CENTER)),
                    new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("GEL").setTextAlignment(TextAlignment.CENTER)),
                    new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("USD")),
                    new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("EUR"))
            };

            for (Cell hfCell : headerFooter) {
                if (i == 0) {
                    grid1.addHeaderCell(hfCell);
                } else {
                    grid1.addFooterCell(hfCell);
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            grid1.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(String.valueOf(DATA[2][0]))));
            grid1.addCell(new Cell().setTextAlignment(TextAlignment.LEFT).add(new Paragraph("გადახდა თანხა")));
            grid1.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(String.valueOf(DATA[2][1]))));
            grid1.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(" ")));
            grid1.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(" ")));
        }
        grid1.setMarginTop(60f);
        doc.add(grid1);
    }
}
