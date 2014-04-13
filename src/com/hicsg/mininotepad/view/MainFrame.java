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
 * ����Ľ����࣬�����˿ؼ��Լ��ؼ��Ĳ���
 * this class is to make a frame to show the application ui
 * @author Eminem
 * @email arjinmc@hotmail.com
 * @date 2012.5.8
 *
 */

public class MainFrame extends JFrame{
 
	private static final long serialVersionUID = 1L;
	//set the default file name as no title
	public String fileName = "�ޱ���";
	private Controller con;
	private JScrollPane sp = new JScrollPane();
	public JTextArea body = new JTextArea();
	//use for paste function
	public JMenuItem pasteItem; //ճ������
	//use for mouse right click menu
	private JPopupMenu popMenu = new JPopupMenu();     //�һ���굯���Ĳ˵�
	//use for auto wrap
	public JMenuItem lineItem = new JMenuItem(); //�Զ�����ѡ��
	//use for undo function
	public UndoManager undoMgr = new UndoManager(); //����������
	//use clipboard to temporary storage of information
	public Clipboard clipboard = null; //������
	
	public void setController(Controller con){
		this.con = con;
	}
	
	//��ʼ��
	//initialization
	public void init(){
		//set application title as file name plus simple notepad
		this.setTitle(fileName+"--���׼��±�");
		this.setSize(500,600);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				//overwrite close function
				con.exit(); //��дĬ�Ϲرհ�ť		
			}

		});
		
		//�˵���
		//menu bar
		JMenuBar mb = new JMenuBar();		
		
		//���塰�ļ����˵��������½����򿪣����棬���Ϊ���˳�����
		//define some functions in file menu
		JMenu fileMenu = new JMenu();
		fileMenu.setText("�ļ�");
		
		//�½�
		//new file function
		JMenuItem newItem = new JMenuItem();
		newItem.setText("�½�");
		newItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.createFile();
			}
			
		});
		
		//��
		//open file function
		JMenuItem openItem = new JMenuItem();
		openItem.setText("��");
		openItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.openFile();
			}
			
		});
		
		//����
		//save file function
		JMenuItem saveItem = new JMenuItem();
		saveItem.setText("����");
		//saveItem.setEnabled(false);
		saveItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.saveFile();
			}
			
		});
		
		//���Ϊ
		//save as file function
		JMenuItem saveForItem = new JMenuItem();
		saveForItem.setText("���Ϊ");
		saveForItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.saveForFile();
			}
			
		});
		
		//�˳�
		//exit function
		JMenuItem exitItem = new JMenuItem();
		exitItem.setText("�˳�");
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
		
		//���塰�༭���˵����������У����ƣ�ճ����ȫѡ����������
		//define some functions into edit menu
		JMenu editMenu = new JMenu();
		editMenu.setText("�༭");
		
		//����
		//cut function
		final JMenuItem cutItem = new JMenuItem();
		cutItem.setText("����");
		cutItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.cut();				
			}
			
		});
				
		//����
		//copy function
		final JMenuItem copyItem = new JMenuItem();
		copyItem.setText("����");
		copyItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.copy();
			}
			
		});
		
		//ճ��
		//paste function
		pasteItem = new JMenuItem();
		pasteItem.setText("ճ��");
		pasteItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.paste();
			}
			
		});
		
		//ȫѡ
		//select all function
		JMenuItem selectAllItem = new JMenuItem();
		selectAllItem.setText("ȫѡ");
		selectAllItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.selectAll();
			}
			
		});
		
		//����
		//undo function
		JMenuItem rollbackItem = new JMenuItem();
		rollbackItem.setText("����");
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
		
		//��ʽ�˵��������Զ����п�ѡ���������ù���
		//define some functions into data format menu
		JMenu formatMenu = new JMenu();
		formatMenu.setText("��ʽ");
		
		//�Զ�����
		//auto wrap
		lineItem = new JMenuItem();
		lineItem.setText("���Զ�����");
		lineItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.lineWrap();
			}
			
		});		
		
		formatMenu.add(lineItem);
		mb.add(formatMenu);
		
		//���ڲ˵�
		//define a menu to show about information
		JMenu aboutMenu = new JMenu();
		aboutMenu.setText("����");
		JMenuItem aboutItem = new JMenuItem("����...");		
		aboutItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				con.about();
			}
			
		});
		aboutMenu.add(aboutItem);
		mb.add(aboutMenu);
		
		//��Ӳ˵���
		this.setJMenuBar(mb);
		
		//����
		final JMenuItem cutItem2 = new JMenuItem();
		cutItem2.setText("����");
		cutItem2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				popMenu.setVisible(false);
				con.cut();				
			}
			
		});
				
		//����
		//copy function for mouse right click menu
		final JMenuItem copyItem2 = new JMenuItem();
		copyItem2.setText("����");
		copyItem2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				popMenu.setVisible(false);
				con.copy();
			}
			
		});
		
		//ճ��
		//paste function for mouse right click menu
		final JMenuItem pasteItem2 = new JMenuItem();
		pasteItem2.setText("ճ��");
		pasteItem2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				popMenu.setVisible(false);
				con.paste();
			}
			
		});
		
		//ȫѡ
		//select all function for mouse right click menu
		JMenuItem selectAllItem2 = new JMenuItem();
		selectAllItem2.setText("ȫѡ");
		selectAllItem2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				popMenu.setVisible(false);
				con.selectAll();
			}
			
		});
		
		//����
		//undo function for mouse right click menu
		final JMenuItem rollbackItem2 = new JMenuItem();
		rollbackItem2.setText("����");
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
			//ֻҪ���¼��̣��ļ����Ǳ��޸Ĺ�
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
				//����һ��¼�
				//the code for mouse right click action
			if(getCode==3){				
					//����ı�����û�����ݣ����ܼ��к͸���
					//if content has nothing,we can't cut or copy
					if(body.getText()==""){
						cutItem2.setEnabled(false);
						copyItem2.setEnabled(false);
					}else{
						cutItem2.setEnabled(true);
						copyItem2.setEnabled(true);
					}
					//���������Ϊ�գ�����ճ��
					//if the clipboard is empty,we can't paste
					if(clipboard.getContents(this)==null){
						pasteItem.setEnabled(false);
						pasteItem2.setEnabled(false);
					}else{
						
						pasteItem.setEnabled(true);
					}
					//��ʾλ�����������λ��
					//show the popup menu near the mouse focus
					popMenu.setLocation(e.getXOnScreen(),e.getYOnScreen());
					popMenu.setVisible(true);
				}
				else{
					//����
					//hide the popup menu
					popMenu.setVisible(false);
				}
			}
		});
		//��ӳ���������
		//add undo manager to the frame
		body.getDocument().addUndoableEditListener(undoMgr);
		sp.setViewportView(body);
		this.add(sp);		
		//get the system's clipboard
		clipboard = getToolkit().getSystemClipboard();//��ȡϵͳ������
	}
	
	
	public MainFrame(){
		init();
	}

}
