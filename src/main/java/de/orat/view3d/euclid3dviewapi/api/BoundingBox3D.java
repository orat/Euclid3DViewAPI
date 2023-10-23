package de.orat.view3d.euclid3dviewapi.api;

import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;

/**
 *
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class BoundingBox3D {
    
    private Point3d lowerCorner;
    private Point3d upperCorner;
    
    public BoundingBox3D(Point3d lower, Point3d upper){
        this.lowerCorner = lower;
        this.upperCorner = upper;
    }
    public BoundingBox3D(Point3f lower, Point3f upper){
        this.lowerCorner = new Point3d(lower);
        this.upperCorner = new Point3d(upper);
    }
    
    public Point3d getLowerCorner(){
        return lowerCorner;
    }
    public Point3d getUpperCorner(){
        return upperCorner;
    }
    
}
