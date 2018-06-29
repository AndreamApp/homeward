package com.andreamapp.homeward.ui.widget;

import com.sun.javafx.application.PlatformImpl;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

/**
 * SwingFXWebView
 */
public class XWebView extends JPanel {

    private Stage stage;
    private WebView browser;
    private JFXPanel jfxPanel;
    private JButton swingButton;
    private WebEngine webEngine;

    public XWebView() {
        initComponents();
    }

    public static void main(String ...args){
        // Run this later:
        SwingUtilities.invokeLater(() -> {
            final JFrame frame = new JFrame();

            frame.getContentPane().add(new XWebView());

            frame.setMinimumSize(new Dimension(640, 480));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    private void initComponents(){
        jfxPanel = new JFXPanel();

        setLayout(new BorderLayout());
        add(jfxPanel, BorderLayout.CENTER);

        swingButton = new JButton();
        swingButton.addActionListener(e -> Platform.runLater(this::createScene));
        swingButton.setText("Reload");

        add(swingButton, BorderLayout.SOUTH);
    }

    /**
     * createScene
     *
     * Note: Key is that Scene needs to be created and run on "FX user thread"
     *       NOT on the AWT-EventQueue Thread
     *
     */
    private void createScene() {
        PlatformImpl.startup(() -> {
            stage = new Stage();

            stage.setTitle("Hello Java FX");
            stage.setResizable(true);

            Group root = new Group();
            Scene scene = new Scene(root, 80, 20);
            stage.setScene(scene);

            // Set up the embedded browser:
            browser = new WebView();
            webEngine = browser.getEngine();
            webEngine.load("https://www.baidu.com");

            ObservableList<Node> children = root.getChildren();
            children.add(browser);

            jfxPanel.setScene(scene);
        });
    }
}