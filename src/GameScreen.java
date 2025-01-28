import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameScreen implements ActionListener, MouseListener {

    public JFrame frame = new JFrame();
    public GameButton[][] buttons;
    private int bWidth;
    private int bLength;
    private int sizeX, sizeY;

    Random rand = new Random();

    public GameScreen(int sizeX, int sizeY, int bombDensity){
        bWidth = 80;
        bLength = 80;
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        buttons = new GameButton[sizeX][sizeY];

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Game");
        frame.setSize(420,420);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        //frame.setLayout(new GridLayout(Math.min(sizeX, sizeY), Math.min(sizeX,sizeY)));
        frame.setVisible(true);

        // setup buttons grid
        for(int row = 0; row < sizeX; row++){
            for(int collumn = 0; collumn < sizeY; collumn++){
                //buttons[row][collumn] = new GameButton(String.format("%d %d", row, collumn), row, collumn, false);
                buttons[row][collumn] = new GameButton("?", row, collumn, false);
                GameButton currentButton = buttons[row][collumn];

                currentButton.setBounds(row*bWidth,collumn*bLength,bWidth,bLength);
                currentButton.setFont(new Font("Arial", Font.BOLD, bWidth / 4));
                currentButton.setFocusable(true);
                currentButton.addActionListener(this);
                currentButton.addMouseListener(this);

                frame.add(currentButton);
            }
        }

        for(int i = 0; i < bombDensity; i++){
            int randX = rand.nextInt(sizeX);
            int randY = rand.nextInt(sizeY);

            if(!buttons[randX][randY].isBomb()){
                buttons[randX][randY].makeBomb();
            } else{
                i--;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof GameButton){
            int pressedX = ((GameButton) e.getSource()).getbX();
            int pressedY = ((GameButton) e.getSource()).getbY();
            GameButton currentButton = buttons[pressedX][pressedY];
            //System.out.println("Clicked");

            //System.out.println(((GameButton) e.getSource()).getbX() + " " + ((GameButton) e.getSource()).getbY() + " " + ((GameButton) e.getSource()).isBomb());

            // check neighbour tiles to count bombs
            this.checkForBombs(pressedX, pressedY, currentButton);
            currentButton.setChecked(true);
        }
    }

    public void checkForBombs(int pressedX, int pressedY, GameButton currentButton){

        ArrayList<GameButton> checkedNeighbours = this.getNeighbours(pressedX,pressedY, 1);

        currentButton.setBombsAround(this.checkAmtBombsAround(pressedX, pressedY, 1));

        for(int i = 0; i < checkedNeighbours.size(); i++){
            checkedNeighbours.get(i).setBombsAround(checkAmtBombsAround(checkedNeighbours.get(i).getbX(), checkedNeighbours.get(i).getbY(), 1));

            if(!checkedNeighbours.get(i).isChecked() && !checkedNeighbours.get(i).isBomb() && currentButton.getBombsAround() == 0){

                if(checkedNeighbours.get(i).getBombsAround() == 0){

                    // only checks the four neighbour tiles
                    if(checkedNeighbours.get(i).getbX() == pressedX || checkedNeighbours.get(i).getbY() == pressedY){
                        checkedNeighbours.get(i).setChecked(true);
                        this.checkForBombs(checkedNeighbours.get(i).getbX(), checkedNeighbours.get(i).getbY(), checkedNeighbours.get(i));
                    }
                } else{
                    checkedNeighbours.get(i).setChecked(true);
                    checkedNeighbours.get(i).setText(String.format("%d", this.checkAmtBombsAround(checkedNeighbours.get(i).getbX(), checkedNeighbours.get(i).getbY(), 1)));
                }
            }

        }

        if(currentButton.isBomb()){
            currentButton.setText("B");
        } else{
            if(currentButton.getBombsAround() == 0){
                currentButton.setText("");
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

                if(checkX == x && checkY == y){

                } else{
                    neighbours.add(buttons[checkX][checkY]);
                }
            }
        }

        //System.out.println(count);
        return neighbours;

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() instanceof GameButton && e.getButton() == MouseEvent.BUTTON3){

            int pressedX = ((GameButton) e.getSource()).getbX();
            int pressedY = ((GameButton) e.getSource()).getbY();
            GameButton currentButton = buttons[pressedX][pressedY];

            System.out.printf("%d %d\n", pressedX, pressedY);

            if(!currentButton.isChecked()){
                if(currentButton.isFlagged()){
                    currentButton.setFlagged(false);
                    currentButton.setText("?");
                } else{
                    currentButton.setFlagged(true);
                    currentButton.setText("F");
                }
            }
        }
    }
}
