package com.limagiran.snake.control;

/**
 *
 * @author Vinicius
 */
public class MainLoop implements Runnable {

    public static final int DEFAULT_UPS = 80;
    public static final int DEFAULT_NO_DELAYS_PER_YIELD = 16;
    public static final int DEFAULT_MAX_FRAME_SKIPS = 5;

    private LoopSteps game;
    private long desiredUpdateTime;
    private boolean running;

    private long afterTime;
    private long beforeTime = System.currentTimeMillis();

    private long overSleepTime = 0;
    private long excessTime = 0;

    private int noDelaysPerYield = DEFAULT_NO_DELAYS_PER_YIELD;
    private int maxFrameSkips = DEFAULT_MAX_FRAME_SKIPS;

    int noDelays = 0;

    public MainLoop(LoopSteps loopSteps, int ups, int maxFrameSkips, int noDelaysPerYield) {
        super();
        setUps(ups);
        game = loopSteps;
        running = true;
        this.maxFrameSkips = maxFrameSkips;
        this.noDelaysPerYield = noDelaysPerYield;
    }

    public MainLoop(LoopSteps loopSteps, int ups) {
        this(loopSteps, ups, DEFAULT_MAX_FRAME_SKIPS, DEFAULT_NO_DELAYS_PER_YIELD);
    }

    public MainLoop(LoopSteps loopSteps) {
        this(loopSteps, DEFAULT_UPS);
    }

    private void sleep(long nanos) {
        try {
            noDelays = 0;
            long beforeSleep = System.nanoTime();
            Thread.sleep(nanos / 1000000L, (int) (nanos % 1000000L));
            overSleepTime = System.nanoTime() - beforeSleep - nanos;
        } catch (Exception e) {
        }
    }

    private void yieldIfNeed() {
        if (++noDelays == noDelaysPerYield) {
            Thread.yield();
            noDelays = 0;
        }
    }

    private long calculateSleepTime() {
        return desiredUpdateTime - (afterTime - beforeTime) - overSleepTime;
    }

    @Override
    public void run() {
        running = true;
        try {
            game.setup();
            while (running) {
                beforeTime = System.nanoTime();
                skipFramesInExcessTime();

                // Updates, renders and paint the screen
                game.processLogics();
                game.renderGraphics();
                game.paintScreen();

                afterTime = System.nanoTime();
                long sleepTime = calculateSleepTime();
                if (sleepTime >= 0) {
                    sleep(sleepTime);
                } else {
                    excessTime -= sleepTime; // Sleep time is negative
                    overSleepTime = 0L;
                    yieldIfNeed();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            running = false;
            game.tearDown();
            System.exit(0);
        }
    }

    private void skipFramesInExcessTime() {
        int skips = 0;
        while ((excessTime > desiredUpdateTime) && (skips < maxFrameSkips)) {
            excessTime -= desiredUpdateTime;
            game.processLogics();
            skips++;
        }
    }

    public void stop() {
        running = false;
    }
    
    public final void setUps(int ups){
        ups = ((ups < 1) ? 1 : (ups > 1000) ? 1000 : ups);
        desiredUpdateTime = 1000000000L / ups;
    }
}
