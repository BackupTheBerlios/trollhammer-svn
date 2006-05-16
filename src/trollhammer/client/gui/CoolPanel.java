package trollhammer.client.gui;
import trollhammer.client.*;
import trollhammer.commun.*;
import javax.swing.*;
import java.awt.*;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

class CoolPanel extends JPanel
{
	private FormLayout layout = null;
	//private PanelBuilder builder = null;
	private CellConstraints cc = null;
	
	public CoolPanel()
	{
		//this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createEtchedBorder());
	}
	public CoolPanel(String col, String row)
	{
		super();
		layout = new FormLayout(col,row);
		this.setLayout(layout);
		//builder = new PanelBuilder(layout);
		//builder.setDefaultDialogBorder();
		cc = new CellConstraints();
		//this.add(builder.getPanel());
		this.setBorder(BorderFactory.createEtchedBorder());
	}
	public CoolPanel(String col, String row, Color bg)
	{
		super();
		layout = new FormLayout(col,row);
		this.setLayout(layout);
		//builder = new PanelBuilder(layout);
		//builder.setDefaultDialogBorder();
		cc = new CellConstraints();
		//this.add(builder.getPanel());
		this.setBackground(bg);
		this.setBorder(BorderFactory.createEtchedBorder());
	}
	public void addC(Component c, CellConstraints cc)
	{
		//builder.add(c,cc);
		//Logger.log("CoolPanel",2,"Kikoooooooo LooooooOOoOOoOOl :)))))))))");
		try
		{
			this.add(c,cc);
		} catch(Exception e) {Logger.log("ADD",2,"heu: "+e.getMessage());}
	}
	public void addLabel(String s, CellConstraints cc)
	{
		//builder.addLabel(s, cc);
		try
		{
			this.add(new JLabel(s),cc);
		} catch(Exception e) {Logger.log("ADDLABEL",2,"heu: "+e.getMessage());}
	}
	public void setRowGroups(int[][] g)
	{
		layout.setRowGroups(g);
	}
	public void setColumnGroups(int [][] g)
	{
		layout.setColumnGroups(g);
	}
}