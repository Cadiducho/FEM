package com.cadiducho.fem.lobby.utils;

import com.cadiducho.fem.core.particles.ParticleEffect;

public enum ParticleType {

    SEGUIR1("Corazones", ParticleEffect.HEART, ParticleID.NONE),
    SEGUIR2("Tren", ParticleEffect.SMOKE_NORMAL, ParticleID.NONE);

    private String name;
    private ParticleEffect pe;
    private ParticleID pid;

    ParticleType(String name, ParticleEffect pe, ParticleID pid){
        this.name = name;
        this.pe = pe;
        this.pid = pid;
    }

    public String getName(){
        return name;
    }

    public ParticleEffect getPE(){
        return pe;
    }

    public ParticleID getPID(){
        return pid;
    }

    public enum ParticleID {
        //TODO: Más
        NONE, HALO, SPIRAL, FOUNTAIN;
    }
}
