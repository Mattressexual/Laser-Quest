package final_project_evanliu_v3;

public class PlayerPew extends Pew {
    
    public PlayerPew() {
        
    }
    
    public PlayerPew(String imgName, double h, double w, double dX, double dY, int dam, double x0, double y0) {
        super(imgName, h, w, dX, dY, dam, x0, y0);
    }
    
    public void move() {
        setY(getY() - deltaY);
    }
    
    @Override
    public void act() {
        move();
    }
    
}
