import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

public class MainScreen implements ActionListener, ChangeListener {

    private JFrame frame = new JFrame();
    private GridBagConstraints c = new GridBagConstraints();
    private JButton startButton = new JButton("Start");
    private JLabel titleLabel = new JLabel("Minesweeper");
    public JSlider rSlider = new JSlider(JSlider.HORIZONTAL,5,100,20);
    public JSlider cSlider = new JSlider(JSlider.HORIZONTAL,5,100,20);
    public JLabel rLabel = new JLabel("Row");
    public JLabel cLabel = new JLabel("Collumn");
    JTextField bField = new JTextField();

    public MainScreen() {

        startButton.setBounds(160,160,100,50);
        startButton.setPreferredSize(new Dimension(160,80));
        startButton.setFocusable(false);
        startButton.addActionListener(this);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setPreferredSize(new Dimension(200,100));

        rSlider.setPaintTicks(true);
        rSlider.setPaintLabels(true);
        rSlider.setPaintTrack(true);
        cSlider.setPaintTicks(true);
        cSlider.setPaintLabels(true);
        cSlider.setPaintTrack(true);

        rSlider.setMajorTickSpacing(95);
        rSlider.setMinorTickSpacing(5);
        cSlider.setMajorTickSpacing(95);
        cSlider.setMinorTickSpacing(5);
        //rSlider.setPreferredSize(new Dimension(1000,200));
        //cSlider.setPreferredSize(new Dimension(1000,200));
        //startButton.setPreferredSize(new Dimension(200,100));

        Hashtable labelTable = new Hashtable();
        labelTable.put(5, new JLabel("5"));
        labelTable.put(50, new JLabel("50"));
        labelTable.put(100, new JLabel("100"));
        rSlider.setLabelTable(labelTable);
        cSlider.setLabelTable(labelTable);

        rSlider.addChangeListener(this);
        cSlider.addChangeListener(this);

        rLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        rLabel.setPreferredSize(new Dimension(200,20));
        rLabel.setHorizontalAlignment(JLabel.LEFT);
        cLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        cLabel.setPreferredSize(new Dimension(200,20));
        cLabel.setHorizontalAlignment(JLabel.LEFT);

        // sets the texts of the sliders to their value
        rLabel.setText("Rows: " + rSlider.getValue());
        cLabel.setText("Cols: " + cSlider.getValue());

        bField.setPreferredSize(new Dimension(50,20));
        bField.setText("50");

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
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;

        frame.add(titleLabel, c);
        c.gridy = 1;
        frame.add(rLabel, c);
        c.gridy = 2;
        frame.add(rSlider, c);
        c.gridy = 3;
        frame.add(cLabel, c);
        c.gridy = 4;
        frame.add(cSlider, c);
        c.gridy = 5;
        frame.add(bField, c);
        c.gridy = 6;
        frame.add(startButton, c);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == startButton){
          
            GameScreen myScreen = new GameScreen(rSlider.getValue(),cSlider.getValue(),Integer.parseInt(bField.getText()));
            frame.dispose();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if(e.getSource() == rSlider || e.getSource() == cSlider){
            rLabel.setText("Rows: " + rSlider.getValue());
            cLabel.setText("Cols: " + cSlider.getValue());
        }
    }
}
