/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import gui.CreaturesFrame;
import java.io.IOException;
import packets.RequestCreatureFlagUpdatePacket;
import structures.Creature;
import structures.Directions;
import structures.Loc;

/**
 * Entity is just a simple combination of Creature and Loc. This class makes it
 * possible to store a Creature and it's location in one array.
 *
 * @author Myzreal
 */
public class Entity extends Creature {

    public Loc location;

    public Entity(Loc location, Creature c) {
        update(location, c);
    }

    public void update(Loc location, Creature c) {
        this.location = location;
        id = c.id;
        direction = c.direction;
        health = c.health;
        name = c.name;
        outfit = c.outfit;
        unanalyzed = c.unanalyzed;
        unanalyzed2 = c.unanalyzed2;
    }

    /**
     * Attempts to attack this Entity.
     */
    public void attack() throws IOException {
        new RequestCreatureFlagUpdatePacket(id, 0, 1, 0).send(Client.getInstance().getOs());
    }
    
    /**
     * Attempts to follow this Entity.
     */
    public void follow() throws IOException {
        new RequestCreatureFlagUpdatePacket(1, 0, id, 0).send(Client.getInstance().getOs());
    }
    
    /**
     * Follows and attacks a creature at the same time.
     * @throws IOException 
     */
    public void chase() throws IOException {
        new RequestCreatureFlagUpdatePacket(id, 0, id, 0).send(Client.getInstance().getOs());
    }

    public boolean isAttacked() {
        Entity e = Client.getInstance().getCurrentlyAttacked();
        return e != null && e.id == id;
    }

    public boolean isFollowed() {
        Entity e = Client.getInstance().getCurrentlyFollowed();
        return e != null && e.id == id;
    }
    
    public boolean isDead() {
        return health == 0;
    }
    
    public void dispose() {
        this.location = null;
        id = 0;
        direction = 0;
        health = 0;
        name = null;
        outfit = null;
        unanalyzed = 0;
        unanalyzed2 = null;
    }

    void move(short direction) {
        this.direction = direction;
        switch (direction) {
            case Directions.NORTH:
                location.y -= 1;
                break;
            case Directions.EAST:
                location.x += 1;
                break;
            case Directions.SOUTH:
                location.y += 1;
                break;
            case Directions.WEST:
                location.x -= 1;
                break;
            case Directions.SOUTH_WEST:
                location.y += 1;
                location.x -= 1;
                this.direction = Directions.WEST;
                break;
            case Directions.NORTH_WEST:
                location.y -= 1;
                location.x -= 1;
                this.direction = Directions.WEST;
                break;
            case Directions.NORTH_EAST:
                location.y -= 1;
                location.x += 1;
                this.direction = Directions.EAST;
                break;
            case Directions.SOUTH_EAST:
                location.y += 1;
                location.x += 1;
                this.direction = Directions.EAST;
                break;
        }
    }

    @Override
    public Entity clone() {
        return new Entity(location.clone(), this);
    }

    public boolean isPlayer() {
        return id > 30000;
    }
}
