package com.uoc.pac.logica.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileIO {
	
	//Se diferencia entre archivo y asset, debido que el asset es un fichero interno de la aplicación (paquete APK.)
    public InputStream readAsset(String fileName) throws IOException;

    public InputStream readFile(String fileName) throws IOException;

    public OutputStream writeFile(String fileName) throws IOException;
}
