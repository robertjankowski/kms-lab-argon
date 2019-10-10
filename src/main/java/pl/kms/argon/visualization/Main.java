package pl.kms.argon.visualization;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        GLProfile glProfile = GLProfile.getGL2ES2();
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        Frame frame = new Frame();
        GLCanvas canvas = new GLCanvas(glCapabilities);
        frame.setUndecorated(true);
        frame.add(canvas);
        frame.setSize(100, 100);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
