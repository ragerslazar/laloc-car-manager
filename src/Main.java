import UI.IHM;

import javax.swing.*;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new IHM().login());
    }
}
