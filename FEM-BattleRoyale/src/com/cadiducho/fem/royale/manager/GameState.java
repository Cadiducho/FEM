package com.cadiducho.fem.royale.manager;

public enum GameState {
    
    PREPARING, LOBBY, COUNTDOWN, PVE, GAME, DEATHMATCH, ENDING;
    public static GameState state;
    
    public static String getParsedStatus() {
        switch (state) {
            case PREPARING: return "STARTING";
            case LOBBY: return "WAITING_FOR_PLAYERS";
            case COUNTDOWN: return "WAITING_FOR_PLAYERS";
            case PVE:
            case GAME:
            case DEATHMATCH:
                return "INGAME";
            case ENDING: return "ENDING";
        }
        return "&7Desconocido";
    }
}
