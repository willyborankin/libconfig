package org.facedetector.db.ui.panel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.LabelAlignment;

import org.facedetector.db.ui.Constants.Gender;
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
		imgName = new JLabel();
		
		layout.row()
			.grid(new JLabel(resourceBundle.getString("file.name.label")))
			.add(imgName)
			.add(imgLabel);
		layout.row()
			.grid(new JLabel(resourceBundle.getString("age.name.label")))
			.add(new JList<String>(new String[]{
				resourceBundle.getString("age.1217.label"),
				resourceBundle.getString("age.1824.label"),
				resourceBundle.getString("age.2534.label"),
				resourceBundle.getString("age.3534.label"),
				resourceBundle.getString("age.4564.label"),
				resourceBundle.getString("age.5565.label"),
				resourceBundle.getString("age.65.label")
			}))
			.spanRow();
		layout.row()
			.grid(new JLabel(resourceBundle.getString("gender.name.label")))
			.add(new JList<String>(new AbstractListModel<String>() {

				private static final long serialVersionUID = 1L;

				@Override
				public int getSize() {
					return Gender.ALL_GENDERS.length;
				}

				@Override
				public String getElementAt(int index) {
					String value = null;
					switch (Gender.ALL_GENDERS[index]) {
					case FEMALE:
						value = resourceBundle.getString("gender.female.label");
						break;
					case MALE:
						value = resourceBundle.getString("gender.male.label");
						break;
					}
					return value;
				}
				
			 }))
			.spanRow();
		layout.emptyRow();
		layout.emptyRow();
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
