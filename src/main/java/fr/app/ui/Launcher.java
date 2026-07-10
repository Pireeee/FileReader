package fr.app.ui;

// Entry point kept separate from MainApp: a jpackage/classpath launch fails
// with "JavaFX runtime components are missing" if the main class itself
// extends javafx.application.Application without being on the module path.
public class Launcher {
    public static void main(String[] args) {
        MainApp.main(args);
    }
}
