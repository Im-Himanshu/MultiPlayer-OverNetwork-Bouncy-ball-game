import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class clientstart {
	private static void createAndShowGUI() {
    final String[] labels = { "Enter Port No. of server","Enter ip of server in Ipv4" };
    int labelsLength = labels.length;
    final JTextField[] textField = new JTextField[labels.length];
    //Create and populate the panel.
    JPanel p = new JPanel(new SpringLayout());
    for (int i = 0; i < labelsLength; i++) {
        JLabel l = new JLabel(labels[i], JLabel.TRAILING);
        p.add(l);
        textField[i] = new JTextField(10);
        l.setLabelFor(textField[i]);
        p.add(textField[i]);
    }
    JButton button = new JButton("Start Game server");
    p.add(new JLabel());
    p.add(button);

    //Lay out the panel.
    SpringUtilities.makeCompactGrid(p,
                                    labelsLength + 1, 2, //rows, cols
                                    7, 7,        //initX, initY
                                    7, 7);       //xPad, yPad

    button.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            //for (int i = 0 ; i < labels.length ; i++)
            //{
            	String[] send = new String[2];
            	send[1] = textField[1].getText().toString();
            	send[0] = textField[0].getText().toString();
            	//String s  =	textField[0].getText().toString();
            	//int s1 = Integer.valueOf(s).intValue();
                //new server(s1,send).start();
              client.main(send);
                //System.out.println(labels[i]+"->"+textField[i].getText());
            //}
        }
    });  
    //Create and set up the window.
    JFrame frame = new JFrame("SpringForm");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //Set up the content pane.
    p.setOpaque(true);  //content panes must be opaque
    frame.setContentPane(p);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

}