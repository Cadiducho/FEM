package com.cadiducho.fem.gem.manager;

public enum GameState {
    
    PREPARING, LOBBY, COUNTDOWN, HIDDING, GAME, EXTENSION, ENDING;
    public static GameState state;
    
    public static String getParsedStatus() {
        switch (state) {
            case PREPARING: return "STARTING";
            case LOBBY: return "WAITING_FOR_PLAYERS";
            case COUNTDOWN: return "WAITING_FOR_PLAYERS";
            case HIDDING:
            case EXTENSION:
            case GAME: 
                return "INGAME";
            case ENDING: return "ENDING";
        }
        return "&7Desconocido";
    }
}
