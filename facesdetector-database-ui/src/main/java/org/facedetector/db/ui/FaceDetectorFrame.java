package org.facedetector.db.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import org.facedetector.db.ui.gradient.GradientPanelHolder;
import org.facedetector.db.ui.model.FileListTreeModel;

import com.jgoodies.forms.factories.Borders;
import com.jidesoft.dialog.JideOptionPane;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBorderLayout;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideSplitPane;

public class FaceDetectorFrame extends JFrame {

	private static final long serialVersionUID = 7312854162419015493L;

	private static ResourceBundle resourceBundle;
	
	public FaceDetectorFrame() {
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		setWindowSize();
		
		setContentPane(createContentPane());

	}

	private void setWindowSize() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		setMaximizedBounds(ge.getMaximumWindowBounds());
		Dimension defaultSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(defaultSize.width / 2, defaultSize.height / 2);
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
	}
	
	private JPanel createContentPane() {
		JPanel contanetPane = new JPanel(new BorderLayout());
		
		contanetPane.add(createSplitPane(), JideBorderLayout.CENTER);
		
		return contanetPane;
	}	
	
	private JideSplitPane createSplitPane() {
		JideSplitPane splitPane = new JideSplitPane();
		
		GradientPanelHolder imgsListPanelHolder = new GradientPanelHolder(resourceBundle.getString("left.panel.imgs.label"));
		imgsListPanelHolder.add(createFilesListTree());
		GradientPanelHolder imgsDescPanelHolder = new GradientPanelHolder(resourceBundle.getString("right.panel.img.desc.label"));
		imgsListPanelHolder.add(createImageSettingPanel());
		
		splitPane.setOneTouchExpandable(true);
		splitPane.add(imgsListPanelHolder, JSplitPane.LEFT);	
		splitPane.add(imgsDescPanelHolder, JSplitPane.RIGHT);	
		splitPane.setBorder(BorderFactory.createEmptyBorder());
		splitPane.setBorder(Borders.DLU2_BORDER);
		
		return splitPane;
	}

	private JScrollPane createFilesListTree() {
		
		JideScrollPane listScrollPane = new JideScrollPane(createJTree());
		listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		listScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listScrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		return listScrollPane;
	}
	
	private JTree createJTree() {
		MutableTreeNode rootNode = new DefaultMutableTreeNode();
		JTree imgsListTree = new JTree(new FileListTreeModel(rootNode));
		imgsListTree.setRootVisible(false);
		return imgsListTree;
	}
	
	private JPanel createImageSettingPanel() {
		JPanel imgSettingPanel = new JPanel();

		imgSettingPanel.setBorder(BorderFactory.createEmptyBorder());
		imgSettingPanel.setBorder(Borders.DLU2_BORDER);
		
		return imgSettingPanel;
	}
	
	public static void main(String[] args) {
		
		resourceBundle = ResourceBundle.getBundle("messages", Locale.getDefault());

		if (resourceBundle == null) throw new IllegalArgumentException("Ooops ... "); 

		setLookAndFeel();
		overrideDefaultProperties();
		
		final FaceDetectorFrame mainFrame = new FaceDetectorFrame();
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int confirmed = JideOptionPane.showConfirmDialog(
						mainFrame,
						resourceBundle.getString("exit.dialog.message"),
						resourceBundle.getString("exit.dialog.title"),
						JideOptionPane.YES_NO_OPTION);
				if (confirmed == JideOptionPane.YES_OPTION) {
					//TODO save data here
				}
				mainFrame.setVisible(false);
				mainFrame.dispose();
			}
		});
		
		mainFrame.setVisible(true);
		
		JFileChooser imgsDirectoryChooser = new JFileChooser();
		imgsDirectoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		if (imgsDirectoryChooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
			//TODO something
		} else {
			JideOptionPane.showConfirmDialog(
					mainFrame,
					resourceBundle.getString("exit.dialog.message"),
					resourceBundle.getString("exit.dialog.title"),
					JideOptionPane.YES_NO_OPTION);
		}
		
	}

	private static void setLookAndFeel() {
		if (System.getProperty("os.name").contains("Linux")) {
			try {
				UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
			} catch (ClassNotFoundException
					| InstantiationException
					| IllegalAccessException
					| UnsupportedLookAndFeelException e) {
			}
		} else {
			LookAndFeelFactory.setDefaultStyle(LookAndFeelFactory.ECLIPSE_STYLE);
			LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
		}
	}
	
	private static void overrideDefaultProperties() {
		UIManager.put("OptionPane.yesButtonText", resourceBundle.getString("yes.label"));
		UIManager.put("OptionPane.noButtonText", resourceBundle.getString("no.label"));
		UIManager.put("FileChooser.openDialogTitleText", resourceBundle.getString("dir.imgs.file.message"));
		UIManager.put("FileChooser.openButtonText", resourceBundle.getString("open.label"));
        UIManager.put("FileChooser.cancelButtonText", resourceBundle.getString("cancel.label"));	
	}
}
