package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SignEditorCMD extends FEMCmd {

    public SignEditorCMD() {
        super("signeditor", Grupo.Moderador, Arrays.asList("se"));
    }
    
    private final List<String> numeros = Arrays.asList("1", "2", "3", "4");
    private final List<String> updown = Arrays.asList("up", "down");
    
    private final HashMap<FEMUser, String> copiedText = new HashMap<>();
    private final HashMap<FEMUser, String[]> copiedLines = new HashMap<>();

    @Override
    public void run(FEMUser user, String label, String[] args) {
        switch(args.length) {
            case 0:
                user.sendMessage("*signeditor.ayuda");
                break;
            default:
                switch(args[0].toLowerCase()) {
                    case "setline":
                        SetLine(user, args);
                        break;
                    case "clear":
                        Clear(user, args);
                        break;
                    case "clearline":
                        ClearLine(user, args);
                        break;
                    case "append":
                        Append(user, args);
                        break;
                    case "copy": case "copiar":
                        Copy(user, args);
                        break;
                    case "paste": case "pegar":    
                        Paste(user, args);
                        break;
                    case "copylines": case "copiarlineas":
                        CopyLines(user, args);
                        break;
                    case "pastelines": case "pegarlineas":
                        PasteLines(user, args);
                        break;
                    case "shiftline":
                        ShiftLine(user, args);
                        break;
                    case "shiftalllines":
                        shiftAllLines(user, args);
                        break;
                    default:
                        user.sendMessage("*signeditor.ayuda");
                        break;
                }
            break;
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        if (curn == 0) {
            return Arrays.asList("setline", "clear", "clearline", "append", "copy", "paste", "copylines", "pastelines", "shiftline", "shiftalllines");
        }
        if (curn == 1) {
            if (args[0].equalsIgnoreCase("setLine")) return numeros;
            else if (args[0].equalsIgnoreCase("clearLine")) return numeros;
            else if (args[0].equalsIgnoreCase("append")) return numeros;
            else if (args[0].equalsIgnoreCase("copy")) return numeros;
            else if (args[0].equalsIgnoreCase("paste")) return numeros;
            else if (args[0].equalsIgnoreCase("shiftLine")) return numeros;
            else if (args[0].equalsIgnoreCase("shiftAllLines")) return updown;
        }
        if (curn == 2) {
            if (args[0].equalsIgnoreCase("shiftLine")) return updown;
        }
        return null; //Nombre de jugadores
    }

    public String colorize(String Message) {
        return Message.replaceAll("&([a-z0-9])", "§$1");
    }
    
    public void errorLineas(FEMUser user) {
        user.sendMessage("*signeditor.error.lineas");    
    }
    
    public void usoCorrecto(FEMUser user, String str) {
        user.sendMessage("*signeditor.error.uso", str);
    }

    public Sign getSign(FEMUser user) {
        Block b = user.getPlayer().getTargetBlock((Set<Material>) null, 100);
        if (!(b.getState() instanceof Sign)) {
            user.sendMessage("*signeditor.error.letrero");
            return null;
	}
        return (Sign) b.getState();
    }

    public void SetLine(FEMUser user, String[] args) {
        if (args.length <= 2) {
            usoCorrecto(user, "/se setLine <Linea> <Texto>");
            return;
        }
        int line;
        try {
            line = Integer.valueOf(args[1]);
        } catch (NumberFormatException e) {
            errorLineas(user);
            return;
        } if ((line < 1) || (line > 4)) {
            errorLineas(user);
            return;
        }
        line--;

        String text = "";
        for (int a = 2; a < args.length; a++) {
            text = text + args[a] + " ";
        }
        text = text.substring(0, text.length() - 1);

        text = colorize(text);
        if (text.length() > 15) {
            user.sendMessage("*signeditor.error.caracteres");
            return;
        }
        Sign s = getSign(user);
        if (s == null) {
            return;
        }
        s.setLine(line, text);
        s.update();
    }

    public void Clear(FEMUser user, String[] args) {
        if (args.length != 1) {
            usoCorrecto(user, "/se clear");
            return;
        }
        Sign s = getSign(user);
        if (s == null) {
            return;
        }
        for (int a = 0; a < 4; a++) {
            s.setLine(a, "");
        }
        s.update();
    }

    public void ClearLine(FEMUser user, String[] args) {
        if (args.length != 2) {
            usoCorrecto(user, "/se clearLine <Linea>");
            return;
        }
        int line;
        try {
            line = Integer.valueOf(args[1]);
        } catch (NumberFormatException e) {
            usoCorrecto(user, "/se clearLine <Linea>");
            return;
        }
        if ((line < 1) || (line > 4)) {
            errorLineas(user);
            return;
        }
        line--;

        Sign s = getSign(user);
        if (s == null) {
            return;
        }
        s.setLine(line, "");
        s.update();
    }

    public void Append(FEMUser user, String[] args) {
        if (args.length <= 2) {
            usoCorrecto(user, "/se append <Linea> <TextAAñadir>");
            return;
        }
        int line;
        try {
            line = Integer.valueOf(args[1]);
        } catch (NumberFormatException e) {
            usoCorrecto(user, "/se append <Linea> <TextoAAñadir>");
            return;
        }
        if ((line < 1) || (line > 4)) {
            errorLineas(user);
            return;
        }
        line--;

        Sign s = getSign(user);
        if (s == null) {
            return;
        }
        String text = "";
        for (int a = 2; a < args.length; a++) {
            text = text + args[a] + " ";
        }
        text = text.substring(0, text.length() - 1);

        text = colorize(text);

        String lineText = s.getLine(line);

        text = lineText + text;
        if (text.length() > 15) {
            user.sendMessage("*signeditor.error.caracteres");
            return;
        }
        s.setLine(line, text);
        s.update();
    }

    public void Copy(FEMUser user, String[] args) {
        if (args.length != 2) {
            usoCorrecto(user, "/se copy <Linea>");
            return;
        }
        int line;
        try {
            line = Integer.valueOf(args[1]);
        } catch (NumberFormatException e) {
            usoCorrecto(user, "/se copy <Linea>");
            return;
        }
        if ((line < 1) || (line > 4)) {
            errorLineas(user);
            return;
        }
        line--;

        Sign s = getSign(user);
        if (s == null) {
            return;
        }
        copiedText.put(user, s.getLine(line));

        user.sendMessage("&aCopiadas '" + s.getLine(line) + "'");
    }

    public void Paste(FEMUser user, String[] args) {
        if (args.length != 2) {
          usoCorrecto(user, "/se paste <ALinea>");
          return;
        }
        int line;
        try {
            line = Integer.valueOf(args[1]);
        } catch (NumberFormatException e) {
            usoCorrecto(user, "/se paste <ALinea>");
            return;
        }
        if ((line < 1) || (line > 4)) {
            errorLineas(user);
            return;
        }
        line--;

        Sign s = getSign(user);
        if (s == null) {
            return;
        }
        s.setLine(line, (String)copiedText.get(user));
        s.update();
    }

    public void CopyLines(FEMUser user, String[] args) {
        if (args.length != 1) {
            usoCorrecto(user, "/se CopyLines");
            return;
        }
        Sign s = getSign(user);
        if (s == null) {
            return;
        }
        copiedLines.put(user, s.getLines());

        user.sendMessage("*signeditor.lineasCopiadas");
      }
    
    public void PasteLines(FEMUser user, String[] args) {
        if (args.length != 1) {
            usoCorrecto(user, "/se pasteLines");
            return;
        }
        Sign s = getSign(user);
        if (s == null) {
            return;
        }
        for (int a = 0; a < 4; a++) {
            s.setLine(a, ((String[])copiedLines.get(user))[a]);
        }
        s.update();
    }

    public void ShiftLine(FEMUser user, String[] args) {
        if (args.length != 3) {
            usoCorrecto(user, "/se shiftLine <Linea> <Up | Down>");
            return;
        }
        int line;
        try {
            line = Integer.valueOf(args[1]);
        } catch (NumberFormatException e) {
            usoCorrecto(user, "/se shiftLine <Linea> <Up | Down>");
            return;
        }
        if ((line < 1) || (line > 4)) {
            errorLineas(user);
            return;
        }
        line--;

        Sign s = getSign(user);
        if (s == null) {
            return;
        }
        if (args[2].equalsIgnoreCase("up")) {
            if (line != 0) {
                s.setLine(line - 1, s.getLine(line));
                s.setLine(line, "");
                s.update();
            } else {
                user.sendMessage("*signeditor.error.fuera");
            }
        } else if (args[2].equalsIgnoreCase("down")) {
            if (line != 3) {
                s.setLine(line + 1, s.getLine(line));
                s.setLine(line, "");
                s.update();
            } else {
                user.sendMessage("*signeditor.error.fuera");
            }
        } else {
            user.sendMessage("*signeditor.error.direccion");
        }
    }

    public void shiftAllLines(FEMUser user, String[] args) {
        if (args.length != 2) {
            usoCorrecto(user, "/se shiftAllLines <Up | Down>");
            return;
        }
        Sign s = getSign(user);
        if (s == null) {
            return;
        }
        String[] lines = s.getLines();

        String[] newLines = new String[4];
        if (args[1].equalsIgnoreCase("up")) {
            newLines[0] = lines[1];
            newLines[1] = lines[2];
            newLines[2] = lines[3];
            newLines[3] = "";
        } else if (args[1].equalsIgnoreCase("down")) {
            newLines[0] = "";
            newLines[1] = lines[0];
            newLines[2] = lines[1];
            newLines[3] = lines[2];
        } else {
            user.sendMessage("*signeditor.error.direccion");
            return;
        }
        for (int a = 0; a < 4; a++) {
            s.setLine(a, newLines[a]);
        }
        s.update();
    }

}
