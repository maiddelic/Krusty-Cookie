package dbtLab3;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

/**
 * The GUI pane where a user books tickets for movie performances. It contains
 * one list of movies and one of performance dates. The user selects a
 * performance by clicking in these lists. The performance data is shown in the
 * fields in the right panel. The bottom panel contains a button which the user
 * can click to book a ticket to the selected performance.
 */
public class BlockedPane extends BasicPane {
	private static final long serialVersionUID = 1;
	/**
	 * A label showing the name of the current user.
	 */
	private JLabel currentUserNameLabel;

	/**
	 * The list model for the movie name list.
	 */
	private DefaultListModel nameListModel;

	/**
	 * The movie name list.
	 */
	private JList nameList;

	/**
	 * The list model for the performance date list.
	 */
	private DefaultListModel dateListModel;

	/**
	 * The performance date list.
	 */
	private JList dateList;

	/**
	 * The text fields where the movie data is shown.
	 */
	private JTextField[] fields;

	/**
	 * The number of the movie name field.
	 */
	private static final int PALLET_ID = 0;

	/**
	 * The number of the performance date field.
	 */
	private static final int COOKIE_NAME = 1;

	/**
	 * The number of the movie theater field.
	 */
	private static final int DATE_PRODUCED = 2;
	
	private static final int IS_BLOCKED = 3;

	/**
	 * The number of the 'number of free seats' field.
	 */
	private static final int ORDER_NBR = 4;

	/**
	 * The total number of fields.
	 */
	private static final int NBR_FIELDS = 5;


	private ArrayList<String> palletIds;
	/**
	 * Create the booking pane.
	 * 
	 * @param db
	 *            The database object.
	 */
	
	public BlockedPane(Database db) {
		super(db);
	}

	/**
	 * Create the left panel, containing the movie name list and the performance
	 * date list.
	 * 
	 * @return The left panel.
	 */
	public JComponent createLeftPanel() {
		nameListModel = new DefaultListModel();

		nameList = new JList(nameListModel);
		nameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		nameList.setPrototypeCellValue("123456789012");
		nameList.addListSelectionListener(new NameSelectionListener());
		JScrollPane p1 = new JScrollPane(nameList);


		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 1));
		p.add(p1);
		return p;
	}

	/**
	 * Create the top panel, containing the fields with the performance data.
	 * 
	 * @return The top panel.
	 */
	public JComponent createTopPanel() {
		String[] texts = new String[NBR_FIELDS];
		texts[PALLET_ID] = "Pallet ID";
		texts[COOKIE_NAME] = "Cookie Name";
		texts[DATE_PRODUCED] = "Date Produced";
		texts[IS_BLOCKED] = "Is Blocked?";
		texts[ORDER_NBR] = "Order Nbr";

		fields = new JTextField[NBR_FIELDS];
		for (int i = 0; i < fields.length; i++) {
			fields[i] = new JTextField(20);
			fields[i].setEditable(false);
		}

		JPanel input = new InputPanel(texts, fields);

		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.LEFT));
		p1.add(new JLabel("Current user: "));
		currentUserNameLabel = new JLabel("");
		p1.add(currentUserNameLabel);

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(p1);
		p.add(input);
		return p;
	}



	/**
	 * Perform the entry actions of this pane: clear all fields, fetch the movie
	 * names from the database and display them in the name list.
	 */
	public void entryActions() {
		clearMessage();
		currentUserNameLabel.setText(CurrentUser.instance().getCurrentUserId());
		fillNameList();
		clearFields();
	}

	/**
	 * Fetch movie names from the database and display them in the name list.
	 */
	private void fillNameList() {
		nameListModel.removeAllElements();
		palletIds = db.getBlockedPallets();
		for(int i = 0; i < palletIds.size(); i++){
			String current = palletIds.get(i);
			nameListModel.addElement(current);
		}
        
	}


	/**
	 * Clear all text fields.
	 */
	private void clearFields() {
		for (int i = 0; i < fields.length; i++) {
			fields[i].setText("");
		}
	}

	/**
	 * A class that listens for clicks in the name list.
	 */
	class NameSelectionListener implements ListSelectionListener {
		/**
		 * Called when the user selects a name in the name list. Fetches
		 * performance dates from the database and displays them in the date
		 * list.
		 * 
		 * @param e
		 *            The selected list item.
		 */
		public void valueChanged(ListSelectionEvent e) {
			if (nameList.isSelectionEmpty()) {
				return;
			}
			String palletId = (String) nameList.getSelectedValue();
			Pallet pallet = db.getPalletInfo(palletId);
			fields[0].setText(pallet.getPalletId());
			fields[1].setText(pallet.getCookieName());
			fields[2].setText(pallet.getDate());
			fields[3].setText(String.valueOf(pallet.blocked()));
			fields[4].setText(pallet.getOrderNbr());
			/* --- insert own code here --- */
		}
	}

	/**
	 * A class that listens for clicks in the date list.
	 */


	/**
	 * A class that listens for button clicks.
	 */
	class ActionHandler implements ActionListener {
		/**
		 * Called when the user clicks the Book ticket button. Books a ticket
		 * for the current user to the selected performance (adds a booking to
		 * the database).
		 * 
		 * @param e
		 *            The event object (not used).
		 */
		public void actionPerformed(ActionEvent e) {
			if (nameList.isSelectionEmpty() || dateList.isSelectionEmpty()) {
				return;
			}
			if (!CurrentUser.instance().isLoggedIn()) {
				displayMessage("Must login first");
				return;
			}
			String movieName = (String) nameList.getSelectedValue();
			String date = (String) dateList.getSelectedValue();
			/* --- insert own code here --- */
		}
	}
}
