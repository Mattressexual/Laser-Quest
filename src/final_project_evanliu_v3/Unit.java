package final_project_evanliu_v3;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Unit extends ImageView {
    
    protected double deltaX, deltaY;
    
    public Unit() {
        
    }
    
    public Unit(String imgName) {
        setImage(new Image(imgName));
    }
    
    public Unit(String imgName, double h, double w) {
        setImage(new Image(imgName, h, w, false, false));
        
        setFitHeight(h);
        setFitWidth(w);
    }
    
    public Unit(String imgName, double h, double w, double dX, double dY) {
        setImage(new Image(imgName, h, w, false, false));
        deltaX = dX;
        deltaY = dY;
        setFitHeight(h);
        setFitWidth(w);
    }
    
    public void setDeltaX(double dX) {
        deltaX = dX;
    }
    
    public void setDeltaY(double dY) {
        deltaY = dY;
    }
    
    public double getDeltaX() {
        return deltaX;
    }
    
    public double getDeltaY() {
        return deltaY;
    }
    
    public abstract void act();
}
