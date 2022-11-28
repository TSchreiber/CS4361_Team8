package t8;

public class RotationEvent {

    public final Face face;
    public final double angle;

    public RotationEvent(Face face, double angle) {
        this.face = face;
        this.angle = angle;
    }

    public Face face() {
        return face;
    }

    public double angle() {
        return angle;
    }

}
