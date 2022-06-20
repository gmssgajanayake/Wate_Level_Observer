import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

abstract class MyFrame extends JFrame{
	private JPanel itemNamePanel;
	private JPanel updateDataPanel;
	private JLabel updateDataLabel;
	JSlider slider;
	MyFrame(){
		setSize(1650,1080);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setVisible(true);
		}
		
	public void createItem(String objectName){
		setTitle(objectName);
		itemNamePanel = new JPanel();
		updateDataPanel=new JPanel();
		
		add(itemNamePanel, BorderLayout.NORTH);
		add(updateDataPanel, BorderLayout.CENTER);
		
		JLabel itemNameLabel= new JLabel(objectName.toUpperCase());
		itemNameLabel.setFont(new Font("", Font.PLAIN, 34));
		itemNamePanel.add(itemNameLabel);
		
		updateDataLabel= new JLabel();
		updateDataLabel.setFont(new Font("", Font.PLAIN, 90));
		updateDataPanel.add(updateDataLabel);
		} 	
		
	public void updateWindow(String data){
		updateDataLabel.setText(data);
		}
		
	public void waterLevel(){
        slider = new JSlider(JSlider.VERTICAL, -00, 100, 0);
		updateDataPanel.add(slider);
		
		slider.setPaintLabels(true);
         
		Hashtable position = new Hashtable();
		position.put(0, new JLabel("0"));
		position.put(25, new JLabel("25"));
		position.put(50, new JLabel("50"));
		position.put(75, new JLabel("75"));
		position.put(100, new JLabel("100"));

		slider.setLabelTable(position); 
		}
	public void updateWaterLevel(int waterLevel){
		slider.setValue(waterLevel);
		}
	public void Size(int x,int y){
		setSize(x,y);
		}	
	}
	

interface WaterLevelObserver{
	public void update(int waterLevel);
}

class Splitter extends MyFrame implements WaterLevelObserver{
	
	Splitter(){
		createItem("Splitter");
		setLocation(200,45);
		setSize(300,300);
		}
	
	public void update(int waterLevel){
		updateWindow(waterLevel>=75 ? "ON":"OFF");
	}	
}
class Alarm extends MyFrame implements WaterLevelObserver{
	Alarm(){
		createItem("Alarm");
		setSize(300,300);
		setLocation(500,45);
		}
	public void update(int waterLevel){
		updateWindow(waterLevel>=50 ? "ON":"OFF");
	}
}
class Display extends MyFrame implements WaterLevelObserver{
	Display(){
		createItem("Water Level");
		setSize(600,350);
		setLocation(350,370);
		waterLevel();
		}
	public void update(int waterLevel){ 
		updateWaterLevel(waterLevel);
	}
}
class SMSSender extends MyFrame implements WaterLevelObserver{
	SMSSender(){
		createItem("SMS Sender");
		setSize(300,300);
		setLocation(800,45);
		}
	public void update(int waterLevel){
		updateWindow(""+waterLevel);
	}
}
class ControlRoom{
	private ArrayList<WaterLevelObserver>observerList=new ArrayList<>();
	private int waterLevel;
		
	public void addWaterLevelObserver(WaterLevelObserver ob){
		observerList.add(ob);
	}
	public void notifyObject(){
		for(WaterLevelObserver ob: observerList){
			ob.update(waterLevel);
		}
	}
	public void setWaterLevel(int waterLevel){
		if(this.waterLevel!=waterLevel){
			this.waterLevel=waterLevel;
			notifyObject();
		} 
	}
}
class Demo{
	public static void main(String args[]){
	//To clear background
		new MyFrame(){};
	//==================
		ControlRoom controlRoom=new ControlRoom();
		controlRoom.addWaterLevelObserver(new Alarm());
		controlRoom.addWaterLevelObserver(new Display());
		controlRoom.addWaterLevelObserver(new SMSSender());
		controlRoom.addWaterLevelObserver(new Splitter());
		
		
		Random r=new Random();
		while(true){
			int rand=r.nextInt(101); //0 to 100
			controlRoom.setWaterLevel(rand);
			try{Thread.sleep(1000);}catch(Exception ex){}
		}
	}
}
