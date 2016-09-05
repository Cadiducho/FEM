package com.cadiducho.fem.core.util;

import com.cadiducho.fem.core.FEMCore;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    
    private static FEMCore plugin;
    
    public DateUtil(FEMCore instance) {
        plugin = instance;
    }
    
    public static Date toFecha(String s) throws ParseException {
        return new SimpleDateFormat("dd/MM/yy").parse(s); //Formato de la fecha
    }
    public static String fechaToString(Date fecha, String formato) {
	return new SimpleDateFormat(formato).format(fecha); //Formateamos a String la decha
    }
    public static String fechaToString(Date fecha) {
	return new SimpleDateFormat("dd/MM/yy").format(fecha); //Formateamos a String la decha
    }
    public static Date sumarDias(Date fecha, int dias){
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(fecha); // Configuramos la fecha que se recibe	
      calendar.add(Calendar.DATE, dias);  // numero de días a añadir, o restar en caso de días<0
      return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos    
    }
    public static long getTiempoRestante(Date fin, int i){
        try {
            Date hoy = new Date();

            switch(i){
                case 1: return (long) ( (fin.getTime() - hoy.getTime()) * 0.025); //diff / 1000 % 60;
                case 2: return (long) ( (fin.getTime() - hoy.getTime()) / 60000 % 60); //diff / (60 * 1000) % 60;
                case 3: return (long) ( (fin.getTime() - hoy.getTime()) / 3600000 % 24); //diff / (60 * 60 * 1000) % 24;
                case 4: return (long) ( (fin.getTime() - hoy.getTime()) / 86400000);
            }
        } catch (Exception e){
            plugin.debugLog(e.getLocalizedMessage());
        }
        return -1;
    }
    
}
