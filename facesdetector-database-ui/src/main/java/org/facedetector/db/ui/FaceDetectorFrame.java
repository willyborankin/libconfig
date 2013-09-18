package org.facedetector.db.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.TreeSelectionModel;

import org.facedetector.db.ui.gradient.GradientPanelHolder;
import org.facedetector.db.ui.panel.ImageSettingPanel;
import org.jdesktop.swingx.treetable.FileSystemModel;
import org.libconfig.Config;
import org.libconfig.ConfigOutputter;
import org.libconfig.Setting;

import com.jgoodies.forms.factories.Borders;
import com.jidesoft.dialog.JideOptionPane;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBorderLayout;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideMenu;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideSplitPane;

public class FaceDetectorFrame extends JFrame {

	private static final long serialVersionUID = 7312854162419015493L;

	private static ResourceBundle resourceBundle;
	
	private FileSystemModel fileSystemModel;
	
	private JTree fileListTree;
	
	private Config config;
	
	public FaceDetectorFrame() {
		super(resourceBundle.getString("main.window.title"));
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setWindowSize();
		setJMenuBar(createMenuBar());
		setContentPane(createContentPane());
		config = new Config();
		config.addGroup("machineLearningRules");
	}

	public static ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public JTree getFileListTree() {
		return fileListTree;
	}

	public Setting getRootSetting() {
		return config.lookup("machineLearningRules");
	}
	
	private void setWindowSize() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		setMaximizedBounds(ge.getMaximumWindowBounds());
		Dimension defaultSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(defaultSize.width / 2, defaultSize.height / 2);
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
	}
	
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JideMenu jideMenu = new JideMenu(resourceBundle.getString("dir.imgs.menu.label"));
		
		jideMenu.add(new JMenuItem(new AbstractAction(resourceBundle.getString("dir.imgs.add.folder.menu.label")) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser imgsDirectoryChooser = new JFileChooser();
				imgsDirectoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (imgsDirectoryChooser.showOpenDialog(FaceDetectorFrame.this) == JFileChooser.APPROVE_OPTION) {
					fileSystemModel.setRoot(imgsDirectoryChooser.getSelectedFile());
				}
			}
			
		}));
		
		jideMenu.add(new JMenuItem(new AbstractAction(resourceBundle.getString("dir.imgs.descr.save.label")) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
			}
			
		}));
		
		jideMenu.addSeparator();
		jideMenu.add(new JMenuItem(new AbstractAction(resourceBundle.getString("dir.imgs.descr.exit.label")) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				FaceDetectorFrame.this.processEvent(new WindowEvent(FaceDetectorFrame.this, WindowEvent.WINDOW_CLOSING));
			}
			
		}));
		
		menuBar.add(jideMenu);

		return menuBar;
	}
	
	private JPanel createContentPane() {
		JPanel contanetPane = new JPanel(new BorderLayout());
		contanetPane.add(createSplitPane(), JideBorderLayout.CENTER);
		return contanetPane;
	}	
	
	private JideSplitPane createSplitPane() {
		JideSplitPane splitPane = new JideSplitPane(JideSplitPane.HORIZONTAL_SPLIT);
		
		fileListTree = createFolderTree();
		ImageSettingPanel imgSettingPanel = new ImageSettingPanel(this);
		
		fileListTree.addTreeSelectionListener(imgSettingPanel);
		
		JideScrollPane listScrollPane = new JideScrollPane(fileListTree);
		listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		listScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listScrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		GradientPanelHolder imgsListPanelHolder = new GradientPanelHolder(resourceBundle.getString("left.panel.imgs.label"));
		imgsListPanelHolder.add(fileListTree);
		GradientPanelHolder imgsDescPanelHolder = new GradientPanelHolder(resourceBundle.getString("right.panel.img.desc.label"));
		imgsDescPanelHolder.add(imgSettingPanel);
		
		splitPane.setOneTouchExpandable(true);
		splitPane.setProportionalLayout(false);
		splitPane.add(imgsListPanelHolder, JideBoxLayout.FLEXIBLE);	
		splitPane.add(imgsDescPanelHolder, JideBoxLayout.FLEXIBLE);	
		splitPane.setBorder(BorderFactory.createEmptyBorder());
		splitPane.setBorder(Borders.DLU2_BORDER);
		
		return splitPane;
	}

	private JTree createFolderTree() {
		fileSystemModel = new FileSystemModel(null);
		JTree imgsListTree = new JTree(fileSystemModel);
		imgsListTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		return imgsListTree;
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
					ConfigOutputter outputter = new ConfigOutputter();
					try {
						File file = (File) mainFrame.getFileListTree().getModel().getRoot();
						outputter.output(mainFrame.getRootSetting().getConfig(), new File(file, "descriptor.conf"));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				mainFrame.setVisible(false);
				mainFrame.dispose();
			}
		});
		
		mainFrame.setVisible(true);
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
