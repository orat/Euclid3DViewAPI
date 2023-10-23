package de.orat.view3d.euclid3dviewapi.util;

import de.orat.view3d.euclid3dviewapi.spi.iAABB;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3d;

/**
 * copy aus Euclid
 * 
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class AxisAlignedBoundingBox implements iAABB {
    
    // first variant to represent the AABB (based on center and size-vector)
    private Point3d center;
    private Vector3d size; // diagonal durch die ganze box
    
    // second variant to represent the AABB (based on corners)
    Point3d xyzmin;
    Point3d xyminzmax; 
    Point3d xminymaxzmin;
    Point3d xminyzmax;
    Point3d xmaxyzmin;
    Point3d xmaxyminzmax;
    Point3d xymaxzmin;
    Point3d xyzmax;
        
    // third variant to represent the AABB (based on two diagonal points)
    Point3f min;
    Point3f max;
    
    public AxisAlignedBoundingBox(Point3f xyzmin, Point3f xyminzmax, 
                                  Point3f xminymaxzmin, Point3f xminyzmax, 
                                  Point3f xmaxyzmin, Point3f xmaxyminzmax,
                                  Point3f xymaxzmin, Point3f xyzmax, 
                                  Point3d center, Vector3d size){
        
        // representation variant 2
        this.xyzmin = new Point3d(xyzmin);
        
        this.xyminzmax = new Point3d(xyminzmax);
        this.xminymaxzmin = new Point3d(xminymaxzmin);
        this.xminyzmax = new Point3d(xminyzmax);
        this.xmaxyzmin = new Point3d(xmaxyzmin);
        this.xmaxyminzmax = new Point3d(xmaxyminzmax);
        this.xymaxzmin = new Point3d(xymaxzmin);
        
        this.xyzmax = new Point3d(xyzmax);
        
        // representation variant 1
        this.center = center;
        this.size = size;
        
        // third representation variant
        this.min = xyzmin; 
        this.max = xyzmax; 
        
    }
    
    public List<Point3d> getCorners(){
        List<Point3d> corners = new ArrayList<>();
        corners.add(xyzmin);
        corners.add(xyminzmax);
        corners.add(xminymaxzmin);
        corners.add(xminyzmax);
        corners.add(xmaxyzmin);
        corners.add(xmaxyminzmax);
        corners.add(xymaxzmin);
        corners.add(xyzmax);
        return corners;
    }
    public AxisAlignedBoundingBox(Point3d center, Vector3d size){
        this.center = center;
        this.size = size;
        System.out.println("AABB: Origin=("+String.valueOf(center.x)+", "+String.valueOf(center.y)+", "+
                String.valueOf(center.z)+"), size=("+String.valueOf(size.x)+", "+String.valueOf(size.y)+", "+
                String.valueOf(size.z)+"!");
        
        //TODO
        // corners bestimmen, mit der nachfolgenden methode aber dann muss ich die
        // Ecken noch richtig zuordnen
    }
    public List<Point3d> getCorners2(){
        List<Point3d> corners = new ArrayList<>();
        Point3d origin = new Point3d(center);
        Vector3d halfSize = new Vector3d(size);
        halfSize.scale(0.5);
        origin.sub(halfSize);
        corners.add(origin);
        Point3d corner1 = new Point3d(origin);
        corner1.x += size.x;
        corners.add(corner1);
        Point3d corner2 = new Point3d(origin);
        corner1.y += size.y;
        corners.add(corner2);
        Point3d corner3 = new Point3d(origin);
        corner1.z += size.z;
        corners.add(corner3);
        
        Point3d origin2 = new Point3d(center);
        origin2.add(halfSize);
        corners.add(origin2);
        
        Point3d corner4 = new Point3d(origin2);
        corner4.x -= size.x;
        corners.add(corner4);
        Point3d corner5 = new Point3d(origin2);
        corner5.y -= size.y;
        corners.add(corner5);
        Point3d corner6 = new Point3d(origin2);
        corner6.z -= size.z;
        corners.add(corner6);
        return corners;
    }
   
    /**
     * Intersection of a line segment given by two points with plane given by
     * a point and the normal vector of the plane.
     * 
     * implementation follows:
     * https://stackoverflow.com/questions/7168484/3d-line-segment-and-plane-intersection/47649285?noredirect=1#comment82256851_47649285
     *
     * @param lineSegment_start line segments start point
     * @param lineSegment_end line segments end point
     * @param plane_normal plane_normal vector of the plane
     * @param plane_point point on the plane
     * @return hitPoint, if the plane hits the line segment
     */
    private static Point3d cutLineSegmentPlane(Point3d lineSegment_start, Point3d lineSegment_end, 
            Vector3d plane_normal, Point3d plane_point){
        
        Vector3d ray = new Vector3d(lineSegment_end);
        ray.sub(lineSegment_start); 

        double d = plane_normal.dot(new Vector3d(plane_point));
        
        if (plane_normal.dot(ray) == 0){
            return null; 
        }

        double t = (d - plane_normal.dot(new Vector3d(lineSegment_start))) / plane_normal.dot(ray);

        Point3d hitPoint = new Point3d();
        hitPoint.x = lineSegment_start.x + ray.x * t; 
        hitPoint.y = lineSegment_start.y + ray.y * t; 
        hitPoint.z = lineSegment_start.z + ray.z * t; 
        return hitPoint;
    }
    
    /**
     * Find the intersections of a plane with each of the 12 edges 
     * of the axis aligned bounding box. 
     * 
     * Some of the edges will intersect with the plane, 
     * some won't. The resulting list of intersection points defines a
     * polygon, which represents the "clipped plane".<p>
     * 
     * @param plane
     * @return sortest array of polygon corners
     */
    public Point3d[] clip(Plane plane){
        
        List<Point3d> corners = new ArrayList<>();
        
        Point3d hitPoint = cutLineSegmentPlane(xyzmin, xyminzmax, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        hitPoint = cutLineSegmentPlane(xyzmin, xminymaxzmin, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        hitPoint = cutLineSegmentPlane(xyzmin, xmaxyzmin, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        
        hitPoint = cutLineSegmentPlane(xyzmax, xminyzmax, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        hitPoint = cutLineSegmentPlane(xyzmax, xmaxyminzmax, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        hitPoint = cutLineSegmentPlane(xyzmax, xymaxzmin, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        
        
        hitPoint = cutLineSegmentPlane(xmaxyzmin, xmaxyminzmax, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        hitPoint = cutLineSegmentPlane(xminymaxzmin, xminyzmax, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        hitPoint = cutLineSegmentPlane(xmaxyzmin, xymaxzmin, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        hitPoint = cutLineSegmentPlane(xyminzmax, xminyzmax, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        hitPoint = cutLineSegmentPlane(xmaxyminzmax, xyminzmax, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        hitPoint = cutLineSegmentPlane(xymaxzmin, xminymaxzmin, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        
        /*System.out.println("unsorted:");
        for (int i=0;i<corners.size();i++){
                System.out.println("Corner "+String.valueOf(i)+": ("+String.valueOf(corners.get(i).x)+", "+
                        String.valueOf(corners.get(i).y)+", "+String.valueOf(corners.get(i).z)+")");
        }*/
        return sortVerticies(corners, plane.n).toArray(Point3d[]::new);
    }
    
    private Point3d findCentroid(List<Point3d> points) {
        int x = 0;
        int y = 0;
        int z = 0;
        for (Point3d p : points) {
            x += p.x;
            y += p.y;
            z += p.z;
        }
        Point3d centerPoint = new Point3d();
        centerPoint.x = x / points.size();
        centerPoint.y = y / points.size();
        centerPoint.z = z / points.size();
        return centerPoint;
    }

    /**
     * Counterclockwise sorting of verticies.
     * 
     * If your polygon is convex, take any point in the interior of the polygon, 
     * e.g. the average of all the vertices. Then you can compute the angle of 
     * each vertex to the center point, and sort according to the computed angles. 
     * This will work for any point inside the polygon. Note you will get a 
     * circular ordering.<p>
     *
     * Detailierte Beschreibung einer weiteren möglichen Implementierung:
     * https://www.baeldung.com/cs/sort-points-clockwise<p>
     * 
     * Hinweise für 3d:
     * https://stackoverflow.com/questions/14370636/sorting-a-list-of-3d-coplanar-points-to-be-clockwise-or-counterclockwise
     * 
     * @param points laying in a plane
     * @return clockwise around center sorted points
     */
    private List<Point3d> sortVerticies(List<Point3d> points, Vector3d n) {
        // get centroid
        Point3d centerPoint = findCentroid(points);
        
        Vector3d r = new Vector3d(points.get(0));
        r.sub(centerPoint);
            
        Collections.sort(points, (a, b) -> {
            double a1 = (Math.toDegrees(Math.atan2(a.x - centerPoint.x, a.y - centerPoint.y)) + 360) % 360;
            double a2 = (Math.toDegrees(Math.atan2(b.x - centerPoint.x, b.y - centerPoint.y)) + 360) % 360;
            
            // funktioniert auch nicht
            //double a1 = clockwise_around_center(a, centerPoint, n);
            //double a2 = clockwise_around_center(b, centerPoint, n);
            
            //return (int) (a1 - a2);
            
            Vector3d p = new Vector3d();
            p.cross(r, new Vector3d(centerPoint));
            boolean result = less(a, b, new Vector3d(centerPoint), p, r);
            if (result) return 1; else return -1;
        });
        
        return points;
    }
    
    /**
     * Sorting vertices laying in a plane.
     *
     * This implementation uses only dot and cross products and no inverse trig 
     * or square roots or anything. You pick the first vertex v in your list and 
     * use that as your reference. Then you cross that vector r = v - center with 
     * the normal vector to get a half-space partition vector p. If the two 
     * inputs are on the same side of p then you can use the triple product 
     * without any problems because the cylindrical angle between them will be 
     * less than π. There’s some edge cases to look out for though so I figured 
     * I’d just share some pseudocode. 
     * 
     * following
     * https://stackoverflow.com/questions/47949485/sorting-a-list-of-3d-points-in-clockwise-order?noredirect=1&lq=1
     * 
     * @param v1
     * @param v2
     * @param c be the center around which the counterclockwise sort is to be performed
     * @param p cross(r, c), the half-plane partition vector
     * @param r vertices[0] - c, use an arbitrary vector as the twelve o’clock reference
     * @return true, if v1 is clockwise from v2 around c
     */
    private boolean less(Point3d v1, Point3d v2, Vector3d c, Vector3d p, Vector3d r){
        Vector3d u1 = new Vector3d(v1);
        u1.sub(c);
        Vector3d u2 = new Vector3d(v2);
        u2.sub(c);
        double h1 = u1.dot(p);
        double h2 = u2.dot(p);
        if (h2 <= 0 && h1 > 0) 
            return false;
        else if (h1 <= 0 && h2 > 0)
            return true;
        else if (h1 == 0 && h2 == 0)
            return u1.dot(r) > 0 & u2.dot(r) < 0;
        else {
            Vector3d cross = new Vector3d();
            cross.cross(u1, u2);
            return cross.dot(c) > 0;
        }
    }
   
    // Make arctan2 function that returns a value from [0, 2 pi) instead of [-pi, pi)
    private static double arctan2(double y, double x){
        double result = Math.atan2(y, x);
        if (result >= 0) return result;
        else return 2*Math.PI+result;
    }

    // Define the key function
    // https://stackoverflow.com/questions/6989100/sort-points-in-clockwise-order
    private static double clockwise_around_center(Point3d point, Point3d center, Vector3d n){
        Vector3d centerPoint = new Vector3d(center);
        Vector3d diff = new Vector3d(point);
        diff.sub(center);
        double rcos = diff.dot(centerPoint);
        Vector3d cross = new Vector3d();
        cross.cross(diff, centerPoint);
        double rsin = n.dot(cross);
        return arctan2(rsin, rcos);
    }

    // für mehr performance:
    // Return approximation of atan2(y,x) / (PI/2);
    private static double approxAtan2(double y, double x) {
      int o = 0;
      if (y < 0) { x = -x; y = -y; o |= 4; }
      if (x <= 0) { double t = x; x = y; y = -t; o |= 2; }
      if (x <= y) { double t = y - x; x += y; y = t; o |= 1; }
      return o + y / x;
    }
    
    // https://www.geometrictools.com/GTE/Mathematics/IntrLine3AlignedBox3.h
    /*public boolean clip(Line3d line, Point3d[] out){
        //TODO
        throw new RuntimeException("not yet implmeneted!");
    }*/
    
    /**
     * Determine clipping point of a line with the bounding box of the current
     * visualization.
     * 
     * Implementation is adapted from:
     * https://stackoverflow.com/questions/3106666/intersection-of-line-segment-with-axis-aligned-box-in-c-sharp
     * 
     * This is the used and working implementation. 
     * 
     * @param line to clip on the AA-bounding-box
     * @return output near point, far point or empty array if no intersection
     */
    public Point3d[] clip(Line3d line){
         
        Vector3d halfSize = new Vector3d(this.size);
        halfSize.scale(0.5d);
        
        Point3d min_ = new Point3d(center);
        min_.sub(halfSize);
        min_.sub(line.getOrigin()); // ist 0
        
        Point3d max_ = new Point3d(center);
        max_.add(halfSize);
        max_.sub(line.getOrigin());
            
        double near = -Double.MAX_VALUE;
        double far = Double.MAX_VALUE;

        // X
        double t1 = min_.x / line.getDirectionVector().x;
        double t2 = max_.x / line.getDirectionVector().x;
        double tMin = Math.min(t1, t2);
        double tMax = Math.max(t1, t2);
        if (tMin > near) near = tMin;
        if (tMax < far) far = tMax;
        if (near > far || far < 0){
            return new Point3d[]{};
        }

        // Y
        t1 = min_.y / line.getDirectionVector().y;
        t2 = max_.y / line.getDirectionVector().y;
        tMin = Math.min(t1, t2);
        tMax = Math.max(t1, t2);
        if (tMin > near) near = tMin;
        if (tMax < far) far = tMax;
        if (near > far || far < 0){
            return new Point3d[]{};
        }

        // Z
        t1 = min_.z / line.getDirectionVector().z;
        t2 = max_.z / line.getDirectionVector().z;
        tMin = Math.min(t1, t2);
        tMax = Math.max(t1, t2);
        if (tMin > near) near = tMin;
        if (tMax < far) far = tMax;
        if (near > far || far < 0){
            return new Point3d[]{};
        }

        Point3d p1 = new Point3d(line.getOrigin());
        Vector3d dir = new Vector3d(line.getDirectionVector());
        dir.scale(near);
        p1.add(dir);
        
        Point3d p2 = new Point3d(line.getOrigin());
        dir = new Vector3d(line.getDirectionVector());
        dir.scale(far);
        p2.add(dir); 
        return new Point3d[]{p1,p2};
    }
    
    
    /** 
     * Clipping of a line based on clipping of a ray (clipRay()).
     * 
     * Does not work - produces memory overflow and crash.
     * 
     * @param line ray with origin as starting point
     * @return 0, 1, or 2 clipping points
     */
    public Point3d[] clip3(Line3d line){
        Point3d hitPoint1 = new Point3d();
        boolean result = clipRay(line, hitPoint1);
        if (result){
            Point3d hitPoint2 = new Point3d();
            Vector3d direction2 = line.getDirectionVector();
            direction2.negate();
            Line3d line2 = new Line3d(line.getOrigin(),direction2);
            result = clipRay(line2, hitPoint2);
            if (result){
                return new Point3d[]{hitPoint1, hitPoint2};
            } else {
                System.out.println("clip3: Only 1 hitpoint found!");
            }
        } else {
            System.out.println("clip3: No hitPoints found!");
        }
        return new Point3d[]{};
    }
    
    /** 
     * Clipping of a line based on clipping of a ray (clipRay2()).
     * 
     * Scheint nicht mit L_45 zu funktionieren, aber sonst ja
     * FIXME
     * 
     * @param line ray with origin as starting point
     * @return 0, 1, or 2 clipping points
     */
    public Point3d[] clip4(Line3d line){
        Point3d hitPoint1 = clipRay2(line);
        if (hitPoint1 != null){
            Vector3d direction2 = line.getDirectionVector();
            direction2.negate();
            Line3d line2 = new Line3d(line.getOrigin(),direction2);
            Point3d hitPoint2 = clipRay2(line2);
            if (hitPoint2 != null){
                return new Point3d[]{hitPoint1, hitPoint2};
            } else {
                System.out.println("clip4: Only 1 hitpoint found!");
            }
        } else {
            System.out.println("clip4: No hitPoints found!");
        }
        return new Point3d[]{};
    }
    
    
   /** 
    *  Fast Ray-Box Intersection
    *  by Andrew Woo
    *  from "Graphics Gems", Academic Press, 1990
    *
    * ungetestet
    * 
    * @param line
    * @param hitPoint
    * @return true if the rays hits the boundingBox
    */
    public boolean clipRay(Line3d line, Point3d hitPoint){

        final int RIGHT	= 0;
        final int LEFT	= 1;
        final int MIDDLE= 2;

        //double origin[NUMDIM], dir[NUMDIM];   /*ray */
        double[] origin = new double[3];
        origin[0] = line.getOrigin().x;
        origin[1] = line.getOrigin().y;
        origin[2] = line.getOrigin().z;

        double[] dir = new double[3];
        dir[0] = line.getDirectionVector().x;
        dir[1] = line.getDirectionVector().y;
        dir[2] = line.getDirectionVector().z;

        //char HitBoundingBox(minB,maxB, origin, dir,coord)
        //double minB[NUMDIM], maxB[NUMDIM];    /*box */
        double[] minB = new double[3];
        minB[0] = center.x-size.x/2;
        minB[1] = center.y-size.y/2;
        minB[2] = center.y-size.y/2;
        double[] maxB = new double[3];
        maxB[0] = center.x+size.x/2;
        maxB[1] = center.y+size.y/2;
        maxB[2] = center.y+size.y/2;

        //double coord[NUMDIM]; /* hit point */

        boolean inside = true;
        int[] quadrant = new int[3];
        /*register*/ int i;
        int whichPlane;
        double[] maxT = new double[3];
        double[] candidatePlane = new double[3];

        /* Find candidate planes; this loop can be avoided if
        rays cast all from the eye(assume perpsective view) */
        for (i=0; i<3; i++){
            if (origin[i] < minB[i]) {
                quadrant[i] = LEFT;
                candidatePlane[i] = minB[i];
                inside = false;
            } else if (origin[i] > maxB[i]) {
                quadrant[i] = RIGHT;
                candidatePlane[i] = maxB[i];
                inside = false;
            } else	{
                quadrant[i] = MIDDLE;
            }
        }

        /* Ray origin inside bounding box */
        if (inside)	{
            hitPoint.x = origin[0];
            hitPoint.y = origin[1];
            hitPoint.z = origin[2];
            return true;
        }

        /* Calculate T distances to candidate planes */
        for (i = 0; i < 3; i++){
            if (quadrant[i] != MIDDLE && dir[i] !=0.)
                maxT[i] = (candidatePlane[i]-origin[i]) / dir[i];
            else
                maxT[i] = -1.;
        }

        /* Get largest of the maxT's for final choice of intersection */
        whichPlane = 0;
        for (i = 1; i < 3; i++)
            if (maxT[whichPlane] < maxT[i])
                whichPlane = i;

        /* Check final candidate actually inside box */
        if (maxT[whichPlane] < 0.) return false;
        double[] coord = new double[3];
        for (i = 0; i < 3; i++) {
            if (whichPlane != i) {
                coord[i] = origin[i] + maxT[whichPlane] *dir[i];
                if (coord[i] < minB[i] || coord[i] > maxB[i])
                        return false;
            } else {
                coord[i] = candidatePlane[i];
            }
        }
        hitPoint.x = coord[0];
        hitPoint.y = coord[1];
        hitPoint.z = coord[2];
        return true; /* ray hits box */
    }
    
    /**
     * Calculates intersection with the given ray between a certain distance
     * interval.
     * 
     * following https://gist.github.com/aadnk/7123926
     * 
     * <p>
     * Ray-box intersection is using IEEE numerical properties to ensure the
     * test is both robust and efficient, as described in:
     * <br>
     * <code>Amy Williams, Steve Barrus, R. Keith Morley, and Peter Shirley: "An
     * Efficient and Robust Ray-Box Intersection Algorithm" Journal of graphics
     * tools, 10(1):49-54, 2005</code>
     * 
     * @param ray incident ray
     * @param minDist
     * @param maxDist
     * @return intersection point on the bounding box (only the first is
     *         returned) or null if no intersection
     */
    public Point3d clipRay2(Line3d ray/*, float minDist, float maxDist*/) {
        Vector3d invDir = new Vector3d(1f / ray.n.x, 1f / ray.n.y, 1f / ray.n.z);

        boolean signDirX = invDir.x < 0;
        boolean signDirY = invDir.y < 0;
        boolean signDirZ = invDir.z < 0;

        Point3f bbox = signDirX ? max : min;
        double tmin = (bbox.x - ray.o.x) * invDir.x;
        bbox = signDirX ? min : max;
        double tmax = (bbox.x - ray.o.x) * invDir.x;
        bbox = signDirY ? max : min;
        double tymin = (bbox.y - ray.o.y) * invDir.y;
        bbox = signDirY ? min : max;
        double tymax = (bbox.y - ray.o.y) * invDir.y;

        if ((tmin > tymax) || (tymin > tmax)) {
            return null;
        }
        if (tymin > tmin) {
            tmin = tymin;
        }
        if (tymax < tmax) {
            tmax = tymax;
        }

        bbox = signDirZ ? max : min;
        double tzmin = (bbox.z - ray.o.z) * invDir.z;
        bbox = signDirZ ? min : max;
        double tzmax = (bbox.z - ray.o.z) * invDir.z;

        if ((tmin > tzmax) || (tzmin > tmax)) {
            return null;
        }
        if (tzmin > tmin) {
            tmin = tzmin;
        }
        if (tzmax < tmax) {
            tmax = tzmax;
        }
        //if ((tmin < maxDist) && (tmax > minDist)) {
            return ray.getPointAtDistance(tmin);
        //}
        //return null;
    }
}
