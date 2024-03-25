package Components;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ImageFrame extends JFrame {
    private BufferedImage imageOriginal = null;
    private BufferedImage resizedImage;
    private BufferedImage savePreviewImage;
    private BufferedImage processedImage;
    private JLabel imageLabel;
    private final JPanel backgroundPanel, errorPanel, imagePanel, toolsPanel;
    private final Color backgroundColor, blue, gray, red;
    private final JLabel errorMessage;
    private JDialog changePreview;
    private boolean grayScaleFlag = false;
    private boolean negativeFlag = false;
    private final JSlider brightnessSlider = new JSlider();
    private final JSlider redSlider = new JSlider();
    private final JSlider greenSlider = new JSlider();
    private final JSlider blueSlider = new JSlider();
    private final JSlider pixelateSlider = new JSlider();
    private final JSlider hueSlider = new JSlider();
    private final JCheckBox grayScale = new JCheckBox("Gray Scale");
    private final JCheckBox negative = new JCheckBox("Negative");
    private final JButton saveButton = new JButton("Save");

    public ImageFrame(TreePath imagePath){
        super("Image processing");
        String imagePathToString = imagePath.toString();
        imagePathToString = imagePathToString.substring(9, imagePathToString.length()-1);
        File imageFile = new File("C:/IMG/" + imagePathToString);
        this.backgroundPanel = new JPanel();
        this.backgroundColor = new Color(30, 30, 30);
        this.blue = new Color(42, 90, 162);
        this.gray = new Color(40,40,40);
        this.red = new Color(196,21,8);
        this.errorPanel = new JPanel();
        this.errorMessage = new JLabel();
        this.imagePanel = new JPanel();
        this.toolsPanel = new JPanel();

        try {
            this.imageOriginal = ImageIO.read(imageFile);
            this.processedImage = ImageIO.read(imageFile);
        } catch(IOException e){
            System.out.println("Failed to load an image");
            errorMessage();
        }

        if(imageOriginal != null){
            imageLabel = new JLabel();
            resizedImage = fitImage(imageOriginal,1024,576);
            imageLabel.setIcon(new ImageIcon(resizedImage));
            displayImageWindow();
            displayToolsWindow();
        }


        backgroundPanel.setBackground(backgroundColor);
        add(backgroundPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void errorMessage(){
        JButton errorButton = new JButton("Close");
        errorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorButton.setBackground(red);
        errorButton.setForeground(Color.white);
        errorButton.setBorderPainted(false);
        errorButton.setFocusable(false);
        errorButton.setFont(new Font("Arial", Font.PLAIN, 15));

        errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));
        errorPanel.add(errorMessage);
        errorPanel.add(Box.createVerticalGlue());
        errorPanel.add(errorButton);
        errorPanel.setBackground(backgroundColor);
        errorMessage.setForeground(red);
        errorMessage.setFont(new Font("Arial", Font.PLAIN, 20));
        errorMessage.setText("Failed to load an image");
        errorMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
        backgroundPanel.add(Box.createVerticalGlue());
        backgroundPanel.add(errorPanel);
        backgroundPanel.add(Box.createVerticalGlue());

        errorButton.addActionListener((ActionEvent ae) -> this.dispose());

        setSize(300,200);
    }

    private void displayImageWindow(){
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.X_AXIS));
        imagePanel.setMaximumSize(new Dimension(resizedImage.getWidth()+40, resizedImage.getHeight()+40));
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));
        imagePanel.setBorder(new EmptyBorder(20, 20, 20,20));
        imagePanel.setBackground(gray);
        imagePanel.add(Box.createVerticalGlue());
        imagePanel.add(imageLabel);
        imagePanel.add(Box.createVerticalGlue());
        backgroundPanel.add(Box.createHorizontalGlue());
        backgroundPanel.add(imagePanel);
        backgroundPanel.add(Box.createHorizontalGlue());

        if(resizedImage.getWidth() - resizedImage.getHeight() >= 0){
            setSize(1300, 750);
        } else {
            setSize(800, 750);
        }
    }

    private void displayToolsWindow(){
        AtomicInteger brightnessValue = new AtomicInteger(0);
        AtomicInteger redValue = new AtomicInteger(0);
        AtomicInteger greenValue = new AtomicInteger(0);
        AtomicInteger blueValue = new AtomicInteger(0);
        AtomicInteger pixelateValue = new AtomicInteger(0);
        AtomicInteger hueValue = new AtomicInteger(0);
        JPanel buttonsPanel = new JPanel();
        ToolsLabel brightnessLabel = new ToolsLabel("Brightness");
        ToolsLabel colorBalanceLabel = new ToolsLabel("Color Balance");
        ToolsLabel pixelateLabel = new ToolsLabel("Pixelate");
        ToolsLabel hueLabel = new ToolsLabel("Hue");

        toolsPanel.setBackground(gray);
        toolsPanel.setMaximumSize(new Dimension(350, 750));
        toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.Y_AXIS));
        toolsPanel.add(Box.createRigidArea(new Dimension(50, 100)));
        toolsPanel.add(buttonsPanel);
        toolsPanel.add(Box.createRigidArea(new Dimension(50, 100)));
        toolsPanel.add(saveButton);
        toolsPanel.add(Box.createRigidArea(new Dimension(50, 100)));

        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonsPanel.add(brightnessLabel);
        buttonsPanel.add(brightnessSlider);
        buttonsPanel.add(Box.createVerticalGlue());
        buttonsPanel.add(colorBalanceLabel);
        buttonsPanel.add(redSlider);
        buttonsPanel.add(greenSlider);
        buttonsPanel.add(blueSlider);
        buttonsPanel.add(Box.createVerticalGlue());
        buttonsPanel.add(pixelateLabel);
        buttonsPanel.add(pixelateSlider);
        buttonsPanel.add(Box.createVerticalGlue());
        buttonsPanel.add(hueLabel);
        buttonsPanel.add(hueSlider);
        buttonsPanel.add(Box.createVerticalGlue());
        buttonsPanel.add(grayScale);
        buttonsPanel.add(Box.createVerticalGlue());
        buttonsPanel.add(negative);
        buttonsPanel.setMaximumSize(new Dimension(300, 600));
        buttonsPanel.setBackground(gray);

        brightnessSlider.setValue(0);
        brightnessSlider.setMinimum(-20);
        brightnessSlider.setMaximum(20);
        brightnessSlider.setBackground(gray);

        redSlider.setValue(0);
        redSlider.setMinimum(-100);
        redSlider.setMaximum(100);
        redSlider.setBackground(gray);

        greenSlider.setValue(0);
        greenSlider.setMinimum(-100);
        greenSlider.setMaximum(100);
        greenSlider.setBackground(gray);

        blueSlider.setValue(0);
        blueSlider.setMinimum(-100);
        blueSlider.setMaximum(100);
        blueSlider.setBackground(gray);

        grayScale.setAlignmentX(Component.CENTER_ALIGNMENT);
        grayScale.setBackground(gray);
        grayScale.setFont(new Font("Arial", Font.PLAIN, 15));
        grayScale.setForeground(Color.lightGray);
        grayScale.setFocusable(false);

        negative.setAlignmentX(Component.CENTER_ALIGNMENT);
        negative.setBackground(gray);
        negative.setFont(new Font("Arial", Font.PLAIN, 15));
        negative.setForeground(Color.lightGray);
        negative.setFocusable(false);

        pixelateSlider.setValue(0);
        pixelateSlider.setMinimum(0);
        pixelateSlider.setMaximum(30);
        pixelateSlider.setBackground(gray);

        hueSlider.setValue(0);
        hueSlider.setMinimum(-180);
        hueSlider.setMaximum(180);
        hueSlider.setBackground(gray);

        saveButton.setBackground(blue);
        saveButton.setForeground(Color.lightGray);
        saveButton.setBorderPainted(false);
        saveButton.setFocusable(false);
        saveButton.setFont(new Font("Arial", Font.PLAIN, 15));
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setMaximumSize(new Dimension(100, 30));

        backgroundPanel.add(toolsPanel);

        brightnessSlider.addChangeListener((ChangeEvent e) -> {
            brightnessValue.set(brightnessSlider.getValue());
            displayPreview(brightnessValue, redValue, greenValue, blueValue, pixelateValue, hueValue, imageLabel, resizedImage);
        });

        redSlider.addChangeListener((ChangeEvent e) -> {
            redValue.set(redSlider.getValue());
            sliderColor(redValue.intValue(), 'r', redSlider);
            displayPreview(brightnessValue, redValue, greenValue, blueValue, pixelateValue, hueValue, imageLabel, resizedImage);
        });

        greenSlider.addChangeListener((ChangeEvent e) -> {
            greenValue.set(greenSlider.getValue());
            sliderColor(greenValue.intValue(), 'g', greenSlider);
            displayPreview(brightnessValue, redValue, greenValue, blueValue, pixelateValue, hueValue, imageLabel, resizedImage);
        });

        blueSlider.addChangeListener((ChangeEvent e) -> {
            blueValue.set(blueSlider.getValue());
            sliderColor(blueValue.intValue(), 'b', blueSlider);
            displayPreview(brightnessValue, redValue, greenValue, blueValue, pixelateValue, hueValue, imageLabel, resizedImage);
        });

        grayScale.addActionListener((ActionEvent e) -> {
            boolean state = grayScaleFlag;
            grayScaleFlag = !state;
            displayPreview(brightnessValue, redValue, greenValue, blueValue, pixelateValue, hueValue, imageLabel, resizedImage);
        });

        negative.addActionListener((ActionEvent e) -> {
            boolean state = negativeFlag;
            negativeFlag = !state;
            displayPreview(brightnessValue, redValue, greenValue, blueValue, pixelateValue, hueValue, imageLabel, resizedImage);
        });

        pixelateSlider.addChangeListener((ChangeEvent e) -> {
            pixelateValue.set(pixelateSlider.getValue());
            displayPreview(brightnessValue, redValue, greenValue, blueValue, pixelateValue, hueValue, imageLabel, resizedImage);
        });

        hueSlider.addChangeListener((ChangeEvent e) -> {
            hueValue.set(hueSlider.getValue());
            displayPreview(brightnessValue, redValue, greenValue, blueValue, pixelateValue, hueValue, imageLabel, resizedImage);
        });

        saveButton.addActionListener((ActionEvent e) -> {
            savePreview(brightnessValue, redValue, greenValue, blueValue, pixelateValue, hueValue);
            saveButton.setEnabled(false);
            brightnessSlider.setEnabled(false);
            redSlider.setEnabled(false);
            greenSlider.setEnabled(false);
            blueSlider.setEnabled(false);
            pixelateSlider.setEnabled(false);
            hueSlider.setEnabled(false);
            grayScale.setEnabled(false);
            negative.setEnabled(false);
        });
    }

    private void displayPreview(AtomicInteger brightness, AtomicInteger red, AtomicInteger green, AtomicInteger blue,
                                AtomicInteger pixelateValue, AtomicInteger hueValue, JLabel imageLabel,
                                BufferedImage originalImage){
        BufferedImage imagePreview = originalImage;
        imagePreview = colorFilter(imagePreview, 'r', red.get());
        imagePreview = colorFilter(imagePreview, 'g', green.get());
        imagePreview = colorFilter(imagePreview, 'b', blue.get());
        imagePreview = adjustHue(imagePreview, hueValue.floatValue());
        if(pixelateValue.get() != 0) imagePreview = pixelate(imagePreview, pixelateValue.get());
        imagePreview = adjustBrightness(imagePreview, brightness.get());
        if(grayScaleFlag) imageLabel.setIcon(new ImageIcon(toGrayScale(imagePreview)));
        else imageLabel.setIcon(new ImageIcon(imagePreview));
        if(negativeFlag) imageLabel.setIcon(new ImageIcon(negative(imagePreview)));
    }

    private void sliderColor(int value, char color, JSlider slider){
        switch (color) {
            case 'r' -> {
                if (value <= (-40)) {
                    slider.setBackground(new Color(0, 40 - value, 40 - value));
                } else if (value < 0) {
                    slider.setBackground(new Color(40 + value, 40 - value, 40 - value));
                } else if (value == 0) {
                    slider.setBackground(new Color(40, 40, 40));
                } else if (value < 40) {
                    slider.setBackground(new Color(40 + value, 40 - value, 40 - value));
                } else if (value > 40) {
                    slider.setBackground(new Color(40 + value, 0, 0));
                }
            }
            case 'g' -> {
                if (value <= (-40)) {
                    slider.setBackground(new Color(40 - value, 0, 40 - value));
                } else if (value < 0) {
                    slider.setBackground(new Color(40 - value, 40 + value, 40 - value));
                } else if (value == 0) {
                    slider.setBackground(new Color(40, 40, 40));
                } else if (value < 40) {
                    slider.setBackground(new Color(40 - value, 40 + value, 40 - value));
                } else if (value > 40) {
                    slider.setBackground(new Color(0, 40 + value, 0));
                }
            }
            case 'b' -> {
                if (value <= (-40)) {
                    slider.setBackground(new Color(40 - value, 40 - value, 0));
                } else if (value < 0) {
                    slider.setBackground(new Color(40 - value, 40 - value, 40 + value));
                } else if (value == 0) {
                    slider.setBackground(new Color(40, 40, 40));
                } else if (value < 40) {
                    slider.setBackground(new Color(40 - value, 40 - value, 40 + value));
                } else if (value > 40) {
                    slider.setBackground(new Color(0, 0, 40 + value));
                }
            }
        }

    }

    private void exportImage(AtomicInteger brightness, AtomicInteger redValue, AtomicInteger greenValue, AtomicInteger blueValue,
                             AtomicInteger pixelateValue, AtomicInteger hueValue, String fileName) throws IOException {
        BufferedImage image = processedImage;
        int pixelationRatio = imageOriginal.getWidth() / resizedImage.getWidth();
        pixelationRatio = pixelationRatio * pixelateValue.get();

        image = colorFilter(image, 'r', redValue.get());
        image = colorFilter(image, 'g', greenValue.get());
        image = colorFilter(image, 'b', blueValue.get());
        image = adjustHue(image, hueValue.floatValue());
        if(pixelateValue.get() != 0) image = pixelate(image, pixelationRatio);
        image = adjustBrightness(image, brightness.get());
        if(grayScaleFlag) image = toGrayScale(image);
        if(negativeFlag) image = negative(image);

        File outputFile = new File("C:/IMG/OUT/" + fileName + ".jpg");
        ImageIO.write(image, "jpg", outputFile);
    }

    private void savePreview(AtomicInteger brightness, AtomicInteger redValue, AtomicInteger greenValue, AtomicInteger blueValue,
                             AtomicInteger pixelateValue, AtomicInteger hueValue){
        JPanel background = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel namePanel = new JPanel();
        ToolsLabel nameLabel = new ToolsLabel("File name: ");
        ToolsLabel errorLabel = new ToolsLabel(" ");
        JTextField nameTextField = new JTextField();
        JButton apply = new JButton("Apply");
        JButton cancel = new JButton("Cancel");
        JPanel imagePreviewPanel = new JPanel();
        JLabel imagePreviewLabel = new JLabel();
        JRadioButton originalRadioButton = new JRadioButton("Original");
        JRadioButton processedRadioButton = new JRadioButton("Processed");
        ButtonGroup radioButtonGroup = new ButtonGroup();
        AtomicReference<String> fileName = new AtomicReference<>("");
        JDialog resultMessageFrame = new JDialog();


        if(resizedImage.getWidth() - resizedImage.getHeight() >= 0){
            savePreviewImage = fitImage(imageOriginal, 550, 309);
        } else {
            savePreviewImage = fitImage(imageOriginal, 309, 550);
        }

        imagePreviewLabel.setBorder(new LineBorder(blue, 3));

        imagePreviewPanel.setMaximumSize(new Dimension(savePreviewImage.getWidth()+50, savePreviewImage.getHeight()+20));
        imagePreviewPanel.setBackground(backgroundColor);
        imagePreviewPanel.add(imagePreviewLabel);

        apply.setBackground(blue);
        apply.setForeground(Color.lightGray);
        apply.setBorderPainted(false);
        apply.setFocusable(false);
        apply.setFont(new Font("Arial", Font.PLAIN, 15));
        apply.setAlignmentX(Component.CENTER_ALIGNMENT);
        apply.setMaximumSize(new Dimension(50, 30));

        cancel.setBackground(red);
        cancel.setForeground(Color.lightGray);
        cancel.setBorderPainted(false);
        cancel.setFocusable(false);
        cancel.setFont(new Font("Arial", Font.PLAIN, 15));
        cancel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancel.setMaximumSize(new Dimension(50, 30));


        originalRadioButton.setBackground(backgroundColor);
        originalRadioButton.setFont(new Font("Arial", Font.PLAIN, 15));
        originalRadioButton.setForeground(Color.lightGray);
        originalRadioButton.setFocusable(false);

        processedRadioButton.setBackground(backgroundColor);
        processedRadioButton.setFont(new Font("Arial", Font.PLAIN, 15));
        processedRadioButton.setForeground(Color.lightGray);
        processedRadioButton.setFocusable(false);
        processedRadioButton.setSelected(true);

        namePanel.add(nameLabel);
        namePanel.add(Box.createRigidArea(new Dimension(20, 10)));
        namePanel.add(nameTextField);
        namePanel.add(Box.createRigidArea(new Dimension(20, 10)));
        namePanel.add(errorLabel);
        namePanel.setBackground(backgroundColor);
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));

        errorLabel.setForeground(red);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        nameTextField.setMaximumSize(new Dimension(200, 20));

        radioButtonGroup.add(originalRadioButton);
        radioButtonGroup.add(processedRadioButton);

        buttonPanel.setBackground(backgroundColor);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setMaximumSize(new Dimension(580, 70));
        buttonPanel.setBorder(new EmptyBorder(20,20,20,20));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(originalRadioButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        buttonPanel.add(processedRadioButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(cancel);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 10)));
        buttonPanel.add(apply);

        background.setBackground(backgroundColor);
        background.setBorder(new EmptyBorder(10, 0,0,0));
        background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
        background.add(imagePreviewPanel);
        background.add(Box.createVerticalGlue());
        background.add(namePanel);
        background.add(Box.createVerticalGlue());
        background.add(buttonPanel);



        ToolsLabel resultLabel = new ToolsLabel("");
        JPanel resultPanel = new JPanel();
        JPanel resultButtonsPanel = new JPanel();
        JButton exitButton = new JButton("Exit");
        JButton continueButton = new JButton("Continue");

        resultMessageFrame.setSize(350, 250);
        resultMessageFrame.setResizable(false);
        resultMessageFrame.setLocationRelativeTo(null);
        resultMessageFrame.setLayout(new BorderLayout());
        resultMessageFrame.add(resultPanel);
        resultMessageFrame.isAlwaysOnTop();

        exitButton.setBackground(red);
        exitButton.setForeground(Color.lightGray);
        exitButton.setBorderPainted(false);
        exitButton.setFocusable(false);
        exitButton.setFont(new Font("Arial", Font.PLAIN, 15));
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setMaximumSize(new Dimension(100, 30));

        continueButton.setBackground(blue);
        continueButton.setForeground(Color.lightGray);
        continueButton.setBorderPainted(false);
        continueButton.setFocusable(false);
        continueButton.setFont(new Font("Arial", Font.PLAIN, 15));
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.setMaximumSize(new Dimension(100, 30));

        resultButtonsPanel.setBackground(backgroundColor);
        resultButtonsPanel.setLayout(new BoxLayout(resultButtonsPanel, BoxLayout.X_AXIS));
        resultButtonsPanel.add(exitButton);

        resultPanel.setBackground(backgroundColor);
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.add(Box.createVerticalGlue());
        resultPanel.add(resultLabel);
        resultPanel.add(Box.createRigidArea(new Dimension(20, 20)));
        resultPanel.add(resultButtonsPanel);
        resultPanel.add(Box.createVerticalGlue());
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 15));


        changePreview = new JDialog();
        changePreview.setTitle("Save Preview");
        changePreview.setBackground(backgroundColor);
        if(resizedImage.getWidth() - resizedImage.getHeight() >= 0){
            changePreview.setSize(580, 480);
        } else {
            changePreview.setSize(480, 580);
        }

        changePreview.addWindowListener(new WindowAdapter(){
            public void windowOpened(WindowEvent e){
                displayPreview(brightness, redValue, greenValue, blueValue, pixelateValue, hueValue, imagePreviewLabel, savePreviewImage);
            }
        });

        changePreview.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                saveButton.setEnabled(true);
                brightnessSlider.setEnabled(true);
                redSlider.setEnabled(true);
                greenSlider.setEnabled(true);
                blueSlider.setEnabled(true);
                pixelateSlider.setEnabled(true);
                hueSlider.setEnabled(true);
                grayScale.setEnabled(true);
                negative.setEnabled(true);
            }
        });

        cancel.addActionListener((ActionEvent e) -> {
            saveButton.setEnabled(true);
            brightnessSlider.setEnabled(true);
            redSlider.setEnabled(true);
            greenSlider.setEnabled(true);
            blueSlider.setEnabled(true);
            pixelateSlider.setEnabled(true);
            hueSlider.setEnabled(true);
            grayScale.setEnabled(true);
            negative.setEnabled(true);
            changePreview.dispose();
        });

        originalRadioButton.addActionListener((ActionEvent e) -> imagePreviewLabel.setIcon(new ImageIcon(savePreviewImage)));

        processedRadioButton.addActionListener((ActionEvent e) -> displayPreview(brightness, redValue, greenValue, blueValue, pixelateValue, hueValue, imagePreviewLabel, savePreviewImage));

        apply.addActionListener((ActionEvent e) -> {
            if(nameTextField.getText().isEmpty()){
                errorLabel.setText("Enter file name");
            } else {
                fileName.set(nameTextField.getText());

                if((fileName.get().contains("\\")) || (fileName.get().contains("/")) || (fileName.get().contains(":")) ||
                        (fileName.get().contains("*")) || (fileName.get().contains("?")) || (fileName.get().contains("\"")) ||
                                (fileName.get().contains("<")) || (fileName.get().contains(">")) || (fileName.get().contains("|"))){
                    errorLabel.setText("\\ / : * ? \" | < > are invalid");
                } else {
                    try {
                        exportImage(brightness, redValue, greenValue, blueValue, pixelateValue, hueValue, fileName.get());
                        changePreview.dispose();
                        resultButtonsPanel.add(Box.createRigidArea(new Dimension(30, 10)));
                        resultButtonsPanel.add(continueButton);
                        resultLabel.setText("Success! Your image is in IMG/OUT folder");
                        resultLabel.setForeground(Color.lightGray);
                        resultMessageFrame.setTitle("Success!");
                        resultMessageFrame.setVisible(true);
                    } catch(Exception exception){
                        System.out.println(exception.getMessage());
                        resultMessageFrame.setVisible(true);
                        resultMessageFrame.setTitle("Error");
                        resultLabel.setText("Something went wrong!");
                        resultLabel.setForeground(red);
                        changePreview.dispose();
                    }
                }
            }
        });

        resultMessageFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if(Objects.equals(resultMessageFrame.getTitle(), "Error")){
                    System.exit(1);
                } else {
                    saveButton.setEnabled(true);
                    brightnessSlider.setEnabled(true);
                    redSlider.setEnabled(true);
                    greenSlider.setEnabled(true);
                    blueSlider.setEnabled(true);
                    pixelateSlider.setEnabled(true);
                    hueSlider.setEnabled(true);
                    grayScale.setEnabled(true);
                    negative.setEnabled(true);
                }
            }
        });

        exitButton.addActionListener((ActionEvent e) -> System.exit(1));

        continueButton.addActionListener((ActionEvent e) -> {
            resultMessageFrame.dispose();
            saveButton.setEnabled(true);
            brightnessSlider.setEnabled(true);
            redSlider.setEnabled(true);
            greenSlider.setEnabled(true);
            blueSlider.setEnabled(true);
            pixelateSlider.setEnabled(true);
            hueSlider.setEnabled(true);
            grayScale.setEnabled(true);
            negative.setEnabled(true);
        });


        changePreview.setAlwaysOnTop(true);
        changePreview.add(background, BorderLayout.CENTER);
        changePreview.setResizable(false);
        changePreview.setLocationRelativeTo(null);
        changePreview.setVisible(true);
    }

















    private static BufferedImage fitImage(BufferedImage image, int containerWidth, int containerHeight){
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int newWidth = imageWidth;
        int newHeight = imageHeight;

        if(imageWidth > containerWidth){
            newWidth = containerWidth;
            newHeight = (newWidth * imageHeight) / imageWidth;
        }
        if(newHeight > containerHeight){
            newHeight = containerHeight;
            newWidth = (newHeight * imageWidth) / imageHeight;
        }

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, newWidth, newHeight, null);
        g.dispose();
        return resizedImage;
    }

    private static BufferedImage adjustBrightness(BufferedImage image, int adjustmentValue){
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        int rgb, r, g, b, rgb2;
        for(int i=0; i<image.getHeight(); i++){
            for(int j=0; j<image.getWidth(); j++){
                rgb = image.getRGB(j, i);
                r = ((rgb >> 16) & 0xFF) + (adjustmentValue * 255 / 100);
                g = ((rgb >> 8) & 0xFF) + (adjustmentValue * 255 / 100);
                b = (rgb & 0xFF) + (adjustmentValue * 255 / 100);
                if(r > 255) r = 255;
                if(g > 255) g = 255;
                if(b > 255) b = 255;
                if(r <= 0) r = 1;
                if(g <= 0) g = 1;
                if(b <= 0) b = 1;
                rgb2 = (255 << 24) | (r << 16) | (g << 8) | b;
                newImage.setRGB(j, i, rgb2);
            }
        }
        return newImage;
    }

    private static BufferedImage colorFilter(BufferedImage image, char color, int adjustmentValue){
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        int rgb, r, g, b, rgb2;
        if(color == 'r'){
            for(int i=0; i<image.getHeight(); i++) {
                for (int j = 0; j < image.getWidth(); j++) {
                    rgb = image.getRGB(j, i);
                    r = ((rgb >> 16) & 0xFF) + (adjustmentValue);
                    g = ((rgb >> 8) & 0xFF);
                    b = (rgb & 0xFF);
                    if (r > 255) r = 255;
                    if (r <= 0) r = 1;
                    rgb2 = (255 << 24) | (r << 16) | (g << 8) | b;
                    newImage.setRGB(j, i, rgb2);
                }
            }
        } else if(color == 'g') {
            for (int i = 0; i < image.getHeight(); i++) {
                for (int j = 0; j < image.getWidth(); j++) {
                    rgb = image.getRGB(j, i);
                    r = ((rgb >> 16) & 0xFF);
                    g = ((rgb >> 8) & 0xFF) + (adjustmentValue);
                    b = (rgb & 0xFF);
                    if (g > 255) g = 255;
                    if (g <= 0) g = 1;
                    rgb2 = (255 << 24) | (r << 16) | (g << 8) | b;
                    newImage.setRGB(j, i, rgb2);
                }
            }
        } else if(color == 'b'){
            for (int i = 0; i < image.getHeight(); i++) {
                for (int j = 0; j < image.getWidth(); j++) {
                    rgb = image.getRGB(j, i);
                    r = ((rgb >> 16) & 0xFF);
                    g = ((rgb >> 8) & 0xFF);
                    b = (rgb & 0xFF) + (adjustmentValue);
                    if (b > 255) b = 255;
                    if (b <= 0) b = 1;
                    rgb2 = (255 << 24) | (r << 16) | (g << 8) | b;
                    newImage.setRGB(j, i, rgb2);
                }
            }
        }
        return newImage;
    }

    private static BufferedImage toGrayScale(BufferedImage image){
        BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = grayImage.getGraphics();
        g.drawImage(image, 0,0, null);
        return grayImage;
    }

    private static BufferedImage pixelate(BufferedImage image, int value){
        BufferedImage newImage;
        for(int w=0; w<image.getWidth(); w+=value){
            for(int h=0; h<image.getHeight(); h+=value){
                Color pixelColor = new Color(image.getRGB(w, h));
                Graphics graphics = image.getGraphics();
                graphics.setColor(pixelColor);
                graphics.fillRect(w, h, value, value);
            }
        }
        newImage = image;
        return newImage;
    }

    private static BufferedImage adjustHue(BufferedImage image, float value){
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        int r, g, b, rgb;
        float hue, saturation, brightness;

        for(int h=0; h<image.getHeight(); h++){
            for(int w=0; w<image.getWidth(); w++){
                rgb = image.getRGB(w, h);
                r = (rgb >> 16) & 0xFF;
                g = (rgb >> 8) & 0xFF;
                b = (rgb) & 0xFF;

                float[] hsb = Color.RGBtoHSB(r, g, b, null);
                saturation = hsb[1];
                brightness = hsb[2];
                hue = hsb[0] + value/180;


                rgb = Color.HSBtoRGB(hue, saturation, brightness);
                r = (rgb >> 16) & 0xFF;
                g = (rgb >> 8) & 0xFF;
                b = rgb & 0xFF;

                rgb = (255 << 24) | (r << 16) | (g << 8) | b;
                newImage.setRGB(w, h, rgb);
            }
        }
        return newImage;
    }

    private static BufferedImage negative(BufferedImage image){
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        int rgb, r, g, b, rgb2;

        for(int h=0; h< image.getHeight(); h++){
            for(int w=0; w< image.getWidth(); w++){
                rgb = image.getRGB(w, h);
                r = ((rgb >> 16) & 0xFF);
                g = ((rgb >> 8) & 0xFF);
                b = (rgb & 0xFF);
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;
                rgb2 = (255 << 24) | (r << 16) | (g << 8) | b;
                newImage.setRGB(w, h, rgb2);
            }
        }
        return newImage;
    }
}
