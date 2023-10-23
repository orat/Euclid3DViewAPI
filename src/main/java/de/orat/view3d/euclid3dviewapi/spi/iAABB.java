package de.orat.view3d.euclid3dviewapi.spi;

import de.orat.view3d.euclid3dviewapi.util.Line3d;
import de.orat.view3d.euclid3dviewapi.util.Plane;
import org.jogamp.vecmath.Point3d;

/**
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public interface iAABB {
    public Point3d[] clip(Plane plane);
    public Point3d[] clip(Line3d line);
}
