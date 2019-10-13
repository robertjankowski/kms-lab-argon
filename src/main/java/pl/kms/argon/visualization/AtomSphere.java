package pl.kms.argon.visualization;

import com.jogamp.opengl.GL2;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class AtomSphere {

    private double x, y, z;
    private double radius;
    private float[] color;

    public AtomSphere(double x, double y, double z, double radius) {
        this.x = transform(x);
        this.y = transform(y);
        this.z = transform(z);
        this.radius = radius;
        this.color = new float[]{1, 0, 0};
    }

    private double transform(double p) {
        // Scale to [-1, 1]
        double max = 5;
        double min = -5;
        return 2 * (p - min) / (max - min) - 1;
    }

    public void draw(GL2 gl2) {
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
        drawSphere(gl2);
    }

    private void drawSphere(GL2 gl2) {
        double x, y, z;
        double gradation = 10;
        for (double alpha = 0.0; alpha < Math.PI; alpha += Math.PI / gradation) {
            gl2.glBegin(GL2.GL_TRIANGLE_STRIP);
            for (double beta = 0.0; beta < 2.01 * Math.PI; beta += Math.PI / gradation) {
                x = radius * cos(beta) * sin(alpha);
                y = radius * sin(beta) * sin(alpha);
                z = radius * cos(alpha);
                gl2.glVertex3d(x, y, z);
                x = radius * cos(beta) * sin(alpha + Math.PI / gradation);
                y = radius * sin(beta) * sin(alpha + Math.PI / gradation);
                z = radius * cos(alpha + Math.PI / gradation);
                gl2.glVertex3d(x, y, z);
            }
            gl2.glEnd();
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
