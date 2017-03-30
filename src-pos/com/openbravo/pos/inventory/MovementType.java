//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

package com.openbravo.pos.inventory;

import com.openbravo.pos.panels.ComboItemLocal;

/**
 *
 * @author adrianromero
 */
public class MovementType extends ComboItemLocal {
    
    // public static final MovementReason NULL = new MovementReason(null, "");
    public static final MovementType IN_PURCHASE = new MovementType(1, "GoodsReceipts");
    public static final MovementType IN_REFUND = new MovementType(2, "Return");
    public static final MovementType IN_MOVEMENT = new MovementType(4, "MovementIn");
    public static final MovementType OUT_SALE = new MovementType(-1, "Sales");
    public static final MovementType OUT_MOVEMENT = new MovementType(-4, "MovementOut");
    
    
   
    private MovementType(Integer iKey, String sKeyValue) {
        super(iKey, sKeyValue);
    }    
    
    public boolean isInput() {
        return m_iKey.intValue() > 0;
    }

    public Double samesignum(Double d) {
        
        if (d == null || m_iKey == null) {
            return d;
        } else if ((m_iKey.intValue() > 0 && d.doubleValue() < 0.0) ||
            (m_iKey.intValue() < 0 && d.doubleValue() > 0.0)) {
            return new Double(-d.doubleValue());
        } else {
            return d;
        }            
    }    
    
    public Double getPrice(Double dBuyPrice, Double dSellPrice) {
        
        if (this == IN_PURCHASE) {
            return dBuyPrice;
        } else if (this == OUT_SALE || this == IN_REFUND) {
            return dSellPrice;
        } else {
            return null;
        }
    }
}
