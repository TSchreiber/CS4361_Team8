package t8;

import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.*;
import javafx.scene.transform.*;
import t8.RubiksCube.*;

public class FaceRotationHandler implements EventHandler<MouseEvent> {

    private RubiksCube cube;
    private Rotate curRotation;
    private Point3D faceCenter;
    private Face curFace;
    private double initAngle;
    private double prevRotation;
    private long prevRotationTime;
    private long curRotationTime;

    public FaceRotationHandler(RubiksCube cube) {
        this.cube = cube;
    }

    @Override
    public void handle(MouseEvent e) {
        if (e.getButton() != MouseButton.PRIMARY) return;
        Node mesh = (Node) e.getTarget();
        Cublet cublet = (Cublet) mesh.getParent();
        if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
            Point3D mousePos = new Point3D(e.getX(), e.getY(), e.getZ());
            Face face = cube.getFace(getNormal(cube.sceneToLocal(mousePos)));
            curFace = face;
            if (face != null) {
                faceCenter = cube.localToScene(face.center);
                curRotation = face.startRotation();
                Point3D vecToCenter = mousePos.subtract(faceCenter);
                initAngle = Math.atan2(vecToCenter.getY(), vecToCenter.getX()) * -180 / Math.PI;
            } else {
                curRotation = null;
            }
        } else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            if (curRotation != null) {
                prevRotation = curRotation.getAngle();
                prevRotationTime = curRotationTime;
                curRotationTime = System.currentTimeMillis();
                Point3D mousePos = new Point3D(e.getX(), e.getY(), e.getZ());
                Point3D vecToCenter = mousePos.subtract(faceCenter);
                double rotation = -Math.atan2(vecToCenter.getY(), vecToCenter.getX()) * 180 / Math.PI - initAngle;
                curRotation.setAngle(rotation);
            }
        } else if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
            if (curRotation != null) {
                // Snap based on the direction of rotation
                double velocity = (curRotation.getAngle() - prevRotation) / (curRotationTime - prevRotationTime);
                double r = Math.round(curRotation.getAngle() / 90f) * 90;
                if (velocity <= -0.1) {
                    r = Math.floor(curRotation.getAngle() / 90f) * 90;
                } else if (velocity >= 0.1) {
                    r = Math.ceil(curRotation.getAngle() / 90f) * 90;
                }
                curRotation.setAngle(r);
                curFace.applyRotation();
            }
        }
    }

    private Point3D getNormal(Point3D mousePos) {
        // The normal will be the axis with the hightest abs value 
        if (Math.abs(mousePos.getX()) > Math.abs(mousePos.getY())) {
            if (Math.abs(mousePos.getX()) > Math.abs(mousePos.getZ())) {
                if (mousePos.getX() < 0) {
                    return new Point3D(-1, 0, 0);
                } else {
                    return new Point3D(1, 0, 0);
                }
            } else {
                if (mousePos.getZ() < 0) {
                    return new Point3D(0, 0, -1);
                } else {
                    return new Point3D(0, 0, 1);
                }
            }
        } else {
            if (Math.abs(mousePos.getY()) > Math.abs(mousePos.getZ())) {
                if (mousePos.getY() < 0) {
                    return new Point3D(0, -1, 0);
                } else {
                    return new Point3D(0, 1, 0);
                }
            } else {
                if (mousePos.getZ() < 0) {
                    return new Point3D(0, 0, -1);
                } else {
                    return new Point3D(0, 0, 1);
                }
            }
        }
    }

}
