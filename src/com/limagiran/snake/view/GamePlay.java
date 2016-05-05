package com.limagiran.snake.view;

import com.limagiran.snake.control.Snake;
import com.limagiran.snake.control.MainLoop;
import com.limagiran.snake.control.LoopSteps;
import com.limagiran.snake.control.Vector2D;
import com.limagiran.snake.util.Images;
import com.limagiran.snake.util.RWObj;
import static com.limagiran.snake.util.Bounds.*;
import static com.limagiran.snake.util.Utils.*;
import static com.limagiran.snake.util.Valores.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Vinicius Silva
 */
public final class GamePlay extends JFrame implements LoopSteps {

    private static GamePlay instance;
    public static final Dimension SCREEN = new Dimension(800, 600);
    public static final Rectangle GRAPHIC_CONTEXT
            = new Rectangle(0, 0, SCREEN.width, SCREEN.height);
    public static final int SCALE_PIXEL = 25;
    public static final int SIZE_HEADER = SCALE_PIXEL;
    public static final int MAX_LEVEL = 10;
    public static final int SIZE_INITIAL_BODY_SNAKE = 5;
    public static int width;
    public static int height;
    public static final int STATUS_MENU = 0;
    public static final int STATUS_PLAY = 1;
    public static final int STATUS_GAME_OVER = 3;

    private final MainLoop loop = new MainLoop(this, 15);
    private Snake snake;
    private Vector2D food;
    private final List<Vector2D> moves = new ArrayList<>();
    private final Vector2D up = new Vector2D(0, -1);
    private final Vector2D down = new Vector2D(0, 1);
    private final Vector2D right = new Vector2D(1, 0);
    private final Vector2D left = new Vector2D(-1, 0);
    private Vector2D move = right;
    private int GAME_STATUS = STATUS_MENU;
    private int level = 1;
    private int initialLevel = level;
    private long points = 0;
    private long record = getRecorde();
    private final List<Snake> animation = new ArrayList<>();
    private final List<Vector2D> animationMoves = new ArrayList<>();

    /**
     * Cria e exibe uma janela do jogo
     */
    public static void main() {
        if (instance == null) {
            SwingUtilities.invokeLater(() -> {
                (instance = new GamePlay()).setVisible(true);
            });
        }
    }

    /**
     * Nova Instância do jogo
     */
    private GamePlay() {
        init();
    }

    /**
     * Iniciar e configurar os componentes
     */
    public void init() {
        setIgnoreRepaint(true);
        insertListeners();
        setResizable(false);
        setTitle("Snake");
        setIconImage(Images.ICON.getImage());
        pack();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                RWObj.gravarObjeto(record, FILE_RECORD, KEY);
                loop.stop();
            }
        });
        setSize(GRAPHIC_CONTEXT.width + getInsets().left + getInsets().right,
                GRAPHIC_CONTEXT.height + getInsets().top + getInsets().bottom);
        setPreferredSize(getSize());
        setLocationRelativeTo(null);
        GRAPHIC_CONTEXT.setBounds(getInsets().left, getInsets().top, SCREEN.width, SCREEN.height);
        width = GRAPHIC_CONTEXT.width / SCALE_PIXEL;
        height = (GRAPHIC_CONTEXT.height - SIZE_HEADER) / SCALE_PIXEL;
        for (int i = 0; i < 15; i++) {
            animation.add(new Snake(8));
            animationMoves.add(right);
        }
        new Thread(loop).start();
    }

    /**
     * Efetuar o processo lógico do game
     */
    @Override
    public void processLogics() {
        switch (GAME_STATUS) {
            case STATUS_PLAY:
                processGame();
                break;
            case STATUS_MENU:
                processMenu();
                break;
        }
    }

    /**
     * Pintar o Graphics2D para exibição na tela
     */
    @Override
    public void renderGraphics() {
        Graphics2D g2 = (Graphics2D) getBufferStrategy().getDrawGraphics()
                .create(GRAPHIC_CONTEXT.x, GRAPHIC_CONTEXT.y,
                        GRAPHIC_CONTEXT.width, GRAPHIC_CONTEXT.height);
        configGraphics(g2);
        g2.setColor(COLOR_BACKGROUND);
        g2.fillRect(0, 0, getWidth(), getHeight());
        paintHeader(g2);
        switch (GAME_STATUS) {
            case STATUS_PLAY:
                snake.paint(g2);
                paintFood(g2);
                break;
            case STATUS_MENU:
                paintMenu(g2);
                break;
            case STATUS_GAME_OVER:
                paintGameOver(g2);
                break;
        }
        g2.dispose();
    }

    /**
     * Pintar a tela
     */
    @Override
    public void paintScreen() {
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }

    /**
     * Configura os listeners de evento para o programa
     */
    public void insertListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                p.y -= getInsets().top;
                p.x -= getInsets().left;
                if (GAME_STATUS == STATUS_MENU) {
                    if (RECT_MENU_PLAY.contains(p)) {
                        play();
                    } else if (RECT_MENU_LEVEL_MENOS.contains(p)) {
                        setLevel(level - 1);
                    } else if (RECT_MENU_LEVEL_MAIS.contains(p)) {
                        setLevel(level + 1);
                    }
                }
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        moves.add(up);
                        break;
                    case KeyEvent.VK_DOWN:
                        moves.add(down);
                        break;
                    case KeyEvent.VK_LEFT:
                        moves.add(left);
                        break;
                    case KeyEvent.VK_RIGHT:
                        moves.add(right);
                        break;
                }
            }
        });
    }

    /**
     * Configuração inicial da partida
     */
    @Override
    public void setup() {
        createBufferStrategy(2);
    }

    /**
     * Finalizar partida
     */
    @Override
    public void tearDown() {
        //ignore
    }

    /**
     * Captura o próximo movimento válido na lista de movimentos realizados pelo
     * jogador
     *
     * @return Vector2D com o próximo movimento válido. Retorna o último
     * movimento realizado caso não haja movimento válido.
     */
    private Vector2D takeNextLegalMove() {
        while (!moves.isEmpty()) {
            Vector2D m = moves.remove(0);
            if (validateMove(m, move)) {
                return m;
            }
        }
        return move;
    }

    /**
     * Inicia uma partida
     */
    private void play() {
        GAME_STATUS = STATUS_PLAY;
        snake = new Snake(SIZE_INITIAL_BODY_SNAKE);
        generateFood();
        moves.clear();
        move = right;
        points = 0L;
        loop.setUps(level * 2);
        initialLevel = level;
    }

    /**
     * Gera um local aleatório na tela para a comida
     */
    private void generateFood() {
        food = new Vector2D(random(width) - 1, random(height) - 1);
        while (snake.getCorpo().stream().anyMatch((v) -> (v.equals(food)))) {
            food = new Vector2D(random(width) - 1, random(height) - 1);
        }
    }

    /**
     * Exibe a tela de GameOver
     */
    private void gameOver() {
        GAME_STATUS = STATUS_GAME_OVER;
        loop.setUps(15);
        new Thread(() -> {
            record = ((points > record) ? points : record);
            sleep(2000);
            GAME_STATUS = STATUS_MENU;
        }).start();
    }

    /**
     * Retorna o record salvo
     *
     * @return record
     */
    private long getRecorde() {
        Long _return = RWObj.lerObjeto(FILE_RECORD, Long.class, KEY);
        return ((_return == null) ? 0L : _return);
    }

    /**
     * Processamento lógico do menu principal
     */
    private void processMenu() {
        Vector2D[] movs = new Vector2D[]{up, down, left, right};
        for (int i = 0; i < animation.size(); i++) {
            if ((random(12) % 12) == 0) {
                Vector2D m = movs[random(movs.length) - 1];
                if (validateMove(m, animationMoves.get(i))) {
                    animation.get(i).move(m);
                    animationMoves.set(i, m);
                }
            } else {
                animation.get(i).move(animationMoves.get(i));
            }
        }
    }

    /**
     * Processamento lógico do jogo
     */
    private void processGame() {
        move = takeNextLegalMove();
        if (!snake.move(move)) {
            gameOver();
        } else if (snake.getCorpo().get(0).equals(food)) {
            if (snake.growUp()) {
                points += level;
                if (snake.getCorpo().size()
                        % (width * height / (MAX_LEVEL + 1 - initialLevel)) == 0) {
                    setLevel(level + 1);
                    loop.setUps(level * 2);
                }
                generateFood();
            } else {
                gameOver();
            }
        }
    }

    /**
     * Pinta o cabeçalho com as informações do jogo
     *
     * @param g2 Graphics2D
     */
    private void paintHeader(Graphics2D g2) {
        g2.setFont(FONT_HEADER);
        g2.setColor(COLOR_FOREGROUND_HEADER);
        String str = String.format("Snake by Lima Giran --- Level: %s - "
                + "Pontuação: %s - Recorde: %s", level, points, record);
        int w = g2.getFontMetrics(FONT_HEADER).stringWidth(str);
        g2.drawString(str, ((GRAPHIC_CONTEXT.width - w) / 2), (SIZE_HEADER - 5));
    }

    /**
     * Pintar a tela de menu
     *
     * @param g Graphics2D
     */
    private void paintMenu(Graphics2D g) {
        paintMenuAnimation(g);
        Stroke stroke = g.getStroke();
        g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(COLOR_BACKGROUND);
        fillRect(RECT_MENU_PLAY, g);
        fillRect(RECT_MENU_LEVEL, g);
        g.setColor(COLOR_MENU);
        drawRect(RECT_MENU_PLAY, g);
        drawRect(RECT_MENU_LEVEL, g);
        g.setStroke(stroke);
        g.setFont(FONTE_MENU);
        drawStringCenter("Jogar", RECT_MENU_PLAY, FONTE_MENU, g);
        drawStringCenter(("Level " + level), RECT_MENU_LEVEL, FONTE_MENU, g);
        drawStringCenter("-", RECT_MENU_LEVEL_MENOS, FONTE_MENU, g);
        drawStringCenter("+", RECT_MENU_LEVEL_MAIS, FONTE_MENU, g);
    }

    /**
     * Pintar a animação de fundo
     *
     * @param g2 Graphics2D
     */
    private void paintMenuAnimation(Graphics2D g2) {
        animation.stream().forEach((c) -> {
            c.getCorpo().stream().forEach((v) -> {
                g2.setColor(COLOR_SNAKE);
                g2.fillOval(((int) v.getX() * SCALE_PIXEL) + 8,
                        (((int) v.getY() + 1) * SCALE_PIXEL) + 8,
                        SCALE_PIXEL - 16, SCALE_PIXEL - 16);
            });
        });
    }

    /**
     * Pinta a comida na tela
     *
     * @param g2 Graphics2D
     */
    private void paintFood(Graphics2D g2) {
        g2.setColor(COLOR_FOOD);
        int x = ((int) food.getX() * SCALE_PIXEL) + 1;
        int y = (((int) food.getY() + 1) * SCALE_PIXEL) + 1;
        g2.fillRect(x, y, SCALE_PIXEL - 2, SCALE_PIXEL - 2);
        addBorder(x, y, (SCALE_PIXEL - 2), COLOR_FOOD, g2);
    }

    private void paintGameOver(Graphics2D g2) {
        String str = "GAME OVER";
        String str2 = "PONTUAÇÃO: " + points;
        g2.setFont(FONTE_MENU);
        drawStringCenter(str, RECT_GAME_OVER, FONTE_MENU, g2);
        g2.setFont(FONT_HEADER);
        drawStringCenter(str2, RECT_GAME_OVER_DETALHES, FONT_HEADER, g2);
    }

    private boolean validateMove(Vector2D atual, Vector2D anterior) {
        return (((atual.equals(up) || atual.equals(down))
                && !(anterior.equals(up) || anterior.equals(down)))
                || ((atual.equals(left) || atual.equals(right))
                && !(anterior.equals(left) || anterior.equals(right))));
    }

    private void setLevel(int l) {
        level = ((l < 1) ? 1 : ((l > MAX_LEVEL) ? MAX_LEVEL : l));
    }
}
