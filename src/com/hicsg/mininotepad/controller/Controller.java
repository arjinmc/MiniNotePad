package com.hicsg.mininotepad.controller;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.sound.sampled.AudioFormat;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.hicsg.mininotepad.view.MainFrame;

/**
 * �����������趨�˹��ܷ���
 * ���ļ��������ļ������Ϊ
 * �༭�ļ������У����ƣ�ճ����ȫѡ��������
 * ���Ƹ�ʽ
 * 
 * This class is a controller
 * include some methods like open file,save file,save as..
 * edit file with cut,copy,paste,select all,undo functions
 * 
 * @author Eminem
 * @email arjinmc@hotmail.com
 * @date 2012.5.8
 */

public class Controller {

	private MainFrame mf;
	//this sign for if the file has been edited
	public boolean isEdited = false;//�Ƿ��Ѿ����޸Ĺ�
	//file path which now is been reading 
	public File txt = null;//��д�ļ�·��
	//file chooser dialog
	public static JFileChooser file = new JFileChooser();;//�ļ��Ի���
	
	public  void setMainFrame(MainFrame mf){
		this.mf = mf;
	}
	//�½��ļ�
	//create new file
	public void createFile(){
		if(!isEdited){
			mf.body.setText("");
		}else{
			//��ȡ�û��������Ϣ
			//get info for what has the user choosen
			//ask for if the file has not been saved,just to save it then create a new one
			int choose = JOptionPane.showConfirmDialog(null, "����û�б��棬" +
					"�Ƿ񱣴����½���");
			//if choose yes,save the file then create a new one
			if(choose==JOptionPane.YES_OPTION){ //�����
				saveFile();
				mf.body.setText("");
			//if choose no,not to save the file but create a new one
			}else if(choose==JOptionPane.NO_OPTION){//�����
				mf.body.setText("");
			}
		}
		
		//���±���
		//upadate the application for title
		mf.fileName="�ޱ���";
		updateTitle();
		isEdited = false;
		
	}
	//���ļ�
	//open file 
	public void openFile(){
		if(!isEdited){
			executeOpen();
		}else{
			//��ȡ�û��������Ϣ
			//ask for if the file has not been saved,just to saved it then to open a file
			int choose = JOptionPane.showConfirmDialog(null, "����û�б��棬" +
					"�Ƿ񱣴��ٴ������ļ���");
			//if choose yes,save the file then onpen a new one
			if(choose==JOptionPane.YES_OPTION){ //�����
				saveFile();
				isEdited = false;
				executeOpen();
			//if choose no,donnot save the file but open a new one
			}else if(choose==JOptionPane.NO_OPTION){//�����
				executeOpen();				
			}
		}
		
	}
	//ִ�ж�ȡ�ļ�
	//this is a method to execute to open a file
	public void executeOpen(){
		//���ļ��Ի���
		//show open file dialog
		int value = file.showOpenDialog(mf);
		//�ж��Ƿ����˴�
		//judge if user choose open option
		if(value == JFileChooser.APPROVE_OPTION){
			//��ȡ�û���ѡ���ļ�
			//get which file has been choosen
			txt = file.getSelectedFile();
			//�ж��ļ��Ƿ����,����������ȡ�ļ�
			//judge the file if it's  existed
			if(txt.exists()){
				//��ԭ�����������
				//clear the mainframe body then read the file show it in the body
				mf.body.setText("");
				mf.fileName = txt.getName();
				readFile();
			//if the file is not existed
			}else{//����ļ�������
				//show the dialog to say the file is not existed
				JOptionPane.showMessageDialog(null, "��ѡ����ļ������ڣ�������ȷ��");
				file.showOpenDialog(mf);
			}
		}		
	}
	
	//��ȡ�ļ�
	//read the file with code utf-8
	public void readFile(){
		//���ж�ȡ		
		BufferedReader br;
		try {
			FileInputStream fs = new FileInputStream(txt);
			InputStreamReader inReader = new InputStreamReader(fs,"UTF-8");
			br = new BufferedReader(inReader);
			String str;
			while((str=br.readLine())!=null){
				mf.body.append(str+"\n");
			}
			br.close();
			//update the title for application frame
			updateTitle();//���±���
			isEdited = false;							
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 				
	}
	
	//�����ļ�
	//save the file
	public void saveFile(){

		//�ж�ѡ����ļ��Ƿ����
		//judge the file has been choosen is existed or not
		//if the file is existed
		if(txt!=null){ //������� 
			writeFile();
			isEdited = false;
		//if the file is not existed,save it as a new one
		}else{ //�ļ�·Ϊ�������û�Ҫ�½���
			saveForFile();
		}
	}
	
	//���Ϊ�ļ�
	//save as method
	public void saveForFile(){
		int value = file.showSaveDialog(mf);
		if(value == JFileChooser.APPROVE_OPTION){
			txt = file.getSelectedFile();
			mf.fileName = txt.getName();
			//judge the file if it's existed
			//�ж��ļ��Ƿ��Ѿ�����
			if(txt.exists()){
				//show the dialog to ask when the file is existed then overcover it.
				if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(mf, 
						"�����õ��ļ��Ѿ����ڣ��Ƿ�Ҫ��������")){
					writeFile();
					isEdited = false;
					//�ٰѸ��ļ����ؽ���
					//read the file again
					readFile();
				}
			}else{
				writeFile();
				isEdited = false;
			}
			
			updateTitle();//���±���
			
		}		
	}
		
	//д���ļ�
	//write the new things to a new file with code utf-8
	public void writeFile(){
		
		PrintWriter pw;
		try {
			FileOutputStream fos = new FileOutputStream(txt);
			OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
			pw = new PrintWriter(osw);
			pw.println(mf.body.getText());
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//����
	//cut function
	public void cut(){
		copy();
		//��ǿ�ʼλ��
		//sign the start position
		int start = mf.body.getSelectionStart();
		//��ǽ���λ��
		//sign the end position
		int end = mf.body.getSelectionEnd();
		//ɾ����ѡ��
		//delete the content from start position to end position
		mf.body.replaceRange("", start, end);
		
	}
	
	//����
	//copy method
	public void copy(){
		//�϶�ѡȡ�ı�
		//temp string to save the content has been selected by using the mouse
		String temp = mf.body.getSelectedText();
		//�ѻ�ȡ�����ݸ��Ƶ������ַ����������̳��˼�����ӿ�
		//put temp string to StringSelection object which is a class implements clipboard
		StringSelection text = new StringSelection(temp);
		//�����ݷ��ڼ�����
		//put the StringSelection object to clipboard
		mf.clipboard.setContents(text, null);
	}
	
	//ճ��
	//paste method
	public void paste(){
		
		//Transferable���Ѽ����������ת��������
		//use Transferable object to make the content of clipboard into data
		Transferable contents = mf.clipboard.getContents(this);
		//DataFalvor
		DataFlavor flavor = DataFlavor.stringFlavor;
		//�������ת��
		//if can be converted
		if(contents.isDataFlavorSupported(flavor)){
			String str;
			try {//��ʼת��
				//start to convert
				str=(String)contents.getTransferData(flavor);
				//���Ҫճ��ʱ������Ѿ�ѡ����һЩ�ַ�
				//if paste when the mouse has selected some string things
				if(mf.body.getSelectedText()!=null){ 
					//��λ��ѡ���ַ��Ŀ�ʼλ��
					//sign the selecting start position
					int start = mf.body.getSelectionStart();
					//��λ��ѡ���ַ���ĩβλ��
					//sign the selecting end position
					int end = mf.body.getSelectionEnd();
					//��ճ���������滻�ɱ�ѡ�е�����
					//paste the real string things between the selecting start and end position
					mf.body.replaceRange(str, start, end);

				}else{
					//��ȡ�������TextArea��λ��
					//get the mouse focus position in TextArea
					int mouse = mf.body.getCaretPosition();
					//��������ڵ�λ��ճ������
					//paste the real things to the position
					mf.body.insert(str, mouse);
				}
				
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch(IllegalArgumentException e){
				e.printStackTrace();
			}
		}
		
		
	}
	
	//ȫѡ
	//select all method
	public void selectAll(){
		//ѡ��ȫ������
		//select all content
		mf.body.selectAll();
	}
	
	//����
	//undo method
	public void rollback(){
		if(mf.undoMgr.isSignificant()){
			mf.undoMgr.undo();
		}
	}
	
	//�Զ�����
	//auto wrap
	public void lineWrap(){
		//not use auto wrap
		if(mf.body.getLineWrap()){
			mf.body.setLineWrap(false);
			mf.lineItem.setText("�Զ�����");
		//use auto wrap
		}else{
			mf.body.setLineWrap(true);
			mf.lineItem.setText("���Զ�����");
		}		
	}
	
	//����
	//about
	public void about(){
		//show the dialog for introduce the copyright of this appliaction
		JOptionPane.showMessageDialog(null,"���׼��±� " +
				"Copyright2012 ����Sway�����ҡ�email��arjinmc@hotmail.com");
	}
	
	//�˳�
	//exit method
	public void exit(){
		if(!isEdited){
			System.exit(0);
		}else{
			//��ȡ�û��������Ϣ
			//if the file has not been save,ask for it then exit
			int choose = JOptionPane.showConfirmDialog(null, "����û�б��棬" +
					"�Ƿ񱣴����˳���");
			//if choose yes,save the file then exit
			if(choose==JOptionPane.YES_OPTION){ //�����
				saveFile();
				System.exit(0);
			//if choose no,just exit
			}else if(choose==JOptionPane.NO_OPTION){//�����
				System.exit(0);
			}
		}
	}
	
	//���±���
	//update the title for application mainframe
	public void updateTitle(){
		//like simple notepad
		mf.setTitle(mf.fileName+"--���׼��±�");
	}
	
	//�ڹ��캯��������file��Ĭ��ѡ��
	//make the default saving file path to desktop
	public Controller(){
		File path = new File(file.getCurrentDirectory().getParent()+"\\Desktop\\");
		file.setCurrentDirectory(path);
	}
}
