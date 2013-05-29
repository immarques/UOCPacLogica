package com.uoc.pac.logica.framework;

public interface Graphics {
    public static enum PixmapFormat {
        ARGB8888, ARGB4444, RGB565
    }

    //Cargamos una imagen en formato PNG o JPG
    public Pixmap newPixmap(String fileName, PixmapFormat format);
    
    //Borramos el framebuffer con el color proporcionado.
    public void clear(int color);

    //Dibujamos un pixel en la posicion proporcionada y con el color indicado.
    //Las coordenadas fuera de la pantalla son ignoradas.
    public void drawPixel(int x, int y, int color);

    //Dibujamos una linea entre la posicion a (x,y) y la posicion b (x2,y2) con el color indicado.
    public void drawLine(int x, int y, int x2, int y2, int color);

    //Dibujamos un cuadrado desde la posicion indicada y el tamaño y color proporcionado.
    public void drawRect(int x, int y, int width, int height, int color);

    //Dibujamos una imagen a partir del objeto cargado en la cordenada (x,y). 
    //Los parametros srcX y srcY indican la cordenada de la imagen desde donde se cargaran los pixels.
    //los parametros srcWidth y srcHeight indican la proporcion de imagen a cargar.
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
            int srcWidth, int srcHeight);

    public void drawPixmap(Pixmap pixmap, int x, int y);

    //Devuelven el tamaño del framebuffer en pixels.
    public int getWidth();

    public int getHeight();
}
