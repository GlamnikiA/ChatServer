import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContactsGUI extends JPanel{
    private JList<String> onlineUserList;
    private JList<String> savedContactsList;
    private JButton btnAdd;
    private JButton btnSend;
    private JButton btnDel;
    private int width = 500;
    private int height = 500;
    private JPanel pnlNorth;
    private JPanel pnlCenter;
    private JPanel pnlSouth;
    private Client client;
    private JScrollPane pane;
    private JFrame frame;

    public ContactsGUI(Client client) {
        initializeMainPanel();
        this.client = client;
    }
    public void createFrame() {
        JFrame frame = new JFrame();
        frame.setSize(new Dimension(width, height));
        frame.setTitle("gu.Contacts");
        frame.setContentPane(this);
        frame.pack();
        frame.setVisible(true);
    }
    public void initializeMainPanel() {
        setLayout(new BorderLayout());
        initializePanels();
        setLayouts();
        int heightFactor = (int) (height * 0.1);
        createNorthPanel(width, 4*heightFactor);
        createCenterPanel(width, 4*heightFactor);
        createSouthPanel(width, 1*heightFactor);
        addButtonListeners();
    }

    private void initializePanels() {
        pnlNorth = new JPanel();
        pnlCenter = new JPanel();
        pnlSouth = new JPanel();
    }
    private void setLayouts() {
        pnlNorth.setLayout(new BorderLayout());
        pnlCenter.setLayout(new BorderLayout());
        pnlSouth.setLayout(new GridLayout(1,3,1,1));
    }

    private void createNorthPanel(int width, int height) {
        pnlNorth.setPreferredSize(new Dimension(width, height));
        pnlNorth.setBorder(new TitledBorder("Online users"));
        onlineUserList = new JList<>();
        onlineUserList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        pane = new JScrollPane(onlineUserList);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pane.setPreferredSize(new Dimension(width, height));
        pnlNorth.add(pane);
        add(pnlNorth, BorderLayout.NORTH);
        addListListener();
    }
    private void createCenterPanel(int width, int height) {
        pnlCenter.setPreferredSize(new Dimension(width, height));
        pnlCenter.setBorder(new TitledBorder("Your contacts"));
        savedContactsList = new JList<>();
        pane = new JScrollPane(savedContactsList);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pane.setPreferredSize(new Dimension(width, height));
        pnlCenter.add(pane);
        add(pnlCenter, BorderLayout.CENTER);
    }
    private void createSouthPanel(int width, int height) {
        pnlSouth.setPreferredSize(new Dimension(width, height));
        btnAdd = new JButton("Add contact");
        btnSend = new JButton("Send to");
        btnDel = new JButton("Delete contact");
        pnlSouth.add(btnAdd);
        pnlSouth.add(btnSend);
        pnlSouth.add(btnDel);
        add(pnlSouth, BorderLayout.SOUTH);
    }
    public void setOnlineUserList(String[] userName) {
        onlineUserList.setListData(userName);
    }
    public void setSavedContactList(String[] username) {
        savedContactsList.setListData(username);
    }
    private void addButtonListeners() {
        ActionListener listener = new addButtonListener();
        btnAdd.addActionListener(listener);
        btnSend.addActionListener(listener);
        btnDel.addActionListener(listener);
    }
    public int getListIndex() {
        return onlineUserList.getSelectedIndex();
    }
    public int getContactListIndex() {
        return savedContactsList.getSelectedIndex();
    }

    private void addListListener() {
        onlineUserList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = onlineUserList.getSelectedIndex();
                if (!e.getValueIsAdjusting() && index>-1) {
                    client.contactListIndicesChanged(index);
                }
            }
        });
    }
    private class addButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnAdd) {
                client.buttonPressed(ButtonType.ContactsAdd);
            } else if(e.getSource() == btnSend) {
                client.buttonPressed(ButtonType.ContactsSend);
            } else if (e.getSource() == btnDel) {
                client.buttonPressed(ButtonType.ContactsDelete);
            }
        }
    }
}
