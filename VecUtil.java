package t8;

import javafx.geometry.*;

public class VecUtil {

    public static Point3D rshift(Point3D p) {
        return new Point3D(p.getZ(), p.getX(), p.getY());
    }

    public static Point3D abs(Point3D p) {
        return new Point3D(
            Math.abs(p.getX()),
            Math.abs(p.getY()),
            Math.abs(p.getZ()));
    }

    /**
     * The given Point3D values will be cast to int.
     */
    public static Point3D bitwiseAnd(Point3D a, Point3D b) {
        return new Point3D(
            (int) a.getX() & (int) b.getX(),
            (int) a.getY() & (int) b.getY(),
            (int) a.getZ() & (int) b.getZ());
    }

}
