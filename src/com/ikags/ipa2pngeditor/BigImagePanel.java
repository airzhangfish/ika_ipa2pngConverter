package com.ikags.ipa2pngeditor;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

public class BigImagePanel extends JPanel {
	/**
	 * 
	 */

	
	public int count=0;
	private static final long serialVersionUID = 1L;
	public Thread thread;

	public BigImagePanel() {
		this.setAutoscrolls(true);
		MyListener myListener = new MyListener();
		addMouseListener(myListener);
		addMouseMotionListener(myListener);
		thread=new Thread(){
			public void run(){
			 try{
				 while(IPADef.isRunning){
					 update_Srceen(); 
					 Thread.sleep(34);
					 count++;
				 }
			 }catch(Exception ex){
				 ex.printStackTrace();
			 }
			}
		};
		thread.start();
	}



	class MyListener extends MouseInputAdapter {

		public void mouseMoved(MouseEvent e) {
			update_Srceen();
		}

		public void mousePressed(MouseEvent e) {
			// 拖拽功能，方便整体移动。
			 if (SwingUtilities.isRightMouseButton(e)) {
				change_xxx = e.getX() - xxx;
				change_yyy = e.getY() - yyy;
				update_Srceen();
			}
		}

		// 鼠标拖拽
		public void mouseDragged(MouseEvent e) {
			
			// 右键拖拽全局
			if (SwingUtilities.isRightMouseButton(e)) {
				xxx = e.getX() - change_xxx;
				yyy = e.getY() - change_yyy;
				update_Srceen();
			}
		}

		public void mouseReleased(MouseEvent e) {
		}

	}

	int xxx = 32;
	int yyy = 32;
	int change_xxx = 0;
	int change_yyy = 0;

	int colsize=0;
	
	public void updata() {



	}

	
	Color bgcolor=new Color(IPADef.BG_R, IPADef.BG_G, IPADef.BG_B);
	Color linecolor=new Color(IPADef.SG_R, IPADef.SG_G, IPADef.SG_B);
	Color colcolor1=new Color(IPADef.CG_R1, IPADef.CG_G1, IPADef.CG_B1);
	Color colcolor2=new Color(IPADef.CG_R2, IPADef.CG_G2, IPADef.CG_B2);
	Color blackcolor=new Color(0, 0, 0);
	public void paint(Graphics g) {
		g.setColor(bgcolor);
		g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(linecolor);
		int maxlength = Math.max(g.getClipBounds().width, g.getClipBounds().height);
		for (int i = 0; i < (maxlength / 32) + 1; i++) {
			g2.drawLine(0, 32 * i, maxlength, 32 * i);
			g2.drawLine(32 * i, 0, 32 * i, maxlength);
		}

		updata();
		// 图像开始
		drawImages(xxx,yyy,g2);

		g2.setColor(blackcolor);
		g2.drawString(xxx+","+yyy,15,15);
		g2.drawString("img="+IPADef.ImageFilepath,15,30);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}
	
	
	public static void drawImages(int offx,int offy,Graphics2D g2){
		if (IPADef.mFrameImageList.size()>0) {
			int size=IPADef.mFrameImageList.size();
			int linoffX=0;
			int linoffY=0;
			int maxwidth=0;
			int lastHeight=0;
			for(int i=0;i<size;i++){
				BufferedImage frame=IPADef.mFrameImageList.elementAt(i);
				g2.drawImage(frame, offx+linoffX, offy+lastHeight, null);	
				linoffX=linoffX+frame.getWidth();
				linoffY=Math.max(linoffY, frame.getHeight());
				maxwidth=Math.max(linoffX, maxwidth);
				if(linoffX>1000){
					lastHeight=lastHeight+linoffY+5;
					linoffX=0;
					linoffY=0;
				}
			}
			IPADef.mBigmapWidth=maxwidth;
			IPADef.mBigmapHeight=lastHeight+linoffY;
		}		
	}
	

	public void update_Srceen() {
		repaint();
	}

}
