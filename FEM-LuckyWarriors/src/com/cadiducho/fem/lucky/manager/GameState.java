package com.cadiducho.fem.lucky.manager;

public enum GameState {
    
    PREPARING, LOBBY, COUNTDOWN, TP, LUCKY, CRAFT, GAME, DEATHMATCH, ENDING;
    public static GameState state;
    
    public static String getParsedStatus() {
        switch (state) {
            case PREPARING: return "STARTING";
            case LOBBY: return "WAITING_FOR_PLAYERS";
            case COUNTDOWN: return "WAITING_FOR_PLAYERS";
            case TP:
            case LUCKY:
            case CRAFT:
            case DEATHMATCH:
                return "INGAME";
            case ENDING: return "ENDING";
        }
        return "&7Desconocido";
    }
}
