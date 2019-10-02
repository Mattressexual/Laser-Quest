package final_project_evanliu_v3;

import com.sun.javafx.scene.traversal.Direction;

public class PlayerUnit extends Unit {
    
    private boolean
            movingUp = false,
            movingDown = false,
            movingLeft = false,
            movingRight = false;
    
    private int HP;
    
    public PlayerUnit() {
        
    }

    public PlayerUnit(String imgName, double h, double w, double dX, double dY) {
        super(imgName, h, w, dX, dY);
        HP = Values.PLAYER_HP;
    }
    
    public void moveStart(Direction direction) {
        switch(direction) {
            case UP: movingUp = true; break;
            case DOWN: movingDown = true; break;
            case LEFT: movingLeft = true; break;
            case RIGHT: movingRight = true; break;
        }
    }
    
    public void moveStop(Direction direction) {
        switch(direction) {
            case UP: movingUp = false; break;
            case DOWN: movingDown = false; break;
            case LEFT: movingLeft = false; break;
            case RIGHT: movingRight = false; break;
        }
    }
    
    public void move() {
        if (movingUp) {
            if (getY() > deltaY) {
                setY(getY() - deltaY);
            }
        }
        if (movingDown) {
            if (getY() < Values.SCREEN_Y - this.getFitHeight() - deltaY) {
                setY(getY() + deltaY);
            }
        }
        if (movingLeft) {
            if (getX() > deltaX) {
                setX(getX() - deltaX);
            }
        }
        if (movingRight) {
            if (getX() < Values.SCREEN_X - this.getFitWidth() - deltaX) {
                setX(getX() + deltaX);
            }
        }
    }
    
    public void shoot() {
        ((GPane)getParent()).getEngine().addUnit(
                new PlayerPew(
                        FileNames.PLAYER_PEW_IMAGE,
                        Values.PLAYER_PEW_HEIGHT,
                        Values.PLAYER_PEW_WIDTH,
                        Values.PLAYER_PEW_DX,
                        Values.PLAYER_PEW_DY,
                        Values.PLAYER_PEW_DAMAGE,
                        getX() + getFitWidth() / 2 - Values.PLAYER_PEW_WIDTH / 2,
                        getY()));
    }
    
    public void takeDamage(int damage) {
        HP -= damage;
    }
    
    @Override
    public void act() {
        move();
    }
    
    public double getDeltaX() {
        return deltaX;
    }
    
    public double getDeltaY() {
        return deltaY;
    }
    
    public int getHP() {
        return HP;
    }
}
