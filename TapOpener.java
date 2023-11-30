import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

public class TapOpener extends JFrame {

    private JTabbedPane tabbedPane;
    private Process rdpProcess;
    private Process puttyProcess;
    private WindowAdapter puttyProcessWindowListener;
    private boolean isNonWindowTabSelected = false;

    public TapOpener() {
        initUI();
    }

    private void initUI() {
        setTitle("Server");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Add window listener to handle main window closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeAllProcesses();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton windowButton = new JButton("Window");
        windowButton.setPreferredSize(new Dimension(windowButton.getPreferredSize().width, 10));
        windowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isNonWindowTabSelected = false;
                createRDCTab();
            }
        });

        JButton nonWindowButton = new JButton("Non-Window");
        nonWindowButton.setPreferredSize(new Dimension(nonWindowButton.getPreferredSize().width, 10));
        nonWindowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isNonWindowTabSelected = true;
                createPuttyTab();
            }
        });

        int verticalSpace = 10;
        buttonPanel.add(Box.createVerticalStrut(verticalSpace));
        buttonPanel.add(windowButton);
        buttonPanel.add(Box.createVerticalStrut(verticalSpace));
        buttonPanel.add(nonWindowButton);

        add(buttonPanel, BorderLayout.WEST);

        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createRDCTab() {
        if (rdpProcess == null) {
            try {
                rdpProcess = new ProcessBuilder("mstsc.exe").start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            JPanel rdpPanel = new JPanel();
            tabbedPane.addTab("RDP", rdpPanel);

            JButton closeButton = new JButton("X");
            closeButton.setMargin(new Insets(0, 0, 0, 0));
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (rdpProcess != null) {
                        rdpProcess.destroy();
                        rdpProcess = null;
                    }
                    int tabIndex = tabbedPane.indexOfComponent(rdpPanel);
                    if (tabIndex != -1) {
                        tabbedPane.removeTabAt(tabIndex);
                    }
                }
            });

            // Add MouseListener to change button color on hover
            closeButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    closeButton.setBackground(Color.RED);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    closeButton.setBackground(null); // Reset to default background color
                }
            });

            JPanel tabTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            tabTitlePanel.add(new JLabel("RDP"));
            tabTitlePanel.add(closeButton);

            tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, tabTitlePanel);
        }
    }

    private void createPuttyTab() {
        if (puttyProcess == null) {
            try {
                puttyProcess = new ProcessBuilder("\"C:\\Program Files\\PuTTY\\putty.exe\"")
                        .redirectOutput(Redirect.INHERIT)
                        .redirectError(Redirect.INHERIT)
                        .start();

                // Add WindowListener to track PuTTY window state
                puttyProcessWindowListener = new WindowAdapter() {
                    private Component puttyPanel;

                    @Override
                    public void windowClosed(WindowEvent e) {
                        int tabIndex = tabbedPane.indexOfComponent(puttyPanel);
                        if (tabIndex != -1) {
                            tabbedPane.removeTabAt(tabIndex);
                            puttyProcess = null; // Reset the process reference
                        }
                    }
                };

                JPanel puttyPanel = new JPanel();
                tabbedPane.addTab("Putty", puttyPanel);

                JButton closeButton = new JButton("X");
                closeButton.setMargin(new Insets(0, 0, 0, 0));
                closeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (puttyProcess != null) {
                            puttyProcess.destroy();
                            puttyProcess = null;
                        }
                        int tabIndex = tabbedPane.indexOfComponent(puttyPanel);
                        if (tabIndex != -1) {
                            tabbedPane.removeTabAt(tabIndex);
                        }
                    }
                });

                // Add MouseListener to change button color on hover
                closeButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        closeButton.setBackground(Color.RED);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        closeButton.setBackground(null); // Reset to default background color
                    }
                });

                JPanel tabTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                tabTitlePanel.add(new JLabel("Putty"));
                tabTitlePanel.add(closeButton);

                tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, tabTitlePanel);

                if (isNonWindowTabSelected) {
                    tabbedPane.setSelectedComponent(puttyPanel);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void closeAllProcesses() {
        if (rdpProcess != null) {
            rdpProcess.destroy();
        }
        if (puttyProcess != null) {
            puttyProcess.destroy();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TapOpener();
        });
    }
}