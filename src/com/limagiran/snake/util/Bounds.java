package com.limagiran.snake.util;

import java.awt.Rectangle;

/**
 *
 * @author Vinicius Silva
 */
public class Bounds {

    public static final Rectangle RECT_MENU_PLAY;
    public static final Rectangle RECT_MENU_LEVEL;
    public static final Rectangle RECT_MENU_LEVEL_MENOS;
    public static final Rectangle RECT_MENU_LEVEL_MAIS;
    public static final Rectangle RECT_GAME_OVER;
    public static final Rectangle RECT_GAME_OVER_DETALHES;

    static {
        RECT_MENU_PLAY = new Rectangle(175, 175, 450, 100);
        RECT_MENU_LEVEL = new Rectangle(175, 325, 450, 100);
        RECT_MENU_LEVEL_MENOS = new Rectangle(175, 325, 75, 100);
        RECT_MENU_LEVEL_MAIS = new Rectangle(550, 325, 75, 100);
        RECT_GAME_OVER = new Rectangle(175, 225, 450, 100);
        RECT_GAME_OVER_DETALHES = new Rectangle(175, 325, 450, 50);
    }
}
