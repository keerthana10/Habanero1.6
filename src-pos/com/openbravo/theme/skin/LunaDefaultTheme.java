/*
 * Copyright 2005 MH-Software-Entwicklung. All rights reserved.
 * Use is subject to license terms.
 */
package com.openbravo.theme.skin;

import javax.swing.plaf.*;
import java.awt.*;

import com.openbravo.theme.*;

public class LunaDefaultTheme extends AbstractTheme {

    public LunaDefaultTheme() {
        super();
        // Setup theme with defaults
        setUpColor();
        // Overwrite defaults with user props
        loadProperties();
        // Setup the color arrays
        setUpColorArrs();
    }

    public String getPropertyFileName() {
        return "LunaTheme.properties";
    }

    public void setUpColor() {
        super.setUpColor();

        // Defaults for LunaLookAndFeel
        //**** CODE FOR CHANGING BACKGROUND OF SCREEN ******
         backgroundColor = new ColorUIResource(255, 255, 255);
        backgroundColorLight = new ColorUIResource(255, 255, 255);
        backgroundColorDark = new ColorUIResource(255, 255, 255);
        alterBackgroundColor = new ColorUIResource(255, 255, 255);

        //CODE FOR CHANGING TEXT COLOR IN FOREGROUND IN COLUMN HEADER OF ITEM FRAME
        selectionForegroundColor = black;

        //CODE FOR CHANING FOREGROUND COLOR OF CATEGORY - PRODUTS
        selectionBackgroundColor = new ColorUIResource(194, 208, 243);//new ColorUIResource(200, 210, 240);

        //CODE FOR BUTTONS OUTLINE COLOR
        frameColor = new ColorUIResource(189, 189, 189); // new ColorUIResource(0, 60, 116);
        focusCellColor = new ColorUIResource(189, 189, 189);

        buttonBackgroundColor = new ColorUIResource(236, 233, 216);
        buttonColorLight =  new ColorUIResource(232, 232, 232);
        buttonColorDark = new ColorUIResource(214, 208, 197);

        rolloverColor = lightOrange;

//CODE FOR  LEFT FRAMES MAIN, ADMIN, SYSTEM FRAMESCONTROL BACKGROUND 54-57

        controlForegroundColor = black;
        controlBackgroundColor = new ColorUIResource(255, 255, 255);
        controlColorLight = white;
        controlColorDark = new ColorUIResource(255, 255, 255);
//CODE FOR WINDOW TITLE FORGEGROUND AND BACKGROUND 59-63
        windowTitleForegroundColor = white;
        windowTitleBackgroundColor = new ColorUIResource(184, 183, 181); //new ColorUIResource(139, 185, 254);
        windowTitleColorLight = new ColorUIResource(184, 183, 181);
        windowTitleColorDark = new ColorUIResource(184, 183, 181);
        windowBorderColor = new ColorUIResource(184, 183, 181);

        windowInactiveTitleForegroundColor = white;
        windowInactiveTitleBackgroundColor = new ColorUIResource(184, 183, 181); // new ColorUIResource(141, 186, 253);
        windowInactiveTitleColorLight = new ColorUIResource(184, 183, 181);
        windowInactiveTitleColorDark = new ColorUIResource(184, 183, 181);
        windowInactiveBorderColor = new ColorUIResource(184, 183, 181);

        menuBackgroundColor = backgroundColor;
        menuSelectionForegroundColor = white;
        menuSelectionBackgroundColor = new ColorUIResource(184, 183, 181);
        menuColorLight = new ColorUIResource(184, 183, 181);
        menuColorDark = backgroundColor;

        toolbarBackgroundColor = backgroundColor;
        toolbarColorLight = menuColorLight;
        toolbarColorDark = backgroundColor;

        tabAreaBackgroundColor = backgroundColor;
        desktopColor = backgroundColor;
    }

    public void setUpColorArrs() {
        super.setUpColorArrs();

        // Generate the color arrays
        DEFAULT_COLORS = ColorHelper.createColorArr(controlColorLight, controlColorDark, 20);
        HIDEFAULT_COLORS = ColorHelper.createColorArr(ColorHelper.brighter(controlColorLight, 90), ColorHelper.brighter(controlColorDark, 30), 20);

        ACTIVE_COLORS = DEFAULT_COLORS;
        INACTIVE_COLORS = ColorHelper.createColorArr(new Color(248, 247, 241), backgroundColor, 20);

        ROLLOVER_COLORS = ColorHelper.createColorArr(ColorHelper.brighter(controlColorLight, 30), ColorHelper.brighter(controlColorDark, 20), 30);
        SELECTED_COLORS = DEFAULT_COLORS;
        PRESSED_COLORS = ColorHelper.createColorArr(controlColorDark, controlColorLight, 20);
        DISABLED_COLORS = ColorHelper.createColorArr(Color.white, Color.lightGray, 20);

        // Generate the color arrays
        Color topHi = windowTitleColorLight;
        Color topLo = ColorHelper.darker(windowTitleColorLight, 10);//new Color(81, 150, 253);
        Color bottomHi = ColorHelper.brighter(windowTitleColorDark, 15);//new Color(3, 101, 241);
        Color bottomLo = windowTitleColorDark;

        WINDOW_TITLE_COLORS = new Color[20];
        Color[] topColors = ColorHelper.createColorArr(topHi, topLo, 8);
        for (int i = 0; i < 8; i++) {
            WINDOW_TITLE_COLORS[i] = topColors[i];
        }
        Color[] bottomColors = ColorHelper.createColorArr(bottomHi, bottomLo, 12);
        for (int i = 0; i < 12; i++) {
            WINDOW_TITLE_COLORS[i + 8] = bottomColors[i];
        }

        WINDOW_INACTIVE_TITLE_COLORS = new Color[WINDOW_TITLE_COLORS.length];
        for (int i = 0; i < WINDOW_INACTIVE_TITLE_COLORS.length; i++) {
            WINDOW_INACTIVE_TITLE_COLORS[i] = ColorHelper.brighter(WINDOW_TITLE_COLORS[i], 20);
        }

        MENUBAR_COLORS = ColorHelper.createColorArr(menuColorLight, menuColorDark, 20);
        TOOLBAR_COLORS = ColorHelper.createColorArr(toolbarColorLight, toolbarColorDark, 20);
 
               // CODE FOR BUTTON COLORS - ENTIRE POS
//        BUTTON_COLORS = new Color[]{
//                    new Color(174, 167, 159),
//                    new Color(174, 167, 159),
//                    new Color(174, 167, 159),
//                    new Color(174, 167, 159),
//                    new Color(174, 167, 159),
//                    new Color(174, 167, 159),
//                    new Color(174, 167, 159),
//                    new Color(174, 167, 159),
//                    new Color(174, 167, 159),
//                    new Color(174, 167, 159),
//                    new Color(174, 167, 159),
//                    new Color(174, 167, 159),
//                    new Color(174, 167, 159),
//                    new Color(174, 167, 159),
//                    new Color(174, 167, 159),
//                    new Color(174, 167, 159),
//                    new Color(174, 167, 159),
//                    new Color(174, 167, 159),};
        BUTTON_COLORS = new Color[]{
                    new Color(240, 240, 240),
                    new Color(240, 240, 240),
                    new Color(240, 240, 240),
                    new Color(240, 240, 240),
                   new Color(240, 240, 240),
                    new Color(240, 240, 240),
                    new Color(240, 240, 240),
                    new Color(240, 240, 240),
                    new Color(240, 240, 240),
                   new Color(240, 240, 240),
                    new Color(240, 240, 240),
                    new Color(240, 240, 240),
                    new Color(240, 240, 240),
                    new Color(240, 240, 240),
                    new Color(240, 240, 240),
                    new Color(240, 240, 240),
                    new Color(240, 240, 240),
                    new Color(240, 240, 240),};

        //CODE - CHANGING TAB COLORS AND COLUMN HEADER, THUMB COLORS 
        TAB_COLORS = ColorHelper.createColorArr(Color.white, new Color(236, 235, 230), 20);
        COL_HEADER_COLORS = TAB_COLORS;
        TRACK_COLORS = ColorHelper.createColorArr(new Color(243, 241, 236), new Color(254, 254, 251), 20);
        THUMB_COLORS = ColorHelper.createColorArr(new Color(218, 230, 254), new Color(180, 197, 240), 20);
        //SLIDER_COLORS = ColorHelper.createColorArr(new Color(218, 230, 254), new Color(180, 197, 240), 20);
        SLIDER_COLORS = THUMB_COLORS;//ColorHelper.createColorArr(new Color(243, 241, 236), new Color(254, 254, 251), 20);
        PROGRESSBAR_COLORS = THUMB_COLORS;
    }
}
