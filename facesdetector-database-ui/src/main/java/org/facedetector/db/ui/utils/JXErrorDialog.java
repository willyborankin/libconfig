package org.facedetector.db.ui.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import org.facedetector.db.ui.FaceDetectorFrame;

public class JXErrorDialog extends JDialog {

	private static final long serialVersionUID = -4549269016595574348L;

    private static final Icon icon = UIManager.getIcon("OptionPane.warningIcon");

    private JLabel errorMessage;

    private JTextArea details;

    private JButton detailButton;

    private JScrollPane detailsScrollPane;

    private JButton reportButton;

    private static ErrorReporter reporter;

    private IncidentInfo incidentInfo;

    private ResourceBundle resourceMap;
    
    public JXErrorDialog(Frame owner) {
        super(owner, true);
        initGui();
    }


    private JXErrorDialog(Dialog owner) {
        super(owner, true);
        initGui();
    }

    private void initGui() {

    	resourceMap = FaceDetectorFrame.getResourceBundle();
    	
        //initialize the gui
        GridBagLayout layout = new GridBagLayout();
        this.getContentPane().setLayout(layout);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridheight = 1;
        gbc.insets = new Insets(22, 12, 11, 17);
        this.getContentPane().add(new JLabel(icon), gbc);

        errorMessage = new JLabel();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(12, 0, 0, 11);
        this.getContentPane().add(errorMessage, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(12, 0, 11, 5);
        JButton okButton = new JButton(resourceMap.getString("ok.error.button.label"));
        this.getContentPane().add(okButton, gbc);

        reportButton = new JButton(new ReportAction());
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(12, 0, 11, 5);
        this.getContentPane().add(reportButton, gbc);
        reportButton.setVisible(false); // not visible by default

        detailButton = new JButton(resourceMap.getString("details.error.expand.button.label"));
        gbc.gridx = 3;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(12, 0, 11, 11);
        this.getContentPane().add(detailButton, gbc);

        details = new JTextArea(7, 60);
        detailsScrollPane = new JScrollPane(details);
        detailsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        details.setEditable(false);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 4;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(6, 11, 11, 11);
        this.getContentPane().add(detailsScrollPane, gbc);

        /*
         * Here i'm going to add invisible empty container to the bottom of the
         * content pane to fix minimal width of the dialog. It's quite a hack,
         * but i have not found anything better.
         */
        Dimension spPredictedSize = detailsScrollPane.getPreferredSize();
        Dimension newPanelSize = new Dimension(spPredictedSize.width + 15, 0);
        Container widthHolder = new Container();
        widthHolder.setMinimumSize(newPanelSize);
        widthHolder.setPreferredSize(newPanelSize);
        widthHolder.setMaximumSize(newPanelSize);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 11, 11, 0);
        this.getContentPane().add(widthHolder, gbc);

        //make the buttons the same size
        int buttonLength = detailButton.getPreferredSize().width;
        int buttonHeight = detailButton.getPreferredSize().height;
        Dimension buttonSize = new Dimension(buttonLength, buttonHeight);
        okButton.setPreferredSize(buttonSize);
        reportButton.setPreferredSize(buttonSize);
        detailButton.setPreferredSize(buttonSize);
        //set the event handling
        okButton.addActionListener(new OkClickEvent());
        detailButton.addActionListener(new DetailsClickEvent());
    }

    /**
     * Set the details section of the error dialog.  If the details are either
     * null or an empty string, then hide the details button and hide the detail
     * scroll pane.  Otherwise, just set the details section.
     * @param details  Details to be shown in the detail section of the dialog.  This can be null
     * if you do not want to display the details section of the dialog.
     */
    private void setDetails(String details) {
        if (details == null || details.equals("")) {
            setDetailsVisible(false);
            detailButton.setVisible(false);
        } else {
            this.details.setText(details);
            setDetailsVisible(false);
            detailButton.setVisible(true);
        }
    }

    /**
     * Set the details section to be either visible or invisible.  Set the
     * text of the Details button accordingly.
     * @param b if true details section will be visible
     */
    private void setDetailsVisible(boolean b) {
        if (b) {
            details.setCaretPosition(0);
            detailsScrollPane.setVisible(true);
            detailButton.setText(resourceMap.getString("details.error.collapse.button.label"));
        } else {
            detailsScrollPane.setVisible(false);
            detailButton.setText(resourceMap.getString("details.error.expand.button.label"));
        }

        pack();
    }

    /**
     * Set the error message for the dialog box
     * @param errorMessage Message for the error dialog
     */
    private void setErrorMessage(String errorMessage) {
        this.errorMessage.setText(errorMessage);
    }

    /**
     * Sets the IncidentInfo for this dialog
     *
     * @param info IncidentInfo that incorporates all the details about the error
     */
    private void setIncidentInfo(IncidentInfo info) {
        this.incidentInfo = info;
        this.reportButton.setVisible(getReporter() != null);
    }

    /**
     * Get curent dialog's IncidentInfo
     *
     * @return <code>IncidentInfo</code> assigned to this dialog
     */
    private IncidentInfo getIncidentInfo() {
        return incidentInfo;
    }

    /**
     * Listener for Ok button click events
     * @author Richard Bair
     */
    private final class OkClickEvent implements ActionListener {

       /* (non-Javadoc)
        * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
        */
        @Override
		public void actionPerformed(ActionEvent e) {
            //close the window
            setVisible(false);
        }
    }

    /**
     * Listener for Details click events.  Alternates whether the details section
     * is visible or not.
     * @author Richard Bair
     */
    private final class DetailsClickEvent implements ActionListener {

       /* (non-Javadoc)
        * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
        */
        @Override
		public void actionPerformed(ActionEvent e) {
            setDetailsVisible(!detailsScrollPane.isVisible());
        }
    }

    /**
     * Constructs and shows the error dialog for the given exception.  The exceptions message will be the
     * errorMessage, and the stacktrace will be the details.
     * @param owner Owner of this error dialog.
     * @param title Title of the error dialog
     * @param e Exception that contains information about the error cause and stack trace
     */
    public static void showDialog(Window owner, String title, Throwable e) {
        IncidentInfo ii = new IncidentInfo(title, null, null, e);
        showDialog(owner, ii);
    }

    /**
     * Show the error dialog.
     * @param owner Owner of this error dialog
     * @param title Title of the error dialog
     * @param errorMessage Message for the error dialog
     * @param details Details to be shown in the detail section of the dialog.  This can be null
     * if you do not want to display the details section of the dialog.
     */
    public static void showDialog(Window owner, String title, String errorMessage, String details) {
        IncidentInfo ii = new IncidentInfo(title, errorMessage, details);
        showDialog(owner, ii);
    }

    /**
     * Show the error dialog.
     * @param owner Owner of this error dialog.
     * @param info <code>IncidentInfo</code> that incorporates all the information about the error
     */
    public static void showDialog(Window owner, IncidentInfo info) {
    	showDialog(owner, null, info);
    }

    public static void showDialog(Component component, String title, Throwable e) {
        IncidentInfo ii = new IncidentInfo(title, null, null, e);
        showDialog(component, ii);
    }

    public static void showDialog(Component component, String title, String errorMessage, Throwable e) {
        IncidentInfo ii = new IncidentInfo(title, errorMessage, null, e);
        showDialog(component, ii);
    }

    public static void showDialog(Component component, String title, String errorMessage, String details) {
        IncidentInfo ii = new IncidentInfo(title, errorMessage, details);
        showDialog(component, ii);
    }

    public static void showDialog(Component component, IncidentInfo info) {
    	showDialog(null, component, info);
    }

    private static void showDialog(Window owner, Component component, IncidentInfo info) {
        JXErrorDialog dlg;

        if (owner instanceof Dialog) {
            dlg = new JXErrorDialog((Dialog)owner);
        } else {
            dlg = new JXErrorDialog((Frame)owner);
        }
        dlg.setTitle(info.getHeader());
        dlg.setModal(true);
        dlg.setErrorMessage(info.getBasicErrorMessage());
        String details = info.getDetailedErrorMessage();
        if(details == null) {
			if (info.getErrorException() != null) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				info.getErrorException().printStackTrace(pw);
				details = sw.toString();
			} else {
				details = "";
			}
        }
        dlg.setDetails(details);
        dlg.setIncidentInfo(info);
        dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dlg.pack();
        if (owner != null) {
        	dlg.setLocationRelativeTo(owner);
        }
        if (component != null) {
        	dlg.setLocationRelativeTo(component);
        }
        dlg.setVisible(true);
    }

    /**
     * Returns the current reporting engine that will be used to report a problem if
     * user clicks on 'Report' button or <code>null</code> if no reporting engine set.
     *
     * @return reporting engine
     */
    public static ErrorReporter getReporter() {
        return reporter;
    }

    /**
     * Set reporting engine which will handle error reporting if user clicks 'report' button.
     *
     * @param rep <code>ErrorReporter</code> to be used or <code>null</code> to turn reporting facility off.
     */
    public static void setReporter(ErrorReporter rep) {
        reporter = rep;
    }

    /**
     * Action for report button
     */
    public class ReportAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public boolean isEnabled() {
            return (getReporter() != null);
        }

        @Override
		public void actionPerformed(ActionEvent e) {
            getReporter().reportIncident(getIncidentInfo());
        }

        @Override
		public Object getValue(String key) {
			if (key == Action.NAME) {
				if (getReporter() != null && getReporter().getActionName() != null) {
					return getReporter().getActionName();
				} else {
					return "";
				}
			}
			return super.getValue(key);
		}
    }
}
