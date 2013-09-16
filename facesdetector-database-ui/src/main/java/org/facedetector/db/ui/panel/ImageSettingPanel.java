package org.facedetector.db.ui.panel;

import static org.facedetector.db.ui.Constants.Gender;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.LabelAlignment;

import org.facedetector.db.ui.FaceDetectorFrame;
import org.facedetector.db.ui.utils.JXErrorDialog;

import com.jgoodies.forms.factories.Borders;

public class ImageSettingPanel extends JPanel implements TreeSelectionListener {

	private static final long serialVersionUID = 9048842349106805945L;

	private final ResourceBundle resourceBundle;
	
	private final JLabel imgName;
	
	private final JLabel imgLabel;
	
	private final FaceDetectorFrame detectorFrame;
	
	public ImageSettingPanel(FaceDetectorFrame detectorFrame) {
		super();
		resourceBundle = FaceDetectorFrame.getResourceBundle();
		this.detectorFrame = detectorFrame;
		DesignGridLayout layout = new DesignGridLayout(this);
		layout.labelAlignment(LabelAlignment.RIGHT);

		imgLabel = new JLabel();
		imgLabel.setSize(150, 850);
		imgLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		imgLabel.setPreferredSize(new Dimension(200, 450));
		
		imgName = new JLabel();
		
		layout.row()
			.grid(new JLabel(resourceBundle.getString("file.name.label")))
			.add(imgName)
			.add(imgLabel);
		layout.row()
			.grid(new JLabel(resourceBundle.getString("age.name.label")))
			.add(new JList<String>(new String[]{"A", "B", "C"}))
			.spanRow();
		layout.row()
			.grid(new JLabel(resourceBundle.getString("gender.name.label")))
			.add(new JList<Gender>(new AbstractListModel<Gender>() {

				private static final long serialVersionUID = 1L;

				@Override
				public int getSize() {
					return Gender.ALL_GENDERS.length;
				}

				@Override
				public Gender getElementAt(int index) {
					return Gender.ALL_GENDERS[index];
				}
				
			 }))
			.spanRow();
		layout.emptyRow();
		layout.emptyRow();
		layout.row().center().fill().add(new JSeparator());
		layout.row()
			.right()
			.add(new JButton(new AbstractAction(resourceBundle.getString("previous.label")) {
					private static final long serialVersionUID = 1L;
	
					@Override
					public void actionPerformed(ActionEvent e) {
						final JTree folderTree = ImageSettingPanel.this.detectorFrame.getFileListTree();
						TreeModel treeModel = folderTree.getModel();
						TreePath treePath = folderTree.getSelectionPath();
						
						folderTree.setSelectionPath(treePath.getParentPath());
					}
					
			}))
			.add(new JButton(new AbstractAction(resourceBundle.getString("next.label")) {

				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
				}
				
			}));
		setBorder(Borders.DLU21_BORDER);
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		TreePath treePath = e.getPath();
		if (treePath != null && treePath.getLastPathComponent() instanceof File) {
			File file = (File) treePath.getLastPathComponent();
			if (!file.isDirectory()) {
				imgName.setText(file.getName());
				try {
					BufferedImage image = ImageIO.read(file);
					imgLabel.setIcon(new ImageIcon(image));
				} catch (Exception ex) {
					JXErrorDialog.showDialog(detectorFrame, "Oops", ex);
				}
			}
		}
	}

}
