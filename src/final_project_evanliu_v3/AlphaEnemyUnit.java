package final_project_evanliu_v3;

public class AlphaEnemyUnit extends EnemyUnit{
    
    
    public AlphaEnemyUnit() {
        
    }
    
    public AlphaEnemyUnit(String imgName, double h, double w, double dX, double dY,double x0, double y0) {
        super(imgName, h, w, dX, dY, x0, y0);
        HP = Values.ALPHA_HP;
    }
    
    public void move() {
        if (deltaX > 0) {
            if (getX() <= Values.SCREEN_X - getFitWidth()) {
                setX(getX() + deltaX);
            }
            else if (getX() > Values.SCREEN_X - getFitWidth()) {
                deltaX *= -1;
            }
        }
        else if (deltaX < 0) {
            if (getX() > 0) {
                setX(getX() + deltaX);
            }
            else if (getX() <= 0) {
                deltaX *= -1;
            }
        }
        
        
        if (deltaY > 0) {
            if (getY() < Values.SCREEN_Y / 2) {
                setY(getY() + deltaY);
            }
            else if (getY() >= Values.SCREEN_Y / 2) {
                deltaY *= -1;
            }
        }
        else if (deltaY < 0) {
            if (getY() > 0) {
                setY(getY() + deltaY);
            }
            else if (getY() <= 0) {
                deltaY *= -1;
            }
        }
    }
    
    public void shoot() {
        ((GPane)getParent()).getEngine().addUnit(
                new AlphaEnemyPew(
                        FileNames.ALPHA_ENEMY_PEW_IMAGE,
                        Values.ALPHA_PEW_HEIGHT,
                        Values.ALPHA_PEW_WIDTH,
                        Values.ALPHA_PEW_DX,
                        Values.ALPHA_PEW_DY,
                        Values.ALPHA_PEW_DAMAGE,
                        getX() + getFitWidth() / 2 - Values.ALPHA_PEW_WIDTH / 2,
                        getY() + getFitHeight(),
                        ((GPane)getParent()).getEngine().getPlayer().getX() + ((GPane)getParent()).getEngine().getPlayer().getFitWidth() / 2,
                        ((GPane)getParent()).getEngine().getPlayer().getY() + ((GPane)getParent()).getEngine().getPlayer().getFitHeight() / 2));
    }
    
    @Override
    public void act() {
        move();
    }
}
