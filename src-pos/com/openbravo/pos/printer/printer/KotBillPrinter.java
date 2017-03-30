package com.openbravo.pos.printer.printer;


import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author abhijit
 */
public class KotBillPrinter implements Printable {

    private String image;
    private int xInc;
    private int yInc;
    private PrinterJob pPrinterJob;
    private PageFormat pPageFormat;
    private Paper pPaper ;
    private Book pBook ;
    private Font font;
    String[] textLines =null;
      int[] pageBreaks = null;
    private java.util.List<TicketLineConstructor> allLines;
    public KotBillPrinter() {
        this.pPrinterJob = PrinterJob.getPrinterJob();
        this.pPageFormat = pPrinterJob.defaultPage();
        this.pPaper = pPageFormat.getPaper();
        System.out.println("pPageFormatImageBillPrinter--"+pPageFormat.getWidth()+pPageFormat.getHeight());
         pPaper.setSize(0, 0);
      pPaper.setImageableArea(70, 50,200, 600);
        // pPaper.setImageableArea(0, 0, pPaper.getWidth() , pPaper.getHeight() -2);
        pPageFormat.setPaper(pPaper);
      //  this.pBook = new Book();
        this.font = new Font("Monospaced", Font.PLAIN, 8).deriveFont(AffineTransform.getScaleInstance(1.0, 1.40));

    }

    public void print(java.util.List<TicketLineConstructor> allLines, String printName) throws PrinterException {
       // this.image = str;
    
       this.allLines = allLines;
       PrintService[] services = PrinterJob.lookupPrintServices();
        pPrinterJob.setPrintable(this);
        boolean ok = pPrinterJob.printDialog();
        for (PrintService s : services) {
        System.out.println("s.getName()--"+s.getName());
        if (s.getName().equalsIgnoreCase(printName)) {
               System.out.println("s.eneter inside()--"+s.getName());
             //  kotlogger.info("Printer name is :"+s.getName());
//               if(s.getName().equalsIgnoreCase("Generic-text-only"))
//               {
//                   s=null;
//               }
//                try {
              if (ok) {
                   pPrinterJob.setPrintService(s);
                    pPrinterJob.print();
//                } catch (PrinterException ex) {
//                    Logger.getLogger(KotImagePrinter.class.getName()).log(Level.SEVERE, null, ex);
//                }
               }
          }

        }
        // System.out.println("printer cnae--"+pPrinterJob.isCancelled());
//         if (ok) {
//             try {
//                  pPrinterJob.print();
//             } catch (PrinterException ex) {
//                 System.out.println("ex printer--"+ex.toString());
//              /* The job did not successfully complete */
//             }
//
//         }

        //pBook.append(this, pPageFormat);
     //   System.out.println("get pBook--"+Printable.NO_SUCH_PAGE);
       // pPrinterJob.setPageable(pBook);
    }

    private void initTextLines() {
        System.out.println("textlines---"+textLines);
        textLines = null;
        if (textLines == null) {
            int numLines=allLines.size();
            textLines = new String[numLines];
            System.out.println("inittextlines---"+numLines);

            for (int i=0;i<numLines;i++) {
                textLines[i]= allLines.get(i).getLine();
            }

        }
    }
    @Override
    public int print(Graphics g, PageFormat aPageFormat, int aPageIndex) {

       g.setFont(font);
        this.pPrinterJob = PrinterJob.getPrinterJob();
        this.pPageFormat = pPrinterJob.defaultPage();
        this.pPaper = pPageFormat.getPaper();
            // //  this.pPaper = aPageFormat.getPaper();
               System.out.println("pPageFormatprint-111-"+pPageFormat.getWidth()+pPageFormat.getHeight()+"---"+pPaper.toString());
    //   pPaper.setSize(612, 792);
       pPaper.setSize(0, 0);
       pPaper.setImageableArea(70, 70,200, 600);
       aPageFormat.setPaper(pPaper);
       FontMetrics metrics = g.getFontMetrics(font);
       int lineHeight = metrics.getHeight();
        System.out.println("lineHeight---"+lineHeight);
      //  System.out.println("pageBreak==="+pageBreaks);
        pageBreaks =null;
    //    if (pageBreaks == null) {
//
           initTextLines();

     //   System.out.println("aPageIndex---"+aPageIndex);
        if (aPageIndex > 0) {
            return NO_SUCH_PAGE;
        }
    
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(aPageFormat.getImageableX(), aPageFormat.getImageableY());
        int i =0;

          int y = 0;

          System.out.println("allLines.size()----"+allLines.size());
         for (int line=0; line<allLines.size(); line++) {
            y += lineHeight;
            g.drawString(textLines[line], 0, y);
        }



        return PAGE_EXISTS;
    }
}
