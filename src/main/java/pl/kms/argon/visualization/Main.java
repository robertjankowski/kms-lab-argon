package pl.kms.argon.visualization;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;

import static javax.swing.WindowConstants.*;

public class Main {
    public static void main(String[] args) {
        GLProfile glProfile = GLProfile.get(GLProfile.GL2);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);

        GLCanvas canvas = new GLCanvas(glCapabilities);
        MainFrame mainFrame = new MainFrame();
        canvas.addGLEventListener(mainFrame);
        canvas.setSize(800, 800);

        JFrame frame = new JFrame("Simulation");
        frame.getContentPane().add(canvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        final FPSAnimator animator = new FPSAnimator(canvas, 400, true);
        animator.start();
    }
}
