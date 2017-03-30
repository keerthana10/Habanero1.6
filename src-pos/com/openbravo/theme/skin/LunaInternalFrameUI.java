/*
 * Copyright 2005 MH-Software-Entwicklung. All rights reserved.
 * Use is subject to license terms.
 */
package com.openbravo.theme.skin;

import javax.swing.*;
import javax.swing.plaf.*;

import com.openbravo.theme.*;

/**
 * @author Michael Hagen
 */
public class LunaInternalFrameUI extends BaseInternalFrameUI {

    public LunaInternalFrameUI(JInternalFrame b) {
        super(b);
    }

    public static ComponentUI createUI(JComponent c) {
        return new LunaInternalFrameUI((JInternalFrame) c);
    }

    protected JComponent createNorthPane(JInternalFrame w) {
        titlePane = new LunaInternalFrameTitlePane(w);
        return titlePane;
    }
}

