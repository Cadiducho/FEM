package com.cadiducho.fem.pro;

import com.cadiducho.fem.core.api.FEMUser;

import java.util.UUID;

public class ProPlayer extends FEMUser{

    private final Protections plugin = Protections.getInstance();

    public ProPlayer(UUID id) {
        super(id);
    }


}
