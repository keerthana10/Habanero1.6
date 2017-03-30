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

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author abhijit
 */
public class ImageBillPrinter implements Printable {

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
    public ImageBillPrinter() {
        this.pPrinterJob = PrinterJob.getPrinterJob();
        this.pPageFormat = pPrinterJob.defaultPage();
        this.pPaper = pPageFormat.getPaper();
        System.out.println("pPageFormatImageBillPrinter--"+pPageFormat.getWidth()+pPageFormat.getHeight());
         pPaper.setSize(0, 0);
        pPaper.setImageableArea(5, 5,pPageFormat.getWidth(), pPageFormat.getHeight());
        pPageFormat.setPaper(pPaper);
      //  this.pBook = new Book();
        this.font = new Font("Monospaced", Font.PLAIN, 8).deriveFont(AffineTransform.getScaleInstance(1.0, 1.40));

    }

    public void print(java.util.List<TicketLineConstructor> allLines) throws PrinterException {
       // this.image = str;

       this.allLines = allLines;
        pPrinterJob.setPrintable(this);
        boolean ok = pPrinterJob.printDialog();
        // System.out.println("printer cnae--"+pPrinterJob.isCancelled());
         if (ok) {
             try {
                  pPrinterJob.print();
             } catch (PrinterException ex) {
                 System.out.println("ex printer--"+ex.toString());
              /* The job did not successfully complete */
             }

         }

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
       pPaper.setImageableArea(5, 5,aPageFormat.getWidth(), aPageFormat.getHeight());
       aPageFormat.setPaper(pPaper);
       FontMetrics metrics = g.getFontMetrics(font);
       int lineHeight = metrics.getHeight();
        System.out.println("lineHeight---"+lineHeight);
      //  System.out.println("pageBreak==="+pageBreaks);
        pageBreaks =null;
    //    if (pageBreaks == null) {
//
           initTextLines();
//            if(textLines.length-1>=36 && textLines.length-1<=41) {
//               int numBreaks = (textLines.length-1)/32;
//                System.out.println("numBreaks--"+numBreaks);
//                pageBreaks = new int[numBreaks];
//                for (int b=0; b<numBreaks; b++) {
//                    pageBreaks[b] = (b+1)*32;
//                }
//            }
//            else if(textLines.length-1>=72 && textLines.length-1<=77) {
//                 int numBreaks = (textLines.length-1)/36;
//                  pageBreaks = new int[numBreaks];
//                    for (int b=0; b<numBreaks; b++) {
//                        if(b==1) {
//                            pageBreaks[b] = 67;
//                        }else{
//                            pageBreaks[b] = (b+1)*36;
//                        }
//                }
//            }else if(textLines.length-1>=108 && textLines.length-1<=113) {
//                 int numBreaks = (textLines.length-1)/36;
//                  pageBreaks = new int[numBreaks];
//                    for (int b=0; b<numBreaks; b++) {
//                        if(b==2) {
//                            pageBreaks[b] = 102;
//                        }else{
//                            pageBreaks[b] = (b+1)*36;
//                        }
//                }
//            }else {
//                int linesPerPage = 38;//(int)(aPageFormat.getImageableHeight()/12);
//                int numBreaks = (textLines.length)/linesPerPage;
//                pageBreaks = new int[numBreaks];
//                for (int b=0; b<numBreaks; b++) {
//                    pageBreaks[b] = (b+1)*linesPerPage;
//                }
//            }
       // }
     //   System.out.println("aPageIndex---"+aPageIndex);
        if (aPageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(aPageFormat.getImageableX(), aPageFormat.getImageableY());
        int i =0;

          int y = 0;
//        int start = (aPageIndex == 0) ? 0 : pageBreaks[aPageIndex-1];
//        int end   = (aPageIndex == pageBreaks.length)
//                         ? textLines.length : pageBreaks[aPageIndex];
//        System.out.println("start--"+start+"---"+end);
//         for( int count = 1 ; count < allLines.size();count++){
//            TicketLineConstructor line = allLines.get(count);
//            g2d.drawString(line.getLine(), 0, 0+(i*12));
//            i++;
//        }
          System.out.println("allLines.size()----"+allLines.size());
         for (int line=0; line<allLines.size(); line++) {
            y += lineHeight;
            g.drawString(textLines[line], 0, y);
        }

//        if(end % 32 == 0) {
//            for(int j= 0; j < 10; j++) {
//                g.drawString("", 0, y);
//
//            }
        //}

        return PAGE_EXISTS;
    }
}
