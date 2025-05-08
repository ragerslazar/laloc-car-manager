import com.ragerslazar.laloc.view.IHM;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new IHM().login());
    }
}
