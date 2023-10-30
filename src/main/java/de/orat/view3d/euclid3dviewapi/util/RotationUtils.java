package de.orat.view3d.euclid3dviewapi.util;

import org.jogamp.vecmath.Vector3d;

/**
  * @author Oliver Rettig (Oliver.Rettig@orat.de)
  */
public class RotationUtils  {
    
    /**
     * Axis angle to euler/cardan determination.
     * 
     * following
     * https://www.euclideanspace.com/maths/geometry/rotations/conversions/angleToEuler/index.htm
     * 
     * @param rot normalized rotation axis
     * @param angle in radians
     * @return heading, attitude, bank in radiians [0;pi]
     */
    public static double[] toEuler(Vector3d rot, double angle) {
        double heading, attitude, bank;
   
        if (rot.lengthSquared() == 0d){
            throw new IllegalArgumentException("Length of the given rotation vector is 0 is not allowed!");
        }
        
        double s=Math.sin(angle);
        double c=Math.cos(angle);
        double t=1-c;
        
        if ((rot.x*rot.y*t + rot.z*s) > 0.998) { // north pole singularity detected
            heading = 2*Math.atan2(rot.x*Math.sin(angle/2),Math.cos(angle/2));
            attitude = Math.PI/2;
            bank = 0;
            return new double[]{heading, attitude, bank};
        }
        if ((rot.x*rot.y*t + rot.z*s) < -0.998) { // south pole singularity detected
            heading = -2*Math.atan2(rot.x*Math.sin(angle/2),Math.cos(angle/2));
            attitude = -Math.PI/2;
            bank = 0;
            return new double[]{heading, attitude, bank};
        }
        heading = Math.atan2(rot.y * s- rot.x * rot.z * t , 1 - (rot.y*rot.y+ rot.z*rot.z ) * t);
        attitude = Math.asin(rot.x * rot.y * t + rot.z * s) ;
        bank = Math.atan2(rot.x * s - rot.y * rot.z * t , 1 - (rot.x*rot.x + rot.z*rot.z) * t);
        return new double[]{heading, attitude, bank};
    }
    
    /**
     * Euler/Cardan angles to rotate an object oriented into z-direction into an arbitray
     * given direction.
     * 
     * @param targetDir target direction into which the rotation should be done
     * @return heading, attitude, bank in radiians [0;pi]
     */
    public static double[] getEulerAnglesToRotateFromZ(Vector3d targetDir){
        
        // Vector in direction of the z-Achse
        Vector3d z = new Vector3d(0,0d,1);
       
        // Rotation axis upright to z and target direction
        Vector3d rot = new Vector3d();
        rot.cross(z, targetDir);
        
        // Rotation angle betwee z-axis and
        // target direction
        double alpha = z.angle(targetDir);
        
        return toEuler(rot, alpha);
    }
}