package com.cadiducho.fem.pro;

import com.cadiducho.fem.core.api.FEMUser;
import lombok.Getter;

import java.util.UUID;

public class ProPlayer extends FEMUser{

    private final Protections plugin = Protections.getInstance();

    @Getter ProArea area;

    public ProPlayer(UUID id) {
        super(id);
        this.area = new ProArea(new ProPlayer(id));
    }

    public void teleportArea(){
        getPlayer().teleport(area.getLocation());
    }
}
