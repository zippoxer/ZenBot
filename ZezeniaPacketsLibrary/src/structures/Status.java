/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

import java.io.IOException;
import network.NetworkMessage;

/**
 *
 * @author Myzreal
 */
public class Status extends Structure {

    public int hp;
    public int maxHp;
    public int mp;
    public int maxMp;
    public int level;
    public long exp;
    public short expPercent; //probably
    public short magicLevel;
    public int magicLevelBonus;
    public short magicLevelPercent;
    public short melee;
    public int meleeBonus;
    public short meleePercent;
    public short distance;
    public int distanceBonus;
    public short distancePercent;
    public short defense;
    public int defenseBonus;
    public short defensePercent;

    public Status() {}
    
    public Status(short hp, short maxhp, short mp, short maxmp, short level,
            int exp, short expPercent, short magicLevel, short magicLevelBonus,
            short magicLevelPercent, short melee, short meleeBonus,
            short meleePercent, short distance, short distanceBonus,
            short distancePercent, short defense, short defenseBonus,
            short defensePercent) {
        this.hp = hp;
        this.maxHp = maxhp;
        this.mp = mp;
        this.maxMp = maxmp;
        this.level = level;
        this.exp = exp;
        this.expPercent = expPercent;
        this.magicLevel = magicLevel;
        this.magicLevelBonus = magicLevelBonus;
        this.magicLevelPercent = magicLevelPercent;
        this.melee = melee;
        this.meleeBonus = meleeBonus;
        this.meleePercent = meleePercent;
        this.distance = distance;
        this.distanceBonus = distanceBonus;
        this.distancePercent = distancePercent;
        this.defense = defense;
        this.defenseBonus = defenseBonus;
        this.defensePercent = defensePercent;
    }

    @Override
    public Status read(NetworkMessage m) throws IOException {
        hp = m.readUshort();
        maxHp = m.readUshort();
        mp = m.readUshort();
        maxMp = m.readUshort();
        level = m.readUshort();
        exp = m.readUint();
        expPercent = m.readByte();
        magicLevel = m.readByte();
        magicLevelBonus = m.readUshort();
        magicLevelPercent = m.readByte();
        melee = m.readByte();
        meleeBonus = m.readUshort();
        meleePercent = m.readByte();
        distance = m.readByte();
        distanceBonus = m.readUshort();
        distancePercent = m.readByte();
        defense = m.readByte();
        defenseBonus = m.readUshort();
        defensePercent = m.readByte();
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeUshort(hp);
        m.writeUshort(maxHp);
        m.writeUshort(mp);
        m.writeUshort(maxMp);
        m.writeUshort(level);
        m.writeUint(exp);
        m.writeByte(expPercent);
        m.writeByte(magicLevel);
        m.writeUshort(magicLevelBonus);
        m.writeByte(magicLevelPercent);
        m.writeByte(melee);
        m.writeUshort(meleeBonus);
        m.writeByte(meleePercent);
        m.writeByte(distance);
        m.writeUshort(distanceBonus);
        m.writeByte(distancePercent);
        m.writeByte(defense);
        m.writeUshort(defenseBonus);
        m.writeByte(defensePercent);
    }
}
