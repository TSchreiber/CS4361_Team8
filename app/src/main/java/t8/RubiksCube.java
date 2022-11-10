package t8;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.*;
import java.net.*;
import java.util.*;
import java.util.stream.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.transform.*;

public class RubiksCube extends Group {

    private static final String OBJ_URI = "rubiksCube.obj";

    private Cublet[][][] cubletMatrix = new Cublet[3][3][3];

    public static class Cublet extends Group {

        public enum Type {
            CENTER,
            CORNER,
            EDGE,
            SHAFT
        }

        public final Type type;

        private Cublet(String cubletId, MeshView... meshes) {
            super(meshes);
            setId(cubletId);
            switch (meshes.length) {
                case 1: type = Type.SHAFT; break;
                case 2: type = Type.CENTER; break;
                case 3: type = Type.EDGE; break;
                case 4: type = Type.CORNER; break;
                default: throw new IllegalArgumentException(
                    String.format("Cublet must have between 1 and 4 meshes, %d found", meshes.length));
            }
        }

    }

    public class Face {

        Point3D normal;
        Point3D center;
        Rotate curRotation;

        public Face(Cublet... cublets) {
            Cublet center = Stream.of(cublets)
                .filter(c -> c.type == Cublet.Type.CENTER)
                .findFirst()
                .get();
            Bounds cubletBounds = center.getBoundsInParent();
            Point3D cubletCenter = new Point3D(
                (cubletBounds.getMaxX() + cubletBounds.getMinX()) / 4f,
                (cubletBounds.getMaxY() + cubletBounds.getMinY()) / 4f,
                (cubletBounds.getMaxZ() + cubletBounds.getMinZ()) / 4f);
            normal = cubletCenter.normalize();
            this.center = cubletCenter.add(normal.multiply(cubletBounds.getWidth() / 2f));
        }

        public Cublet[][] getCublets() {
            Cublet[][] out = new Cublet[3][3];
            Point3D v = VecUtil.bitwiseAnd(
                    normal.add(new Point3D(1,1,1)),
                    new Point3D(2,2,2));
            Point3D di = VecUtil.rshift(VecUtil.abs(normal));
            Point3D dj = VecUtil.rshift(di);
            Point3D vi = new Point3D(v.getX(), v.getY(), v.getZ());
            for (int i=0; i<3; i++) {
                Point3D vj = new Point3D(vi.getX(), vi.getY(), vi.getZ());
                for (int j=0; j<3; j++) {
                    out[i][j] = cubletMatrix[(int)vj.getX()][(int)vj.getY()][(int)vj.getZ()];
                    vj = vj.add(dj);
                }
                vi = vi.add(di);
            }
            return out;
        }

        public void setCublets(Cublet[][] cublets) {
            Point3D v = VecUtil.bitwiseAnd(
                    normal.add(new Point3D(1,1,1)),
                    new Point3D(2,2,2));
            Point3D di = VecUtil.rshift(VecUtil.abs(normal));
            Point3D dj = VecUtil.rshift(di);
            Point3D vi = new Point3D(v.getX(), v.getY(), v.getZ());
            for (int i=0; i<3; i++) {
                Point3D vj = new Point3D(vi.getX(), vi.getY(), vi.getZ());
                for (int j=0; j<3; j++) {
                    cubletMatrix[(int)vj.getX()][(int)vj.getY()][(int)vj.getZ()] = cublets[i][j];
                    vj = vj.add(dj);
                }
                vi = vi.add(di);
            }
        }

        public Rotate startRotation() {
            curRotation = new Rotate(0, normal);
            Stream.of(getCublets()).flatMap(x -> Stream.of(x))
                .forEach(c -> c.getTransforms().add(0,curRotation));
            return curRotation;
        }

        public void applyRotation() {
            int angle = ((int) Math.round(curRotation.getAngle() / 90f) * 90 + 360) % 360;
            // the local rotation angle is not necessarily the same as the global rotation angle.
            boolean containsNegative = -1 == ((int)normal.getX() | (int)normal.getY() | (int)normal.getZ());
            if (containsNegative) {
                if (angle == 90) angle = 270;
                else if (angle == 270) angle = 90;
            }
            switch(angle) {
                case 0:
                    Stream.of(getCublets())
                        .flatMap(x -> Stream.of(x))
                        .forEach(c -> c.getTransforms().remove(0));
                    break;

                case 90:
                    setCublets(
                        CubletMatrixUtil.rotateCounterClockwise(
                        getCublets()));
                    break;

                case 180:
                    setCublets(
                        CubletMatrixUtil.rotate180(
                        getCublets()));
                    break;

                case 270:
                    setCublets(
                        CubletMatrixUtil.rotateClockwise(
                        getCublets()));
                    break;
            }
        }

        public String toString() {
            Cublet[][] cublets = getCublets();
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<3; i++) {
                for (int j=0; j<3; j++) {
                    String name = cublets[i][j].getId();
                    if (name.contains("inner"))
                        name = " + ";
                    else
                        name = name.substring(name.lastIndexOf("_")+1);
                    sb.append(String.format("%3s ", name));
                }
                sb.append("\n");
            }
            return sb.toString();
        }

    }

    private Face[] faces = new Face[6];
    private Map<Face, Face[]> faceAdj = new HashMap<>();

    public RubiksCube() throws Exception {
        MeshView[] meshes = loadMeshes();
        Cublet[] cublets = buildCublets(meshes);
        getChildren().addAll(cublets);
        faces = buildFaces(cublets);

        cubletMatrix[0][0][0] = cublets[16];
        cubletMatrix[1][0][0] = cublets[0];
        cubletMatrix[2][0][0] = cublets[14];
        cubletMatrix[0][1][0] = cublets[25];
        cubletMatrix[1][1][0] = cublets[23];
        cubletMatrix[2][1][0] = cublets[6];
        cubletMatrix[0][2][0] = cublets[19];
        cubletMatrix[1][2][0] = cublets[8];
        cubletMatrix[2][2][0] = cublets[5];

        cubletMatrix[0][0][1] = cublets[10];
        cubletMatrix[1][0][1] = cublets[2];
        cubletMatrix[2][0][1] = cublets[1];
        cubletMatrix[0][1][1] = cublets[17];
        cubletMatrix[1][1][1] = cublets[7];
        cubletMatrix[2][1][1] = cublets[22];
        cubletMatrix[0][2][1] = cublets[24];
        cubletMatrix[1][2][1] = cublets[3];
        cubletMatrix[2][2][1] = cublets[21];

        cubletMatrix[0][0][2] = cublets[15];
        cubletMatrix[1][0][2] = cublets[4];
        cubletMatrix[2][0][2] = cublets[13];
        cubletMatrix[0][1][2] = cublets[20];
        cubletMatrix[1][1][2] = cublets[26];
        cubletMatrix[2][1][2] = cublets[11];
        cubletMatrix[0][2][2] = cublets[18];
        cubletMatrix[1][2][2] = cublets[12];
        cubletMatrix[2][2][2] = cublets[9];
    }

    public Face getFace(Node mesh) {
        Point3D targetNormal = getNormal(mesh);
        return Stream.of(faces)
            .filter(f -> f.normal.equals(targetNormal))
            .findFirst()
            .orElse(null);
    }

    public Face getFace(Point3D targetNormal) {
        return Stream.of(faces)
            .filter(f -> f.normal.equals(targetNormal))
            .findFirst()
            .orElse(null);
    }

    private Point3D getNormal(Node mesh) {
        Bounds meshBounds = mesh.getBoundsInLocal();
        Point3D meshCenter = new Point3D(
                (meshBounds.getMaxX() + meshBounds.getMinX()) / 2f,
                (meshBounds.getMaxY() + meshBounds.getMinY()) / 2f,
                (meshBounds.getMaxZ() + meshBounds.getMinZ()) / 2f);
        Bounds cubletBounds = mesh.getParent().getBoundsInLocal();
        Point3D cubletCenter = new Point3D(
                (cubletBounds.getMaxX() + cubletBounds.getMinX()) / 2f,
                (cubletBounds.getMaxY() + cubletBounds.getMinY()) / 2f,
                (cubletBounds.getMaxZ() + cubletBounds.getMinZ()) / 2f);
        return meshCenter.subtract(cubletCenter).normalize();
    }

    private MeshView[] loadMeshes() throws ImportException {
        ObjModelImporter objImporter = new ObjModelImporter();
        URL modelURL = Launcher.class.getResource(OBJ_URI);
        objImporter.read(modelURL);
        MeshView[] meshViews = objImporter.getImport();
        return meshViews;
    }

    private Cublet[] buildCublets(MeshView[] meshes) throws Exception {
        Map<String,List<MeshView>> cubletMap = new HashMap<>();
        for (MeshView mesh : meshes) {
            String meshId = mesh.getId();
            String cubletId = meshId.substring(0, meshId.lastIndexOf('_'));
            if (!cubletMap.containsKey(cubletId))
                cubletMap.put(cubletId, new ArrayList<MeshView>());
            cubletMap.get(cubletId).add(mesh);
        }
        Cublet[] cublets = new Cublet[27];
        int i = 0;
        for (String cubletId : cubletMap.keySet()) {
            if (i >= 27) throw new Exception("The loaded model contains more than 27 cublets.");
            MeshView[] cubletMeshes = cubletMap.get(cubletId)
                .toArray(size -> new MeshView[size]);
            cublets[i++] = new Cublet(cubletId, cubletMeshes);
        }
        return cublets;
    }

    private Face[] buildFaces(Cublet[] cublets) {
        Map<String, List<Cublet>> faces = new HashMap<>();
        for (Cublet c : cublets) {
            if (c.type == Cublet.Type.SHAFT) continue;
            String[] fs = c.getId().substring(c.getId().lastIndexOf("_") + 1).split("");
            for (String f : fs) {
                if (!faces.containsKey(f))
                    faces.put(f, new ArrayList<Cublet>());
                faces.get(f).add(c);
            }
        }
        Face[] out = new Face[6];
        int i = 0;
        for (String face : faces.keySet()) {
            out[i++] = new Face(faces.get(face).toArray(size -> new Cublet[size]));
        }
        return out;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                sb.append("|");
                for (int k=0; k<3; k++) {
                    String name = cubletMatrix[i][j][k].getId();
                    if (name.contains("inner"))
                        name = "+";
                    else
                        name = name.substring(name.lastIndexOf("_")+1);
                    sb.append(String.format("%3s ", name));
                }
                sb.append("| ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
