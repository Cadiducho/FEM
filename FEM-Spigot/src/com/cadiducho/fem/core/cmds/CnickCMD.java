package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.DateUtil;
import com.cadiducho.fem.core.util.Metodos;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class CnickCMD extends FEMCmd {
    
    public CnickCMD() {
        super("cnick", Grupo.Admin, Arrays.asList("cambionick"));
    }
    
    private static final JSONParser jsonParser = new JSONParser();
    
    @Override
    public void run(FEMUser user, String label, String[] args) {        
        if (args.length == 0) {
            user.sendMessage("*cnick.uso");
            return;
        }

        String uuidstr = plugin.getServer().getOfflinePlayer(args[0]).getUniqueId().toString();
        JSONArray array;
        try {
            String url = Metodos.readUrl("https://api.mojang.com/user/profiles/" + uuidstr.replace("-", "") + "/names");
            array = (JSONArray) jsonParser.parse(url);
        } catch (Exception ex) {
            user.sendMessage("*cnick.error", args[0]);
            return;
        }
        
        user.sendRawMessage(Metodos.colorizar("&a&nHistorial de nombres de&f &b"+args[0]));
        user.sendRawMessage("");
        array.forEach(obj -> {
            JSONObject object = (JSONObject) obj;
            if (object.get("changedToAt") == null) {
                if (array.get(array.size()-1).equals(obj)) {
                    user.sendRawMessage("- "+object.get("name")+ " (Actual)");
                } else {
                    user.sendRawMessage("- "+object.get("name")+" (Original)");   
                }
            } else {
                long dateL = ((Long)object.get("changedToAt"));
                if (array.get(array.size()-1).equals(obj)) {
                    user.sendRawMessage("- "+object.get("name")+ " (Actual)");
                } else {
                    user.sendRawMessage("- "+object.get("name")+ " (desde el "+DateUtil.fechaToString(new Date(dateL), "dd/MM/YY HH:mm")+")");
                }  
            }
        });
        user.sendRawMessage(Metodos.colorizar("&a_________________________________"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
