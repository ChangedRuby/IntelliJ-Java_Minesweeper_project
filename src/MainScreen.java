import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainScreen implements ActionListener {

    JFrame frame = new JFrame();
    JButton myButton = new JButton("Start");

    public MainScreen() {

        myButton.setBounds(160,160,100,50);
        myButton.setFocusable(false);
        myButton.addActionListener(this);

        frame.add(myButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Minesweeper");
        frame.setSize(420,420);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == myButton){
            System.out.println("Button clicked");

            GameScreen myScreen = new GameScreen(20,10,20);
        }
    }
}
