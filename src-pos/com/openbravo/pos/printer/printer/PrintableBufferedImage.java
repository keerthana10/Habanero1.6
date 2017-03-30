package com.openbravo.pos.printer.printer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Hashtable;

public class PrintableBufferedImage extends BufferedImage implements Printable{

    public PrintableBufferedImage(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    public PrintableBufferedImage(int arg0, int arg1, int arg2,
            IndexColorModel arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    public PrintableBufferedImage(ColorModel arg0, WritableRaster arg1,
            boolean arg2, Hashtable arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    public int print(Graphics g, PageFormat pf, int p) throws PrinterException {
        if (p == 0) {
            // resize and position the image
            double x = pf.getImageableX();
            double y = pf.getImageableY();
            double h = (double)getHeight();
            double w = (double)getWidth();

            double scalingFactor = Math.min(pf.getImageableHeight() / h, pf.getImageableWidth() / w)*0.99;

            x = (pf.getWidth() - (w * scalingFactor))/2;
            y = (pf.getHeight() - (h *scalingFactor))/2;

            Graphics2D g2d = (Graphics2D)g;

            g2d.translate(x,y);
            g2d.scale(scalingFactor, scalingFactor);
            g2d.drawImage(this, 0,0, null);
            return PAGE_EXISTS;
        }
        return NO_SUCH_PAGE;
    }
}