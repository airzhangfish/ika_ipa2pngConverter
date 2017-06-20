package com.ikags.ipa2pngeditor;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import com.ikags.ipa2pngeditor.pngs.ConvertHandler;
import com.ikags.util.CommonUtil;


public class Ipa2pngViewer extends JFrame implements ActionListener {

	String mVerion="-Version 1.0.2 in 2014-3-6";
	String titlename = "Ika ipa2Png Converter    "+mVerion;
	// 关于
	private String aboutStr = "Ika ipa2Png Converter\n  " + "Creator by airzhangfish \n " + mVerion+"\n " + " E-mail:52643971@qq.com\n http://www.ikags.com/";

	private static final long serialVersionUID = 1L;
	private JTabbedPane jtp;
	
	private JMenuBar jMenuBar1 = new JMenuBar();
	private JMenu jMenuFile = new JMenu("File");
	private JMenuItem jMenuFileLoadImage = new JMenuItem("Load ipa res folder...");
	private JMenuItem jMenuFileExit = new JMenuItem("Exit");

	
	private JMenu jMenuHelp = new JMenu("Help");
	private JMenuItem jMenuHelpAbout = new JMenuItem("About");
	private JMenuItem jMenuHelpHomepage = new JMenuItem("Homepage");
	BigImagePanel mapPanel = new BigImagePanel();

	public void actionPerformed(ActionEvent actionEvent) {
		Object source = actionEvent.getSource();


		if (source == jMenuFileLoadImage) {
			JFileChooser mJFileChooser=new JFileChooser(".");
			mJFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			mJFileChooser.setDialogTitle(" set IPA unziped png folder");
			int ret=mJFileChooser.showOpenDialog(null);
			if(ret==JFileChooser.APPROVE_OPTION){
				File mfile=mJFileChooser.getSelectedFile();
				loadipaImage(mfile);
			}
		}
		

		// 关于
		if (source == jMenuHelpAbout) {
			JOptionPane.showMessageDialog(this, aboutStr, "About", JOptionPane.INFORMATION_MESSAGE);
		}
		// 打开作者主页
		if (source == jMenuHelpHomepage) {
			CommonUtil.browserURL("http://www.ikags.com");
		}
		// 软件退出
		if (source == jMenuFileExit) {
			System.exit(0);
		}

	}


	
	public void loadipaImage(File file) {
		try {
			new ConvertHandler(file);
			JOptionPane.showMessageDialog(this, "Convert finished ,please check the folder.", "Convert finished", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "load folder error .info :\r\n" + ex.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
			ex.printStackTrace();
		}
	}
	

	public Ipa2pngViewer() {

		this.setSize(400, 300); // 窗体的大小
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true); // 窗体
		this.setTitle(titlename); // 设置标题

		enableInputMethods(true);

		jMenuFile.add(jMenuFileLoadImage);
		jMenuFile.add(jMenuFileExit);
		jMenuFileLoadImage.addActionListener(this);
		jMenuFileExit.addActionListener(this);
		
		
		jMenuHelp.add(jMenuHelpAbout);
		jMenuHelpAbout.addActionListener(this);
		
		jMenuHelp.add(jMenuHelpHomepage);
		jMenuHelpHomepage.addActionListener(this);
		// 总工具栏

		jMenuBar1.add(jMenuFile);
		jMenuBar1.add(jMenuHelp);
		this.setJMenuBar(jMenuBar1);

		Container contents = getContentPane();
		jtp = new JTabbedPane(JTabbedPane.TOP);
		jtp.addTab("IPA2PNG", mapPanel);
		contents.add(jtp);
		setVisible(true);
	}

	public static void main(String args[]) {
		CommonUtil.setMySkin(2);
		new Ipa2pngViewer();
	}
	
	

}
