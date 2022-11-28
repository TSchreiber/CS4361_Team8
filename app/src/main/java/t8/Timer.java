package t8;

import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.application.Platform;

public class Timer {

    private long time;
    private long startTime;
    private long offset;
    private SimpleStringProperty sspTime;
    boolean running = false;
    boolean paused = false;

    public Timer() {
        sspTime = new SimpleStringProperty("00:00:000");
    }

    public void startTimer() {
        this.time = System.currentTimeMillis();
        this.offset = 0;
        this.startTime = this.time;
        running = true;
        setPaused(false);
        new Thread( () -> {
            while (running) {
                try {
                    Thread.currentThread();
                    Thread.sleep(50);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (!isPaused()) {
                    updateTime();
                }
            }
        }).start();
    }

    public void stopTimer() {
        running = false;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        // going from unpaused to paused
        if (!isPaused() && paused) {
            this.offset += System.currentTimeMillis() - startTime;
        } 
        // going from paused to unpaused
        else if (isPaused() && !paused) {
            this.startTime = System.currentTimeMillis();
        }
        this.paused = paused;
    }

    public void pause() {
        setPaused(true);
    }

    public void unpause() {
        setPaused(false);
    }

    public void updateTime() {
        if (Platform.isFxApplicationThread()) {
            this.time = System.currentTimeMillis();
            long dt = time - startTime + offset;
            sspTime.set(String.format("%02d:%02d:%03d", dt/60_000, (dt / 1000) % 60, dt % 1000));
        } else {
            Platform.runLater(() -> updateTime());
        }
    }

    public void resetTimer() {
        this.startTime = System.currentTimeMillis();
        this.offset = 0;
        this.running = true;
        this.paused = false;
    }

    public long getTime() {
        return this.time - this.startTime + offset;
    }

    public SimpleStringProperty getSspTime() {
        return sspTime;
    }
}

