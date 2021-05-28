package renderer.shapes;

import java.awt.*;

public class Tetrahedon {

    private MyPolygon[] polygons;
    private Color color;

    public Tetrahedon(Color color, MyPolygon... polygons){
        this.color = color;
        this.polygons = polygons;
        this.setPolygonColor();
    }

    public Tetrahedon(MyPolygon... polygons){
        this.color = Color.white;
        this.polygons = polygons;
    }

    public void render(Graphics g){
        for (MyPolygon poly : this.polygons) {
            poly.render(g);
        }
    }

    public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees){
        for (MyPolygon p : this.polygons){
            p.rotate(CW, xDegrees, yDegrees, zDegrees);
        }
        this.sortPolygons();
    }

    private void sortPolygons(){
        MyPolygon.sortPolygones(this.polygons);
    }

    private void setPolygonColor(){
        for (MyPolygon poly : this.polygons){
            poly.setColor(this.color);
        }
    }

}
