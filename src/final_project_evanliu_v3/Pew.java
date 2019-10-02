package final_project_evanliu_v3;

public abstract class Pew extends Unit {
    
    protected double deltaX, deltaY;
    protected double xStart, yStart;
    protected int damage;
    
    public Pew() {
        
    }
    
    public Pew(String imgName, double h, double w, double dX, double dY, int dam, double x0, double y0) {
        super(imgName, h, w);
        deltaX = dX;
        deltaY = dY;
        xStart = x0;
        yStart = y0;
        damage = dam;
        setX(x0);
        setY(y0);
    }

    public int getDamage() {
        return damage;
    }
    
    public double getDeltaX() {
        return deltaX;
    }
    
    public double getDeltaY() {
        return deltaY;
    }
    
    public void setDamage(int dam) {
        damage = dam;
    }
    
    public void setDeltaX(double dX) {
        deltaX = dX;
    }
    
    public void setDeltaY(double dY) {
        deltaY = dY;
    }

    public abstract void act();
}
