import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ChatPanel extends JPanel implements WindowListener {

	private Main main;

	public ChatPanel(Main main) {
		this.main = main;
	}

	@Override
	public void paintComponent(Graphics g) {

		if (main.getChat() == null) {
			return;
		}

		int lineSpacing = 15;

		int newMessagePos = 50;

		int bottomSize = 25;

		int topPanelSize = 50;

		Graphics panelG = g;

		panelG.setColor(Color.LIGHT_GRAY);

		panelG.fillRect(0, 0, getWidth(), getHeight());

		panelG.setColor(Color.WHITE);

		panelG.fillRoundRect(0, getHeight() - bottomSize - (newMessagePos / 3),
				getWidth(), 100, 50, 50);

		panelG.fillRect(0, getHeight() - bottomSize - (newMessagePos / 3),
				getWidth() / 2, 100);

		String userMessage = main.getChat().getUserMessage();
		ArrayList<String> messages = main.getChat().getChatHistory();

		for (int i = 0; i < messages.size(); i++) {

			String curMessage = messages.get(messages.size() - 1 - i);

			setFont(g, curMessage);

			panelG.drawString(curMessage, 10, getHeight() - newMessagePos
					- (lineSpacing * i));

		}

		panelG.setColor(Color.WHITE);

		panelG.fillRect(0, 0, getWidth(), topPanelSize / 2);
		panelG.fillRoundRect(0, 0, getWidth(), topPanelSize, 50, 50);

		panelG.setColor(Color.BLACK);

		panelG.drawString(userMessage, 10, getHeight() - bottomSize);

		panelG.drawString("Chat Room: " + main.getChatRoom(), 10, 10);
	}

	private void setFont(Graphics g, String curMessage) {

		if (curMessage.substring(0, curMessage.indexOf(':')).equals(
				main.getUsername())) {
			g.setColor(Color.RED);

		} else if (curMessage.substring(0, curMessage.indexOf(':')).equals(
				"SERVER")) {
			g.setColor(Color.WHITE);

		} else {

			g.setColor(Color.BLACK);
		}

		if (curMessage.indexOf('/') != -1) {

			if (curMessage.substring(curMessage.indexOf('/'),
					curMessage.indexOf('/') + 8).equals("/whisper")) {

				g.setColor(Color.BLUE);
			}
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		main.getChat().writeServerMessage(
				main.getUsername() + " Has Left The Chat Room");
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}
}
