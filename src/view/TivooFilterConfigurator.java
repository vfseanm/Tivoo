package view;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import model.*;
import controller.*;
import sharedattributes.*;

@SuppressWarnings("serial")
public class TivooFilterConfigurator extends JFrame {
    
    private static TivooFilterConfigurator myInstance;
    protected TivooModel myModel;
    protected TivooController myController;
    protected TivooGenerator myView;
    
    private JTextArea myKeywordsArea;
    private JCheckBox mySpecificBox;
    private JTree myEventTree;
    
    public static TivooFilterConfigurator getInstance(TivooModel model, TivooGenerator view, 
	    TivooController controller) {
	if (myInstance != null) myInstance.dispose();
	myInstance = new TivooFilterConfigurator(model, view, controller);
	myInstance.setVisible(true);
	return myInstance;
    }
    
    private TivooFilterConfigurator(TivooModel model, TivooGenerator view, 
	    TivooController controller) {
	myView = view;
	myModel = model;
	myController = controller;
	setTitle("Keyword Filter");
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	// getContentPane().add(new JPanel());
	setLayout(new BorderLayout());
	JPanel inputpanel = new JPanel(new BorderLayout());
	inputpanel.setLayout(new GridLayout(3, 2));
	JLabel label = new JLabel("<html>Keywords<br>(separate by \",\")</html>");
	myKeywordsArea = new JTextArea(1, 1);
	myKeywordsArea.setBorder(BorderFactory.createEtchedBorder());
	JScrollPane inputscrollpane = new JScrollPane(myKeywordsArea);
	inputpanel.add(label);
	inputpanel.add(inputscrollpane);
	JCheckBox specificBox = new JCheckBox("Event-specific filtering:");
	inputpanel.add(specificBox);
	buildTree();
	myEventTree.setBorder(BorderFactory.createEtchedBorder());
	JScrollPane treescrollpane = new JScrollPane(myEventTree);
	inputpanel.add(treescrollpane);
	JButton confirm = new JButton("Confirm");
	confirm.addActionListener(new ConfirmAction());
	JButton cancel = new JButton("Cancel");
	cancel.addActionListener(new DisposeAction());
	inputpanel.add(confirm);
	inputpanel.add(cancel);
	
	add(inputpanel, BorderLayout.WEST);

	// must be first since other panels may refer to page
	//add(makePageDisplay(), BorderLayout.CENTER);
	// control the navigation
	//enableButtons();
	setLocationRelativeTo(null);
	setSize(400, 200);
	//keywordframe.pack();
	setVisible(true);
	setLocationRelativeTo(myView);
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void checkFilter() {
	String[] splitted = myKeywordsArea.getText().split(",");
	Set<String> splittedset = new HashSet<String>();
	for (String s: splitted)
	    splittedset.add(s);
	myController.doFilterByKeywordsAttributes(splittedset, true);
	//if (mySpecificBox.get)
    }
    
    public void buildTree() {
	DefaultMutableTreeNode root = new DefaultMutableTreeNode("Event types");
	DefaultTreeModel model = new DefaultTreeModel(root);
	TivooEventType[] seentypes = myModel.getSeenTypesArray();
	int count = 0;
	for (int i = 0; i < seentypes.length; i++) {
	    TivooEventType type = seentypes[i];
	    DefaultMutableTreeNode subroot = new DefaultMutableTreeNode(type);
	    if (type.getSpecialAttributes().isEmpty()) continue;
	    for (TivooAttribute attr: type.getSpecialAttributes())
		subroot.add(new DefaultMutableTreeNode(attr));
	    model.insertNodeInto(subroot, root, count);
	    count++;
	}
	myEventTree = new JTree(model);
    }
    
    private class DisposeAction implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    dispose();
	}
    }
    
    private class ConfirmAction implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    checkFilter();
	    dispose();
	}
    }
    
}