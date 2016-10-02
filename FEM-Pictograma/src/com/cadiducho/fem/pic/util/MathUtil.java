package com.cadiducho.fem.pic.util;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class MathUtil {

    public static int getRandomInt(int index) {
        Random r = new Random();

        int ri = r.nextInt(index);

        return ri;
    }

    public static double offset(Location a, Location b) {
        return offset(a.toVector(), b.toVector());
    }

    public static double offset(Vector a, Vector b) {
        return a.subtract(b).length();
    }

    public static Vector getTraj(Location from, Location to) {
        return getTraj(from.toVector(), to.toVector());
    }

    public static Vector getTraj(Vector from, Vector to) {
        return to.subtract(from).normalize();
    }
}
