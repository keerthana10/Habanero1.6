package com.openbravo.pos.printer.printer;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.*;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author abhijit
 */
public class ImagePrinter implements Printable {

    private String image;
    private int xInc;
    private int yInc;
    private PrinterJob pPrinterJob;
    private PageFormat pPageFormat;
    private Paper pPaper ;
    private Book pBook ;
    private Font font;
    private java.util.List<TicketLineConstructor> allLines;
    public ImagePrinter() {
        this.pPrinterJob = PrinterJob.getPrinterJob();
        this.pPageFormat = pPrinterJob.defaultPage();
        this.pPaper = pPageFormat.getPaper();
        pPaper.setSize(432, 570);
        pPaper.setImageableArea(-70, 35,432, 570);
        pPageFormat.setPaper(pPaper);
        this.pBook = new Book();
        this.font = new Font("Monospaced", Font.PLAIN, 8).deriveFont(AffineTransform.getScaleInstance(1.0, 1.40));

    }

    public void print(java.util.List<TicketLineConstructor> allLines) throws PrinterException {
       // this.image = str;
        this.allLines = allLines;
        pBook.append(this, pPageFormat);
        pPrinterJob.setPageable(pBook);
        pPrinterJob.print();
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
            g2d.drawString(line.getLine(), 100, 100+(i*12));
            i++;
        }
        
        //g2d.drawString("#######################", 100, 258);
        g2d.setTransform(pOrigTransform);
        return PAGE_EXISTS;
    }
}
