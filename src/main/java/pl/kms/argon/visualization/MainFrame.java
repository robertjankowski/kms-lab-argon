package pl.kms.argon.visualization;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;

import static pl.kms.argon.constants.Constants.N;

public class MainFrame implements GLEventListener, MouseMotionListener {

    private Queue<AtomSphere[]> atomSpheres = loadAtoms("pos.csv");
    private double mouseX; // TODO: add camera rotation
    private double mouseY;

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
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
            for (AtomSphere atom : atomSpheres.remove()) {
                atom.draw(gl);
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
            Queue<AtomSphere[]> atomSpheres = new LinkedList<>();
            AtomSphere[] atoms = new AtomSphere[(int) N.getValue()];
            while (line != null) {
                String[] positions = line.split(",");
                double x = Double.parseDouble(positions[0]);
                double y = Double.parseDouble(positions[1]);
                double z = Double.parseDouble(positions[2]);
                AtomSphere atom = new AtomSphere(x, y, z, 0.02);
                atoms[counter] = atom;
                if (counter++ == (int) N.getValue() - 1) {
                    atomSpheres.add(atoms);
                    counter = 0;
                    atoms = new AtomSphere[(int) N.getValue()];
                }
                line = br.readLine();
            }
            return atomSpheres;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        System.out.println(e.getX());
        System.out.println(e.getY());
        System.out.println();
    }
}
