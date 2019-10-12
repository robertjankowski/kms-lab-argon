package pl.kms.argon.visualization;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import static pl.kms.argon.constants.Constants.N;

public class MainFrame implements GLEventListener {

    private GLU glu = new GLU();
    private GLUquadric quad;
    private Queue<AtomSphere[]> atomSpheres = loadAtoms("pos.csv");

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        quad = glu.gluNewQuadric();
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        glu.gluDeleteQuadric(quad);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        final GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0f, 0f, 0f, 0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        if (!atomSpheres.isEmpty()) {
            System.out.println(atomSpheres.size());
            for (AtomSphere atom : atomSpheres.remove()) {
                atom.draw(gl, glu, quad);
            }
        }
        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {

    }

    private Queue<AtomSphere[]> loadAtoms(String filename) {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filename), StandardCharsets.US_ASCII)) {
            String line = br.readLine();
            int counter = 0;
            AtomSphere[] atoms = new AtomSphere[(int) N.getValue()];
            Queue<AtomSphere[]> atomSpheres = new LinkedList<>();
            while (line != null) {
                String[] positions = line.split(",");
//                double x = Double.parseDouble(positions[0]);
//                double y = Double.parseDouble(positions[1]);
//                double z = Double.parseDouble(positions[2]);
                Random r = new Random();
                double x = r.nextGaussian();
                double y = r.nextGaussian();
                double z = r.nextGaussian();
                AtomSphere atom = new AtomSphere(x, y, z, 0.02);
                atoms[counter] = atom;
                if (counter++ == (int) N.getValue() - 1) {
                    atomSpheres.add(atoms);
                    counter = 0;
                }
                line = br.readLine();
            }
            return atomSpheres;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
