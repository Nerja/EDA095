package client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Gui extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextArea text;
	private JTextField input;
	private DataMonitor monitor;

	public Gui(DataMonitor monitor) {
		super("Haxxer Chat");
		this.monitor = monitor;
		text = new JTextArea();
		text.setRows(25);
		setLayout(new BorderLayout());
		add(text, BorderLayout.NORTH);
		input = new JTextField();
		add(input, BorderLayout.SOUTH);
		setSize(500, 500);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setVisible(true);
		input.addActionListener(this);
	}

	public void appendMessage(String msg) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				text.append(msg + "\n");
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		monitor.putOutgoing(input.getText());
		input.setText("");
	}
}
