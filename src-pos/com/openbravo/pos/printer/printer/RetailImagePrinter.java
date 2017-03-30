package com.openbravo.pos.printer.printer;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import javax.print.PrintService;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author abhijit
 */
public class RetailImagePrinter implements Printable {

    private String image;
    private int xInc;
    private int yInc;
    private PrinterJob pPrinterJob;
    private PageFormat pPageFormat;
    private Paper pPaper ;
    private Book pBook ;
    private Font font;
    private java.util.List<TicketLineConstructor> allLines;
    public RetailImagePrinter() {
        this.pPrinterJob = PrinterJob.getPrinterJob();
        this.pPageFormat = pPrinterJob.defaultPage();
        this.pPaper = pPageFormat.getPaper();
      //  pPaper.setSize(200, 570);
     //   pPaper.setImageableArea(-150, 0,200, 570);
        pPageFormat.setPaper(pPaper);
        this.pBook = new Book();
        this.font = new Font("Monospaced", Font.PLAIN, 8).deriveFont(AffineTransform.getScaleInstance(1.0, 1.40));

    }

    public synchronized void print(java.util.List<TicketLineConstructor> allLines, String printername) throws PrinterException {
       // this.image = str;
        this.allLines = allLines;
        PrintService[] services = pPrinterJob.lookupPrintServices();

        System.out.println("services---"+services.length);
    // Retrieve specified print service from the array
        for (PrintService s : services) {
        System.out.println("s.getName()--"+s.getName());
        if (s.getName().equalsIgnoreCase(printername)) {
               System.out.println("s.eneter inside()--"+s.getName());
            pPrinterJob.setPrintService(s);
            pPrinterJob.setPrintable(this);

            pPrinterJob.print();
            pBook.append(this, pPageFormat);
            pPrinterJob.setPageable(pBook);
          }

        }
    }

    @Override
    public int print(Graphics g, PageFormat aPageFormat, int aPageIndex) {
        System.out.println("print----");
        if (aPageIndex > 0) {
            return NO_SUCH_PAGE;
        }
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform pOrigTransform = g2d.getTransform();
        g2d.setFont(font);
        g2d.translate(aPageFormat.getImageableX(), aPageFormat.getImageableY());
        int i =0;
        for(TicketLineConstructor line : allLines){
            g2d.drawString(line.getLine(), 0, 0+(i*12));
            i++;
        }
        
        //g2d.drawString("#######################", 100, 258);
        g2d.setTransform(pOrigTransform);

        return PAGE_EXISTS;
    }
}
