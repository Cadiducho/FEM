package com.cadiducho.fem.lobby.ping;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerPingUtil {

    public static byte PACKET_HANDSHAKE = 0;
    public static byte PACKET_STATUSREQUEST = 0;
    public static byte PACKET_PING = 1;
    public static int PROTOCOL_VERSION = 4;
    public static int STATUS_HANDSHAKE = 1;

    public static void validate(Object o, String m) {
        if (o == null) {
            throw new RuntimeException(m);
        }
    }

    public static void io(boolean b, String m) throws IOException {
        if (b) {
            throw new IOException(m);
        }
    }

    public static int readVarInt(DataInputStream in) throws IOException {
        int i = 0;
        int j = 0;
        for (;;) {
            int k = in.readByte();

            i |= (k & 0x7F) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
            if ((k & 0x80) != 128) {
                break;
            }
        }
        return i;
    }

    public static void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
        for (;;) {
            if ((paramInt & 0xFFFFFF80) == 0) {
                out.writeByte(paramInt);
                return;
            }
            out.writeByte(paramInt & 0x7F | 0x80);
            paramInt >>>= 7;
        }
    }
}
