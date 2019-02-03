import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowListener;
import java.util.*;

import javax.swing.JFrame;

public class ConvexHullDemo 
{
	static Graphics g;
	static int Point_number=1000;
	static Random random=new Random();
	static int MinX=60;
	static int MinY=50;
	static int MaxX=930;
	static int MaxY=900;
	
	public static void main(String[] args) 
	{
		Draw d=new Draw();
		
		for(int i=0;i<Point_number;i++)
		{
			d.DrawPoint(random.nextInt(MaxX+1-MinX)+MinX, random.nextInt(MaxY+1-MinY)+MinY);
		}		
		
	}
     
    	

}

class Draw extends Canvas 
{
	Point points[]=new Point[10000];
	Point points_find[]=new Point[10000];
	int X[]=new int[1000];
	int Y[]=new int[1000];
	int Start_Points_i=-1;
	int pointCount=0;
	int points_find_count=0;
	static int MaxX=772;
	static int MaxY=743;
	Draw()
	{
		pointCount=0;
		JFrame JF=new JFrame("ConvexHullDemo");
		JF.add(this,BorderLayout.CENTER);
		JF.setPreferredSize(new Dimension(1000,1000));
		JF.pack();
		JF.setVisible(true);
		JF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void DrawPoint(int x,int y)
	{
		points[pointCount]=new Point(x,y);
		pointCount++;
		System.out.println(pointCount+". ( "+x+" , "+y+" )");
        //g.setColor(Color.BLACK);
	}
	
	
	@Override
	public void paint(Graphics g)
	{
		//System.out.println(count);
		g.create(0,0,1000,1000);
		//System.out.println("Start_Points_i "+Start_Points_i);
		
		//System.out.println("Start_Points_i "+Start_Points_i);
		Jarvis_march();
		
		for(int i=0;i<pointCount;i++)
		{			
			
			if(i==Start_Points_i)
				{g.setColor(Color.RED);}			
			else
				{g.setColor(Color.black);}
			
			g.fillOval(points[i].GetX(), points[i].GetY(), 10, 10);
			//System.out.println(X[i]+" "+Y[i]);
			
		}
		
		/*drawline*/
		/*寫起始點座標*/
		g.setColor(Color.red);
		g.drawString("("+points_find[0].GetX()+","+points_find[0].GetY()+")", points_find[0].GetX()+4, points_find[0].GetY()+4);
		for(int j=0;j< points_find_count;j++)
		{
			g.setColor(Color.red);			
			/*將候選點連線*/
			g.drawLine(points_find[j].GetX()+4, points_find[j].GetY()+4, points_find[(j+1)%points_find_count].GetX()+4, points_find[(j+1)%points_find_count].GetY()+4);
			
			/*寫上點座標*/
			g.drawString("("+points_find[(j+1)%points_find_count].GetX()+","+points_find[(j+1)%points_find_count].GetY()+")", points_find[(j+1)%points_find_count].GetX()+4, points_find[(j+1)%points_find_count].GetY()+4);
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
								
        //g.setColor(Color.BLACK);
	}
	
	boolean compare(Point a, Point b)
	{
	   return (a.GetX() < b.GetX()) || ((a.GetX() == b.GetX() && a.GetY() > b.GetY()));
	}
	
	int cross(Point o, Point a, Point b)
	{
	    return (a.GetX() - o.GetX()) * (b.GetY() - o.GetY()) - (a.GetY() - o.GetY()) * (b.GetX() - o.GetX());

	}
	 
	// 兩點距離的平方
	int length2(Point a, Point b)
	{
	    return (a.GetX() - b.GetX()) * (a.GetX() - b.GetX()) + (a.GetY() - b.GetY()) * (a.GetY() - b.GetY());
	}
	 
	// 以o點作為中心點，a點比較遠，b點比較近。
	boolean far(Point o, Point a, Point b)
	{
	    return length2(o, a) > length2(o, b);
	}
	 
	void Jarvis_march()
	{
	    /* 起點必須是凸包上的頂點。這裡用最低最左邊的點作為起點。 */
		
		int start = 0;
	    for (int i=0; i<pointCount; ++i)
	    {
	        if (compare(points[i], points[start]))
	            {start = i;}
	    }
	    
	    //points_find_count為凸包頂點的樹木
	    points_find_count = 0;         
	    points_find[points_find_count] = points[start]; // 記錄起點
	    Start_Points_i=start;
	    System.out.println("start point:( "+points[start].GetX()+" "+points[start].GetY()+" )"); 
	    points_find_count++;
	    
	    
	    for (int m=1; true; ++m)
	    {
	        /* 找出位於最外圍的一點，若凸包上有多點共線則找最遠的一點。 */
	 
	        int next = start;
	        for (int i=0; i<pointCount; ++i)
	        {
	            int c = cross(points_find[m-1], points[i], points[next]);
	            if (c > 0 ||c == 0 && far(points_find[m-1], points[i], points[next]))
	            	{ next = i;}
	        }
	 
	        if (next == start) break;   // 繞一圈後回到起點了
	        points_find[m] = points[next];// 記錄方才所找到的點
	        points_find_count++;
	    }
	    
	    System.out.println("vertex of ConvexHull: "+points_find_count);
	}
	
}

class Point
{
	int X,Y;
	Point(int x,int y)
	{
		this.X=x;
		this.Y=y;
	}
	
	void SetX(int x)
	{this.X=x;}
	
	void SetY(int y)
	{this.Y=y;}
	
	int GetX()
	{return this.X;}
	
	int GetY()
	{return this.Y;}
}

