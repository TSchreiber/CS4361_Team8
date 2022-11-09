package t8;

import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.*;
import javafx.scene.transform.*;
import javafx.scene.shape.*;
import t8.RubiksCube.*;
import java.util.*;
import java.util.stream.*;
import org.apache.commons.collections4.queue.CircularFifoQueue;

public class FaceRotationHandler implements EventHandler<MouseEvent> {

    private RubiksCube cube;
    private Rotate curRotation;
    private Point3D faceCenter;
    private Face face;
    private double anchorAngle;
    private Queue<Sample> samples;

    public FaceRotationHandler(RubiksCube cube) {
        this.cube = cube;
        samples = new CircularFifoQueue<>(2);
    }

    @Override
    public void handle(MouseEvent e) {
        if (e.getButton() != MouseButton.PRIMARY) return;
        if (!targetIsFace(e)) return;

        switch (e.getEventType().getName()) {
            case "MOUSE_PRESSED": 
                initRotation(e);
                break;
            case "MOUSE_DRAGGED": 
                if (!isInitialized()) return;
                updateAngle(e);
                break;
            case "MOUSE_RELEASED": 
                if (!isInitialized()) return;
                finalizeRotation();
                clearRotation();
                break;
        }

    }

    private boolean targetIsFace(MouseEvent e) {
        // the faces of each cublet are MeshView objects which is a subtype of Node
        if (!(e.getTarget() instanceof Node)) 
            return false;
        // if a node has a Cublet as a parent then we have clicked on the face
        // of a cublet
        return (((Node)e.getTarget()).getParent() instanceof Cublet);
    }

    private boolean isInitialized() {
        return curRotation != null;
    }

    private void initRotation(MouseEvent e) {
        MeshView mv = (MeshView) e.getTarget();
        Bounds bounds = mv.getBoundsInParent();
        Point3D center = new Point3D(bounds.getCenterX(), bounds.getCenterY(), bounds.getCenterZ());
        face = cube.getFaceWithNormal(getClosestFaceNormal(center));
        if (face != null) {
            curRotation = new Rotate(0, face.normal());
            Stream.of(cube.get(face))
                .flatMap(row -> Stream.of(row))
                .flatMap(cublet -> cublet.getChildren().stream())
                .forEach(mesh -> mesh.getTransforms().add(0, curRotation));
            faceCenter = cube.localToScene(face.normal().multiply(3));
            Point3D mousePos = new Point3D(e.getX(), e.getY(), 0);
            Point3D vecToCenter = mousePos.subtract(faceCenter);
            anchorAngle = Math.atan2(vecToCenter.getY(), vecToCenter.getX()) * -180 / Math.PI;
        } else {
            curRotation = null;
        }
    }

    private void updateAngle(MouseEvent e) {
        Sample s = new Sample(getMouseAngle(e));
        samples.add(s);
        curRotation.setAngle(s.angle);
    }

    private void finalizeRotation() {
        // Snap based on the direction of rotation
        double velocity = getVelocity();
        double r = Math.round(curRotation.getAngle() / 90f) * 90;
        if (velocity <= -0.1) {
            r = Math.floor(curRotation.getAngle() / 90f) * 90;
        } else if (velocity >= 0.1) {
            r = Math.ceil(curRotation.getAngle() / 90f) * 90;
        }
        curRotation.setAngle(r);
        cube.rotate(face, (int) r);
    }

    private void clearRotation() {
        curRotation = null;
        samples.clear();
    }

    private double getMouseAngle(MouseEvent e) {
        Point3D mousePos = new Point3D(e.getX(), e.getY(), e.getZ());
        Point3D vecToCenter = mousePos.subtract(faceCenter);
        return -Math.atan2(vecToCenter.getY(), vecToCenter.getX()) * 180 / Math.PI - anchorAngle;
    }

    private Point3D getClosestFaceNormal(Point3D mousePos) {
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

    private static class Sample {
        
        public final double angle;
        public final long time;

        public Sample(double angle) {
            this.angle = angle;
            this.time = System.currentTimeMillis();
        }

    }

    private double getVelocity() {
        Sample[] lastTwoSamples = samples.stream().limit(2).toArray(s -> new Sample[s]);
        if (lastTwoSamples.length < 2) {
            return 0;
        } else {
            long dt = lastTwoSamples[0].time - lastTwoSamples[1].time;
            double da = lastTwoSamples[0].angle - lastTwoSamples[1].angle;
            return da/dt;
        }
    }

}
