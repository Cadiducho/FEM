/*package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.ItemUtil;
import com.cadiducho.fem.core.util.FEMFileLoader;
import com.cadiducho.fem.core.util.Metodos;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class StaffCMD extends FEMCmd {
    
    private final String apiBase = "https://meriland.es/api/";
    
    public StaffCMD() {
        super("staff", Rango.Miembro, Arrays.asList());
    }
    
    private static final JSONParser jsonParser = new JSONParser();
    
    @Override
    public void run(FEMUser user, String label, String[] args) {    
        try {
            JSONArray staffArray = this.apiRequest(user, "staff/");
            if (staffArray != null) {
                List<JSONObject> staff = new ArrayList<>();
                int in = 0;
                for (Object obj : staffArray) {
                    JSONObject object = (JSONObject) obj;
                    in++;
                    staff.add(object);
                }
                
                int invSize = 9;	
		for (int i = 54; i >= 9; i -= 9) {
                    if (in < i) {
                        invSize = i;
                    }
                }
                
		Inventory inv = Bukkit.createInventory(user.getPlayer(), invSize, Metodos.colorizar(FEMFileLoader.getLang().getString("staff.menu")));
                int i = 0;
                for (JSONObject obj : staff) {
                    List<String> lore = new ArrayList<>();
                    int rango = Integer.parseInt((String) obj.get("rank"));
                    String twitter = (String) obj.get("twitter");
                    String ranstr = (rango == 1) ? "&bAdmin" : ((rango == 2) ? "&dModerador" : (rango == 3 ? "&6Técnico" : "&cRango desconocido"));
                    lore.add(Metodos.colorizar(ranstr));
                    lore.add(Metodos.colorizar(Metodos.colorizar("&5&o"+obj.get("description"))));
                    if (!"".equals(twitter)) {
                        twitter = Metodos.colorizar("&eSígueme en &b@"+twitter.replace("https://twitter.com/", ""));
                        lore.add(twitter);
                    }
                    
                    inv.setItem(i, ItemUtil.createHeadPlayer((String) obj.get("nick"), lore));
                    i++;
                }
                user.getPlayer().openInventory(inv);
            } else user.sendMessage("&c¡Ha ocurrido un error obteniendo los datos de los miembros del staff! (null)");
        } catch (Exception ex) {
            user.sendMessage("&cHa ocurrido un error abriendo la interfaz!");
            ex.printStackTrace();
        }
    }
    
    private JSONArray apiRequest(FEMUser user, String params) {             
        String url = this.apiBase + params;
        
        try {
            String json = Metodos.readUrl(url);
            Object objectReject = jsonParser.parse(json);
            if (objectReject instanceof JSONObject) {
                JSONObject proyectosRequest = (JSONObject) objectReject;
                if (((Long) proyectosRequest.get("status")).intValue() == 1) {
                    return (JSONArray) proyectosRequest.get("result");
                }
                else {
                    user.sendMessage("&cHa ocurrido un error de peticion invalida");
                    System.out.println(proyectosRequest.toJSONString());
                    System.out.println(url);
                }

            } else {
                user.sendMessage("&cHa ocurrido un error al consultar los proyectos");
            }
        } catch (Exception ex) {
            user.sendMessage("&cHa ocurrido un error al consultar la API");
            System.out.println(url);
            ex.printStackTrace();
        }
        
        return null;
    }
}
*/