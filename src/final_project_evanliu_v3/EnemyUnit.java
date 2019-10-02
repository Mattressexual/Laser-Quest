package final_project_evanliu_v3;

public abstract class EnemyUnit extends Unit {
    
    protected int HP, scoreValue;
    protected double xStart, yStart;
    
    public EnemyUnit() {
        
    }
    
    public EnemyUnit(String imgName, double h, double w, double dX, double dY,double x0, double y0) {
        super(imgName, h, w, dX, dY);
        xStart = x0 - w / 2;
        yStart = y0 - h;
        setX(xStart);
        setY(yStart);
    }
    
    public int getHP() {
        return HP;
    }
    
    public void setHP(int hp) {
        HP = hp;
    }
    
    public int getScoreValue() {
        return scoreValue;
    }
    
    public void takeDamage(int damage) {
        HP -= damage;
    }
}
