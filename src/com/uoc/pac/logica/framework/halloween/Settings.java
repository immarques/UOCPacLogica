package com.uoc.pac.logica.framework.halloween;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.uoc.pac.logica.framework.FileIO;

public class Settings {
	//Esta variable nos permite saber si se reproducen o no los sonidos.
    public static boolean soundEnabled = true;
    //Esta variable nos permite bloquear los limites de la pantalla.
    public static boolean limitedScreen = false;
    //Esta variable nos permite visualizar los delimitadores del mundo logico.
    public static boolean viewDelimited = false;
    //Permite que seamos inmortales al golpear una ardilla
    public static boolean inmortal = false;
    //Permite establecer dificultad, false = facil, true = dificil
    public static boolean dificultad = false;
    //Array de puntuaciones
    public final static int[] highscores = new int[] { 100, 80, 50, 30, 10 };
    public final static String file = ".halloween";

    /*
     * Este metodo nos permite cargar el fichero de configuracion. En el se almacena la variable de sonido
     * y las puntuaciones realizadas en cada partida.
     * Si algo sale mal durante la carga o no podemos acceder al fichero, simplemente se cargaran los valores
     * por defecto.
     */
    public static void load(FileIO files) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(files.readFile(file)));
            soundEnabled = Boolean.parseBoolean(in.readLine());
            limitedScreen = Boolean.parseBoolean(in.readLine());
            viewDelimited = Boolean.parseBoolean(in.readLine());
            inmortal = Boolean.parseBoolean(in.readLine());
            dificultad = Boolean.parseBoolean(in.readLine());
            for(int i = 0; i < 5; i++) {
                highscores[i] = Integer.parseInt(in.readLine());
            }
        } catch (IOException e) {
            
        } catch (NumberFormatException e) {
            
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }

    /*
     * Se almacena en el fichero de configuracion de la memoria externa, la variable sonido y las ultimas puntuanciones.
     */
    public static void save(FileIO files) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    files.writeFile(file)));
            out.write(Boolean.toString(soundEnabled));
            out.write("\n");
            out.write(Boolean.toString(limitedScreen));
            out.write("\n");
            out.write(Boolean.toString(viewDelimited));
            out.write("\n");
            out.write(Boolean.toString(inmortal));
            out.write("\n");
            out.write(Boolean.toString(dificultad));
            out.write("\n");
            for(int i = 0; i < 5; i++) {
                out.write(Integer.toString(highscores[i]));
                out.write("\n");
            }

        } catch (IOException e) {
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
            }
        }
    }

    /*
     * Este metodo nos permite añadir una nueva puntuacion entre las 5 más altas. El array se vuelve a ordenar de mayor a menor.
     */
    public static void addScore(int score) {
        for(int i=0; i < 5; i++) {
            if(highscores[i] < score) {
                for(int j= 4; j > i; j--)
                    highscores[j] = highscores[j-1];
                highscores[i] = score;
                break;
            }
        }
    }
}
