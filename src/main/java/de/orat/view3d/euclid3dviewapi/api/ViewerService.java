package de.orat.view3d.euclid3dviewapi.api;

import de.orat.view3d.euclid3dviewapi.spi.iEuclidViewer3D;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class ViewerService {
    private static ViewerService viewerService;
    private final ServiceLoader<iEuclidViewer3D> loader;
    
    public static synchronized ViewerService getInstance(){
        if (viewerService == null){
            viewerService = new ViewerService();
        }
        return viewerService;
    }
    
    private ViewerService(){
        loader = ServiceLoader.load(iEuclidViewer3D.class);
    }
    
    public Optional<iEuclidViewer3D> getViewer(){
        return loader.findFirst();
    }
}
