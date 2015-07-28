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
import javax.swing.JProgressBar;

public class MainClient implements Observer {
	boolean isConnected = false;
	private JFrame frmDistributedbox;
	ClientConnector cc = ClientConnector.getInstance();
	JTree treeItems = new JTree();
	private JLabel lblProgression;
	private JProgressBar progressBar;

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
		frmDistributedbox.setBounds(100, 100, 899, 654);
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
		
		JPanel panel = new JPanel();
		
		JPanel panel_1 = new JPanel();
		GroupLayout groupLayout = new GroupLayout(
				frmDistributedbox.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(11)
					.addComponent(panelItems, GroupLayout.PREFERRED_SIZE, 290, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(panelButtons, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(pnlConnection, GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
								.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addComponent(PanelItems, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap(38, GroupLayout.PREFERRED_SIZE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panelItems, GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addContainerGap()
									.addComponent(pnlConnection, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(11)
									.addComponent(panel, GroupLayout.PREFERRED_SIZE, 19, Short.MAX_VALUE))
								.addComponent(panelButtons, GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE))
							.addGap(21)
							.addComponent(PanelItems, GroupLayout.PREFERRED_SIZE, 412, GroupLayout.PREFERRED_SIZE)))
					.addGap(34))
		);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblProgression = new JLabel("transfert du fichier : ");
		lblProgression.setHorizontalAlignment(SwingConstants.CENTER);
		lblProgression.setForeground(Color.BLACK);
		panel_1.add(lblProgression);
		 panel.setLayout(new BorderLayout(0, 0));
		
		 progressBar = new JProgressBar();
		progressBar.setValue(0);
		panel.add(progressBar);
		PanelItems.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		PanelItems.add(scrollPane);

		final JTextArea txtLogs = new JTextArea();
		txtLogs.setEditable(false);
		txtLogs.setText("Connectez-vous au name node pour d�buter");
		scrollPane.setViewportView(txtLogs);
		pnlConnection.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		final JLabel lblConnexion = new JLabel("D�connect�");
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
		panelButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		final JButton btnAjouter = new JButton("Ajout");

		btnAjouter.setEnabled(false);
		btnAjouter.setHorizontalAlignment(SwingConstants.RIGHT);
		panelButtons.add(btnAjouter);

		final JButton btnSupprimer = new JButton("Supp");

		btnSupprimer.setEnabled(false);
		
		btnSupprimer.setHorizontalAlignment(SwingConstants.RIGHT);
		panelButtons.add(btnSupprimer);

		final JButton btnOuvrir = new JButton("Ouvrir");
		btnOuvrir.setEnabled(false);

		btnOuvrir.setHorizontalAlignment(SwingConstants.RIGHT);
		btnOuvrir.setEnabled(false);
		panelButtons.add(btnOuvrir);
		frmDistributedbox.getContentPane().setLayout(groupLayout);
		treeItems.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				System.out.println("something happens");
				boolean somethingIsSelected = !(treeItems.isSelectionEmpty());
				btnSupprimer.setEnabled(somethingIsSelected);
				btnOuvrir.setEnabled(somethingIsSelected);

			}
		});
		btnAjouter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int returnVal = chooser.showOpenDialog(new JFrame());
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeItems
						.getLastSelectedPathComponent();

				String dossierParent = " ";
				// rebuild du open au niveau du selected Node
				if (selectedNode != null) {
					dossierParent = "";
					String temp = rebuildNodeString(selectedNode);
					System.out.println(temp);
					if (temp.contains("\\")) {

						String[] tempSplit = temp.replace("\\", "/").split("/");
						for (int i = 0; i < tempSplit.length; i++) {

							if (!tempSplit[i].contains(".")) {

								if (i != 0) {
									dossierParent += "\\";
								}
								dossierParent += tempSplit[i];
							}
						}
					}
					else if(selectedNode.getChildCount() > 0){
						dossierParent += selectedNode.toString() + "\\";
					}
				}

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File fichierOuDossier = chooser.getSelectedFile();
					txtLogs.append("\nFichier s�lectionn�, envoi en cours");

					recurseFolder(cc, fichierOuDossier, dossierParent);

				} else {
					txtLogs.append("\najout annul� par l'utilisateur");
				}
			}
		});
		btnSupprimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// rebuild chemin du node root\\dossier1\\fichier
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeItems
						.getLastSelectedPathComponent();

				String rebuild = rebuildNodeString(selectedNode);

				System.out.println(rebuild);
				cc.deleteFile(rebuild);

				txtLogs.append("\nSupression de l'�l�ment, " + rebuild);

			}
		});
		btnOuvrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeItems
						.getLastSelectedPathComponent();
				String rebuild = rebuildNodeString(selectedNode);

				cc.readFile(rebuild);

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
					lblConnexion.setText("D�connect�");
					lblConnexion.setForeground(Color.RED);

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
							txtLogs.append("Connexion �tablie avec le serveur");

							// set isConnected True
							isConnected = true;
							cc.getListFileAvailaible();
							lblConnexion.setText("Connecte");
							lblConnexion.setForeground(Color.GREEN);
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

	private void recurseFolder(ClientConnector cc, File file,
			String dossierParent) {
		if (file.isDirectory()) {
			if(dossierParent != ""){
				dossierParent += "\\";
			}
			dossierParent += file.getName();
			File[] filesInDirectory = file.listFiles();
			for (File f : filesInDirectory) {
				recurseFolder(cc, file, dossierParent);
			}
		} else {
			System.out.println("�criture du fichier " + file.getName() + " dans " + dossierParent);
			if(dossierParent.lastIndexOf("\\") != dossierParent.length()-1)
				dossierParent += "\\";
			cc.addFile(file, dossierParent);
			
		}
	}

	public String rebuildNodeString(DefaultMutableTreeNode selectedNode) {
		boolean isroot = false;
		ArrayList<String> rebuildStrings = new ArrayList<String>();
		if(selectedNode.toString().equals("DisBox")){
			isroot=true;
		}
		while (!isroot) {
			rebuildStrings.add(selectedNode.toString());
			
			if (!(selectedNode.getParent().toString().equals("DisBox"))) {
				selectedNode = (DefaultMutableTreeNode) selectedNode
						.getParent();
			} else {
				isroot = true;
			}
		}
		String rebuild = "";
		for (int i = rebuildStrings.size() - 1; i >= 0; i--) {
			if (i != rebuildStrings.size() - 1) {
				rebuild += "\\";
			}
			rebuild += rebuildStrings.get(i);
		}
		return rebuild;
	}

	@Override
	public void update(Observable o, Object arg) {


		if(arg==null){
		updateFileList(ClientConnector.getInstance().getListFileAvailaible());
		}else{

			if(FileProgressUpdate.class.isInstance(arg)){
				FileProgressUpdate update=(FileProgressUpdate)arg;
				lblProgression.setText("Transfert du fichier : "+update.getFileName());
				progressBar.setValue(update.getPercent());
					progressBar.setStringPainted(true);
				progressBar.repaint();

			}

		}


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

	private void buildTreeFromString(DefaultTreeModel model, final String str) {
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
