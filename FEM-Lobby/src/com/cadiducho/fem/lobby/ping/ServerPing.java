package com.cadiducho.fem.lobby.ping;

import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerPing {
    
    public ServerPingReply getPing(ServerPingOptions options) throws IOException {
        ServerPingUtil.validate(options.getHostname(), "Hostname cannot be null.");
        ServerPingUtil.validate(options.getPort(), "Port cannot be null.");

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(options.getHostname(), options.getPort()), options.getTimeout());

        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        ByteArrayOutputStream handshake_bytes = new ByteArrayOutputStream();
        DataOutputStream handshake = new DataOutputStream(handshake_bytes);

        handshake.writeByte(ServerPingUtil.PACKET_HANDSHAKE);
        ServerPingUtil.writeVarInt(handshake, ServerPingUtil.PROTOCOL_VERSION);
        ServerPingUtil.writeVarInt(handshake, options.getHostname().length());
        handshake.writeBytes(options.getHostname());
        handshake.writeShort(options.getPort());
        ServerPingUtil.writeVarInt(handshake, ServerPingUtil.STATUS_HANDSHAKE);

        ServerPingUtil.writeVarInt(out, handshake_bytes.size());
        out.write(handshake_bytes.toByteArray());

        out.writeByte(1);
        out.writeByte(ServerPingUtil.PACKET_STATUSREQUEST);

        ServerPingUtil.readVarInt(in);
        int id = ServerPingUtil.readVarInt(in);

        ServerPingUtil.io(id == -1, "Server prematurely ended stream.");
        ServerPingUtil.io(id != ServerPingUtil.PACKET_STATUSREQUEST, "Server returned invalid packet.");

        int length = ServerPingUtil.readVarInt(in);
        ServerPingUtil.io(length == -1, "Server prematurely ended stream.");
        ServerPingUtil.io(length == 0, "Server returned unexpected value.");

        byte[] data = new byte[length];
        in.readFully(data);
        String json = new String(data, options.getCharset());

        out.writeByte(9);
        out.writeByte(ServerPingUtil.PACKET_PING);
        out.writeLong(System.currentTimeMillis());

        ServerPingUtil.readVarInt(in);
        id = ServerPingUtil.readVarInt(in);
        ServerPingUtil.io(id == -1, "Server prematurely ended stream.");
        ServerPingUtil.io(id != ServerPingUtil.PACKET_PING, "Server returned invalid packet.");

        handshake.close();
        handshake_bytes.close();
        out.close();
        in.close();
        socket.close();

        return (ServerPingReply) new Gson().fromJson(json, ServerPingReply.class);
    }
}
