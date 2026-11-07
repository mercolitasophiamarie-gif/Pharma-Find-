import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;

public class PharmaDashboard extends JFrame {

    // --- Color Palette ---
    private static final Color GRADIENT_START = new Color(12, 148, 123);
    private static final Color GRADIENT_END = new Color(25, 120, 175);
    private static final Color BG_COLOR = new Color(242, 247, 249);
    private static final Color TEAL = new Color(15, 145, 126);
    private static final Color TEXT_DARK = new Color(33, 43, 54);
    private static final Color TEXT_MUTED = new Color(130, 140, 150);
    private static final Color SUCCESS_GREEN = new Color(46, 160, 67);
    private static final Color WARNING_YELLOW = new Color(210, 153, 34);

    public PharmaDashboard() {
        setTitle("PharmaFind Dashboard");
        setSize(420, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // --- Main Scrollable Area ---
        MainContentPanel mainContent = new MainContentPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setOpaque(false);

        // Build Sections
        mainContent.add(buildHeaderSection());
        mainContent.add(buildSearchBar());
        mainContent.add(buildFilterChips());
        mainContent.add(buildPromotionsSection());
        mainContent.add(buildNearbyPharmaciesSection());

        // Push everything to the top
        mainContent.add(Box.createVerticalGlue());

        // Wrapping in ScrollPane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Background container for the gradient
        JPanel backgroundWrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw white/light background for the whole frame
                g2.setColor(BG_COLOR);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Draw top gradient curve behind the content
                GradientPaint gp = new GradientPaint(0, 0, GRADIENT_START, getWidth(), 200, GRADIENT_END);
                g2.setPaint(gp);
                Path2D path = new Path2D.Double();
                path.moveTo(0, 0);
                path.lineTo(getWidth(), 0);
                path.lineTo(getWidth(), 190);
                // A subtle curve at the bottom of the gradient
                path.quadTo(getWidth() / 2.0, 240, 0, 190);
                path.closePath();
                g2.fill(path);
            }
        };
        backgroundWrapper.add(scrollPane, BorderLayout.CENTER);

        add(backgroundWrapper, BorderLayout.CENTER);
        add(buildBottomNav(), BorderLayout.SOUTH);
    }

    // =========================================================
    // UI BUILDER METHODS
    // =========================================================

    private JPanel buildHeaderSection() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(40, 25, 10, 25));

        // Top Row: Time/Status placeholder & Avatar
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        JLabel timeLabel = new JLabel("9:41");
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        topRow.add(timeLabel, BorderLayout.WEST);

        // Avatar Circle
        RoundedPanel avatar = new RoundedPanel(36, Color.WHITE);
        avatar.setPreferredSize(new Dimension(36, 36));
        avatar.setLayout(new GridBagLayout());
        JLabel initial = new JLabel("M");
        initial.setFont(new Font("SansSerif", Font.BOLD, 16));
        initial.setForeground(TEAL);
        avatar.add(initial);
        topRow.add(avatar, BorderLayout.EAST);

        // Greeting
        JLabel greeting = new JLabel("Hello, Maria");
        greeting.setFont(new Font("SansSerif", Font.BOLD, 26));
        greeting.setForeground(Color.WHITE);

        // Subtitle
        JLabel subtitle = new JLabel("Find your medicine nearby");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(new Color(255, 255, 255, 200));

        // Location Tag
        JPanel locationTag = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        locationTag.setOpaque(false);
        locationTag.add(new IconLabel(IconType.PIN, Color.WHITE, 12));
        JLabel locText = new JLabel("Naval, Biliran");
        locText.setForeground(Color.WHITE);
        locText.setFont(new Font("SansSerif", Font.PLAIN, 12));
        locationTag.add(locText);

        header.add(topRow);
        header.add(Box.createVerticalStrut(15));
        header.add(alignLeft(greeting));
        header.add(Box.createVerticalStrut(5));
        header.add(alignLeft(subtitle));
        header.add(Box.createVerticalStrut(8));
        header.add(alignLeft(locationTag));

        return header;
    }

    private JPanel buildSearchBar() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        wrapper.setOpaque(false);

        RoundedPanel searchBar = new RoundedPanel(45, Color.WHITE);
        searchBar.setPreferredSize(new Dimension(360, 50));
        searchBar.setLayout(new BorderLayout(10, 0));
        searchBar.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));

        // Shadow effect
        searchBar.setDrawShadow(true);

        searchBar.add(new IconLabel(IconType.SEARCH, TEXT_MUTED, 18), BorderLayout.WEST);

        JTextField searchField = new JTextField("Search medicine...");
        searchField.setBorder(null);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        searchField.setForeground(TEXT_MUTED);
        searchBar.add(searchField, BorderLayout.CENTER);

        // Right Pin Button
        RoundedPanel pinBtn = new RoundedPanel(36, TEAL);
        pinBtn.setPreferredSize(new Dimension(40, 40));
        pinBtn.setLayout(new GridBagLayout());
        pinBtn.add(new IconLabel(IconType.PIN, Color.WHITE, 16));
        searchBar.add(pinBtn, BorderLayout.EAST);

        wrapper.add(searchBar);
        return wrapper;
    }

    private JPanel buildFilterChips() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 20, 15, 20));

        panel.add(createChip("Use my location", TEAL, new Color(230, 248, 245), TEAL, IconType.PIN));
        panel.add(createChip("Open now", TEXT_DARK, Color.WHITE, new Color(220, 220, 220), null));
        panel.add(createChip("In stock", TEXT_DARK, Color.WHITE, new Color(220, 220, 220), null));

        return panel;
    }

    private JPanel buildPromotionsSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        // Header
        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);
        JLabel title = new JLabel("Featured promotions");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(TEXT_DARK);

        JLabel seeAll = new JLabel("See all");
        seeAll.setFont(new Font("SansSerif", Font.BOLD, 13));
        seeAll.setForeground(TEAL);

        headerRow.add(title, BorderLayout.WEST);
        headerRow.add(seeAll, BorderLayout.EAST);
        panel.add(headerRow);
        panel.add(Box.createVerticalStrut(15));

        // Promo Cards
        JPanel cardsRow = new JPanel(new GridLayout(1, 2, 15, 0));
        cardsRow.setOpaque(false);

        // Card 1
        RoundedPanel card1 = new RoundedPanel(20, new Color(42, 157, 143));
        card1.setPreferredSize(new Dimension(160, 120));
        card1.setLayout(new BoxLayout(card1, BoxLayout.Y_AXIS));
        card1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card1.add(createWhiteLabel("10% OFF", 22, true));
        card1.add(Box.createVerticalStrut(8));
        card1.add(createWhiteLabel("Vitamins & supplements", 11, false));
        card1.add(Box.createVerticalStrut(5));
        card1.add(createWhiteLabel("Naval Community Pharmacy", 10, false));

        // Card 2
        RoundedPanel card2 = new RoundedPanel(20, new Color(48, 126, 186));
        card2.setPreferredSize(new Dimension(160, 120));
        card2.setLayout(new BoxLayout(card2, BoxLayout.Y_AXIS));
        card2.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card2.add(createWhiteLabel("Buy 2 Get 1", 20, true));
        card2.add(Box.createVerticalStrut(8));
        card2.add(createWhiteLabel("Cough & cold relief", 11, false));
        card2.add(Box.createVerticalStrut(5));
        card2.add(createWhiteLabel("Biliran Health Drugstore", 10, false));

        cardsRow.add(card1);
        cardsRow.add(card2);
        panel.add(cardsRow);

        return panel;
    }

    private JPanel buildNearbyPharmaciesSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 25, 20, 25));

        // Header
        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);
        JLabel title = new JLabel("Nearby pharmacies");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(TEXT_DARK);

        JLabel viewMap = new JLabel("View map");
        viewMap.setFont(new Font("SansSerif", Font.BOLD, 13));
        viewMap.setForeground(TEAL);

        headerRow.add(title, BorderLayout.WEST);
        headerRow.add(viewMap, BorderLayout.EAST);
        panel.add(headerRow);
        panel.add(Box.createVerticalStrut(15));

        // List
        panel.add(createPharmacyCard("Naval Community Pharmacy", "0.4 km • Open until 9:00 PM", "In stock", "Updated 10 min ago", SUCCESS_GREEN));
        panel.add(Box.createVerticalStrut(12));
        panel.add(createPharmacyCard("Biliran Health Drugstore", "0.9 km • Open 24 hours", "Low stock", "Updated 1 hr ago", WARNING_YELLOW));
        panel.add(Box.createVerticalStrut(12));
        panel.add(createPharmacyCard("Seaside Pharmacy", "1.3 km • Open until 8:00 PM", "In stock", "Updated 25 min ago", SUCCESS_GREEN));
        panel.add(Box.createVerticalStrut(12));
        panel.add(createPharmacyCard("Rivera Family Drugstore", "1.8 km • Closes 7:00 PM", "In stock", "Updated 2 hrs ago", SUCCESS_GREEN));

        return panel;
    }

    private JPanel buildBottomNav() {
        JPanel nav = new JPanel(new GridLayout(1, 4));
        nav.setBackground(Color.WHITE);
        nav.setPreferredSize(new Dimension(getWidth(), 65));
        nav.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));

        nav.add(createNavBtn("Home", IconType.HOME, TEAL));
        nav.add(createNavBtn("Map", IconType.PIN, TEXT_MUTED));
        nav.add(createNavBtn("Saved", IconType.BOOKMARK, TEXT_MUTED));
        nav.add(createNavBtn("Profile", IconType.PROFILE, TEXT_MUTED));

        return nav;
    }

    // =========================================================
    // HELPER METHODS
    // =========================================================

    private JPanel alignLeft(JComponent c) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        p.add(c);
        return p;
    }

    private JLabel createWhiteLabel(String text, int size, boolean bold) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("SansSerif", bold ? Font.BOLD : Font.PLAIN, size));
        return l;
    }

    private JPanel createChip(String text, Color fg, Color bg, Color border, IconType icon) {
        RoundedPanel chip = new RoundedPanel(25, bg);
        chip.setBorderColor(border);
        chip.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 6));

        if (icon != null) {
            chip.add(new IconLabel(icon, fg, 14));
        }

        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(fg);
        chip.add(label);

        return chip;
    }

    private JPanel createPharmacyCard(String name, String details, String stock, String updated, Color stockColor) {
        RoundedPanel card = new RoundedPanel(20, Color.WHITE);
        card.setDrawShadow(true);
        card.setLayout(new BorderLayout(15, 0));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Left Icon
        RoundedPanel iconPanel = new RoundedPanel(40, TEAL);
        iconPanel.setPreferredSize(new Dimension(40, 40));
        iconPanel.setLayout(new GridBagLayout());
        iconPanel.add(new IconLabel(IconType.PLUS_PIN, Color.WHITE, 18));

        JPanel iconWrapper = new JPanel(new BorderLayout());
        iconWrapper.setOpaque(false);
        iconWrapper.add(iconPanel, BorderLayout.NORTH);
        card.add(iconWrapper, BorderLayout.WEST);

        // Center Details
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);

        JLabel nameLbl = new JLabel(name);
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 15));
        nameLbl.setForeground(TEXT_DARK);

        JLabel detailsLbl = new JLabel(details);
        detailsLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        detailsLbl.setForeground(TEXT_MUTED);

        // Status row
        JPanel statusRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        statusRow.setOpaque(false);

        // Custom dot for stock
        JPanel dot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(stockColor);
                g2.fillOval(0, 4, 8, 8);
            }
        };
        dot.setPreferredSize(new Dimension(10, 16));
        dot.setOpaque(false);

        JLabel stockLbl = new JLabel(stock);
        stockLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        stockLbl.setForeground(stockColor);

        JLabel updatedLbl = new JLabel(" " + updated);
        updatedLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        updatedLbl.setForeground(new Color(170, 170, 170));

        statusRow.add(dot);
        statusRow.add(stockLbl);
        statusRow.add(updatedLbl);

        center.add(nameLbl);
        center.add(Box.createVerticalStrut(4));
        center.add(detailsLbl);
        center.add(Box.createVerticalStrut(6));
        center.add(statusRow);

        card.add(center, BorderLayout.CENTER);

        // Right Chevron
        card.add(new IconLabel(IconType.CHEVRON_RIGHT, new Color(200, 200, 200), 20), BorderLayout.EAST);

        return card;
    }

    private JPanel createNavBtn(String text, IconType type, Color color) {
        JPanel btn = new JPanel();
        btn.setLayout(new BoxLayout(btn, BoxLayout.Y_AXIS));
        btn.setOpaque(false);

        btn.add(Box.createVerticalStrut(10));
        IconLabel icon = new IconLabel(type, color, 22);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.add(icon);

        btn.add(Box.createVerticalStrut(4));
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(color);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.add(lbl);

        // Hover effect placeholder
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); }
        });

        return btn;
    }

    // =========================================================
    // CUSTOM COMPONENTS
    // =========================================================

    // Panel with rounded corners and optional shadow
    class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;
        private Color borderColor = null;
        private boolean drawShadow = false;

        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }

        public void setBorderColor(Color c) { this.borderColor = c; }
        public void setDrawShadow(boolean draw) { this.drawShadow = draw; }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int shadowGap = drawShadow ? 4 : 0;
            int shadowOffset = drawShadow ? 2 : 0;

            if (drawShadow) {
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(shadowOffset, shadowOffset, getWidth() - shadowGap, getHeight() - shadowGap, radius, radius);
            }

            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth() - shadowGap, getHeight() - shadowGap, radius, radius);

            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - shadowGap - 1, getHeight() - shadowGap - 1, radius, radius);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Prevents strange repainting artifacts on scrolling
    class MainContentPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            // Keep transparent, let wrapper paint background
        }
    }

    // --- Vector Icon Engine to avoid external image requirements ---
    enum IconType { SEARCH, PIN, PLUS_PIN, CHEVRON_RIGHT, HOME, BOOKMARK, PROFILE }

    class IconLabel extends JLabel {
        private IconType type;
        private Color color;
        private int size;

        public IconLabel(IconType type, Color color, int size) {
            this.type = type;
            this.color = color;
            this.size = size;
            setPreferredSize(new Dimension(size, size));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            int w = getWidth();
            int h = getHeight();

            switch (type) {
                case SEARCH:
                    g2.drawOval(2, 2, w - 8, h - 8);
                    g2.drawLine(w - 7, h - 7, w - 2, h - 2);
                    break;
                case PIN:
                    Path2D pin = new Path2D.Double();
                    pin.moveTo(w/2.0, h-2);
                    pin.curveTo(w/2.0, h-2, 2, h/2.0, 2, w/2.0);
                    pin.curveTo(2, 0, w-2, 0, w-2, w/2.0);
                    pin.curveTo(w-2, h/2.0, w/2.0, h-2, w/2.0, h-2);
                    g2.draw(pin);
                    g2.drawOval(w/2 - 2, h/2 - 4, 4, 4);
                    break;
                case PLUS_PIN:
                    Path2D bg = new Path2D.Double();
                    bg.moveTo(w/2.0, h);
                    bg.curveTo(w/2.0, h, 0, h/2.0+2, 0, w/2.0);
                    bg.curveTo(0, 0, w, 0, w, w/2.0);
                    bg.curveTo(w, h/2.0+2, w/2.0, h, w/2.0, h);
                    g2.fill(bg);
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(2.0f));
                    g2.drawLine(w/2, h/2 - 6, w/2, h/2 + 2);
                    g2.drawLine(w/2 - 4, h/2 - 2, w/2 + 4, h/2 - 2);
                    break;
                case CHEVRON_RIGHT:
                    g2.drawLine(w/3, h/4, 2*w/3, h/2);
                    g2.drawLine(2*w/3, h/2, w/3, 3*h/4);
                    break;
                case HOME:
                    Path2D home = new Path2D.Double();
                    home.moveTo(2, h/2.0);
                    home.lineTo(w/2.0, 2);
                    home.lineTo(w-2, h/2.0);
                    g2.draw(home);
                    g2.drawRect(4, h/2, w-8, h/2-2);
                    break;
                case BOOKMARK:
                    Path2D bm = new Path2D.Double();
                    bm.moveTo(w/4.0, 2);
                    bm.lineTo(3*w/4.0, 2);
                    bm.lineTo(3*w/4.0, h-2);
                    bm.lineTo(w/2.0, h-6);
                    bm.lineTo(w/4.0, h-2);
                    bm.closePath();
                    g2.draw(bm);
                    break;
                case PROFILE:
                    g2.drawOval(w/2 - 4, 2, 8, 8);
                    Path2D shoulder = new Path2D.Double();
                    shoulder.moveTo(2, h-2);
                    shoulder.quadTo(w/2.0, h-12, w-2, h-2);
                    g2.draw(shoulder);
                    break;
            }
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        // Use system look and feel for smoother font rendering
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new PharmaDashboard().setVisible(true);
        });
    }
}