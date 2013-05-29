package com.uoc.pac.logica.framework;

import java.util.List;

public interface Input {
	
	//registra el c�digo de la tecla pulsada, y su car�cter Unicode en caso de producirse el evento KEY_UP.
    public static class KeyEvent {
    	//cuando se pulsa una tecla
        public static final int KEY_DOWN = 0;
      //cuando deja de pulsarse una tecla
        public static final int KEY_UP = 1;

        public int type;
        public int keyCode;
        public char keyChar;

        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (type == KEY_DOWN)
                builder.append("key down, ");
            else
                builder.append("key up, ");
            builder.append(keyCode);
            builder.append(",");
            builder.append(keyChar);
            return builder.toString();
        }
    }

    //Se almacena el tipo de TouchEvent, la posici�n del dedo con respecto al origen del componente 
    //de interfaz de usuario, y el ID del puntero que se le dio al dedo por el controlador de pantalla t�ctil.
    public static class TouchEvent {
    	//cuando se pulsa sobre la pantalla con el dedo
        public static final int TOUCH_DOWN = 0;
        //cuando se levanta el dedo de la pantalla
        public static final int TOUCH_UP = 1;
      //cuando arrastra el dedo sobre la pantalla
        public static final int TOUCH_DRAGGED = 2;

        public int type;
        public int x, y;
        public int pointer;

        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (type == TOUCH_DOWN)
                builder.append("touch down, ");
            else if (type == TOUCH_DRAGGED)
                builder.append("touch dragged, ");
            else
                builder.append("touch up, ");
            builder.append(pointer);
            builder.append(",");
            builder.append(x);
            builder.append(",");
            builder.append(y);
            return builder.toString();
        }
    }

    //Nos permite saber si una tecla ha sido pulsada
    public boolean isKeyPressed(int keyCode);

    //Nos permite saber si se ha pulsado un punto de la pantalla
    public boolean isTouchDown(int pointer);

    //Obtenermos la cordenada X del punto.
    public int getTouchX(int pointer);

    //Obtenermos la cordenada Y del punto.
    public int getTouchY(int pointer);

    //devuelven los respectivos valores de aceleraci�n de cada eje del aceler�metro.
    public float getAccelX();

    public float getAccelY();

    public float getAccelZ();

    //Devuelven los eventos KeyEvent y TouchEvent que se han registrado desde la �ltima vez que se llamo a estos m�todos.
    //Los eventos est�n ordenados as� como se producieron por lo que el evento m�s reciente esta al final de la lista.
    public List<KeyEvent> getKeyEvents();

    public List<TouchEvent> getTouchEvents();
}
