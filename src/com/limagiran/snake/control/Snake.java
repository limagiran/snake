package com.limagiran.snake.control;

import com.limagiran.snake.util.Utils;
import com.limagiran.snake.util.Valores;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import static com.limagiran.snake.view.GamePlay.*;

/**
 *
 * @author Vinicius
 */
public class Snake {

    private final List<Vector2D> body = new ArrayList<>();
    private Vector2D lastBody;

    /**
     * Cria um novo objeto Cobra
     *
     * @param size tamanho inicial da cobra.
     */
    public Snake(int size) {
        for (int i = size - 1; i > 0; i--) {
            body.add(new Vector2D(i, 0));
        }
        body.add(new Vector2D(0, 0));
        lastBody = body.get(body.size() - 1);
    }

    /**
     * Retorna a lista de coordenadas onde cada pedaço do body da cobra está
     *
     * @return Lista de Vector2D
     */
    public List<Vector2D> getCorpo() {
        return body;
    }

    /**
     * Movimenta a cobra
     *
     * @param direction direção no formato (x, y) para movimentar a cobra.
     * <br>Ex.: (1, 0) direita, (-1, 0) esquerda, ...
     *
     * @return <i>true</i> para movimento realizado. <i>false</i> para colisão.
     */
    public boolean move(Vector2D direction) {
        Vector2D temp;
        if (!body.isEmpty()) {
            lastBody = body.get(0).clone();
            body.get(0).addMe(direction);
            for (int i = 1; i < body.size(); i++) {
                temp = body.get(i).clone();
                body.get(i).set(lastBody.getX(), lastBody.getY());
                lastBody.set(temp.getX(), temp.getY());
            }

            body.stream().forEach((v) -> {
                int x = (int) v.getX(), y = (int) v.getY();
                v.set(((x < 0) ? (width - (Math.abs(x) % width)) : x) % width,
                        ((y < 0) ? (height - (Math.abs(y) % height)) : y) % height);
            });
        }
        return isCollision();
    }

    /**
     * Pinta a cobra
     *
     * @param g Graphics2D
     */
    public void paint(Graphics2D g) {
        body.stream().forEach((v) -> {
            g.setColor(Valores.COLOR_SNAKE);
            int x = ((int) v.getX() * SCALE_PIXEL) + 2;
            int y = (((int) v.getY() + 1) * SCALE_PIXEL) + 2;
            g.fillRect(x, y, SCALE_PIXEL - 4, SCALE_PIXEL - 4);
            Utils.addBorder(x, y, (SCALE_PIXEL - 4), Valores.COLOR_SNAKE, g);
        });
    }

    /**
     * Aumenta o body da cobra
     *
     * @return <i>true</i> para body aumentado. <i>false</i> caso não haja
     * espaço na tela para aumentar o body.
     */
    public boolean growUp() {
        body.add(lastBody);
        return (body.size() != (width * height));
    }

    /**
     * Verifica se houve colisão no body da cobra
     *
     * @return <i>true</i> para colisão. <i>false</i> o contrário.
     */
    private boolean isCollision() {
        for (int i = 1; i < body.size(); i++) {
            if (body.get(i).equals(body.get(0))) {
                body.get(i).set(lastBody.getX(), lastBody.getY());
                return false;
            }
        }
        return true;
    }
}
