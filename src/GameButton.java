import javax.swing.*;

public class GameButton extends JButton {

    private int x, y;
    private boolean isBomb;
    private int bombsAround;
    private boolean isChecked = false;
    private boolean isFlagged = false;

    public GameButton(String text, int x, int y, boolean isBomb) {
        super.setText(text);
        this.x = x;
        this.y = y;
    }

    public void makeBomb(){
        this.isBomb = true;
    }

    public int getbX(){
        return this.x;
    }

    public int getbY(){
        return this.y;
    }

    public boolean isBomb(){
        return this.isBomb;
    }

    public int getBombsAround() {
        return bombsAround;
    }

    public void setBombsAround(int bombsAround) {
        this.bombsAround = bombsAround;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }
}
