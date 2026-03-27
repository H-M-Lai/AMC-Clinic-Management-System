package javaproj.Utils;

import java.awt.CardLayout;
import javax.swing.JPanel;

public class PanelController {
    
    private JPanel contentPanel;

    public PanelController(JPanel contentPanel) {
        this.contentPanel = contentPanel;
    }

    // Shows a panel that was previously added to the CardLayout by name.
    public void show(String name) {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, name);
    }
    
    public JPanel getContentPanel() {
        return contentPanel;
    }
}
