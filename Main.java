
public class Main {

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        javax.swing.SwingUtilities.invokeLater(() -> {
            PharmaFindLogin login = new PharmaFindLogin();
            login.setVisible(true);
        });
    }
}
