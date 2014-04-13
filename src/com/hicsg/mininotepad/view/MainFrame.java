package com.hicsg.mininotepad.view;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.undo.UndoManager;

import com.hicsg.mininotepad.controller.Controller;

/**
 * 软件的界面类，定义了控件以及控件的布局
 * this class is to make a frame to show the application ui
 * @author Eminem
 * @email arjinmc@hotmail.com
 * @date 2012.5.8
 *
 */

public class MainFrame extends JFrame{
 
	private static final long serialVersionUID = 1L;
	//set the default file name as no title
	public String fileName = "无标题";
	private Controller con;
	private JScrollPane sp = new JScrollPane();
	public JTextArea body = new JTextArea();
	//use for paste function
	public JMenuItem pasteItem; //粘贴功能
	//use for mouse right click menu
	private JPopupMenu popMenu = new JPopupMenu();     //右击鼠标弹出的菜单
	//use for auto wrap
	public JMenuItem lineItem = new JMenuItem(); //自动换行选项
	//use for undo function
	public UndoManager undoMgr = new UndoManager(); //撤销管理器
	//use clipboard to temporary storage of information
	public Clipboard clipboard = null; //剪贴板
	
	public void setController(Controller con){
		this.con = con;
	}
	
	//初始化
	//initialization
	public void init(){
		//set application title as file name plus simple notepad
		this.setTitle(fileName+"--简易记事本");
		this.setSize(500,600);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				//overwrite close function
				con.exit(); //重写默认关闭按钮		
			}

		});
		
		//菜单栏
		//menu bar
		JMenuBar mb = new JMenuBar();		
		
		//定义“文件”菜单，包含新建，打开，保存，另存为，退出功能
		//define some functions in file menu
		JMenu fileMenu = new JMenu();
		fileMenu.setText("文件");
		
		//新建
		//new file function
		JMenuItem newItem = new JMenuItem();
		newItem.setText("新建");
		newItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.createFile();
			}
			
		});
		
		//打开
		//open file function
		JMenuItem openItem = new JMenuItem();
		openItem.setText("打开");
		openItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.openFile();
			}
			
		});
		
		//保存
		//save file function
		JMenuItem saveItem = new JMenuItem();
		saveItem.setText("保存");
		//saveItem.setEnabled(false);
		saveItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.saveFile();
			}
			
		});
		
		//另存为
		//save as file function
		JMenuItem saveForItem = new JMenuItem();
		saveForItem.setText("另存为");
		saveForItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.saveForFile();
			}
			
		});
		
		//退出
		//exit function
		JMenuItem exitItem = new JMenuItem();
		exitItem.setText("退出");
		exitItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.exit();
			}
			
		});
		
		fileMenu.add(newItem);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(saveForItem);
		fileMenu.add(exitItem);		
		mb.add(fileMenu);
		
		//定义“编辑”菜单，包含剪切，复制，粘贴，全选，撤销功能
		//define some functions into edit menu
		JMenu editMenu = new JMenu();
		editMenu.setText("编辑");
		
		//剪切
		//cut function
		final JMenuItem cutItem = new JMenuItem();
		cutItem.setText("剪切");
		cutItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.cut();				
			}
			
		});
				
		//复制
		//copy function
		final JMenuItem copyItem = new JMenuItem();
		copyItem.setText("复制");
		copyItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.copy();
			}
			
		});
		
		//粘贴
		//paste function
		pasteItem = new JMenuItem();
		pasteItem.setText("粘贴");
		pasteItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.paste();
			}
			
		});
		
		//全选
		//select all function
		JMenuItem selectAllItem = new JMenuItem();
		selectAllItem.setText("全选");
		selectAllItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.selectAll();
			}
			
		});
		
		//撤销
		//undo function
		JMenuItem rollbackItem = new JMenuItem();
		rollbackItem.setText("撤销");
		rollbackItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.rollback();
			}
			
		});
		
		editMenu.add(rollbackItem);
		editMenu.add(cutItem);
		editMenu.add(copyItem);
		editMenu.add(pasteItem);
		editMenu.add(selectAllItem);		
		editMenu.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(body.getText()==""){
					cutItem.setEnabled(false);
					copyItem.setEnabled(false);
				}else{
					cutItem.setEnabled(true);
					copyItem.setEnabled(true);
				}
				
				if(clipboard.getContents(this)==null){
					pasteItem.setEnabled(false);
				}else{
					pasteItem.setEnabled(true);
				}
			}
			
		});
		mb.add(editMenu);
		
		//格式菜单，具有自动换行可选和字体设置功能
		//define some functions into data format menu
		JMenu formatMenu = new JMenu();
		formatMenu.setText("格式");
		
		//自动换行
		//auto wrap
		lineItem = new JMenuItem();
		lineItem.setText("√自动换行");
		lineItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.lineWrap();
			}
			
		});		
		
		formatMenu.add(lineItem);
		mb.add(formatMenu);
		
		//关于菜单
		//define a menu to show about information
		JMenu aboutMenu = new JMenu();
		aboutMenu.setText("关于");
		JMenuItem aboutItem = new JMenuItem("关于...");		
		aboutItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.about();
			}
			
		});
		aboutMenu.add(aboutItem);
		mb.add(aboutMenu);
		
		//添加菜单栏
		this.setJMenuBar(mb);
		
		//剪切
		final JMenuItem cutItem2 = new JMenuItem();
		cutItem2.setText("剪切");
		cutItem2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				popMenu.setVisible(false);
				con.cut();				
			}
			
		});
				
		//复制
		//copy function for mouse right click menu
		final JMenuItem copyItem2 = new JMenuItem();
		copyItem2.setText("复制");
		copyItem2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				popMenu.setVisible(false);
				con.copy();
			}
			
		});
		
		//粘贴
		//paste function for mouse right click menu
		final JMenuItem pasteItem2 = new JMenuItem();
		pasteItem2.setText("粘贴");
		pasteItem2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				popMenu.setVisible(false);
				con.paste();
			}
			
		});
		
		//全选
		//select all function for mouse right click menu
		JMenuItem selectAllItem2 = new JMenuItem();
		selectAllItem2.setText("全选");
		selectAllItem2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				popMenu.setVisible(false);
				con.selectAll();
			}
			
		});
		
		//撤销
		//undo function for mouse right click menu
		final JMenuItem rollbackItem2 = new JMenuItem();
		rollbackItem2.setText("撤销");
		rollbackItem2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				popMenu.setVisible(false);
				con.rollback();
			}
			
		});
		
		popMenu.add(rollbackItem2);
		popMenu.add(cutItem2);
		popMenu.add(copyItem2);
		popMenu.add(pasteItem2);
		popMenu.add(selectAllItem2);
				
		body.setLineWrap(true);
		body.addKeyListener(new KeyAdapter(){
			//只要按下键盘，文件就是被修改过
			//if the keyboard has been pressed,sign it as the file has been edited
			@Override
			public void keyTyped(KeyEvent e) {
				con.isEdited = true;				
			}
	
		});
		body.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				int getCode =e.getButton();
				//鼠标右击事件
				//the code for mouse right click action
			if(getCode==3){				
					//如果文本区域没有内容，不能剪切和复制
					//if content has nothing,we can't cut or copy
					if(body.getText()==""){
						cutItem2.setEnabled(false);
						copyItem2.setEnabled(false);
					}else{
						cutItem2.setEnabled(true);
						copyItem2.setEnabled(true);
					}
					//如果剪贴板为空，不能粘贴
					//if the clipboard is empty,we can't paste
					if(clipboard.getContents(this)==null){
						pasteItem.setEnabled(false);
						pasteItem2.setEnabled(false);
					}else{
						
						pasteItem.setEnabled(true);
					}
					//显示位置在鼠标所在位置
					//show the popup menu near the mouse focus
					popMenu.setLocation(e.getXOnScreen(),e.getYOnScreen());
					popMenu.setVisible(true);
				}
				else{
					//隐藏
					//hide the popup menu
					popMenu.setVisible(false);
				}
			}
		});
		//添加撤销管理器
		//add undo manager to the frame
		body.getDocument().addUndoableEditListener(undoMgr);
		sp.setViewportView(body);
		this.add(sp);		
		//get the system's clipboard
		clipboard = getToolkit().getSystemClipboard();//获取系统剪贴板
	}
	
	
	public MainFrame(){
		init();
	}

}
