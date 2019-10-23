package pl.kms.argon.visualization;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class Main {
    public static void main(String[] args) {
        GLProfile glProfile = GLProfile.get(GLProfile.GL2);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);

        GLCanvas canvas = new GLCanvas(glCapabilities);
        MainFrame mainFrame = new MainFrame();
        canvas.addMouseMotionListener(mainFrame);
        canvas.addGLEventListener(mainFrame);
        canvas.setSize(800, 800);

        JFrame frame = new JFrame("Simulation");
        frame.getContentPane().add(canvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        final FPSAnimator animator = new FPSAnimator(canvas, 400, false);
        animator.start();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (animator.isStarted())
                    animator.stop();
                System.exit(0);
            }
        });
    }
}
