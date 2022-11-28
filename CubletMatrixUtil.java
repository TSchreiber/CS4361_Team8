package t8;

import t8.RubiksCube.Cublet;

public class CubletMatrixUtil {

    public static Cublet[][] rotateClockwise(Cublet[][] mat) {
        return reverseColumns(transverse(mat));
    }

    public static Cublet[][] rotateCounterClockwise(Cublet[][] mat) {
        return transverse(reverseColumns(mat));
    }

    public static Cublet[][] rotate180(Cublet[][] mat) {
        return rotateClockwise(rotateClockwise(mat));
    }

    public static Cublet[][] transverse(Cublet[][] mat) {
        int n = mat.length;
        Cublet[][] out = new Cublet[n][n];
        for (int i=0; i<n; i++) {
            for (int j=0; j<n; j++) {
                out[i][j] = mat[j][i];
            }
        }
        return out;
    }

    public static Cublet[][] reverseColumns(Cublet[][] mat) {
        int m = mat.length;
        int n = mat[0].length;
        Cublet[][] out = new Cublet[m][n];
        for (int i=0; i<m; i++) {
            for (int j=0; j<n; j++) {
                out[i][j] = mat[i][n-j-1];
            }
        }
        return out;
    }

}
