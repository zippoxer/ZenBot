/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bot;

import client.Client;
import gui.CavebotFrame;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Myzreal
 */
public class StatisticsCalculator extends Thread {

    private static long expirationTime = 600 * 1000;
    private Map<Long, Integer> expGains = new ConcurrentHashMap<>();
    private Map<Long, Integer> goldGains = new ConcurrentHashMap<>();
    private long startTime;

    public StatisticsCalculator() {
        CavebotFrame.getInstance().timerunningLabel.setText("0");
        CavebotFrame.getInstance().xpperhourLabel.setText("0");
        CavebotFrame.getInstance().goldperhourLabel.setText("0");
    }

    @Override
    public void run() {
        try {
            startTime = System.currentTimeMillis();
            while (true) {
                long seconds = (System.currentTimeMillis() - startTime) / 1000;
                long minutes = seconds / 60;
                if (minutes > 0) {
                    seconds %= 60;
                }
                long hours = minutes / 60;
                if (hours > 0) {
                    minutes %= 60;
                }
                StringBuilder s = new StringBuilder(seconds + "s");
                if (minutes > 0 || hours > 0) {
                    s.insert(0, minutes + "m ");
                }
                if (hours > 0) {
                    s.insert(0, hours + "h ");
                }
                CavebotFrame.getInstance().timerunningLabel.setText(s.toString());

                long expPerHour = (3600 * 1000) / expirationTime * sum(expGains);
                CavebotFrame.getInstance().xpperhourLabel.setText(String.valueOf(expPerHour));

                long goldPerHour = (3600 * 1000) / expirationTime * sum(goldGains);
                CavebotFrame.getInstance().goldperhourLabel.setText(String.valueOf(goldPerHour));
                sleep(10);
            }
        } catch (InterruptedException ex) {
        }
    }

    public void addExp(int n) {
        add(expGains, n);
    }

    public void addGold(int n) {
        add(goldGains, n);
    }

    private void add(Map<Long, Integer> gains, int n) {
        long time = System.currentTimeMillis();
        Integer gain = gains.get(time);
        if (gain == null) {
            gains.put(time, n);
        } else {
            gains.put(time, gain + n);
        }
    }

    private int sum(Map<Long, Integer> gains) {
        int sum = 0;
        synchronized (gains) {
            for (Map.Entry<Long, Integer> gain : gains.entrySet()) {
                if (System.currentTimeMillis() - gain.getKey() < expirationTime) {
                    sum += gain.getValue();
                } else {
                    // expired
                    gains.remove(gain.getKey());
                }
            }
        }
        return sum;
    }
}
