package de.orat.view3d.euclid3dviewapi.spi;

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
    
    /**
     * Get an axis aligned bounding box.
     * 
     * @return current axis aligned bounding box
     */
    public iAABB getAABB();
    
    /**
     * Add a sphere to the scene.
     * 
     * Used to visualize spheres and round points.
     * 
     * @param location location of the point
     * @param label name of the point
     * @param color color
     * @return handle to reference the object for later transformation
     */
    public long addSphere(Point3d location, double radius, Color color, 
                          String label, boolean transparency);
    /**
     * Add a line to the scene.
     * 
     * @param p1
     * @param p2
     * @param color
     * @param radius
     * @param label
     * @return handle
     */
    public long addLine(Point3d p1, Point3d p2, Color color, double radius, String label);
    
    /**
     * Add an arrow to the scene.
     * 
     * @param location
     * @param direction, length of direction is shown in the visualization
     * @param radius
     * @param color
     * @param label
     * @return handle
     */
    public long addArrow(Point3d location, Vector3d direction, double radius, 
                         Color color, String label);
    
    /**
     * Add a circle to the scene.
     * 
     * @param location
     * @param normal
     * @param radius
     * @param color
     * @param label
     * @param isDashed if true and also isFull==true then the circle-line is shown dashed
     * @param isFilled if false only the circle-line is shown
     * @return handle
     */
    public long addCircle(Point3d location, Vector3d normal, double radius, 
            Color color, String label, boolean isDashed, boolean isFilled);
    /**
     * Add a polygone to the scene.
     * 
     * @param location
     * @param corners
     * @param color
     * @param label
     * @param showNormal true, then show a normal vector
     * @param tranparency
     * @return handle
     */
    public long addPolygone(Point3d location, Point3d[] corners, Color color, 
            String label, boolean showNormal, boolean tranparency);
    
    /**
     * Add a plane to the 3d view with clipping.
     * 
     * @param location first point of the plane, unit is [mm]
     * @param n normal vector
     * @param color color of the plane
     * @param label the text of the label of the plane
     * @param showNormal not yet implemented
     * @return false if outside the bounding-box
     */
    /*default long addPolygone(Point3d location, Vector3d n, java.awt.Color color, 
            String label, boolean showNormal){
        
        if (!isValid(location) || !isValid(n)){
            throw new IllegalArgumentException("addPlane(): location or attitude with illegal values!");
        }
        
        // Clipping
        
        Plane plane = new Plane(new Vector3d(location), n);

        // clipping
        iAABB aabb = createAxisAlignedBoundBox();
        
        // testweise die Ecken der bounding box visualisieren
        //List<Point3d> points = aabb.getCorners();
        //for (int i=0;i<points.size();i++){
        //    this.addPoint(points.get(i), Color.BLUE, 30, String.valueOf(i));
        //}
        
        Point3d[] corners = aabb.clip(plane); // corners of a polygon in a plane
        
        boolean result = false;
        if (corners.length > 2){
            org.jzy3d.colors.Color col = new org.jzy3d.colors.Color(color.getRed(),color.getGreen(),color.getBlue(), color.getAlpha());
            return addPlane(location, corners, col, label);
        } else {
            System.out.println("addPlane \""+label+"\" failed. Corners cauld not be determined!");
            return -1;
        }
    }*/
    
   
    
    /**
     * Add a cube to the scene.
     * 
     * @param location
     * @param normal
     * @param radius
     * @param color
     * @param label
     * @param isDahed
     * @return handle
     */
    public long addCube(Point3d location, Vector3d dir, double width, 
            Color color, String label, boolean tranparency);
    
    /**
     * Add a robot to the scene.
     * 
     * @param type robot type, default=ur5e=0
     * @param location
     * @param orientation
     * @param label
     * @param color
     * @return handle
     */
    public long addRobot(int type, Point3d location, Matrix3d orientation);
    public void moveRobot(long handle, double[] angels);
    
    
    public long addMesh(String path, Matrix4d transform);
    
    public boolean removeNode(long handle);
    
    // default impl. könnte das ursprüngliche Objekt löschen und ein neues Objekt
    // in der transformierten Pose erzeugen
    // Copellia kann aber transform
    public void transform(long handle, Matrix4d transform);
}
