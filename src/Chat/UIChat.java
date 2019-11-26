package Chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class UIChat {
    JFrame frame;
    JButton submit;
    JTextArea text;
    JTextField message;
    JPanel panel;

    {
        frame = new JFrame("MyChat");
        panel = new JPanel(new BorderLayout());
        submit = new JButton("Ввод");
        text = new JTextArea(20, 40);
        message = new JTextField(40);

        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setFont(new Font("Arial", Font.PLAIN, 16));
        message.setFont(new Font("Arial", Font.PLAIN, 16));

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        panel.add(text, BorderLayout.NORTH);
        panel.add(message, BorderLayout.CENTER);
        panel.add(submit, BorderLayout.SOUTH);
        frame.add(panel);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);

        KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                submit.doClick();
            }
        }
    };
        message.addKeyListener(keyListener);
    }

}
