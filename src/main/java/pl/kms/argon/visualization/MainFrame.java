package pl.kms.argon.visualization;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainFrame implements GLEventListener, MouseMotionListener {

    private List<AtomSphere[]> atomSpheres = loadAtoms("output/5_1_pos.csv");
    private TextRenderer timeRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 36));
    private final int N = 27;
    private final double tau = 1e-2;
    private String tauFormat = "0.00";
    private double timestep = 0.0; // ps
    private int iteration = 0;

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        System.out.println("Atoms: " + atomSpheres.size() + "   in array:" + atomSpheres.get(0).length);
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

        renderTimestep();
        if (iteration < atomSpheres.size())
            for (AtomSphere atom : atomSpheres.get(iteration))
                atom.draw(gl);
        else
            resetSimulation();
        iteration++;
        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
    }

    private void resetSimulation() {
        iteration = 0;
        timestep = 0.0;
    }

    private String getRoundedTimestep(double timestep, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(timestep);
    }

    private void renderTimestep() {
        timeRenderer.beginRendering(800, 800);
        timeRenderer.setColor(1.0f, 0.2f, 0.2f, 0.8f);
        timeRenderer.draw("t = " + getRoundedTimestep(timestep, tauFormat) + "ps", 100, 100);
        timeRenderer.endRendering();
        timestep += tau;
    }

    private List<AtomSphere[]> loadAtoms(String filename) {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filename), StandardCharsets.US_ASCII)) {
            String line = br.readLine();
            int counter = 0;
            List<AtomSphere[]> atomSpheres = new ArrayList<>();
            AtomSphere[] atoms = new AtomSphere[N];
            while (line != null) {
                String[] positions = line.split(",");
                double x = Double.parseDouble(positions[0]);
                double y = Double.parseDouble(positions[1]);
                double z = Double.parseDouble(positions[2]);
                AtomSphere atom = new AtomSphere(x, y, z, 0.05);
                atoms[counter] = atom;
                if (counter++ == N - 1) {
                    atomSpheres.add(atoms);
                    counter = 0;
                    atoms = new AtomSphere[N];
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
    }
}
