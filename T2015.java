import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics;


public class T2015 implements ActionListener //Our 2015 implements ActionListener interface.
{
    private JFrame f; //Main window.
    private JPanel p; //A drawing area inside main window.
	private JButton btn1;
	private JButton btn2;
	private JButton btn3;
	private JButton btn4;
	private JButton btn5;
	private JLabel lbl;
	private TGraph tg;
	
	public void SetDataStructure()
	{
	  tg=new TGraph();
	}
	public void RenderPoints()
	{
	    Graphics g = p.getGraphics(); //Graphics context of JPanel.
		int [][] drawdata =tg.GetGraphData();
		try {
		for(int i=0; i<drawdata.length; i++)
		{
		g.setColor(Color.RED);
		g.drawRect(drawdata[i][0],drawdata[i][1],5,5);
		g.setColor(Color.BLUE);
        g.drawLine(drawdata[i][0],drawdata[i][1],drawdata[drawdata[i][2]-1][0],drawdata[drawdata[i][2]-1][1]);
		g.drawLine(drawdata[i][0],drawdata[i][1],drawdata[drawdata[i][3]-1][0],drawdata[drawdata[i][3]-1][1]);
		g.drawLine(drawdata[i][0],drawdata[i][1],drawdata[drawdata[i][4]-1][0],drawdata[drawdata[i][4]-1][1]);
	    Thread.sleep(200);
	 }
	}catch (Exception e) {}
	}
	public void actionPerformed(ActionEvent e)  //This function is called when a button is pressed.
	{
		String source=e.getActionCommand();
		if(source.equals("Graph"))
		{
		   lbl.setText(source);
		   RenderPoints();
		}
		
		if(source.equals("Traverse1"))
		{
		   lbl.setText(source);
		   tg.depthSearch();
		   RenderPoints();
		}
		
		if(source.equals("Traverse2"))
		{
		   lbl.setText(source);
		   tg.breadthSearch();
		   RenderPoints();
		}
		
		if(source.equals("Minimum"))
		{
		   lbl.setText(source);
		   tg.minimumSpanningTree();
		   RenderPoints();
		
		}
		if(source.equals("Floyd"))
		{
		   lbl.setText(source);
		   tg.floyd();
		   RenderPoints();
		 
		}
		
		
    }
	public void buildGUI()
	{
	    f=new JFrame();
		f.setLayout(null); //We do not use any predefined layout.
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBounds(50,50,1024,768); //Extents for main window.
		p=new JPanel();
		p.setBounds(10,10,800,600); //Extents for drawing area.
		p.setBackground(Color.GREEN);
		btn1=new JButton("Graph");
		btn1.setBounds(850,10,100,20);	//Extents for a button.
		btn1.addActionListener(this);   //An action listener is connected to a button.
        btn2=new JButton("Traverse1");
		btn2.setBounds(850,50,100,20);
		btn2.addActionListener(this);
        btn3=new JButton("Traverse2");
		btn3.setBounds(850,90,100,20);
        btn3.addActionListener(this);	
        btn4=new JButton("Minimum");
		btn4.setBounds(850,130,100,20);
        btn4.addActionListener(this);	
		btn5=new JButton("Floyd");
		btn5.setBounds(850,170,100,20);
        btn5.addActionListener(this);	
		lbl=new JLabel("Tira2015");
		lbl.setBounds(850,210,100,20);
		f.add(p);  //Add drawing area inside the main window.
		f.add(btn1);
		f.add(btn2);
		f.add(btn3);
		f.add(btn4);
		f.add(btn5);
		f.add(lbl);
	}
	    
	
	public static void main(String[] args)
	{
	    T2015 ht=new T2015();		      
		ht.buildGUI();
		ht.SetDataStructure();
	}
}