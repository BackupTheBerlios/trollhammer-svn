package trollhammer;
import javax.swing.*;
import java.awt.*;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

class CoolPanel extends JPanel
{
	private FormLayout layout = null;
	private PanelBuilder builder = null;
	private CellConstraints cc = null;
	
	public CoolPanel()
	{
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createEtchedBorder());
	}
	public CoolPanel(String col, String row)
	{
		layout = new FormLayout(col,row);
		builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		cc = new CellConstraints();
		this.add(builder.getPanel());
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createEtchedBorder());
	}
	public void add(Component c, CellConstraints cc)
	{
		builder.add(c,cc);
	}
	public void addLabel(String s, CellConstraints cc)
	{
		builder.addLabel(s, cc);
	}
}