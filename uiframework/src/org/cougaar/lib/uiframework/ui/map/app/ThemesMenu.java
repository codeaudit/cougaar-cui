package org.cougaar.lib.uiframework.ui.map.app;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

// import assessment.themes.*;
import org.cougaar.lib.uiframework.ui.themes.*;

public class ThemesMenu extends JMenu
{
    public ThemesMenu(JFrame frame)
    {
      this(frame, "Themes", 'T');
    }
    public ThemesMenu(JFrame frame, String title, char mnemonic)
    {
        super(title);
        this.frame=frame;
        setMnemonic(mnemonic);
        createMenuItems();
    }

    //
    //  ** The following is code borrowed from Sun's SwingSet example **
    //  this is here just for development purposes for experimenting with
    //  different look and feels
    //

    // The current Look & Feel
    private String metal    = "javax.swing.plaf.metal.MetalLookAndFeel";
    private String currentLookAndFeel = metal;
    private ButtonGroup themesMenuGroup = new ButtonGroup();
    private JFrame frame;

    /**
     * Create menus
     */
    // private JMenuBar
    private void createMenuItems() {
    JMenu themesMenu=this;
	JMenuItem mi;


	mi = createThemesMenuItem(themesMenu, "Default", 'D', "",
            new DefaultMetalTheme());
	mi.setSelected(true); // This is the default theme

	createThemesMenuItem(themesMenu, "Aqua", 'A', "", new AquaTheme());

	createThemesMenuItem(themesMenu, "Charcoal", 'C', "", new CharcoalTheme());

	createThemesMenuItem(themesMenu, "High Contrast", 'H', "", new ContrastTheme());

	createThemesMenuItem(themesMenu, "Emerald", 'E', "", new EmeraldTheme());

	createThemesMenuItem(themesMenu, "Ruby", 'R', "", new RubyTheme());

  /**/
	createThemesMenuItem(themesMenu, "Presentation", 'P', "", new DemoMetalTheme());

	createThemesMenuItem(themesMenu, "Sandstone", 'S', "", new KhakiMetalTheme());

	createThemesMenuItem(themesMenu, "Big High Contrast", 'I', "", new BigContrastMetalTheme());

        createThemesMenuItem(themesMenu, "Blue", 'B', "", new BlueTheme());
                                       // */
        createThemesMenuItem(themesMenu, "Cougaar", 'O', "", new CougaarTheme());

        createThemesMenuItem(themesMenu, "Cougaar Presentation", 'O', "", new CougaarPresentationTheme());
              //    */
	//return menuBar;
    }


    /**
     * Creates a JRadioButtonMenuItem for the Themes menu
     */
    private JMenuItem createThemesMenuItem(JMenu menu, String label, char mnemonic,
			       String accessibleDescription, DefaultMetalTheme theme) {
        JRadioButtonMenuItem mi = (JRadioButtonMenuItem) menu.add(new JRadioButtonMenuItem(label));
	themesMenuGroup.add(mi);
	mi.setMnemonic(mnemonic);
	mi.getAccessibleContext().setAccessibleDescription(accessibleDescription);
	mi.addActionListener(new ChangeThemeAction(frame, theme));

	return mi;
    }


  class ChangeThemeAction extends AbstractAction {
	  JFrame frame;
	  DefaultMetalTheme theme;
    protected ChangeThemeAction(JFrame frame, DefaultMetalTheme theme) {
            super("ChangeTheme");
	    this.frame = frame;
	    this.theme = theme;
    }

    public void actionPerformed(ActionEvent e) {
	    MetalLookAndFeel.setCurrentTheme(theme);
	    try {
	      UIManager.setLookAndFeel(currentLookAndFeel);
	      SwingUtilities.updateComponentTreeUI(frame);
	    } catch (Exception ex) {
	      System.out.println("Failed loading L&F: " + currentLookAndFeel);
	      System.out.println(ex);
	    }
    }
  }

    static void addCloseListener(JFrame frame) {
 	    frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
		      // need a shutdown event to notify other gui beans and
		      // then exit.
		      System.exit(0);
        }
	    });
    }

    static public void main(String[] args) {
	    JFrame testFrame=new JFrame("Test Application");
      addCloseListener(testFrame);
	    JRootPane rootPane = testFrame.getRootPane();
	    JMenuBar menu = new JMenuBar();
      rootPane.setJMenuBar(menu);

      ThemesMenu tmenu=new ThemesMenu(testFrame);
      menu.add(tmenu);

      testFrame.pack();
	    testFrame.setVisible(true);
    }
}
