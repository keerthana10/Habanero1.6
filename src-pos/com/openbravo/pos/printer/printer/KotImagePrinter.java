/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.printer;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;

/**
 *
 * @author preethi
 */
public class KotImagePrinter {

  public java.util.List<TicketLineConstructor> ticketAllLines;
  Logger kotlogger = Logger.getLogger("KotLog");  
  public  void printKot(final java.util.List<TicketLineConstructor> allLines, String printername )throws PrinterException {
  ticketAllLines = allLines;
  final PrinterJob job = PrinterJob.getPrinterJob();

  Printable contentToPrint = new Printable(){
   @Override
   public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)  {
       Graphics2D g2d = (Graphics2D) graphics;

       g2d.translate(20, 15);
       
       if (pageIndex >0){return NO_SUCH_PAGE;} //Only one page
       
        g2d.setFont(new Font("Verdana", Font.BOLD, 10));
        g2d.drawString(ticketAllLines.get(0).getLine(), 0,0);
         
        g2d.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 9));
         int i =1;
        for( int count = 1 ; count < ticketAllLines.size();count++){
            TicketLineConstructor line = ticketAllLines.get(count);
            g2d.drawString(line.getLine(), 0, 0+(i*12));
            i++;
        }

    return PAGE_EXISTS;

   }

};

      PageFormat pageFormat = new PageFormat();
      pageFormat.setOrientation(PageFormat.PORTRAIT);
      Paper pPaper = pageFormat.getPaper();
      pPaper.setImageableArea(0, 0, pPaper.getWidth() , pPaper.getHeight() -2);
      pageFormat.setPaper(pPaper);
      job.setPrintable(contentToPrint, pageFormat);
      PrintService[] services = PrinterJob.lookupPrintServices();

        System.out.println("services---"+services.length);
    // Retrieve specified print service from the array
        for (PrintService s : services) {
        System.out.println("s.getName()--"+s.getName());
        if (s.getName().equalsIgnoreCase(printername)) {
               System.out.println("s.eneter inside()--"+s.getName());
             //  kotlogger.info("Printer name is :"+s.getName());
//               if(s.getName().equalsIgnoreCase("Generic-text-only"))
//               {
//                   s=null;
//               }
//                try {
                    job.setPrintService(s);
                    job.print();
//                } catch (PrinterException ex) {
//                    Logger.getLogger(KotImagePrinter.class.getName()).log(Level.SEVERE, null, ex);
//                }         
           
          }

        }

    }
    }