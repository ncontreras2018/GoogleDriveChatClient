import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Main implements ActionListener {

	private File file;

	private ChatPanel panel;

	private Chat chat;

	private String username;

	private String chatRoom;

	public static void main(String[] args) {
		new Main();
	}

	private Main() {

		chatRoom = JOptionPane.showInputDialog("Enter Chatroom Name:");

		if (chatRoom == null) {
			return;
		}
		
		String connectionResult = setUpConnection();
		
		if (connectionResult != null) {
			JOptionPane.showMessageDialog(null, connectionResult);
			return;
		}

		while (!isUserNameLegal()) {
			username = JOptionPane.showInputDialog("Enter A Username:");
			
			if (username == null) {
				return;
			}
		}

		JFrame f = setUpGUI();

		chat = new Chat(this);

		f.addKeyListener(chat);

		new Timer(50, this).start();
	}

	private boolean isUserNameLegal() {
		if (username == null || username.isEmpty()) {
			return false;
		}

		if (username.contains("/") || username.contains(":")) {
			return false;
		}

		return true;
	}

	private JFrame setUpGUI() {
		JFrame frame = new JFrame();

		panel = new ChatPanel(this);

		panel.setPreferredSize(new Dimension(600, 400));

		frame.add(panel);
		
		frame.addWindowListener(panel);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.pack();

		frame.setVisible(true);

		return frame;
	}

	public File getFile() {
		return file;
	}

	public Chat getChat() {
		return chat;
	}

	public ChatPanel getChatPanel() {
		return panel;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		chat.update();

		panel.repaint();

	}

	private File findGoogleDriveFolder() {
		ArrayList<File> curFiles = new ArrayList<File>();

		curFiles.add(new File("C:/")); //Windows base dir
		
		if (!curFiles.get(0).exists()) {
			curFiles.clear();
			curFiles.add(new File("/")); //MAC base dir
		}

		while (curFiles.size() > 0) {

			ArrayList<File> nextFiles = new ArrayList<File>();

			for (File f : curFiles) {
				File[] newFiles = f.listFiles();

				if (newFiles != null) {

					for (File cur : newFiles) {
						if (cur.getName().equals("Google Drive")) {
							return cur;
						} else {
							nextFiles.add(cur);
						}
					}
				}
			}
			curFiles = nextFiles;
		}
		return null;
	}

	private String setUpConnection() {
		
		File driveFolder = findGoogleDriveFolder();
		
		if (driveFolder == null) {
			return "Connection Failed:\nGoogle Drive Folder Not Found";
		}

		file = new File(driveFolder.getAbsolutePath()
				+ "/GoogleDriveChat/chat.txt");
		
		if (file == null || !file.exists()) {
			return "Connection Failed:\nCould Not Connect With Server";
		}
		return null;
	}

	public String getChatRoom() {
		return chatRoom;
	}
}
