package com.cadiducho.fem.color.manager;

public enum GameState {
    
    PREPARING, LOBBY, COUNTDOWN, GAME, ENDING;
    public static GameState state;
    
    public static String getParsedStatus() {
        switch (state) {
            case PREPARING: return "&cPreparando";
            case LOBBY: return "&aEsperando";
            case COUNTDOWN: return "&6Comenzando";
            case GAME: 
                return "&cJugando";
            case ENDING: return "&dTerminando";
        }
        return "&7Desconocido";
    }
}
