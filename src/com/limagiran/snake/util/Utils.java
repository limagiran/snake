package com.limagiran.snake.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import static java.awt.RenderingHints.*;

/**
 *
 * @author Vinicius Silva
 */
public class Utils {

    /**
     * Gera um número aleatório
     *
     * @param x valor máximo a ser retornado
     * @return um número aleatório entre 1 e o limite informado. Retorna -1 para
     * valores menores do que 1.
     */
    public static int random(int x) {
        return ((x < 1) ? -1 : ((int) ((Math.random() * 100000) % (x)) + 1));
    }

    /**
     * Thread.sleep();
     *
     * @param time tempo em espera
     */
    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception ignore) {
        }
    }

    /**
     * Pintar uma borda sombreada no pixel
     *
     * @param x início da área na coordenada x
     * @param y início da área na coordenada y
     * @param scale tamanho da área
     * @param color cor da borda
     * @param g contexto gráfico
     */
    public static void addBorder(int x, int y, int scale,
            Color color, Graphics2D g) {
        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(color.darker());
        g.drawLine(++x, ++y, (x + scale - 3), y);
        g.drawLine(x, y, x, (y + scale - 3));
        g.setColor(color.darker().darker());
        x += (scale - 2);
        y += (scale - 2);
        g.drawLine(x, y, (x - scale + 2), y);
        g.drawLine(x, y, x, (y - scale + 2));
    }

    /**
     * DrawRect
     *
     * @param r Rectangle
     * @param g Graphics2D
     * @see Graphics2D#drawRect(int, int, int, int)
     */
    public static void drawRect(Rectangle r, Graphics2D g) {
        g.drawRect(r.x, r.y, r.width, r.height);
    }

    /**
     * FillRect
     *
     * @param r Rectangle
     * @param g Graphics2D
     * @see Graphics2D#fillRect(int, int, int, int)
     */
    public static void fillRect(Rectangle r, Graphics2D g) {
        g.fillRect(r.x, r.y, r.width, r.height);
    }

    /**
     * Configura a renderização padrão para o contexto gráfico
     *
     * @param g contexto gráfico
     */
    public static void configGraphics(Graphics2D g) {
        g.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        g.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY);
        g.setRenderingHint(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_QUALITY);
    }

    /**
     * Desenha o texto centralizado em um retângulo, em um contexto Graphics2D
     *
     * @param t texto a ser desenhado
     * @param r área em objeto retângulo
     * @param f fonte do texto
     * @param g Graphics2D
     */
    public static void drawStringCenter(String t, Rectangle r, Font f, Graphics2D g) {
        int w = r.x + (r.width - g.getFontMetrics(f).stringWidth(t)) / 2;
        int h = r.y + g.getFontMetrics(f).getAscent();
        h -= g.getFontMetrics(f).getDescent();
        h += (r.height - f.getSize()) / 2;
        g.drawString(t, w, h);
    }

}
