package de.orat.view3d.euclid3dviewapi.spi;

import de.orat.view3d.euclid3dviewapi.api.BoundingBox3D;
import java.awt.Color;
import org.jogamp.vecmath.Matrix3d;
import org.jogamp.vecmath.Matrix4d;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;

/**
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public interface iEuclidViewer3D {
    
    public void open() throws Exception;
    public void close();
    
    public BoundingBox3D getBoundingBox();
    
    /**
     * Used to visualize spheres and round points.
     * 
     * @param location location of the point
     * @param label name of the point
     * @param color color
     * @return id id to reference the object for later transformation
     */
    public long addSphere(Point3d location, double radius, Color color, String label);
    //public long addOrientedPoint(Vector3d attitude, Point3d location, Color color, String label);
    //public long addPointPair(Point3d p1, Point3d p2, String label, Color color1, Color color2, double lineRadius, double pointRadius);
    public long addLine(Point3d p1, Point3d p2, Color color, double radius, String label);
    public long addArrow(Point3d location, Vector3d direction, double radius, Color color, String label);
    /**
     * Used to visualize circles and oriented points.
     * 
     * @param location
     * @param normal
     * @param radius
     * @param color
     * @param label
     * @param isDahed if true and also isFull==true then the circle-line is shown dashed
     * @param isFull if false only the circle-line is shown
     * @return 
     */
    public long addCircle(Point3d location, Vector3d normal, double radius, Color color, String label, boolean isDahed, boolean isFull);
    //public long addPlane(Point3d location, Vector3d normal, Color color, String label, boolean showNormal);
    public long addPolygone(Point3d location, Point3d[] corners, Color color, String label, boolean showNormal);
    
    /**
     * Used to visualize flat points.
     * 
     * @param location
     * @param normal
     * @param radius
     * @param color
     * @param label
     * @param isDahed
     * @return 
     */
    public long addCube(Point3d location, double height, double width, double depth, 
            Color color, String label, boolean isDahed);
    /**
     * 
     * @param type robot type, default=ur5e=0
     * @param location
     * @param orientation
     * @param label
     * @param color
     * @return 
     */
    public long addRobot(int type, Point3d location, Matrix3d orientation);
    public void moveRobot(long id, double[] angels);
    
    public boolean removeNode(long id);
    
    // default impl. könnte das ursprüngliche Objekt löschen und ein neues Objekt
    // in der transformierten Pose erzeugen
    // Copellia kann das aber selbst
    public void transform(long id, Matrix4d transform);
}
