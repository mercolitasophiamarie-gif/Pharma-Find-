import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

public class PharmaFindLogin extends JFrame {

    // Set to false to hide the "Use my location & search" button.
    private static final boolean SHOW_LOCATION_BUTTON = true;

    private static final int WINDOW_WIDTH = 460;
    private static final int WINDOW_HEIGHT = 780;
    private static final int HEADER_HEIGHT = 210;
    private static final int FIELD_HEIGHT = 42;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField medicineField;

    private boolean passwordVisible = false;
    private char defaultEchoChar;

    private final Color GREEN = new Color(15, 145, 126);
    private final Color BLUE = new Color(25, 130, 172);
    private final Color DARK_GREEN = new Color(24, 125, 105);
    private final Color BORDER_GREY = new Color(205, 215, 215);
    private final Color FIELD_BG = new Color(241, 245, 247);
    private final Color ICON_GREY = new Color(138, 148, 158);
    private final Color TEXT_DARK = new Color(65, 75, 75);
    private final Color TEXT_GREY = new Color(105, 115, 120);
    private final Color HINT_GREY = new Color(156, 163, 175);

    public PharmaFindLogin() {

        setTitle("PharmaFind");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Root paints the gradient behind the header AND behind the rounded top of the card.
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setPaint(new GradientPaint(0, 0, new Color(12, 148, 123),
                        getWidth(), HEADER_HEIGHT + 30, new Color(25, 120, 175)));
                g2.fillRect(0, 0, getWidth(), HEADER_HEIGHT + 30);
                g2.dispose();
            }
        };

        // ================= HEADER =================

        HeaderPanel header = new HeaderPanel();
        header.setPreferredSize(new Dimension(WINDOW_WIDTH, HEADER_HEIGHT));
        mainPanel.add(header, BorderLayout.NORTH);

        // ================= MAIN CONTENT CONTAINER =================

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(16, 28, 16, 28));

        // ================= MEDICINE SEARCH =================

        medicineField = new HintField("Search medicine (e.g., Paracetamol)");
        content.add(fixed(createFieldPanel(new DrawnIcon(DrawnIcon.SEARCH), medicineField, FIELD_BG, 20, null), FIELD_HEIGHT));
        content.add(Box.createVerticalStrut(8));

        // ================= LOCATION BUTTON =================

        if (SHOW_LOCATION_BUTTON) {
            PillButton locationButton = new PillButton("Use my location & search");
            locationButton.addActionListener(e -> {
                String medicine = medicineField.getText().trim();
                if (medicine.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a medicine name.");
                } else {
                    JOptionPane.showMessageDialog(this, "Location search will be implemented later.");
                }
            });
            content.add(fixed(locationButton, FIELD_HEIGHT));
        }
        content.add(Box.createVerticalStrut(10));

        // ================= OR LOGIN =================

        JLabel orLabel = new JLabel("or log in");
        orLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        orLabel.setForeground(Color.GRAY);

        JPanel dividerPanel = new JPanel(new GridBagLayout());
        dividerPanel.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridy = 0;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        gc.gridx = 0;
        dividerPanel.add(createLine(), gc);
        gc.gridx = 1;
        gc.weightx = 0;
        gc.insets = new Insets(0, 10, 0, 10);
        dividerPanel.add(orLabel, gc);
        gc.gridx = 2;
        gc.weightx = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        dividerPanel.add(createLine(), gc);

        content.add(fixed(dividerPanel, 16));
        content.add(Box.createVerticalStrut(8));

        // ================= EMAIL LABEL & FIELD =================

        content.add(createLabel("Email or username"));
        content.add(Box.createVerticalStrut(3));

        emailField = new HintField("name@email.com");
        content.add(fixed(createFieldPanel(new DrawnIcon(DrawnIcon.MAIL), emailField, Color.WHITE, 12, null), FIELD_HEIGHT));
        content.add(Box.createVerticalStrut(8));

        // ================= PASSWORD LABEL & FIELD =================

        content.add(createLabel("Password"));
        content.add(Box.createVerticalStrut(3));

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 13));
        passwordField.setBorder(BorderFactory.createEmptyBorder());
        passwordField.setOpaque(false);
        defaultEchoChar = passwordField.getEchoChar();

        JButton showPassword = new JButton(new DrawnIcon(DrawnIcon.EYE));
        showPassword.setBorderPainted(false);
        showPassword.setFocusPainted(false);
        showPassword.setContentAreaFilled(false);
        showPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showPassword.setToolTipText("Show or hide password");

        showPassword.addActionListener(e -> {
            passwordVisible = !passwordVisible;
            if (passwordVisible) {
                passwordField.setEchoChar((char) 0);
                showPassword.setIcon(new DrawnIcon(DrawnIcon.EYE_OFF));
            } else {
                passwordField.setEchoChar(defaultEchoChar);
                showPassword.setIcon(new DrawnIcon(DrawnIcon.EYE));
            }
        });
        passwordField.addActionListener(e -> login());

        content.add(fixed(createFieldPanel(new DrawnIcon(DrawnIcon.LOCK), passwordField, Color.WHITE, 12, showPassword), FIELD_HEIGHT));
        content.add(Box.createVerticalStrut(4));

        // ================= FORGOT PASSWORD =================

        JPanel forgotPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        forgotPanel.setOpaque(false);

        JButton forgotButton = createLink("Forgot password?");
        forgotButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Password recovery will be implemented later."));
        forgotPanel.add(forgotButton);

        content.add(fixed(forgotPanel, 16));
        content.add(Box.createVerticalStrut(6));

        // ================= LOGIN BUTTON =================

        GradientButton loginButton = new GradientButton("Log In");
        loginButton.addActionListener(e -> login());

        content.add(fixed(loginButton, 46));
        content.add(Box.createVerticalStrut(8));

        // ================= CREATE ACCOUNT =================

        JPanel createPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        createPanel.setOpaque(false);
        createPanel.add(createPlain("New here?"));
        JButton createButton = createLink("Create an account");
        createButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "User registration screen will be created later."));
        createPanel.add(createButton);
        content.add(fixed(createPanel, 16));
        content.add(Box.createVerticalStrut(2));

        // ================= PHARMACY REGISTRATION =================

        JPanel pharmacyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        pharmacyPanel.setOpaque(false);
        pharmacyPanel.add(createPlain("Own a pharmacy?"));
        JButton registerButton = createLink("Register your pharmacy");
        registerButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Pharmacy registration screen will be created later."));
        pharmacyPanel.add(registerButton);
        content.add(fixed(pharmacyPanel, 16));
        content.add(Box.createVerticalStrut(8));

        // ================= INFORMATION NOTICE =================

        JPanel noticePanel = new RoundedBox(new Color(242, 247, 248), null, 10);
        noticePanel.setLayout(new BorderLayout(8, 0));
        noticePanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel noticeText = new JLabel(
                "<html>PharmaFind shows medicine availability only.<br>" +
                        "No online orders or payments.</html>"
        );
        noticeText.setFont(new Font("Arial", Font.PLAIN, 11));
        noticeText.setForeground(TEXT_GREY);

        noticePanel.add(new JLabel(new DrawnIcon(DrawnIcon.INFO)), BorderLayout.WEST);
        noticePanel.add(noticeText, BorderLayout.CENTER);

        content.add(fixed(noticePanel, 48));

        // ================= WHITE CARD WRAPPER (No Scrollbar Needed) =================

        RoundedCardPanel cardWrapper = new RoundedCardPanel(content);
        mainPanel.add(cardWrapper, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    // ================= HELPERS =================

    /**
     * Full width, fixed height, left aligned (so BoxLayout lines everything up).
     */
    private <T extends JComponent> T fixed(T c, int height) {
        c.setPreferredSize(new Dimension(10, height));
        c.setMinimumSize(new Dimension(10, height));
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    // Helper to create a rounded text field with an icon on the left
    private JPanel createFieldPanel(Icon icon, JComponent field, Color fill, int radius, JComponent trailing) {
        RoundedBox panel = new RoundedBox(fill, BORDER_GREY, radius);
        panel.setLayout(new BorderLayout(8, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 8));

        field.setFont(new Font("Arial", Font.PLAIN, 13));
        if (field instanceof JTextField) {
            ((JTextField) field).setBorder(BorderFactory.createEmptyBorder());
            field.setOpaque(false);
        }

        panel.add(new JLabel(icon), BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        if (trailing != null) {
            panel.add(trailing, BorderLayout.EAST);
        }
        return panel;
    }

    private JComponent createLine() {
        JPanel line = new JPanel();
        line.setBackground(BORDER_GREY);
        line.setPreferredSize(new Dimension(10, 1));
        line.setMinimumSize(new Dimension(10, 1));
        return line;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(TEXT_DARK);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel createPlain(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setForeground(TEXT_GREY);
        return label;
    }

    private JButton createLink(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(DARK_GREEN);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your email and password.", "Missing Information", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Login interface is ready. Database authentication will be connected later.");
        }
    }

    // ================= TEXT FIELD WITH PLACEHOLDER =================

    class HintField extends JTextField {
        private final String hint;

        HintField(String hint) {
            this.hint = hint;
            setForeground(new Color(55, 65, 81));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(HINT_GREY);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(hint, getInsets().left, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        }
    }

    // ================= ROUNDED BOX (fields, notice) =================

    class RoundedBox extends JPanel {
        private final Color fill;
        private final Color border;
        private final int radius;

        RoundedBox(Color fill, Color border, int radius) {
            this.fill = fill;
            this.border = border;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            RoundRectangle2D shape = new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, radius * 2, radius * 2);
            g2.setColor(fill);
            g2.fill(shape);
            if (border != null) {
                g2.setColor(border);
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(shape);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ================= CARD WRAPPER FOR CURVED TOP =================

    class RoundedCardPanel extends JPanel {
        public RoundedCardPanel(Component comp) {
            setLayout(new BorderLayout());
            setOpaque(false);
            add(comp);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            // rounded top corners
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight() + 40, 40, 40));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ================= GRADIENT BUTTON =================

    class GradientButton extends JButton {
        GradientButton(String text) {
            super(text);
            setFont(new Font("Arial", Font.BOLD, 15));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gradient = new GradientPaint(0, 0, getModel().isPressed() ? GREEN.darker() : GREEN,
                    getWidth(), 0, getModel().isPressed() ? BLUE.darker() : BLUE);
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ================= "USE MY LOCATION" PILL BUTTON =================

    class PillButton extends JButton {
        PillButton(String text) {
            super(text);
            setFont(new Font("Arial", Font.BOLD, 13));
            setForeground(DARK_GREEN);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            RoundRectangle2D shape = new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, getHeight(), getHeight());
            g2.setColor(getModel().isPressed() ? new Color(214, 240, 236) : new Color(231, 248, 245));
            g2.fill(shape);
            g2.setColor(GREEN);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(shape);

            // pin icon + text, centered together
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int iconW = 16, gap = 8;
            int textW = fm.stringWidth(getText());
            int startX = (getWidth() - (iconW + gap + textW)) / 2;
            int iconY = (getHeight() - 18) / 2;

            g2.translate(startX, iconY);
            g2.setColor(GREEN);
            Path2D pin = new Path2D.Double();
            pin.moveTo(8, 1);
            pin.curveTo(4, 1, 1, 3.5, 1, 7);
            pin.curveTo(1, 11, 8, 18, 8, 18);
            pin.curveTo(8, 18, 15, 11, 15, 7);
            pin.curveTo(15, 3.5, 12, 1, 8, 1);
            pin.closePath();
            g2.fill(pin);
            g2.setColor(new Color(231, 248, 245));
            g2.fill(new Ellipse2D.Double(5.5, 4.5, 5, 5));
            g2.translate(-startX, -iconY);

            g2.setColor(getForeground());
            g2.drawString(getText(), startX + iconW + gap, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
            g2.dispose();
        }
    }

    // ================= ICONS DRAWN IN CODE =================

    class DrawnIcon implements Icon {
        static final int SEARCH = 0, MAIL = 1, LOCK = 2, EYE = 3, EYE_OFF = 4, INFO = 5;
        private final int type;

        DrawnIcon(int type) {
            this.type = type;
        }

        @Override
        public int getIconWidth() {
            return 20;
        }

        @Override
        public int getIconHeight() {
            return 20;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.translate(x, y);
            g2.setColor(ICON_GREY);
            g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            switch (type) {
                case SEARCH:
                    g2.draw(new Ellipse2D.Double(2, 2, 11, 11));
                    g2.drawLine(11, 11, 17, 17);
                    break;
                case MAIL:
                    g2.draw(new RoundRectangle2D.Double(1, 4, 18, 12, 3, 3));
                    g2.drawLine(2, 6, 10, 12);
                    g2.drawLine(18, 6, 10, 12);
                    break;
                case LOCK:
                    g2.fill(new RoundRectangle2D.Double(3, 9, 14, 10, 3, 3));
                    g2.drawArc(5, 2, 10, 12, 0, 180);
                    g2.drawLine(5, 8, 5, 9);
                    g2.drawLine(15, 8, 15, 9);
                    break;
                case EYE:
                case EYE_OFF:
                    Path2D eye = new Path2D.Double();
                    eye.moveTo(1, 10);
                    eye.quadTo(10, 2, 19, 10);
                    eye.quadTo(10, 18, 1, 10);
                    g2.draw(eye);
                    g2.draw(new Ellipse2D.Double(7.5, 7.5, 5, 5));
                    if (type == EYE_OFF) {
                        g2.drawLine(3, 17, 17, 3);
                    }
                    break;
                case INFO:
                    g2.draw(new Ellipse2D.Double(1, 1, 18, 18));
                    g2.drawLine(10, 9, 10, 15);
                    g2.fill(new Ellipse2D.Double(9, 5.5, 2, 2));
                    break;
                default:
                    break;
            }
            g2.dispose();
        }
    }

    // ================= HEADER DESIGN =================

    class HeaderPanel extends JPanel {
        HeaderPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setOpaque(false);

            add(Box.createVerticalStrut(20));

            // LOGO: white circle with a teal map pin and a white plus
            JPanel logoContainer = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Color.WHITE);
                    g2.fillOval(0, 0, getWidth(), getHeight());

                    double s = getWidth() / 70.0;
                    g2.scale(s, s);
                    Path2D pin = new Path2D.Double();
                    pin.moveTo(35, 14);
                    pin.curveTo(26, 14, 19, 21, 19, 30);
                    pin.curveTo(19, 41, 35, 56, 35, 56);
                    pin.curveTo(35, 56, 51, 41, 51, 30);
                    pin.curveTo(51, 21, 44, 14, 35, 14);
                    pin.closePath();
                    g2.setColor(GREEN);
                    g2.fill(pin);
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(32, 20, 6, 20, 2, 2);
                    g2.fillRoundRect(25, 27, 20, 6, 2, 2);
                    g2.dispose();
                }
            };
            logoContainer.setPreferredSize(new Dimension(70, 70));
            logoContainer.setMinimumSize(new Dimension(70, 70));
            logoContainer.setMaximumSize(new Dimension(70, 70));
            logoContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoContainer.setOpaque(false);
            add(logoContainer);

            add(Box.createVerticalStrut(8));

            // TITLE
            JLabel title = new JLabel("PharmaFind");
            title.setFont(new Font("Arial", Font.BOLD, 24));
            title.setForeground(Color.WHITE);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(title);

            add(Box.createVerticalStrut(3));

            // SUBTITLE
            JLabel subtitle = new JLabel("Find nearby pharmacies with your medicine");
            subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
            subtitle.setForeground(new Color(235, 247, 247));
            subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(subtitle);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // WAVE EFFECT
            g2.setColor(new Color(255, 255, 255, 30));
            g2.setStroke(new BasicStroke(2));

            for (int i = 0; i < 2; i++) {
                Path2D wave = new Path2D.Double();
                wave.moveTo(-20, 80 + i * 45);
                for (int x = 0; x <= getWidth() + 20; x += 10) {
                    double y = 80 + i * 45 + 14 * Math.sin((x / 50.0) + i);
                    wave.lineTo(x, y);
                }
                g2.draw(wave);
            }

            g2.dispose();
        }
    }
}