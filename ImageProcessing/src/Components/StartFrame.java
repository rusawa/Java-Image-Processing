package Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Objects;

public class StartFrame extends JFrame {
    private final JPanel backgroundPanel;
    private final JPanel startPanel;
    private final Color backgroundColor;
    private final Color blue;
    private final JLabel labelStart1, labelStart2, labelStart3;
    private final JButton startButton;
    private TreePath imagePath = null;
    private JLabel errorMessage;

    public StartFrame(){
        super("Image app demo");
        this.backgroundPanel = new JPanel();
        this.startPanel = new JPanel();
        this.backgroundColor = new Color(30, 30, 30);
        this.blue = new Color(42, 90, 162);
        this.labelStart1 = new JLabel("Choose an image to start");
        this.labelStart2 = new JLabel("Image should be located in \"C:/IMG\" folder");
        this.labelStart3 = new JLabel("Accepted format: .jpg");
        this.startButton = new JButton("Start");
        this.errorMessage = new JLabel(" ");

        errorMessage.setForeground(Color.red);
        errorMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

        labelStart1.setFont(new Font("Arial", Font.PLAIN, 20));
        labelStart1.setForeground(Color.lightGray);
        labelStart1.setAlignmentX(Component.CENTER_ALIGNMENT);

        labelStart2.setFont(new Font("Arial", Font.PLAIN, 15));
        labelStart2.setForeground(Color.gray);
        labelStart2.setAlignmentX(Component.CENTER_ALIGNMENT);

        labelStart3.setFont(new Font("Arial", Font.PLAIN, 15));
        labelStart3.setForeground(Color.gray);
        labelStart3.setAlignmentX(Component.CENTER_ALIGNMENT);

        startButton.setBackground(blue);
        startButton.setForeground(Color.lightGray);
        startButton.setBorderPainted(false);
        startButton.setFocusable(false);
        startButton.setFont(new Font("Arial", Font.PLAIN, 15));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        startPanel.setBackground(backgroundColor);
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));
        startPanel.setMaximumSize(new Dimension(300, 110));
        startPanel.add(labelStart1);
        startPanel.add(Box.createVerticalGlue());
        startPanel.add(labelStart2);
        startPanel.add(labelStart3);
        startPanel.add(Box.createVerticalGlue());
        startPanel.add(startButton);

        backgroundPanel.setBackground(backgroundColor);
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.X_AXIS));
        backgroundPanel.add(Box.createHorizontalGlue());
        backgroundPanel.add(startPanel, BorderLayout.CENTER);
        backgroundPanel.add(Box.createHorizontalGlue());

        add(backgroundPanel);

        startButton.addActionListener((ActionEvent e) -> {
            ChooseImageFrame inputTree = new ChooseImageFrame();
            inputTree.setLocationRelativeTo(null);
            startButton.setEnabled(false);
            inputTree.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    startButton.setEnabled(true);
                }
            });
        });


        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(800, 500);
    }

    class ChooseImageFrame extends JDialog {
        private final JScrollPane scrollPane;
        private final DefaultMutableTreeNode root;
        private final JPanel backgroundPanel, buttonPanel;
        private final JButton cancelButton, okButton;
        private DefaultTreeCellRenderer treeCellRenderer = new DefaultTreeCellRenderer();

        ChooseImageFrame(){
            this.root = new DefaultMutableTreeNode("C:/IMG");
            File file = new File("C:/IMG");
            for(File x : Objects.requireNonNull(file.listFiles()))
                root.add(new DefaultMutableTreeNode(x.getName()));
            JTree tree = new JTree(root);
            this.scrollPane = new JScrollPane(tree);
            this.buttonPanel = new JPanel();
            this.backgroundPanel = new JPanel();
            this.cancelButton = new JButton("Cancel");
            this.okButton = new JButton("Open");

            treeCellRenderer.setBackgroundNonSelectionColor(backgroundColor);
            treeCellRenderer.setTextNonSelectionColor(Color.lightGray);
            tree.setCellRenderer(treeCellRenderer);
            tree.setBackground(backgroundColor);

            scrollPane.setHorizontalScrollBar(null);
            scrollPane.setBorder(null);
            scrollPane.setMaximumSize(new Dimension(400, 100));

            cancelButton.setBackground(blue);
            cancelButton.setForeground(Color.lightGray);
            cancelButton.setBorderPainted(false);
            cancelButton.setFocusable(false);
            cancelButton.setFont(new Font("Arial", Font.PLAIN, 15));
            cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            cancelButton.setMaximumSize(new Dimension(50, 30));

            okButton.setBackground(blue);
            okButton.setForeground(Color.lightGray);
            okButton.setBorderPainted(false);
            okButton.setFocusable(false);
            okButton.setFont(new Font("Arial", Font.PLAIN, 15));
            okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            okButton.setMaximumSize(new Dimension(50, 30));

            buttonPanel.setBackground(backgroundColor);
            buttonPanel.setMaximumSize(new Dimension(400, 50));
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
            buttonPanel.add(Box.createHorizontalGlue());
            buttonPanel.add(okButton);
            buttonPanel.add(Box.createHorizontalGlue());
            buttonPanel.add(cancelButton);
            buttonPanel.add(Box.createHorizontalGlue());
            buttonPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

            backgroundPanel.setBackground(backgroundColor);
            backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
            backgroundPanel.add(scrollPane);
            backgroundPanel.add(Box.createRigidArea(new Dimension(10, 5)));
            backgroundPanel.add(errorMessage);
            backgroundPanel.add(buttonPanel);

            add(backgroundPanel);

            cancelButton.addActionListener((ActionEvent e) -> {
                this.dispose();
                startButton.setEnabled(true);
                errorMessage.setText("");
            });

            okButton.addActionListener((ActionEvent e) -> {
                if(!tree.isSelectionEmpty()) {
                    imagePath = tree.getSelectionPath();
                    if(imagePath != null){
                        String imagePathToString = imagePath.toString();
                        try {
                            if(!(imagePathToString.endsWith(".jpg]")) && !(imagePathToString.endsWith(".png]"))){
                                throw new Exception("Unsupported file format");
                            } else {
                                this.dispose();
                                getStartFrame().dispose();
                                ImageFrame imageFrame = new ImageFrame(imagePath);
                                imageFrame.setLocationRelativeTo(null);
                            }
                        } catch(Exception ex){
                            System.out.println(ex.getMessage());
                            errorMessage.setText(ex.getMessage());
                        }

                    }
                }
            });


            setTitle("Choose an image");
            getContentPane().setBackground(backgroundColor);
            setResizable(false);
            setSize(350, 400);
            setVisible(true);
        }
    }

    private StartFrame getStartFrame(){
        return StartFrame.this;
    }
}
