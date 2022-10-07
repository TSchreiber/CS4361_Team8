package t8;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.*;
import java.net.*;
import java.util.*;
import javafx.scene.*;
import javafx.scene.shape.*;

public class RubiksCube extends Group {

    private static final String OBJ_URI = "rubiksCube.obj";

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

}
