package com.cadiducho.fem.chat;

public class AntiSpamData {
    long lastMessages[] = {0L, 0L, 0L};
    long lastSpam = 0L;

    public boolean isSpamming() {
        long now = System.currentTimeMillis();
        if (now - lastSpam < 60000) {
            // Tiene que esperar aún un minuto
            return true;
        }
        long delta = now - lastMessages[2];
        lastMessages[2] = lastMessages[1];
        lastMessages[1] = lastMessages[0];
        lastMessages[0] = now;
        // 4 mensajes en menos de 4 minutos -> Spam
        boolean isSpamming = delta < 4000;
        if (isSpamming) {
            lastSpam = now;
        }
        return isSpamming;
    }
}