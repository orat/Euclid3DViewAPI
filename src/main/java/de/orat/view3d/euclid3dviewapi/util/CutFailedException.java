package de.orat.view3d.euclid3dviewapi.util;

/**
 * @author Oliver Rettig
 * 
 * TODO
 * entfernen und statt dessen die but-Methoden mit boolean result und output
 * Objekte als Argumente
*/
public class CutFailedException extends Exception {
     public CutFailedException(String msg) {
        super(msg);
    }
}
