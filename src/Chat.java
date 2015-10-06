import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Chat implements KeyListener {

	private Main main;

	private ArrayList<String> chatHistory = new ArrayList<String>();

	private ArrayList<String> consoleHistory = new ArrayList<String>();

	private String userInput;

	public Chat(Main main) {
		this.main = main;
		setUpChat();
	}

	public void update() {

		try {
			readFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setUpChat() {

		try {
			readFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		userInput = "";

		writeServerMessage(main.getUsername() + " Has Joined The Chat Room");

	}

	private void readFile() throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader(
				main.getFile()));

		consoleHistory.clear();
		chatHistory.clear();

		String line = reader.readLine();

		while (line != null && !line.isEmpty()) {

			consoleHistory.add(line);

			String room = line.substring(0, line.indexOf('/'));

			line = line.substring(line.indexOf('/') + 1);

			if (room.equals(main.getChatRoom())) {

				if (isMessageForMe(line)) {
					chatHistory.add(line);
				}
			}

			line = reader.readLine();
		}

		reader.close();

	}

	private boolean isMessageForMe(String message) {

		if (message.substring(0, message.indexOf(':')).equals(
				main.getUsername())) {
			return true;
		}

		String messageNoName = message.substring(message.indexOf(' ') + 1);

		if (messageNoName.indexOf("/whisper") == 0) {
			if (messageNoName.substring(9, messageNoName.indexOf(':')).equals(
					main.getUsername())) {
				return true;
			}
			return false;
		}
		return true;
	}

	public void writeServerMessage(String message) {
		message = main.getChatRoom() + "/" + "SERVER" + ": " + message;

		try {
			readFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		consoleHistory.add(message);

		PrintWriter writer = null;
		try {
			writer = new PrintWriter(main.getFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		for (String s : consoleHistory) {
			writer.println(s);
		}

		writer.close();
	}

	private void writeToFile(String message) {

		if (commandHandler(message) == null) {
			return;
		}

		if (message.isEmpty()) {
			return;
		}

		message = main.getChatRoom() + "/" + main.getUsername() + ": "
				+ message;

		try {
			readFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		consoleHistory.add(message);

		PrintWriter writer = null;
		try {
			writer = new PrintWriter(main.getFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		for (String s : consoleHistory) {
			writer.println(s);
		}

		writer.close();
	}

	private String commandHandler(String userInput) {

		switch (userInput) {

		case ("/clearServer"):
			clearFile();
			return null;

		default:
			return userInput;
		}
	}

	private void clearFile() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(main.getFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		writer.close();
	}

	@Override
	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {

		case (8):
			if (userInput.length() > 0) {
				userInput = userInput.substring(0, userInput.length() - 1);
			}
			return;

		case (10):
			writeToFile(userInput);
			userInput = "";
			return;

		case (16):
			return;

		default:
			userInput += e.getKeyChar();
			return;
		}
	}

	public String getUserMessage() {
		return userInput;
	}

	public ArrayList<String> getChatHistory() {
		return chatHistory;
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
