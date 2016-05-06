package com.limagiran.snake.util;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import static java.io.File.separator;

/**
 *
 * @author Vinicius
 */
public class Valores {
    public static final String KEY = "#k3y$snk#";
    public static final Color COLOR_SNAKE = Color.WHITE;
    public static final Color COLOR_FOOD = Color.WHITE.darker();
    public static final Color COLOR_BACKGROUND = Color.BLACK;
    public static final Color COLOR_MENU = Color.WHITE;
    public static final Color COLOR_BACKGROUND_HEADER = Color.BLACK;
    public static final Color COLOR_FOREGROUND_HEADER = Color.GRAY.brighter();   
    
    public static final Font FONT_HEADER = new Font("Modern", Font.PLAIN, 18);
    public static final Font FONTE_MENU = new Font("Modern", Font.PLAIN, 80);
    
    public static final File FILE_RECORD;
    
    static {
        FILE_RECORD = new File(System.getProperty("user.home")
                + separator + "Snake by Lima Giran" + separator + "record.snk");
        if (FILE_RECORD.getParentFile() != null) {
            FILE_RECORD.getParentFile().mkdirs();
        }
    }
}
