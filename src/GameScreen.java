import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class GameScreen implements ActionListener, MouseListener, ComponentListener, Runnable {

    public JFrame frame = new JFrame();
    public GridBagConstraints c = new GridBagConstraints();

    public GameButton[][] buttons;
    private int bWidth;
    private int bLength;
    private int sizeX, sizeY;
    private String[] grass_colours, font_colours;
    private Color original_color;
    public int amtMoves;

    Random rand = new Random();

    public GameScreen(int sizeX, int sizeY, int bombDensity){
        bWidth = 80;
        bLength = 80;
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        GameButton.amtTiles = 0;
        GameButton.amtBomb = 0;
        GameButton.amtChecked = 0;
        this.amtMoves = 0;

        try{
            imageMaster = ImageIO.read(new File("sprites/minesweeper_flag.png"));
        } catch(Exception e){
            e.printStackTrace();
        }

        buttons = new GameButton[sizeX][sizeY];

        frame.setLayout(new GridBagLayout());

        c.insets = new Insets(0,0,0,0);
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = sizeX;
        c.ipady = sizeY;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Game");
        frame.setSize(840,840);
        frame.setMinimumSize(new Dimension(180,180));
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        //frame.setLayout(new GridLayout(Math.min(sizeX, sizeY), Math.min(sizeX,sizeY)));
        frame.setVisible(true);

        // setup buttons grid
        for(int row = 0; row < sizeX; row++){
            for(int collumn = 0; collumn < sizeY; collumn++){
                //buttons[row][collumn] = new GameButton(String.format("%d %d", row, collumn), row, collumn, false);
                buttons[row][collumn] = new GameButton(" ", row, collumn, false);
                GameButton currentButton = buttons[row][collumn];

                // setup arrays of colours
                grass_colours = new String[]{"#AAD751","#8db342","#E5C29F","#D7B899"};
                font_colours = new String[]{"#FFFFFF","#0000FF","#008000","#FF0000","#301934","#550000","#00FFFF","#A020F0","#808080"};

                //currentButton.setBounds(row*bWidth,collumn*bLength,bWidth,bLength);
                //currentButton.setIcon(new ImageIcon("green_button.png"));

                currentButton.setFont(new Font("Arial", Font.BOLD, bWidth / 4));
                currentButton.setBorder(new LineBorder(Color.BLACK, 0));
                currentButton.setForeground(Color.BLACK);
                currentButton.setBackground(Color.decode(grass_colours[((row  + collumn) % 2)]));
                original_color = currentButton.getBackground();

                // limits the button size
                currentButton.setPreferredSize(currentButton.getSize());
                currentButton.setMaximumSize(currentButton.getPreferredSize());

                currentButton.setFocusable(false);
                currentButton.setIcon(null);
                currentButton.addActionListener(this);
                currentButton.addMouseListener(this);
                currentButton.addComponentListener(this);
                //currentButton.setIcon(new ImageIcon("https://images.emojiterra.com/google/noto-emoji/unicode-15/color/512px/1f6a9.png"));

                frame.add(currentButton, c);

                c.gridy++;
            }

            c.gridx++;
            c.gridy = 0;
        }

        if(bombDensity <= sizeX * sizeY){
            this.placeBombs(bombDensity);
        } else{
            System.out.println("Too many bombs!");
        }
    }

    private void placeBombs(int bombDensity){

        if(GameButton.amtTiles - GameButton.amtBomb >= bombDensity){
            for(int i = 0; i < bombDensity; i++){
                int randX = rand.nextInt(sizeX);
                int randY = rand.nextInt(sizeY);

                if(!buttons[randX][randY].isBomb()){
                    buttons[randX][randY].setBomb(true);
                } else{
                    i--;
                }
            }
        } else{
            System.out.println("No tiles to place bombs");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof GameButton){
            int pressedX = ((GameButton) e.getSource()).getbX();
            int pressedY = ((GameButton) e.getSource()).getbY();
            GameButton currentButton = buttons[pressedX][pressedY];
            this.amtMoves++;

            //System.out.println("Clicked");
            /*
            System.out.println(GameButton.amtBtn);
            System.out.println(GameButton.amtBomb);
            */
            //System.out.println(((GameButton) e.getSource()).getbX() + " " + ((GameButton) e.getSource()).getbY() + " " + ((GameButton) e.getSource()).isBomb());

            if(currentButton.isBomb()){
                // if the clicked bomb is a bomb and it is the first move, delete the bomb and replace it in another place
                if(this.amtMoves == 1){
                    this.placeBombs(1);
                    currentButton.setBomb(false);
                } else{
                    System.out.println("You lose");

                    //MainScreen mainScreen = new MainScreen();
                    //frame.dispose();
                }
            }

            // check neighbour tiles to count bombs
            try{
                this.checkForBombs(pressedX, pressedY, currentButton);
            } catch (StackOverflowError err){
                System.out.println("Stack overflow error");
            }
            currentButton.setChecked(true);

            this.checkWinCondition();
        }
    }

    public void checkForBombs(int pressedX, int pressedY, GameButton currentButton) throws StackOverflowError{

        ArrayList<GameButton> checkedNeighbours = this.getNeighbours(pressedX,pressedY, 1);

        currentButton.setBombsAround(this.checkAmtBombsAround(pressedX, pressedY, 1));

        if(!currentButton.isBomb()){
            currentButton.setBackground(Color.decode(grass_colours[((currentButton.getbX() + currentButton.getbY()) % 2) + 2]));
            currentButton.setForeground(Color.decode(font_colours[currentButton.getBombsAround()]));
            currentButton.setIcon(null);
        }

        for(int i = 0; i < checkedNeighbours.size(); i++){
            GameButton currentNeighbour = checkedNeighbours.get(i);

            // currentNeighbour is the neighbour being checked
            // currentButton is the root

            currentNeighbour.setBombsAround(checkAmtBombsAround(currentNeighbour.getbX(), currentNeighbour.getbY(), 1));

            if(!currentNeighbour.isChecked() && !currentNeighbour.isBomb() && currentButton.getBombsAround() == 0 && !currentButton.isBomb()){

                //currentButton.setBackground(Color.decode(grass_colours[((currentButton.getbX() + currentButton.getbY()) % 2) + 2]));

                if(currentNeighbour.getBombsAround() == 0){

                    // only checks the four neighbour tiles # up left down right
                    if(currentNeighbour.getbX() == pressedX || currentNeighbour.getbY() == pressedY){
                        currentNeighbour.setChecked(true);
                        this.checkForBombs(currentNeighbour.getbX(), currentNeighbour.getbY(), currentNeighbour);
                    }
                } else{
                    currentNeighbour.setChecked(true);

                    currentNeighbour.setBackground(Color.decode(grass_colours[((currentNeighbour.getbX() + currentNeighbour.getbY()) % 2) + 2]));
                    currentNeighbour.setForeground(Color.decode(font_colours[currentNeighbour.getBombsAround()]));
                    currentNeighbour.setIcon(null);

                    currentNeighbour.setText(String.format("%d", this.checkAmtBombsAround(checkedNeighbours.get(i).getbX(), checkedNeighbours.get(i).getbY(), 1)));
                }
            }

        }

        if(currentButton.isBomb()){
            if(!currentButton.isChecked()){

                Thread loseThread = new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        System.out.println("Thread awoken");

                        MainScreen mainScreen = new MainScreen();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                loseThread.start();

                this.showAllBombs();
            }
        } else{
            if(currentButton.getBombsAround() == 0){
                currentButton.setText(" ");
            } else{
                currentButton.setText(String.format("%d", currentButton.getBombsAround()));
            }
        }
    }

    public int checkAmtBombsAround(int x, int y, int radius){
        ArrayList<GameButton> checkedNeighbours = this.getNeighbours(x,y, radius);

        int bombCount = 0;
        for(int i = 0; i < checkedNeighbours.size(); i++){
            if(checkedNeighbours.get(i).isBomb()){
                bombCount++;
            }
            //checkedNeighbours.get(i).setText("Checked");
        }

        return bombCount;
    }

    public void showBomb(GameButton button){

        int rng = rand.nextInt(9);
        currentButton = button;
        currentButton.setText("");
        currentButton.setBackground(Color.decode(font_colours[rng]));

        try{
            imageMaster = ImageIO.read(new File("sprites/bomb_icon.png"));
        } catch(Exception e){
            e.printStackTrace();
        }
        currentButton.setChecked(true);
        currentButton.setIcon(new ImageIcon(imageMaster.getScaledInstance(Math.min(currentButton.getSize().width,currentButton.getSize().height), Math.min(currentButton.getSize().width,currentButton.getSize().height), Image.SCALE_SMOOTH)));

    }

    public void showAllBombs(){
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                if(buttons[i][j].isBomb()){
                    this.showBomb(buttons[i][j]);
                }
            }
        }
    }

    public ArrayList<GameButton> getNeighbours(int x, int y, int radius){
        //int count = 0;
        ArrayList<GameButton> neighbours = new ArrayList<>();

        int maxCheckX, maxCheckY;

        if(x+radius > sizeX-1){
            maxCheckX = sizeX-1;
        } else{
            maxCheckX = x+radius;
        }
        if(y+radius > sizeY-1){
            maxCheckY = sizeY-1;
        } else{
            maxCheckY = y+radius;
        }


        for(int checkX = x-radius; checkX <= maxCheckX; checkX++){
            for(int checkY = y-radius; checkY <= maxCheckY; checkY++){
                //count++;

                if(checkX < 0){
                    checkX = 0;
                }
                if(checkY < 0){
                    checkY = 0;
                }
                if(checkX > sizeX-1){
                    checkX = sizeX-1;
                }
                if(checkY > sizeY-1){
                    checkY = sizeY-1;
                }

                if(checkX != x || checkY != y){
                    neighbours.add(buttons[checkX][checkY]);
                }
            }
        }

        //System.out.println(count);
        return neighbours;

    }

    public void checkWinCondition(){
        if(GameButton.amtChecked >= GameButton.amtTiles - GameButton.amtBomb){
            System.out.println("Ganhou");

            MainScreen mainScreen = new MainScreen();
            frame.dispose();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(e.getSource() instanceof GameButton){
            /*
            GameButton currentButton = (GameButton) e.getSource();
            currentButton.setBackground(currentButton.getBackground().brighter());
            */
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(e.getSource() instanceof GameButton){
            //GameButton currentButton = (GameButton) e.getSource();
            //currentButton.setBackground(original_color);
        }
    }

    private BufferedImage imageMaster;
    private Image imageScaled;

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() instanceof GameButton && e.getButton() == MouseEvent.BUTTON3){

            int pressedX = ((GameButton) e.getSource()).getbX();
            int pressedY = ((GameButton) e.getSource()).getbY();
            GameButton currentButton = buttons[pressedX][pressedY];

            if(!currentButton.isChecked()){
                if(currentButton.isFlagged()){
                    currentButton.setFlagged(false);
                    currentButton.setText(" ");
                    currentButton.setIcon(null);
                } else{
                    currentButton.setFlagged(true);
                    currentButton.setText("");
                    currentButton.setIcon(new ImageIcon(imageScaled));
                }
            }
        }
    }

    private GameButton currentButton;
    private Dimension size;
    private Insets insets;

    @Override
    public void componentResized(ComponentEvent e) {
        //System.out.println("size");

        currentButton = (GameButton) e.getComponent();
        size = currentButton.getSize();
        insets = currentButton.getInsets();

        int dimension = Math.min(size.width - insets.left - insets.right, size.height - insets.top - insets.bottom);

        currentButton.setFont(new Font("Arial", Font.BOLD, size.height / 2));

        //size.width -= insets.left + insets.right;
        //size.height -= insets.top + insets.bottom;

        size.width = dimension;
        size.height = dimension;

        /*
        if(size.width > size.height){
            size.width = -1;
        } else{
            size.height = -1;
        }
        */

        if(imageMaster != null){
            imageScaled = imageMaster.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
        }

        if(currentButton.isFlagged() && !currentButton.isChecked() && currentButton.getIcon().getIconHeight() != imageScaled.getHeight(null) &&
                currentButton.getIcon().getIconWidth() != imageScaled.getWidth(null)){

            currentButton.setIcon(new ImageIcon(imageScaled));
        }
        if(currentButton.isChecked() && currentButton.isBomb()){
            currentButton.setIcon(new ImageIcon(imageScaled));
        }

    }

    public void dispose(){
        frame.dispose();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    @Override
    public void run() {
        System.out.println("Runnable started");
    }
}
