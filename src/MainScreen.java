import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainScreen implements ActionListener {

    public JFrame frame = new JFrame();
    public GridBagConstraints c = new GridBagConstraints();
    public JButton myButton = new JButton("Start");
    public JLabel myLabel = new JLabel("Minesweeper");

    public MainScreen() {

        myButton.setBounds(160,160,100,50);
        myButton.setPreferredSize(new Dimension(160,80));
        myButton.setFocusable(false);
        myButton.addActionListener(this);

        myLabel.setFont(new Font("Arial", Font.BOLD, 24));
        myLabel.setHorizontalAlignment(JLabel.CENTER);
        myLabel.setPreferredSize(new Dimension(200,200));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Minesweeper");
        frame.setSize(420,420);
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 0;
        c.ipady = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;

        frame.add(myLabel, c);

        c.gridy = 1;

        frame.add(myButton, c);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == myButton){
            System.out.println("Game started");

            GameScreen myScreen = new GameScreen(15,15,20);
            frame.dispose();
        }
    }
}
