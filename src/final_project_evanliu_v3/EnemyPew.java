package final_project_evanliu_v3;

public abstract class EnemyPew extends Pew {
    
    public EnemyPew() {
        
    }
    
    public EnemyPew(String imgName, double h, double w, double dX, double dY, int dam, double x0, double y0) {
        super(imgName, h, w, dX, dY, dam, x0, y0);
    }
}
