package view;

import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.border.*;
import javax.swing.*;
import org.dom4j.*;
import org.jdesktop.swingx.*;
import writers.*;
import controller.*;
import model.*;

@SuppressWarnings("serial")
public class TivooGenerator extends JFrame {
    // constants
    public static final Dimension SIZE = new Dimension(400, 300);
    public static final String BLANK = " ";
    public static final String TITLE = "TivooGenerator";

    // the real worker
    protected TivooModel myModel;
    protected TivooModelForBrowser myBrowserModel;
    protected TivooController myController;
    protected TivooGenerator myView;
    protected TivooBrowser myBrowser;
    protected TivooFilterConfigurator myConfigurator;
    
    protected Set<String> myKeyWordSet;
    private JCheckBox myTimeFrameBox;
    private JCheckBox myKeyWordBox;
    private JCheckBox myConflictBox;
    private JXDatePicker myStartPicker;
    private JXDatePicker myEndPicker;
    private ButtonGroup myOutputGroup;
    private JRadioButton mySortedListButton = new JRadioButton("Sorted List");
    private JRadioButton myDailyCalendarButton = new JRadioButton("Daily Calendar");
    private JRadioButton myWeeklyCalendarButton = new JRadioButton("Weekly Calendar");
    private JRadioButton myMonthlyCalendarButton = new JRadioButton("Monthly Calendar");
    
    private Map<JRadioButton, TivooWriter> writermap 
    = new HashMap<JRadioButton, TivooWriter>() {{
	put(mySortedListButton, new DailyCalendarWriter());
	put(myDailyCalendarButton, new DailyCalendarWriter());
	put(myWeeklyCalendarButton, new WeeklyCalendarWriter());
	put(myMonthlyCalendarButton, new DailyCalendarWriter());
    }};
    
    public TivooGenerator(TivooModel model, TivooController controller) {
	myBrowserModel = new TivooModelForBrowser();
	setTitle(TITLE);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// getContentPane().add(new JPanel());
	myModel = model;
	myController = controller;
	myView = this;
	// add components to frame
	setLayout(new BorderLayout());
	add(makeOutputPanel(), BorderLayout.EAST);
	add(makeFilterPanel(), BorderLayout.WEST);
	add(makeCommitPanel(), BorderLayout.SOUTH);
	// control the navigation
	//enableButtons();
	addMenu();
	setSize(SIZE);
	setLocationRelativeTo(null);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	pack();
	setVisible(true);
    }

    public void addMenu() {
	JMenuBar menubar = new JMenuBar();
	// ImageIcon icon = new ImageIcon(getClass().getResource("exit.png"));
	JMenu file = new JMenu("File");
	file.setMnemonic(KeyEvent.VK_F);
	JMenuItem exititem = new JMenuItem("Exit Q");
	exititem.setMnemonic(KeyEvent.VK_Q);
	exititem.setToolTipText("Exit application");
	exititem.addActionListener(new ExitAction());
	
	JMenuItem openitem = new JMenuItem("Open O");
	openitem.setMnemonic(KeyEvent.VK_O);
	openitem.setToolTipText("Open XML file");
	openitem.addActionListener(new LoadFileAction());
	
	JMenuItem clearitem = new JMenuItem("Clear C");
	clearitem.setMnemonic(KeyEvent.VK_C);
	clearitem.setToolTipText("Clear events");
	clearitem.addActionListener(new ClearAction());
	file.add(openitem);
	file.add(clearitem);
	file.add(exititem);
	menubar.add(file);
	setJMenuBar(menubar);

    }

    private void loadFile() throws IOException, DocumentException {
        JFileChooser fc = new JFileChooser(".");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            myController.doRead(fc.getSelectedFile());
        }
    }
    
    private void pollFilter() {

    }
    
    private TivooWriter checkOutputFormat() {
	for (Enumeration<AbstractButton> e = myOutputGroup.getElements(); e.hasMoreElements(); ) {
	    JRadioButton b = (JRadioButton) e.nextElement();
	    if (b.getModel() == myOutputGroup.getSelection()) {
	    	return writermap.get(b);
	    }
	}
	return null;
    }
    
    private JComponent makeFilterPanel() {
	JPanel panel = new JPanel(new BorderLayout());
	Border border = BorderFactory.createTitledBorder
		(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Filters");
	panel.setBorder(border);
	JPanel timeframepanel = new JPanel(new BorderLayout(10, 10));
	timeframepanel.setLayout(new GridLayout(1, 2));
	myTimeFrameBox = new JCheckBox("By Time Frame");
	myStartPicker = new JXDatePicker();
	myEndPicker = new JXDatePicker();
	timeframepanel.add(myTimeFrameBox);
	timeframepanel.add(myStartPicker);
	timeframepanel.add(myEndPicker);
	myKeyWordBox = new JCheckBox("By Keyword");
	JPanel keywordpanel = new JPanel(new BorderLayout());
	keywordpanel.setLayout(new GridLayout(1, 3));
	JButton keywordconfig = new JButton("Config");
	keywordconfig.setSize(20, 10);
	keywordconfig.addActionListener(new FilterConfigAction());
	keywordpanel.add(myKeyWordBox);
	keywordpanel.add(keywordconfig);

	panel.setLayout(new GridLayout(2, 3));
	panel.add(timeframepanel);
	panel.add(keywordpanel);
	return panel;
    }
    
    private JComponent makeOutputPanel() {
	JPanel panel = new JPanel(new BorderLayout());
	Border border = BorderFactory.createTitledBorder
		(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Output Format");
	panel.setBorder(border);
	myConflictBox = new JCheckBox("Highlight conflicting events");
	myOutputGroup = new ButtonGroup();
	myOutputGroup.add(mySortedListButton);
	myOutputGroup.add(myDailyCalendarButton);
	myOutputGroup.add(myWeeklyCalendarButton);
	myOutputGroup.add(myMonthlyCalendarButton);
	panel.setLayout(new GridLayout(5, 1));
	panel.add(mySortedListButton);
	panel.add(myDailyCalendarButton);
	panel.add(myWeeklyCalendarButton);
	panel.add(myMonthlyCalendarButton);
	panel.add(myConflictBox);
	return panel;
    }
    
    private JPanel makeCommitPanel() {
	JPanel panel = new JPanel(new BorderLayout());
	JButton output = new JButton("Output");
	output.addActionListener(new WriteAndOpenBrowserAction());
	panel.add(output);
	return panel;
    }
    
    private class WriteAndOpenBrowserAction implements ActionListener {
	public void actionPerformed(ActionEvent e) {
    	    pollFilter();
    	    TivooWriter writer = checkOutputFormat();
    	    if (writer == null) {
    		showError("Please select an output format!");
    		return;
    	    }
    	    myController.doWrite(writer, "output/testhtml_viewtest.html", 
    		    "output/details_viewtest");
    	    myBrowser = TivooBrowser.getInstance(myBrowserModel, myView, myController);
	}
    }

    private class FilterConfigAction implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    myConfigurator = TivooFilterConfigurator.getInstance(myModel, myView, myController);
	}
    }
    
    private class LoadFileAction implements ActionListener {
	public void actionPerformed(ActionEvent event) {
	    try {
		loadFile();
	    } catch (IOException e){
		e.printStackTrace();
	    }  catch (DocumentException e) {
		e.printStackTrace();
	    }
	}
    }
    
    private class ExitAction implements ActionListener {
	public void actionPerformed(ActionEvent event) {
	    System.exit(0);
	}
    }
    
    private class ClearAction implements ActionListener {
	public void actionPerformed(ActionEvent event) {
	    myModel.reset();
	}
    }
    
    public void showError(String message) {
	JOptionPane.showMessageDialog(this, message, "Generator Error",
		JOptionPane.ERROR_MESSAGE);
    }
    
}