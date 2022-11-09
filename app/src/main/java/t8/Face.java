package t8;

import javafx.geometry.*;

public enum Face {
    WHITE (new Point3D( 0, 0,-1)),
    RED   (new Point3D( 1, 0, 0)),
    GREEN (new Point3D( 0, 1, 0)),
    BLUE  (new Point3D( 0,-1, 0)),
    ORANGE(new Point3D(-1, 0, 0)),
    YELLOW(new Point3D( 0, 0, 1));

    Point3D normal;

    Face(Point3D normal) {
        this.normal = normal;
    }

    public Point3D normal() {
        return normal;
    }
}

