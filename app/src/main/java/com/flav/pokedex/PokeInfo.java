package com.flav.pokedex;

import java.io.Serializable;

public class PokeInfo implements Serializable {
    public String name;
    public String id;
    public String spriteURL;
    public String speed;
    public String defSpe;
    public String atkSpe;
    public String def;
    public String atk;
    public String hp;
    public String[] types;
    public String weight;

    public PokeInfo(String name, String id, String spriteURL,
                    String speed, String defSpe,
                    String atkSpe, String def, String atk,
                    String hp, String[] types, String weight) {
        this.name = name;
        this.id = id;
        this.spriteURL = spriteURL;
        this.speed = speed;
        this.defSpe = defSpe;
        this.atkSpe = atkSpe;
        this.def = def;
        this.atk = atk;
        this.hp = hp;
        this.types = types;
        this.weight = weight;
    }
}
