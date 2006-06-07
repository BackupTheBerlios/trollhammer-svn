// Modifié par cfrey: 4h passées à trouver et à modifier ce bâtard. Basé sur un
// code de RJHM van den Bergh <rvdb@comweb.nl>, permission d'usage et de distri-
// bution.

package trollhammer;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Console extends WindowAdapter implements WindowListener, ActionListener, Runnable {
	private JFrame frame;
	private JTextArea textArea;
	private JTextField consoleInput;
	private Thread reader;
	private Thread reader2;
	private boolean quit;
					
	private final PipedInputStream pin = new PipedInputStream(); 
	private final PipedInputStream pin2 = new PipedInputStream(); 

	private final PipedOutputStream poutout = new PipedOutputStream();
	
	public Console() {
		frame = new JFrame("Server Console");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension((int)(screenSize.width/2), (int)(screenSize.height/2));
		int x = (int)(frameSize.width/2);
		int y = (int)(frameSize.height/2);
		frame.setBounds(x, y, frameSize.width, frameSize.height);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		JButton button = new JButton("clear");
		
		consoleInput = new JTextField(256);
		
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(consoleInput, BorderLayout.NORTH);
		frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
		frame.getContentPane().add(button, BorderLayout.SOUTH);
		frame.setVisible(true);
		
		frame.addWindowListener(this);		
		button.addActionListener(this);
		consoleInput.addActionListener(this);
		
		try {
			PipedInputStream pinin = new PipedInputStream(this.poutout);
			System.setIn(pinin);//new PipedInputStream(pinin, true)); 
		}
		catch (java.io.IOException io) {
			textArea.append("Couldn't redirect STDIN to this console\n"+io.getMessage());
		}
		catch (SecurityException se) {
			textArea.append("Couldn't redirect STDIN to this console\n"+se.getMessage());
	    }
		
		try {
			PipedOutputStream pout = new PipedOutputStream(this.pin);
			System.setOut(new PrintStream(pout, true)); 
		} 
		catch (java.io.IOException io) {
			textArea.append("Couldn't redirect STDOUT to this console\n"+io.getMessage());
		}
		catch (SecurityException se) {
			textArea.append("Couldn't redirect STDOUT to this console\n"+se.getMessage());
	    } 
		
		try {
			PipedOutputStream pout2 = new PipedOutputStream(this.pin2);
			System.setErr(new PrintStream(pout2, true));
		} 
		catch (java.io.IOException io) {
			textArea.append("Couldn't redirect STDERR to this console\n"+io.getMessage());
		}
		catch (SecurityException se) {
			textArea.append("Couldn't redirect STDERR to this console\n"+se.getMessage());
	    } 		
			
		quit = false;

		reader = new Thread(this);
		reader.setDaemon(true);	
		reader.start();
		
		reader2 = new Thread(this);	
		reader2.setDaemon(true);	
		reader2.start();
	}
	
	public synchronized void windowClosed(WindowEvent evt) {
		quit = true;
		this.notifyAll();
		
		try {
			poutout.close();
		} catch (Exception e) {}
			
		try {
			reader.join(1000);
			pin.close();
		} catch (Exception e) {}
		
		try {
			reader2.join(1000);
			pin2.close();
		} catch (Exception e) {}
		
		System.exit(0);
	}		
		
	public synchronized void windowClosing(WindowEvent evt) {
		frame.setVisible(false);
		frame.dispose();
	}
	
	public synchronized void actionPerformed(ActionEvent evt) {
		String command = evt.getActionCommand();
		//System.out.println(evt);
		if (command.equals("clear")) {
			textArea.setText("");
			consoleInput.setText("");
		} else {
			consoleInput.selectAll();
			//String command = consoleInput.getText();
			textArea.append(command+"\n"); // echo ou pas ?
			textArea.setCaretPosition(textArea.getText().length());
			try {
				for (char c : command.toCharArray()) {
					poutout.write(c);
				}
				poutout.write('\n');
				poutout.flush();
			} catch (IOException e) {
				textArea.setText("IOError ...");
			}
		}
	}

	public synchronized void run() {
		try {			
			while (Thread.currentThread() == reader) {
				try {
					this.wait(100);
				} catch(InterruptedException ie) {}
				
				if (pin.available() != 0) {
					String input = this.readLine(pin);
					textArea.append(input);
					textArea.setCaretPosition(textArea.getText().length());
				}
				
				if (quit) return;
			}
		
			while (Thread.currentThread() == reader2)
			{
				try {
					this.wait(100);
				} catch(InterruptedException ie) {}
				
				if (pin2.available() != 0) {
					String input = this.readLine(pin2);
					textArea.append(input);
					textArea.setCaretPosition(textArea.getText().length());
				}
				
				if (quit) return;
			}			
		} catch (Exception e) {
			textArea.append("\nConsole reports an Internal error.");
			textArea.append("The error is: "+e);			
		}
	}
	
	public synchronized String readLine(PipedInputStream in) throws IOException {
		String input = "";
		
		do {
			int available = in.available();
			if (available == 0) break;
			byte b[] = new byte[available];
			in.read(b);
			input = input+new String(b, 0, b.length);														
		} while(!input.endsWith("\n") && !input.endsWith("\r\n") && !quit);
		
		return input;
	}	
}