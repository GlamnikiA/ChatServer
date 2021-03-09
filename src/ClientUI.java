
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class ClientUI extends JPanel{
    private Client client;
    private final int width = 700;
    private final int height = 700;
    private JPanel pnlNorth;
    private JPanel pnlNorthW;
    private JPanel pnlNorthE;
    private JPanel pnlCenter;
    private JPanel pnlCenterW;
    private JPanel pnlCenterE;
    private JPanel pnlCenterN;
    private JPanel pnlSouth;
    private JPanel pnlSouthW;
    private JPanel pnlSouthC;
    private JPanel pnlSouthE;
    private JTextPane tpClientList;
    private JScrollPane chatPane;
    private JScrollPane clientPane;
    private JTextArea taChatbox;
    private JButton btnMessageImage;
    private JButton btnSend;
    private JButton btnContacts;
    private JTextField tfMessage;
    private JLabel lblMessage;
    private JTextField tfPort;
    private JTextField tfHost;
    private JTextField tfName;
    private JButton btnChoosePic;
    private JButton btnConnect;
    private JButton btnDisconnect;
    private JLabel lblHost;
    private JLabel lblPort;
    private JLabel lblName;
    private JLabel lblPicture;
    private JLabel lblSendTo;
    private JSeparator sep;
    private ImageIcon profilePicture;
    private String pictureFile;
    private ImageIcon messageImage;
    private ArrayList<JLabel> lblUsers = new ArrayList<>();
    private StringBuilder sendTo = new StringBuilder();
    int count = 0;

    public ClientUI(Client client) {
        this.client = client;
        createPanels();
        setLayout();
        initializeComponents();
        add(pnlNorth, BorderLayout.NORTH);
        pnlNorthE.add(clientPane);
        pnlNorthW.add(chatPane);
        pnlNorth.add(pnlNorthE, BorderLayout.EAST);
        pnlNorth.add(pnlNorthW, BorderLayout.WEST);
        add(pnlCenter, BorderLayout.CENTER);
        pnlCenterN.add(lblSendTo, BorderLayout.CENTER);
        pnlCenter.add(pnlCenterN, BorderLayout.NORTH);
        pnlCenterW.add(lblMessage, BorderLayout.CENTER);
        pnlCenterW.add(tfMessage, BorderLayout.EAST);
        pnlCenter.add(pnlCenterW, BorderLayout.CENTER);
        pnlCenterE.add(btnMessageImage);
        pnlCenterE.add(btnSend);
        pnlCenterE.add(btnContacts);
        pnlCenter.add(pnlCenterE, BorderLayout.EAST);
        pnlCenter.add(sep, BorderLayout.SOUTH);

        pnlSouthW.add(lblHost);
        pnlSouthW.add(lblPort);
        pnlSouthW.add(lblName);
        pnlSouthW.add(lblPicture);
        pnlSouth.add(pnlSouthW, BorderLayout.WEST);
        add(pnlSouth, BorderLayout.SOUTH);

        pnlSouthC.add(tfHost);
        pnlSouthC.add(tfPort);
        pnlSouthC.add(tfName);
        pnlSouthC.add(btnChoosePic);
        pnlSouth.add(pnlSouthC, BorderLayout.CENTER);

        pnlSouthE.add(btnConnect);
        pnlSouthE.add(btnDisconnect);
        pnlSouth.add(pnlSouthE, BorderLayout.EAST);

        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Messenger");
        frame.setContentPane(this);
        frame.pack();
        frame.setVisible(true);

        addListeners();
    }
    private void createPanels() {
        pnlNorth = new JPanel();
        pnlNorthE = new JPanel();
        pnlNorthW = new JPanel();
        pnlCenter = new JPanel();
        pnlCenterN = new JPanel();
        pnlCenterW = new JPanel();
        pnlCenterE = new JPanel();
        pnlSouth = new JPanel();
        pnlSouthC = new JPanel();
        pnlSouthE = new JPanel();
        pnlSouthW = new JPanel();
    }
    private void setLayout() {
        setLayout(new BorderLayout());
        pnlNorth.setLayout(new BorderLayout());
        pnlCenter.setLayout(new BorderLayout());
        pnlCenterW.setLayout(new BorderLayout());
        pnlCenterN.setLayout(new BorderLayout());
        pnlCenterE.setLayout(new GridLayout(1,3,1,1));
        pnlSouth.setLayout(new BorderLayout());
        pnlSouthW.setLayout(new GridLayout(4, 1, 1, 1));
        pnlSouthC.setLayout(new GridLayout(4,1,1,1));
        pnlSouthE.setLayout(new GridLayout(1,2,1,1));
    }
    private void initializeComponents() {
        tpClientList = new JTextPane();
        tpClientList.setEditable(false);
        tpClientList.setBorder(new TitledBorder("Anslutna klienter"));
        Font font = new Font("Arial", Font.PLAIN, 14);
        tpClientList.setFont(font);
        tpClientList.setLayout(new BoxLayout(tpClientList, BoxLayout.PAGE_AXIS));
        clientPane = new JScrollPane(tpClientList);
        clientPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        clientPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        clientPane.setPreferredSize(new Dimension(160, 400));

        taChatbox = new JTextArea();
        taChatbox.setEditable(false);
        chatPane = new JScrollPane(taChatbox);
        chatPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        chatPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        chatPane.setPreferredSize(new Dimension(460, 400));
        taChatbox.setFont(font);

        lblSendTo = new JLabel("Send To: ");
        lblSendTo.setPreferredSize(new Dimension(80, 30));

        lblMessage = new JLabel("Message: ");
        lblMessage.setPreferredSize(new Dimension(85, 30));
        tfMessage = new JTextField();
        tfMessage.setPreferredSize(new Dimension(300, 30));
        tfMessage.setOpaque(true);
        btnMessageImage = new JButton("...");
        btnMessageImage.setPreferredSize(new Dimension(100, 30));
        btnSend = new JButton("Send");
        btnSend.setPreferredSize(new Dimension(100, 30));
        btnContacts = new JButton("Contacts");
        btnContacts.setPreferredSize(new Dimension(100,30));
        sep = new JSeparator();
        sep.setSize(width, 0);

        lblHost = new JLabel("Host: ");
        lblHost.setPreferredSize(new Dimension(150, 30));
        lblPort = new JLabel("Port: ");
        lblPort.setPreferredSize(new Dimension(150,30));
        lblName = new JLabel("Name: ");
        lblName.setPreferredSize(new Dimension(150,30));
        lblPicture = new JLabel("Profile Picture: ");
        lblPicture.setPreferredSize(new Dimension(150,30));


        tfHost = new JTextField();
        tfPort = new JTextField();
        tfName = new JTextField();
        btnChoosePic = new JButton("...");

        btnConnect = new JButton("Connect");
        btnConnect.setPreferredSize(new Dimension(100, 170));
        btnDisconnect = new JButton("Disconnect");
        btnDisconnect.setPreferredSize(new Dimension(100, 170));
    }
    private void addListeners() {
        ActionListener listener = new ButtonActionListeners();
        btnConnect.addActionListener(listener);
        btnDisconnect.addActionListener(listener);
        btnSend.addActionListener(listener);
        btnChoosePic.addActionListener(listener);
        btnMessageImage.addActionListener(listener);
        btnContacts.addActionListener(listener);
    }

    public void displayUser(JLabel user) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                lblUsers.add(user);
                tpClientList.add(user);
                revalidate();
            }
        });
    }
    public void resetGUI() {
        for(JLabel lbl : lblUsers) {
            tpClientList.remove(lbl);
        }
        repaint();
    }
    public void removeUser(JLabel user) {
        for(JLabel lbl : lblUsers) {
            if(lbl.getText().equals(user.getText())) {
                tpClientList.remove(lbl);
                lblUsers.remove(user);
                repaint();
            }
        }
    }
    public JLabel getLblSendTo() {
        return lblSendTo;
    }

    public JTextField getTfHost() {
        return tfHost;
    }

    public JTextField getTfPort() {
        return tfPort;
    }

    public JTextField getTfName() {
        return tfName;
    }

    public ImageIcon getProfilePicture() {
        return profilePicture;
    }

    public JTextField getTfMessage() {
        return tfMessage;
    }

    public ImageIcon getMessageImage() {
        return messageImage;
    }

    public JTextArea getTaChatbox() {
        return taChatbox;
    }
    public void setTextForLabel(String text) {
        if(count == 0) {
            sendTo.append(text);
        } else {
            sendTo.append(", " + text);
        }
        lblSendTo.setText("Skicka till: " + sendTo);
        count++;
    }
    public void resetLabel() {
        sendTo.setLength(0);
        count = 0;
    }
    public void displayImage(String sender, String text, ImageIcon image) {
        Image scaled = GUIUtilities.scaleImage(image.getImage(), 400,400);
        ImageIcon scaledImage = new ImageIcon(scaled);
        GUIUtilities.displayImage(sender, text, scaledImage);
        revalidate();
    }
    private class ButtonActionListeners implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnSend) {
                client.buttonPressed(ButtonType.Send);
                tfMessage.setText("");
                messageImage = null;
            } else if(e.getSource() == btnConnect) {
                if(tfName.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Ange ett användarnamn");
                } else if(tfPort.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Ange port");
                } else if(tfHost.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Ange host");
                } else {
                    if(pictureFile == null) {
                        pictureFile = "files//avatardefault_92824.png";
                        profilePicture = GUIUtilities.scaleImage(pictureFile, 50, 50);
                    }
                    client.buttonPressed(ButtonType.Connect);
                    btnConnect.setEnabled(false);
                    tfHost.setEnabled(false);
                    tfName.setEnabled(false);
                    tfPort.setEnabled(false);
                    btnChoosePic.setEnabled(false);
                    btnDisconnect.setEnabled(true);
                }
            } else if(e.getSource() == btnDisconnect) {
                client.buttonPressed(ButtonType.Disconnect);
                btnChoosePic.setEnabled(true);
                btnConnect.setEnabled(true);
                btnDisconnect.setEnabled(false);
                tfHost.setEnabled(true);
                tfName.setEnabled(true);
                tfPort.setEnabled(true);
                repaint();
            }
            else if(e.getSource() == btnChoosePic) {
                JFileChooser jfc = new JFileChooser();
                int response = jfc.showOpenDialog(null);
                if(response == JFileChooser.APPROVE_OPTION) {
                    pictureFile = jfc.getSelectedFile().toString();
                    if(GUIUtilities.isImage(jfc.getSelectedFile().toString()) == false) {
                        JOptionPane.showMessageDialog(null, "Fel filformat");
                    } else {
                        profilePicture = GUIUtilities.scaleImage(pictureFile, 50, 50);
                    }
                }
            } else if(e.getSource() == btnMessageImage) {
                JFileChooser jfc = new JFileChooser();
                int response = jfc.showOpenDialog(null);
                if(response == JFileChooser.APPROVE_OPTION) {
                    String pictureFile = jfc.getSelectedFile().toString();
                    if(GUIUtilities.isImage(jfc.getSelectedFile().toString()) == false) {
                        JOptionPane.showMessageDialog(null, "Fel filformat");
                    } else {
                        messageImage = new ImageIcon(pictureFile);
                    }
                }
            } else if(e.getSource() == btnContacts) {
                client.buttonPressed(ButtonType.Contacts);
            }
        }
    }
}
