package view;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.event.*;
import javax.swing.*;

import model.*;

@SuppressWarnings("serial")
public class TivooViewer extends JFrame {
    // constants
    public static final Dimension SIZE = new Dimension(800, 600);
    public static final String BLANK = " ";
    // web page
    private JEditorPane myPage;
    // information area
    private JLabel myStatus;
    // navigation
    private JTextField myURLDisplay;
    private JButton myBackButton;
    private JButton myNextButton;
    private JButton myHomeButton;
    // favorites
    private JButton myAddButton;
    private DefaultComboBoxModel myFavorites;
    private JComboBox myFavoritesDisplay;
    // the real worker
    protected TivooModel myModel;

    /**
     * Create a view of the given model of a web browser.
     */
    public TivooViewer(TivooModel model) {
	setSize(SIZE);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// getContentPane().add(new JPanel());
	pack();
	setVisible(true);
	myModel = model;
	// add components to frame
	setLayout(new BorderLayout());
	// must be first since other panels may refer to page
	add(makePageDisplay(), BorderLayout.CENTER);
	add(makeInputPanel(), BorderLayout.NORTH);
	add(makeInformationPanel(), BorderLayout.SOUTH);
	// control the navigation
	enableButtons();
	initUI();
    }

    public final void initUI() {
	JMenuBar menubar = new JMenuBar();
	// ImageIcon icon = new ImageIcon(getClass().getResource("exit.png"));
	JMenu file = new JMenu("File");
	file.setMnemonic(KeyEvent.VK_F);
	JMenuItem eMenuItem = new JMenuItem("Exit");
	eMenuItem.setMnemonic(KeyEvent.VK_C);
	eMenuItem.setToolTipText("Exit application");
	eMenuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent event) {
		System.exit(0);
	    }
	});
	file.add(eMenuItem);
	menubar.add(file);
	setJMenuBar(menubar);

	setTitle("Simple menu");
	setSize(SIZE);
	setLocationRelativeTo(null);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Display given URL.
     */
    public void showPage(String url) {
	try {
	    // let user leave off initial protocol
	    if (!url.startsWith("http")) {
		url = "http://" + url;
	    }
	    // check for a valid URL before updating model, view
	    new URL(url);
	    // must be a valid URL, now update model and display results
	    myModel.go(url);
	    update(url);
	} catch (MalformedURLException e) {
	    showError("Could not load " + url);
	}
    }

    /**
     * Display given message as an error in the GUI.
     */
    public void showError(String message) {
	JOptionPane.showMessageDialog(this, message, "Browser Error",
		JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Display given message as information in the GUI.
     */
    public void showStatus(String message) {
	myStatus.setText(message);
    }

    // move to the next URL in the history
    private void next() {
	String url = myModel.next();
	if (url != null) {
	    update(url);
	}
    }

    // move to the previous URL in the history
    private void back() {
	String url = myModel.back();
	if (url != null) {
	    update(url);
	}
    }

    // change current URL to the home page, if set
    private void home() {
	String url = myModel.getHome();
	if (url != null) {
	    showPage(url);
	}
    }

    // update just the view to display given URL
    private void update(String url) {
	try {
	    myPage.setPage(url);
	    myURLDisplay.setText(url);
	    enableButtons();
	} catch (IOException e) {
	    // should never happen since only checked URLs make it this far ...
	    showError("Could not load " + url);
	}
    }

    // prompt user for name of favorite to add to collection
    private void addFavorite() {
	String name = JOptionPane.showInputDialog(this, "Enter name",
		"Add Favorite", JOptionPane.QUESTION_MESSAGE);
	if (name != null) {
	    // update model and display results
	    myModel.addFavorite(name);
	    myFavorites.addElement(name);
	}
    }

    // only enable buttons when useful to user
    private void enableButtons() {
	myBackButton.setEnabled(myModel.hasPrevious());
	myNextButton.setEnabled(myModel.hasNext());
	myHomeButton.setEnabled(myModel.getHome() != null);
    }

    // convenience method to create HTML page display
    private JComponent makePageDisplay() {
	// displays the web page
	myPage = new JEditorPane();
	myPage.setPreferredSize(SIZE);
	// allow editor to respond to link-clicks/mouse-overs
	myPage.setEditable(false);
	myPage.addHyperlinkListener(new LinkFollower());
	return new JScrollPane(myPage);
    }

    // organize user's options for controlling/giving input to model
    private JComponent makeInputPanel() {
	JPanel panel = new JPanel(new BorderLayout());
	panel.add(makeNavigationPanel(), BorderLayout.NORTH);
	panel.add(makePreferencesPanel(), BorderLayout.SOUTH);
	return panel;
    }

    // make the panel where "would-be" clicked URL is displayed
    private JComponent makeInformationPanel() {
	// BLANK must be non-empty or status label will not be displayed in GUI
	myStatus = new JLabel(BLANK);
	return myStatus;
    }

    // make user-entered URL/text field and back/next buttons
    private JComponent makeNavigationPanel() {
	JPanel panel = new JPanel();
	myBackButton = new JButton("Back");
	panel.add(myBackButton);

	myNextButton = new JButton("Next");
	panel.add(myNextButton);

	myHomeButton = new JButton("Home");
	panel.add(myHomeButton);

	// if user presses return, load/show the URL
	myURLDisplay = new JTextField(35);
	myURLDisplay.addActionListener(new ShowPageAction());
	panel.add(myURLDisplay);

	JButton goButton = new JButton("Go");
	goButton.addActionListener(new ShowPageAction());
	myBackButton.addActionListener(new BackAction());
	// myNextButton.addActionListener(new NextAction());
	panel.add(goButton);

	return panel;
    }

    // make buttons for setting favorites/home URLs
    private JComponent makePreferencesPanel() {
	JPanel panel = new JPanel();

	myAddButton = new JButton("Add Favorite");
	panel.add(myAddButton);

	myFavorites = new DefaultComboBoxModel();
	myFavorites.addElement(" All Favorites ");
	myFavoritesDisplay = new JComboBox(myFavorites);
	panel.add(myFavoritesDisplay);

	JButton setHomeButton = new JButton("Set Home");
	panel.add(setHomeButton);

	return panel;
    }

    /**
     * Inner class to factor out showing page associated with the entered URL
     */
    private class ShowPageAction implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    showPage(myURLDisplay.getText());
	}
    }

    private class BackAction implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    back();
	}
    }

    /**
     * Inner class to deal with link-clicks and mouse-overs
     */
    private class LinkFollower implements HyperlinkListener {
	public void hyperlinkUpdate(HyperlinkEvent evt) {
	    // user clicked a link, load it and show it
	    if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		showPage(evt.getURL().toString());
	    }
	    // user moused-into a link, show what would load
	    else if (evt.getEventType() == HyperlinkEvent.EventType.ENTERED) {
		showStatus(evt.getURL().toString());
	    }
	    // user moused-out of a link, erase what was shown
	    else if (evt.getEventType() == HyperlinkEvent.EventType.EXITED) {
		showStatus(BLANK);
	    }
	}
    }
}
