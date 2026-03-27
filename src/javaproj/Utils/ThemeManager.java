package javaproj.Utils;

import java.awt.*;
import javax.swing.*;

public class ThemeManager {
    private static ThemeManager instance;
    private boolean darkMode = false;

    private final Color navbarLight = new Color(245, 243, 238);
    private final Color navbarDark  = new Color(60, 63, 65);
    private final Color contentLight = Color.WHITE;
    private final Color contentDark  = new Color(40, 42, 45);

    private ThemeManager() {}

    public static ThemeManager getInstance() {
        if (instance == null) instance = new ThemeManager();
        return instance;
    }

    // Flips the theme and repaints all open application windows.
    public void toggleThemeAndRefreshAll() {
        darkMode = !darkMode;

        for (Window w : Window.getWindows()) {
            if (!w.isDisplayable()) continue;
            if (isLGoodDatePickerWindow(w)) continue;
            applyThemeToWindow(w);
            //SwingUtilities.updateComponentTreeUI(w);
            w.invalidate();
            w.validate();
            w.repaint();
        }
    }
    
    private boolean newWindowHookInstalled = false;
    
    // Applies the current theme to windows opened after startup.
    public void installAutoThemeForNewWindows() {
        if (newWindowHookInstalled) return;
        newWindowHookInstalled = true;

        Toolkit.getDefaultToolkit().addAWTEventListener(ev -> {
            if (ev instanceof java.awt.event.WindowEvent we
                && we.getID() == java.awt.event.WindowEvent.WINDOW_OPENED) {
                Window w = we.getWindow();
                if (isLGoodDatePickerWindow(w)) return;
                applyThemeToWindow(w);
                //SwingUtilities.updateComponentTreeUI(w);
                w.invalidate();
                w.validate();
                w.repaint();
            }
        }, AWTEvent.WINDOW_EVENT_MASK);
    }
    
    public Color getNavbarColor()  { return darkMode ? navbarDark  : navbarLight; }
    public Color getContentColor() { return darkMode ? contentDark : contentLight; }

    private void applyThemeToWindow(Window w) {
        if (w instanceof RootPaneContainer rpc) {
            Component cp = rpc.getContentPane();
            if (cp instanceof JPanel p) {
                applyTheme(p);
            }
        }
    }

    // Applies theme colors to a panel and its child components.
    public void applyTheme(JPanel panel) {
        String name = panel.getName();
        
        if ("navbar".equals(name) || "contentSecondary".equals(name)) {
            panel.setBackground(getNavbarColor());
        } else {
            panel.setBackground(getContentColor());
        }

        for (Component comp : panel.getComponents()) {
            String cn = comp.getClass().getName();
            if (cn.startsWith("com.github.lgooddatepicker")) {
                continue;
            }
            if (comp instanceof JPanel p) {
                applyTheme(p);
            } else if (comp instanceof JLabel lbl) {
                lbl.setForeground(darkMode ? Color.WHITE : Color.BLACK);
                if ("blue".equals(lbl.getName())) lbl.setForeground(new Color(0,102,204));
                if (lbl.isOpaque()) {
                    lbl.setBackground(darkMode ? contentDark : Color.WHITE);
                    if ("contentSecondary".equals(lbl.getName())) {
                        lbl.setBackground(darkMode ? navbarDark : new Color(209,209,209));
                    }
                    
                }
            } else if (comp instanceof JTextArea txt) {
                txt.setForeground(darkMode ? Color.WHITE : Color.BLACK);
                txt.setBackground(darkMode ? contentDark : Color.WHITE);
                if ("contentSecondary".equals(txt.getName())) {
                    txt.setBackground(darkMode ? navbarDark : new Color(209,209,209));
                }
            } else if (comp instanceof JButton btn) {
                btn.setForeground(darkMode ? Color.WHITE : Color.BLACK);
                btn.setBackground(darkMode ? navbarDark : navbarLight);
                if ("contentSecondary".equals(btn.getName())) {
                    btn.setBackground(new Color(4,110,130));
                    btn.setForeground(Color.WHITE);
                }
            } else if (comp instanceof JTextField tf) {
                tf.setForeground(darkMode ? Color.WHITE : Color.BLACK);
                tf.setBackground(darkMode ? contentDark : Color.WHITE);
            }   else if (comp instanceof JCheckBox cb) {
            cb.setForeground(darkMode ? Color.WHITE : Color.BLACK);
            cb.setBackground(getContentColor());
            cb.setOpaque(true);

        }
        }
        panel.repaint();
    }
    
    private static boolean isLGoodDatePickerWindow(Window w) {
        return w.getClass().getName().startsWith("com.github.lgooddatepicker");
    }
}
