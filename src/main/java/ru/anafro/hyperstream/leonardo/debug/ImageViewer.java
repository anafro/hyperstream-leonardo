package ru.anafro.hyperstream.leonardo.debug;

import ru.anafro.hyperstream.leonardo.utils.platform.Platform;

import javax.swing.*;
import java.awt.*;

import static ru.anafro.hyperstream.leonardo.debug.Debug.$;

public class ImageViewer {
    private final JFrame frame;
    private final JLabel imageView;

    public ImageViewer() {
        if (ImageViewer.isUnsupported()) {
            System.err.println("Your " + Platform.name() + " does not support image viewers. It's fine, we'll just ignore viewer.show() calls! We continue working...");
            this.frame = null;
            this.imageView = null;
            return;
        }

        final var panel = new JPanel(new BorderLayout());

        this.frame = new JFrame("A generated profile image");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);
        this.frame.setContentPane(panel);
        this.imageView = new JLabel();
        panel.add(imageView, BorderLayout.CENTER);
    }

    public void show(Image image) {
        if (ImageViewer.isUnsupported()) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            final var icon = new ImageIcon(image);
            this.imageView.setIcon(icon);
            this.frame.setIconImage(image);
            this.frame.pack();
            this.frame.setLocationRelativeTo(null /* means "to screen center" */);
            this.frame.setVisible(true);
            this.frame.requestFocus();
            this.frame.requestFocusInWindow();
        });
    }

    private static boolean isUnsupported() {
        return GraphicsEnvironment.isHeadless();
    }
}
