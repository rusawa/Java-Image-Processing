package Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ToolsLabel extends JLabel {

    public ToolsLabel(String title){
        super(title);
        this.setForeground(Color.lightGray);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setBorder(new EmptyBorder(10, 0, 10, 0));
        this.setFont(new Font("Arial", Font.PLAIN, 15));
    }
}
