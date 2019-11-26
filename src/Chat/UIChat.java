package Chat;

import javax.swing.*;
import java.awt.*;

class UIChat {
    JFrame frame;
    JButton submit;
    JButton submit2;
    JTextArea text;
    JTextField message;
    JPanel panel;

    {
        frame = new JFrame();
        panel = new JPanel(new BorderLayout());
        submit = new JButton("Ввод");
        text = new JTextArea(20, 30);
        message = new JTextField(30);

        text.setEditable(false);
        text.setLineWrap(true);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        panel.add(text, BorderLayout.NORTH);
        panel.add(message, BorderLayout.CENTER);
        panel.add(submit, BorderLayout.SOUTH);
        frame.add(panel);
        frame.setVisible(true);
        frame.pack();
    }
}
