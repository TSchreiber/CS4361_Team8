package t8;

import java.util.function.Consumer;
import java.util.Map;
import java.util.stream.Stream;

public class SequenceStringProcessor implements Consumer<String> {

    private RubiksCube cube;

    public SequenceStringProcessor(RubiksCube cube) {
        this.cube = cube;
    }

    public void accept(String sequenceString) {
        Stream.of(sequenceString.split(" "))
            .map(SequenceStringProcessor::toRotation)
            .forEach(r -> cube.rotate(r.face, r.angle));
    }

    private static final Map<Character,Face> charToFaceMap = Map.of(
        'B', Face.YELLOW,
        'D', Face.GREEN,
        'F', Face.WHITE,
        'L', Face.ORANGE,
        'R', Face.RED,
        'U', Face.BLUE );
    private static Rotation toRotation(String code) {
        Face face = charToFaceMap.get(code.charAt(0));
        int angle = -90;
        if (code.length() >= 2) {
            switch (code.charAt(1)) {
                case '2':
                    angle = 180;
                    break;

                case '\'':
                    angle = 90;
                    break;
            }
        }

        return new Rotation(face, angle);
    }

    private static class Rotation {

        Face face;
        int angle;

        public Rotation(Face face, int angle) {
            this.face = face;
            this.angle = angle;
        }

    }

}
