package pl.kms.argon.visualization;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class AtomSphere {

    private double x, y, z;
    private double radius;
    private float[] color;
    private GLUT glut = new GLUT();

    public AtomSphere(double x, double y, double z, double radius) {
        this.x = transform(x);
        this.y = transform(y);
        this.z = transform(z);
        this.radius = radius;
        this.color = new float[]{1, 0, 0, 1};
    }

    public void setColor(float[] color) {
        this.color = color;
    }

    private double transform(double p) {
        // Scale to [-1, 1]
        double max = 3;
        double min = -3;
        return 2 * (p - min) / (max - min) - 1;
    }

    public void draw(GL2 gl2, boolean isSolid) {
        // Prepare light parameters.
        float SHINE_ALL_DIRECTIONS = 1;
        float[] lightPos = {-50, 0, 0, SHINE_ALL_DIRECTIONS};
        float[] lightColorAmbient = {0.2f, 0.2f, 0.2f, 1f};
        float[] lightColorSpecular = {0.8f, 0.8f, 0.8f, 1f};

        // Set light parameters.
        gl2.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos, 0);
        gl2.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightColorAmbient, 0);
        gl2.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightColorSpecular, 0);

        // Enable lighting in GL.
        gl2.glEnable(GL2.GL_LIGHT1);
        gl2.glEnable(GL2.GL_LIGHTING);

        // Set material properties.
        gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, color, 0);
        gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, color, 0);
        gl2.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 0.5f);

        gl2.glLoadIdentity();
        gl2.glTranslated(x, y, z);
        if (isSolid)
            glut.glutSolidSphere(radius, 10, 10);
        else {
            gl2.glRotatef(45, 1, 0, 0);
            glut.glutWireSphere(radius, 20, 20);
        }
    }

    @Override
    public String toString() {
        return "AtomSphere{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
