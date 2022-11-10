package t8; 

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javafx.geometry.Point3D;
import java.util.*;

public class SequenceStringProcessorTest {

    class SinglePointTester {

        RubiksCube cube;
        SequenceStringProcessor processor;

        public SinglePointTester(RubiksCube cube) {
            this.cube = cube;
            processor = new SequenceStringProcessor(cube);
        }

        public void check(Point3D before, String code, Point3D after) {
            String a = cube.get(before).getId();
            processor.accept(code);
            String b = cube.get(after).getId();
            assertEquals(a,b);
        }

    }

    @Test
    void single_character_commands_rotate_the_face_clockwise() throws Exception {
        RubiksCube cube = new RubiksCube();
        SinglePointTester spt = new SinglePointTester(cube);
        spt.check(
            new Point3D(1,0,2), 
            "B",
            new Point3D(0,1,2));
        spt.check(
            new Point3D(1,0,0), 
            "F",
            new Point3D(2,1,0));
        spt.check(
            new Point3D(0,0,1), 
            "L",
            new Point3D(0,1,0));
        spt.check(
            new Point3D(2,0,1), 
            "R",
            new Point3D(2,1,2));
        spt.check(
            new Point3D(1,0,0), 
            "U",
            new Point3D(0,0,1));
        spt.check(
            new Point3D(1,2,0), 
            "D",
            new Point3D(2,2,1));
    }

    @Test
    void apostrophe_commands_rotate_the_face_couterclockwise() throws Exception {
        RubiksCube cube = new RubiksCube();
        SinglePointTester spt = new SinglePointTester(cube);
        spt.check(
            new Point3D(0,1,2),
            "B'",
            new Point3D(1,0,2)); 
        spt.check(
            new Point3D(2,1,0),
            "F'",
            new Point3D(1,0,0)); 
        spt.check(
            new Point3D(0,1,0),
            "L'",
            new Point3D(0,0,1)); 
        spt.check(
            new Point3D(2,1,2),
            "R'",
            new Point3D(2,0,1)); 
        spt.check(
            new Point3D(0,0,1),
            "U'",
            new Point3D(1,0,0)); 
        spt.check(
            new Point3D(2,2,1),
            "D'",
            new Point3D(1,2,0)); 
    }

    @Test
    void double_commands_rotate_the_face_180_degrees() throws Exception {
        RubiksCube cube = new RubiksCube();
        SinglePointTester spt = new SinglePointTester(cube);
        spt.check(
            new Point3D(1,0,2), 
            "B2",
            new Point3D(1,2,2));
        spt.check(
            new Point3D(1,0,0), 
            "F2",
            new Point3D(1,2,0));
        spt.check(
            new Point3D(0,0,1), 
            "L2",
            new Point3D(0,2,1));
        spt.check(
            new Point3D(2,0,1), 
            "R2",
            new Point3D(2,2,1));
        spt.check(
            new Point3D(1,0,0), 
            "U2",
            new Point3D(1,0,2));
        spt.check(
            new Point3D(2,2,1),
            "D2",
            new Point3D(0,2,1)); 
    }

    @Test
    void R_R_is_the_same_as_R2() throws Exception {
        RubiksCube cube = new RubiksCube();
        SequenceStringProcessor processor = new SequenceStringProcessor(cube);

        processor.accept("R R");
        String a = cube.get(2, 2, 1).getId();

        cube = new RubiksCube();
        processor = new SequenceStringProcessor(cube);

        processor.accept("R2");
        String b = cube.get(2, 2, 1).getId();

        assertEquals(a,b);
    }

}
