package no.nordicsemi.android.blinky;

import android.util.Log;

import java.util.Random;

public class RandomThread implements Runnable {
    byte[] list_Random = {2, 5, 8, 10, 0};
    RandomEventListener listener;

    public void setListener(RandomEventListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        while (true) {

            try {
                if (this.listener != null) {
                    this.listener.onSpeed(getRandomValue());
                }
                long duration = getRandomSecond();
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private long getRandomSecond() {
        int max = 5000;
        int min = 200;
        Random r = new Random();
        return (long) r.nextInt((max - min) + 1) + min;
    }

    private byte getRandomValue() {
        int max = list_Random.length;
        int min = 0;
        Random r = new Random();
        int index = r.nextInt(max - min) + min;
        return list_Random[index];
    }

}
