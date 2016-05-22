package es.ucm.fdi.tp.practica6.ui;


import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A graphical console for the server based on the example uploaded to Campus Virtual
 */
public class ServerWindow extends JFrame{
    /**
     * SCroll Panel which will display useful info messages
     */
    private JScrollPane serverInfoScrollPanel;
    /**
     * Name for the server as a listener for events
     */
    protected String name;
    private JPanel commandPanel;

    private ServerChangesListener server;

    /**
     * Interface which listens to the changes of the server
     */
    public interface ServerChangesListener{
        /**
         * Notifies the receiver of the quit button pressing.
         */
        void onQuitButtonPressed();

    }

    /**
     * Construct a new ServerWindow from all of its atributes
     * @param name
     * @param server
     */
    public ServerWindow(String name, ServerChangesListener server) {

        this.name = name;
        this.server = server;
        initComponents();

    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Call this whenever you wanna log something
     * @param news
     */

    public void addLineToStatus(String news) {
        ((JTextArea) serverInfoScrollPanel.getViewport().getView())
                .append(news + "\n");
    }

    /**
     * Closes this window (and all others).
     */
    public void closeWindow() {
        System.exit(0);
    }



    /**
     * Creates the border to be used by all subpanels: a line border with the
     * given title.
     *
     * @param title
     *            that will be set for the new border.
     * @return the new border.
     */
    private TitledBorder defaultBorder(String title) {
        return new TitledBorder(new LineBorder(new Color(153, 180, 209)),
                title, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(
                0, 0, 0));
    }

    /**
     * Initializes the components of the window
     */
    private void initComponents() {

        GridBagConstraints gridBagConstraints;

        buildCommandPanel();
        buildServerInfoScrollPanel();
        setLayout(new GridBagLayout());



        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(commandPanel, gridBagConstraints);


        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(serverInfoScrollPanel, gridBagConstraints);
    }

    /**
     * Builds a panel where buttons witch actions will be built
     */
    private void buildCommandPanel() {
        commandPanel = new JPanel();
        this.add(commandPanel);
        commandPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        final JButton btnQuit = new JButton("Quit");

        btnQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (btnQuit.isEnabled()) {
                    server.onQuitButtonPressed();
                }
            }
        });
        commandPanel.add(btnQuit);

    }

    /**
     * Builds the scroll panel which will contain all of the text for the events of the server and notifications
     */
    private void buildServerInfoScrollPanel() {
        serverInfoScrollPanel = new JScrollPane();

        this.add(serverInfoScrollPanel);
        this.setTitle("Server");
        serverInfoScrollPanel.setBorder(defaultBorder("Status messages"));
        serverInfoScrollPanel.setPreferredSize(new Dimension(400, 200));

        JTextArea statusMessagesTxt = new JTextArea();
        statusMessagesTxt.setEditable(false);

        DefaultCaret caret = (DefaultCaret) statusMessagesTxt.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        serverInfoScrollPanel.setViewportView(statusMessagesTxt);
        // Source:
        // http://stackoverflow.com/questions/2483572/making-a-jscrollpane-automatically-scroll-all-the-way-down
    }
}


