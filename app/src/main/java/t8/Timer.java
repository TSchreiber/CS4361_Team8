package t8;

import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.application.Platform;

public class Timer {

    private long time;
    private long startTime;
    private SimpleStringProperty sspTime;
    boolean running = false;

    public Timer() {
        sspTime = new SimpleStringProperty("00:00:000");
    }

    public void startTimer() {
        this.time = System.currentTimeMillis();
        this.startTime = this.time;
        running = true;
        new Thread( () -> {
            while (running) {
                try {
                    Thread.currentThread();
                    Thread.sleep(50);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                updateTime();
            }
        }).start();
    }

    public void stopTimer() {
        running = false;
    }

    public void updateTime() {
        if (Platform.isFxApplicationThread()) {
            this.time = System.currentTimeMillis();
            long dt = time - startTime;
            sspTime.set(String.format("%02d:%02d:%03d", dt/60_000, (dt / 1000) % 60, dt % 1000));
        } else {
            Platform.runLater(() -> updateTime());
        }
    }

    public void resetTimer() {
        this.startTime = System.currentTimeMillis();
    }

    public long getTime() {
        return this.time - this.startTime;
    }

    public SimpleStringProperty getSspTime() {
        return sspTime;
    }
}

