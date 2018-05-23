/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.Random;

/**
 *
 * @author Myzreal
 */
public class FoodEater extends Thread {

    @Override
    public void run() {
        try {
            while (true) {
                containerloop:
                for (Container container : Client.getInstance().getOpenContainers()) {
                    for (Item item : container.items) {
                        if (Item.isFood(item.id)) {
                            item.use();
                            break containerloop;
                        }
                    }
                }
                sleep(new Random().nextInt(5000) + 16000);
            }
        } catch (Exception e) {
        }
    }
}