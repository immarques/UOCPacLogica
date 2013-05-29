package com.uoc.pac.logica.framework;

import com.uoc.pac.logica.framework.Graphics.PixmapFormat;

public interface Pixmap {
	//Devuelven el tamaño de la imagen cargada en pixels.
    public int getWidth();

    public int getHeight();

    public PixmapFormat getFormat();
    
    //para liberar memoria y recursos.
    public void dispose();
}
