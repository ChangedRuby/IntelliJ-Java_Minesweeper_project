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
    private JButton myButton = new JButton("Start");
    private JLabel myLabel = new JLabel("Minesweeper");
    public JSlider rSlider = new JSlider(JSlider.HORIZONTAL,5,100,20);
    public JSlider cSlider = new JSlider(JSlider.HORIZONTAL,5,100,20);
    public JLabel rowLabel = new JLabel();
    public JLabel colLabel = new JLabel();

    public MainScreen() {

        myButton.setBounds(160,160,100,50);
        myButton.setPreferredSize(new Dimension(160,80));
        myButton.setFocusable(false);
        myButton.addActionListener(this);

        myLabel.setFont(new Font("Arial", Font.BOLD, 24));
        myLabel.setHorizontalAlignment(JLabel.CENTER);
        myLabel.setPreferredSize(new Dimension(200,100));

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
        //myButton.setPreferredSize(new Dimension(200,100));

        Hashtable labelTable = new Hashtable();
        labelTable.put(5, new JLabel("5"));
        labelTable.put(50, new JLabel("50"));
        labelTable.put(100, new JLabel("100"));
        rSlider.setLabelTable(labelTable);
        cSlider.setLabelTable(labelTable);

        rSlider.addChangeListener(this);

        rowLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        rowLabel.setPreferredSize(new Dimension(1000,10));
        rowLabel.setSize(1000,10);
        rowLabel.setHorizontalAlignment(JLabel.LEFT);

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
        //frame.add(rowLabel, c);
        c.gridy = 1;
        frame.add(rSlider, c);
        c.gridy = 2;
        frame.add(cSlider, c);
        c.gridy = 3;
        frame.add(myButton, c);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == myButton){
          
            GameScreen myScreen = new GameScreen(20,10,20);
            frame.dispose();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if(e.getSource() == rSlider){
            rowLabel.setText("Rows: " + rSlider.getValue());
        }
    }
}
