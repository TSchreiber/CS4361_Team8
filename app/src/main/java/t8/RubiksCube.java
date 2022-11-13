package t8;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.*;
import com.google.common.collect.*;
import java.net.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.transform.*;
import org.worldcubeassociation.tnoodle.puzzle.*;
import org.worldcubeassociation.tnoodle.scrambles.*;

public class RubiksCube extends Group {

    private static final String OBJ_URI = "rubiksCube.obj";
    private static Cublet[][][] solvedCube;

    public static final BiMap<Face,Point3D> faceNormals = HashBiMap.create(Map.of(
        Face.WHITE,  new Point3D( 0, 0,-1),
        Face.RED,    new Point3D( 1, 0, 0),
        Face.GREEN,  new Point3D( 0, 1, 0),
        Face.BLUE,   new Point3D( 0,-1, 0),
        Face.ORANGE, new Point3D(-1, 0, 0),
        Face.YELLOW, new Point3D( 0, 0, 1)));

    private Cublet[][][] cubletMatrix = new Cublet[3][3][3];
    private List<Consumer<RotationEvent>> rotationEventHandlers = new LinkedList<>();

    public void addRotationEventHandler(Consumer<RotationEvent> handler) {
        rotationEventHandlers.add(handler);
    }

    public void setCublets(Face face, Cublet[][] cublets) {
        Point3D normal = faceNormals.get(face);
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

    public Cublet get(int x, int y, int z) {
        return cubletMatrix[x][y][z];
    }

    public Cublet get(Point3D p) {
        return cubletMatrix[(int)p.getX()][(int)p.getY()][(int)p.getZ()];
    }

    public Cublet[][] get(Face face) {
        Cublet[][] out = new Cublet[3][3];
        Point3D normal = faceNormals.get(face);
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

    public Face getFaceWithNormal(Point3D targetNormal) {
        return faceNormals.inverse().get(targetNormal);
    }

    public void rotate(Face face, int angle) {
        angle = (angle + 360) % 360;
        if (angle == 0) return;
        // The direction to rotate the matrix will be inverted for the 
        // back side of the cube
        if (angle == 90 || angle == 270) {
            if (face.normal().getX() < 0 ||
                face.normal().getY() < 0 ||
                face.normal().getZ() < 0) 
            {
                angle = (angle + 180) % 360;
            }
        }
        switch (angle) {
            case 90:
                setCublets(face,
                    CubletMatrixUtil.rotateCounterClockwise(
                    get(face)));
                break;

            case 180:
                setCublets(face,
                    CubletMatrixUtil.rotate180(
                    get(face)));
                break;

            case 270:
                setCublets(face,
                    CubletMatrixUtil.rotateClockwise(
                    get(face)));
                break;

            default:
                throw new IllegalArgumentException(
                    String.format("Provided rotation angle, %d not a multple of 90", angle));
        }
        RotationEvent e = new RotationEvent(face, angle);
        rotationEventHandlers.stream().forEach(h -> h.accept(e));
    }

    /**
     * set doView to true for non-interactive rotations so the 
     * meshes will have a new rotation transform applied.
     */
    public void rotate(Face face, int angle, boolean doView) {
        if (doView) {
            Rotate r = new Rotate(angle, face.normal());
            Stream.of(get(face))
                .flatMap(row -> Stream.of(row))
                .flatMap(cublet -> cublet.getChildren().stream())
                .forEach(mesh -> mesh.getTransforms().add(0, r));
        }
        rotate(face, angle);
    }

    public void scramble() {
        scramble(new Random());
    }

    public void scramble(Random random) {
        String sequenceString = 
             new ThreeByThreeCubePuzzle()
            .generateRandomMoves(random)
            .generator;
        new SequenceStringProcessor(this).accept(sequenceString);
    }

    public boolean isSolved() {
        // This will give the incorrect result for certain cases when
        // all the cublets are in the correct position but rotated 
        // incorrectly. This does not happen often, so we can ignore
        // it for now.
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                for (int k=0; k<3; k++) {
                    if (cubletMatrix[i][j][k] != solvedCube[i][j][k]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public class Cublet extends Group {

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

    public RubiksCube() throws Exception {
        MeshView[] meshes = loadMeshes();
        Cublet[] cublets = buildCublets(meshes);
        getChildren().addAll(cublets);

        cubletMatrix[0][0][0] = cublets[16];
        cubletMatrix[1][0][0] = cublets[0];
        cubletMatrix[2][0][0] = cublets[14];
        cubletMatrix[0][1][0] = cublets[25];
        cubletMatrix[1][1][0] = cublets[23];
        cubletMatrix[2][1][0] = cublets[6];
        cubletMatrix[0][2][0] = cublets[19];
        cubletMatrix[1][2][0] = cublets[7];
        cubletMatrix[2][2][0] = cublets[5];

        cubletMatrix[0][0][1] = cublets[9];
        cubletMatrix[1][0][1] = cublets[2];
        cubletMatrix[2][0][1] = cublets[1];
        cubletMatrix[0][1][1] = cublets[17];
        cubletMatrix[1][1][1] = cublets[12];
        cubletMatrix[2][1][1] = cublets[22];
        cubletMatrix[0][2][1] = cublets[24];
        cubletMatrix[1][2][1] = cublets[3];
        cubletMatrix[2][2][1] = cublets[21];

        cubletMatrix[0][0][2] = cublets[15];
        cubletMatrix[1][0][2] = cublets[4];
        cubletMatrix[2][0][2] = cublets[13];
        cubletMatrix[0][1][2] = cublets[20];
        cubletMatrix[1][1][2] = cublets[26];
        cubletMatrix[2][1][2] = cublets[10];
        cubletMatrix[0][2][2] = cublets[18];
        cubletMatrix[1][2][2] = cublets[11];
        cubletMatrix[2][2][2] = cublets[8];

        solvedCube = new Cublet[3][3][3];
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                for (int k=0; k<3; k++) {
                    solvedCube[i][j][k] = cubletMatrix[i][j][k];
                }
            }
        }
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

    public void reset() {
        /*
        Stream.of(cubletMatrix)
            .flatMap(row -> Stream.of(row))
            .flatMap(cublet -> cublet.getChildren().stream())
            .forEach(mesh -> mesh.getTransforms().clear());
            */
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                for (int k=0; k<3; k++) {
                    cubletMatrix[i][j][k] = solvedCube[i][j][k];
                    cubletMatrix[i][j][k].getChildren().stream()
                    .forEach(mesh -> mesh.getTransforms().clear());
                }
            }
        }
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
