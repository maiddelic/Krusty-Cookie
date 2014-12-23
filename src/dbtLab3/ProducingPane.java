package dbtLab3;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dbtLab3.ProducingPane.ActionHandler;
import dbtLab3.ProducingPane.DateSelectionListener;
import dbtLab3.ProducingPane.NameSelectionListener;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.ArrayList;

	public class ProducingPane extends BasicPane {
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

		/**
		 * Create the booking pane.
		 * 
		 * @param db
		 *            The database object.
		 */
		private ArrayList<String> cookies;
		private ArrayList<String> dates;
	public ProducingPane(Database db) {
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

		dateListModel = new DefaultListModel();

		dateList = new JList(dateListModel);
		dateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dateList.setPrototypeCellValue("123456789012");
		dateList.addListSelectionListener(new DateSelectionListener());
		JScrollPane p2 = new JScrollPane(dateList);

		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 2));
		p.add(p1);
		p.add(p2);
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
	 * Create the bottom panel, containing the book ticket-button and the
	 * message line.
	 * 
	 * @return The bottom panel.
	 */
	public JComponent createBottomPanel() {
		JButton[] prodButton = new JButton[2];
		prodButton[0] = new JButton("Produce");
		prodButton[1] = new JButton("Block");
		return new ButtonAndMessagePanel(prodButton, messageLabel,
				new ActionHandler());
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
		cookies = db.getCookies();
		for(int i = 0; i < cookies.size(); i++){
			String current = cookies.get(i);
			nameListModel.addElement(current);
		}
        
	}

	/**
	 * Fetch performance dates from the database and display them in the date
	 * list.
	 */
	private void fillDateList() {
		dateListModel.removeAllElements();
		for(int i = 0; i < dates.size(); i++){
			String current = dates.get(i);
			dateListModel.addElement(current);
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
	
	private void updatePallet(){
		String palletId = (String) dateList.getSelectedValue();
		palletId = palletId.replaceAll("\\/.*", "");
		Pallet pallet = db.getPalletInfo(palletId);
		fields[0].setText(pallet.getPalletId());
		fields[1].setText(pallet.getCookieName());
		fields[2].setText(pallet.getDate());
		fields[3].setText(String.valueOf(pallet.blocked()));
		fields[4].setText(pallet.getOrderNbr());
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
			String cookieName = (String) nameList.getSelectedValue();
			dates = db.getProductionDatesIds(cookieName);
			fillDateList();
		}
	}

	/**
	 * A class that listens for clicks in the date list.
	 */
	class DateSelectionListener implements ListSelectionListener {
		/**
		 * Called when the user selects a name in the date list. Fetches
		 * performance data from the database and displays it in the text
		 * fields.
		 * 
		 * @param e
		 *            The selected list item.
		 */
		public void valueChanged(ListSelectionEvent e) {
			if (nameList.isSelectionEmpty() || dateList.isSelectionEmpty()) {
				return;
			}
			updatePallet();
			/* --- insert own code here --- */
		}
	}

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
			String buttonClicked = ((JButton) e.getSource()).getText();
			if(buttonClicked.equals("Produce")){
				if (nameList.isSelectionEmpty()) {
					return;
				}
				String cookieName = (String) nameList.getSelectedValue();
				db.produce(cookieName);
				dates = db.getProductionDatesIds(cookieName);
				fillDateList();
			}else{
				if (nameList.isSelectionEmpty() || dateList.isSelectionEmpty()) {
					return;
				}
				String palletId = (String) dateList.getSelectedValue();
				palletId = palletId.replaceAll("\\/.*", "");
				System.out.println(palletId);
				db.block(palletId);
				updatePallet();
			}
		}
	}
}