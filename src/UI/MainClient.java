package UI;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.Color;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JTextArea;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.filechooser.FileNameExtensionFilter;

import FileServerEntity.FileManager.InitFileSynchronizer;
import FileServerEntity.Message.ServerMessage.InitSymchronizerMessage;
import GUI.ClientConnector;
import GUI.SystemConnector;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;

public class MainClient implements Observer {
	boolean isConnected = false;
	private JFrame frmDistributedbox;
	ClientConnector cc = ClientConnector.getInstance();
	JTree treeItems = new JTree();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainClient window = new MainClient();
					window.frmDistributedbox.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainClient() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frmDistributedbox = new JFrame();
		frmDistributedbox.setTitle("DistributedBox");
		frmDistributedbox.setBounds(100, 100, 666, 510);
		frmDistributedbox.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ClientConnector.getInstance().addObserver(this);

		JMenuBar menuBar = new JMenuBar();
		frmDistributedbox.setJMenuBar(menuBar);

		final JMenu mnConnection = new JMenu("Connexion");
		menuBar.add(mnConnection);

		final JMenuItem menuItemConnect = new JMenuItem("Connecte");

		mnConnection.add(menuItemConnect);

		JPanel panelButtons = new JPanel();

		JPanel pnlConnection = new JPanel();
		pnlConnection.setForeground(Color.RED);

		final JPanel panelItems = new JPanel();

		JPanel PanelItems = new JPanel();
		GroupLayout groupLayout = new GroupLayout(
				frmDistributedbox.getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGap(11)
										.addComponent(panelItems,
												GroupLayout.PREFERRED_SIZE,
												242, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addComponent(
																				panelButtons,
																				GroupLayout.PREFERRED_SIZE,
																				108,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				ComponentPlacement.UNRELATED)
																		.addComponent(
																				pnlConnection,
																				GroupLayout.PREFERRED_SIZE,
																				125,
																				GroupLayout.PREFERRED_SIZE))
														.addComponent(
																PanelItems,
																GroupLayout.DEFAULT_SIZE,
																374,
																Short.MAX_VALUE))
										.addGap(14)));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.TRAILING)
														.addComponent(
																panelItems,
																GroupLayout.DEFAULT_SIZE,
																405,
																Short.MAX_VALUE)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGroup(
																				groupLayout
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addGroup(
																								groupLayout
																										.createSequentialGroup()
																										.addGap(31)
																										.addComponent(
																												pnlConnection,
																												GroupLayout.PREFERRED_SIZE,
																												GroupLayout.DEFAULT_SIZE,
																												GroupLayout.PREFERRED_SIZE))
																						.addComponent(
																								panelButtons,
																								GroupLayout.PREFERRED_SIZE,
																								89,
																								GroupLayout.PREFERRED_SIZE))
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				PanelItems,
																				GroupLayout.DEFAULT_SIZE,
																				309,
																				Short.MAX_VALUE)))
										.addGap(34)));
		PanelItems.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		PanelItems.add(scrollPane);

		final JTextArea txtLogs = new JTextArea();
		txtLogs.setEditable(false);
		txtLogs.setText("This is a really long line to prove the point that the scroller works even thought if the logs are this long this will be so stupid\r\n\r\nhello\r\nmom");
		scrollPane.setViewportView(txtLogs);
		pnlConnection.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		final JLabel lblConnexion = new JLabel("D\u00E9connect\u00E9");
		lblConnexion.setForeground(Color.RED);
		lblConnexion.setHorizontalAlignment(SwingConstants.CENTER);
		pnlConnection.add(lblConnexion);
		panelItems.setLayout(new BorderLayout(0, 0));

		JScrollPane ScrollPanelItems = new JScrollPane();
		panelItems.add(ScrollPanelItems);

		treeItems.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int row = treeItems.getRowForLocation(arg0.getX(), arg0.getY());
				if (row == -1)
					treeItems.clearSelection();
			}
		});

		treeItems.setVisibleRowCount(21);
		ScrollPanelItems.setViewportView(treeItems);
		treeItems.setEditable(true);
		treeItems.setBackground(Color.WHITE);
		treeItems.setModel(new DefaultTreeModel(new DefaultMutableTreeNode(
				"DisBox") {
			{
				
			}
		}));

		final JButton btnAjouter = new JButton("Ajout");

		btnAjouter.setEnabled(false);
		btnAjouter.setHorizontalAlignment(SwingConstants.RIGHT);
		panelButtons.add(btnAjouter);

		final JButton btnSupprimer = new JButton("Supp");

		btnSupprimer.setEnabled(false);
		btnSupprimer.setHorizontalAlignment(SwingConstants.RIGHT);
		panelButtons.add(btnSupprimer);
		frmDistributedbox.getContentPane().setLayout(groupLayout);
		treeItems.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				System.out.println("something happens");
				boolean somethingIsSelected = !(treeItems.isSelectionEmpty());
				btnSupprimer.setEnabled(somethingIsSelected);

			}
		});
		btnAjouter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int returnVal = chooser.showOpenDialog(new JFrame());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File fichierOuDossier = chooser.getSelectedFile();
					txtLogs.append("\nFichier sélectionné, envoi en cours");
					if(fichierOuDossier.isDirectory()){
						File[] filesInDirectory = fichierOuDossier.listFiles();
						for ( File file : filesInDirectory ) {
							System.out.println("envoie de " + file.getName());
							cc.addFile(file, " ");	
						}
					}
					else{
						cc.addFile(fichierOuDossier, " ");	
					}
					// envoie le fichier Ã  ajouter et le path. le path est le
					// path choisie par l'utilisateur ex
					// root/bleu/nouvveaufichier.txt

					

					// send file/dossier to server
				} else {
					txtLogs.append("\najout annulï¿½ par l'utilisateur");
				}
			}
		});
		btnSupprimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cc.getServerConnectedTo().sendMessage(/* messageDelete */null);
				// refresh treeItems aprï¿½s rï¿½ponse

			}
		});
		menuItemConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (isConnected) {
					isConnected = false;

					// kill connection
					// cc.setServerConnectedTo(null); //??

					// change name of button back to Connect
					mnConnection.setText("Connecte");
					lblConnexion.setText("Déconnecté");
					lblConnexion.setForeground(new Color(16));

				} else {
					// popup avec info du nameNode -- needed?

					String connectionString = (String) JOptionPane
							.showInputDialog(
									new JFrame(),
									"Entrez les informations de connexions pour le NameNode",
									"Connexion..", JOptionPane.PLAIN_MESSAGE,
									null, null, "localhost:10111");

					// If a string was returned
					if ((connectionString != null)
							&& (connectionString.length() > 0)
							&& (connectionString.contains(":"))) {
						// extract IPAdress, portNumber
						String hostname = connectionString.split(":")[0];
						Inet4Address ip;
						int port;
						try {
							ip = (Inet4Address) Inet4Address
									.getByName(hostname);
							port = Integer.parseInt(connectionString.split(":")[1]);
							System.out.println("connecting to " + ip.toString()
									+ " on the port " + port);
							// connect to nameNode, server infos will be
							// dispatched
							cc.ConnectToFileSystem(ip, port);
							// Connect to Server
							txtLogs.setText("");
							txtLogs.append("\nConnexion établie avec le serveur");

							// set isConnected True
							isConnected = true;
							cc.getListFileAvailaible();
							lblConnexion.setText("Connecté");
							lblConnexion.setForeground(new Color(9));
							mnConnection.setText("Deconnecte");
							// refresh treeItems
						} catch (Exception e) {
							txtLogs.append("\nEchec de connexion, "
									+ connectionString
									+ " n'est pas une connexion valide");
						}

					} else {
						txtLogs.append("\nEchec de connexion, "
								+ connectionString
								+ " n'est pas une connexion valide");
					}

				}
				btnAjouter.setEnabled(isConnected);
			}
		});
	}

	@Override
	public void update(Observable o, Object arg) {

		updateFileList(ClientConnector.getInstance().getListFileAvailaible());
	}

	private void updateFileList(final ArrayList<String> listeFile) {

		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("DisBox");

		// Create the tree model and add the root node to it
		DefaultTreeModel model = new DefaultTreeModel(root);

		// pour chacun des fichiers dans la liste
		for (String f : listeFile) {
			String file = f.replace("\\", "/");

			// Build the tree from the various string samples
			buildTreeFromString(model, file);

		}
		treeItems.setModel(model);
	}

	private void buildTreeFromString(DefaultTreeModel model,
			final String str) {
		// Fetch the root node
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

		// Split the string around the delimiter
		String[] strings = str.split("/");

		// Create a node object to use for traversing down the tree as it
		// is being created
		DefaultMutableTreeNode node = root;
		// Iterate of the string array
		for (String s : strings) {
			// Look for the index of a node at the current level that
			// has a value equal to the current string
			int index = childIndex(node, s);

			// Index less than 0, this is a new node not currently present on
			// the tree
			if (index < 0) {
				// Add the new node
				DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(s);
				node.insert(newChild, node.getChildCount());
				node = newChild;
			}
			// Else, existing node, skip to the next string
			else {
				node = (DefaultMutableTreeNode) node.getChildAt(index);
			}
		}
	}

	private int childIndex(final DefaultMutableTreeNode node,
			final String childValue) {
		Enumeration<DefaultMutableTreeNode> children = node.children();
		DefaultMutableTreeNode child = null;
		int index = -1;

		while (children.hasMoreElements() && index < 0) {
			child = children.nextElement();

			if (child.getUserObject() != null
					&& childValue.equals(child.getUserObject())) {
				index = node.getIndex(child);
			}
		}

		return index;
	}
}
