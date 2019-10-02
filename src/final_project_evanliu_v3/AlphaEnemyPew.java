package final_project_evanliu_v3;

public class AlphaEnemyPew extends EnemyPew {
    
    double xDistance, yDistance, hypotenuse;
    
    public AlphaEnemyPew() {
        
    }
    
    public AlphaEnemyPew(String imgName, double h, double w, double dX, double dY, int dam, double x0, double y0, double pX, double pY) {
        super(imgName, h, w, dX, dY, dam, x0, y0);
        seekTarget(pX, pY);
    }
    
    public void seekTarget(double targetX, double targetY) {
        xDistance = targetX - getX();
        yDistance = targetY - getY();
        hypotenuse = Math.sqrt(xDistance * xDistance + yDistance * yDistance);
        
        deltaX = xDistance / hypotenuse * Values.ALPHA_PEW_SPEED;
        if (Math.random() >= Values.ALPHA_MISS_CHANCE) {
            deltaX += deltaX * 0.1;
        }
        else {
            deltaX -= deltaX * 0.1;
        }
        
        deltaY = yDistance / hypotenuse * Values.ALPHA_PEW_SPEED;
        if (Math.random() > Values.ALPHA_MISS_CHANCE) {
            deltaY += deltaY * 0.1;
        }
        else {
            deltaY -= deltaY * 0.1;
        }
    }
    
    public void move() {
        setY(getY() + deltaY);
        setX(getX() + deltaX);
    }
    
    @Override
    public void act() {
        move();
    }
}
