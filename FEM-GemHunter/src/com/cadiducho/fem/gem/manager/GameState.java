package com.cadiducho.fem.gem.manager;

public enum GameState {
    
    PREPARING, LOBBY, COUNTDOWN, HIDDING, GAME, EXTENSION, ENDING;
    public static GameState state;
    
    public static String getParsedStatus() {
        switch (state) {
            case PREPARING: return "&cPreparando";
            case LOBBY: return "&aEsperando";
            case COUNTDOWN: return "&6Comenzando";
            case HIDDING:
            case EXTENSION:
            case GAME: 
                return "&cJugando";
            case ENDING: return "&dTerminando";
        }
        return "&7Desconocido";
    }
}
