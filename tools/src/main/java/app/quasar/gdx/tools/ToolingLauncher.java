package app.quasar.gdx.tools;

import app.quasar.gdx.QuasarConfig;
import app.quasar.gdx.QuasarGame;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class ToolingLauncher {
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("Quasar Tooling");

        new Lwjgl3Application(new QuasarGame(new QuasarConfig(true)), config);
    }
}
