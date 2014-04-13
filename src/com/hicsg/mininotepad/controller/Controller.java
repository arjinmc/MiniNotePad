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
 * 控制器，所设定了功能方法
 * 打开文件，保存文件，另存为
 * 编辑文件（剪切，复制，粘贴，全选，撤销）
 * 控制格式
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
	public boolean isEdited = false;//是否已经被修改过
	//file path which now is been reading 
	public File txt = null;//读写文件路径
	//file chooser dialog
	public static JFileChooser file = new JFileChooser();;//文件对话框
	
	public  void setMainFrame(MainFrame mf){
		this.mf = mf;
	}
	//新建文件
	//create new file
	public void createFile(){
		if(!isEdited){
			mf.body.setText("");
		}else{
			//获取用户点击的信息
			//get info for what has the user choosen
			//ask for if the file has not been saved,just to save it then create a new one
			int choose = JOptionPane.showConfirmDialog(null, "您还没有保存，" +
					"是否保存再新建？");
			//if choose yes,save the file then create a new one
			if(choose==JOptionPane.YES_OPTION){ //点击是
				saveFile();
				mf.body.setText("");
			//if choose no,not to save the file but create a new one
			}else if(choose==JOptionPane.NO_OPTION){//点击否
				mf.body.setText("");
			}
		}
		
		//更新标题
		//upadate the application for title
		mf.fileName="无标题";
		updateTitle();
		isEdited = false;
		
	}
	//打开文件
	//open file 
	public void openFile(){
		if(!isEdited){
			executeOpen();
		}else{
			//获取用户点击的信息
			//ask for if the file has not been saved,just to saved it then to open a file
			int choose = JOptionPane.showConfirmDialog(null, "您还没有保存，" +
					"是否保存再打开其他文件？");
			//if choose yes,save the file then onpen a new one
			if(choose==JOptionPane.YES_OPTION){ //点击是
				saveFile();
				isEdited = false;
				executeOpen();
			//if choose no,donnot save the file but open a new one
			}else if(choose==JOptionPane.NO_OPTION){//点击否
				executeOpen();				
			}
		}
		
	}
	//执行读取文件
	//this is a method to execute to open a file
	public void executeOpen(){
		//打开文件对话框
		//show open file dialog
		int value = file.showOpenDialog(mf);
		//判断是否点击了打开
		//judge if user choose open option
		if(value == JFileChooser.APPROVE_OPTION){
			//获取用户所选的文件
			//get which file has been choosen
			txt = file.getSelectedFile();
			//判断文件是否存在,如果存在则读取文件
			//judge the file if it's  existed
			if(txt.exists()){
				//把原来的内容清空
				//clear the mainframe body then read the file show it in the body
				mf.body.setText("");
				mf.fileName = txt.getName();
				readFile();
			//if the file is not existed
			}else{//如果文件不存在
				//show the dialog to say the file is not existed
				JOptionPane.showMessageDialog(null, "您选择的文件不存在，请检查正确！");
				file.showOpenDialog(mf);
			}
		}		
	}
	
	//读取文件
	//read the file with code utf-8
	public void readFile(){
		//逐行读取		
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
			updateTitle();//更新标题
			isEdited = false;							
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 				
	}
	
	//保存文件
	//save the file
	public void saveFile(){

		//判断选择的文件是否存在
		//judge the file has been choosen is existed or not
		//if the file is existed
		if(txt!=null){ //如果存在 
			writeFile();
			isEdited = false;
		//if the file is not existed,save it as a new one
		}else{ //文件路为空则是用户要新建的
			saveForFile();
		}
	}
	
	//另存为文件
	//save as method
	public void saveForFile(){
		int value = file.showSaveDialog(mf);
		if(value == JFileChooser.APPROVE_OPTION){
			txt = file.getSelectedFile();
			mf.fileName = txt.getName();
			//judge the file if it's existed
			//判断文件是否已经存在
			if(txt.exists()){
				//show the dialog to ask when the file is existed then overcover it.
				if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(mf, 
						"您设置的文件已经存在，是否要覆盖它？")){
					writeFile();
					isEdited = false;
					//再把该文件加载进来
					//read the file again
					readFile();
				}
			}else{
				writeFile();
				isEdited = false;
			}
			
			updateTitle();//更新标题
			
		}		
	}
		
	//写入文件
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
	
	//剪切
	//cut function
	public void cut(){
		copy();
		//标记开始位置
		//sign the start position
		int start = mf.body.getSelectionStart();
		//标记结束位置
		//sign the end position
		int end = mf.body.getSelectionEnd();
		//删除所选段
		//delete the content from start position to end position
		mf.body.replaceRange("", start, end);
		
	}
	
	//复制
	//copy method
	public void copy(){
		//拖动选取文本
		//temp string to save the content has been selected by using the mouse
		String temp = mf.body.getSelectedText();
		//把获取的内容复制到连续字符器，这个类继承了剪贴板接口
		//put temp string to StringSelection object which is a class implements clipboard
		StringSelection text = new StringSelection(temp);
		//把内容放在剪贴板
		//put the StringSelection object to clipboard
		mf.clipboard.setContents(text, null);
	}
	
	//粘贴
	//paste method
	public void paste(){
		
		//Transferable，把剪贴板的内容转换成数据
		//use Transferable object to make the content of clipboard into data
		Transferable contents = mf.clipboard.getContents(this);
		//DataFalvor
		DataFlavor flavor = DataFlavor.stringFlavor;
		//如果可以转换
		//if can be converted
		if(contents.isDataFlavorSupported(flavor)){
			String str;
			try {//开始转换
				//start to convert
				str=(String)contents.getTransferData(flavor);
				//如果要粘贴时，鼠标已经选中了一些字符
				//if paste when the mouse has selected some string things
				if(mf.body.getSelectedText()!=null){ 
					//定位被选中字符的开始位置
					//sign the selecting start position
					int start = mf.body.getSelectionStart();
					//定位被选中字符的末尾位置
					//sign the selecting end position
					int end = mf.body.getSelectionEnd();
					//把粘贴的内容替换成被选中的内容
					//paste the real string things between the selecting start and end position
					mf.body.replaceRange(str, start, end);

				}else{
					//获取鼠标所在TextArea的位置
					//get the mouse focus position in TextArea
					int mouse = mf.body.getCaretPosition();
					//在鼠标所在的位置粘贴内容
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
	
	//全选
	//select all method
	public void selectAll(){
		//选中全部内容
		//select all content
		mf.body.selectAll();
	}
	
	//撤销
	//undo method
	public void rollback(){
		if(mf.undoMgr.isSignificant()){
			mf.undoMgr.undo();
		}
	}
	
	//自动换行
	//auto wrap
	public void lineWrap(){
		//not use auto wrap
		if(mf.body.getLineWrap()){
			mf.body.setLineWrap(false);
			mf.lineItem.setText("自动换行");
		//use auto wrap
		}else{
			mf.body.setLineWrap(true);
			mf.lineItem.setText("√自动换行");
		}		
	}
	
	//关于
	//about
	public void about(){
		//show the dialog for introduce the copyright of this appliaction
		JOptionPane.showMessageDialog(null,"简易记事本 " +
				"Copyright2012 创意Sway工作室。email：arjinmc@hotmail.com");
	}
	
	//退出
	//exit method
	public void exit(){
		if(!isEdited){
			System.exit(0);
		}else{
			//获取用户点击的信息
			//if the file has not been save,ask for it then exit
			int choose = JOptionPane.showConfirmDialog(null, "您还没有保存，" +
					"是否保存再退出？");
			//if choose yes,save the file then exit
			if(choose==JOptionPane.YES_OPTION){ //点击是
				saveFile();
				System.exit(0);
			//if choose no,just exit
			}else if(choose==JOptionPane.NO_OPTION){//点击否
				System.exit(0);
			}
		}
	}
	
	//更新标题
	//update the title for application mainframe
	public void updateTitle(){
		//like simple notepad
		mf.setTitle(mf.fileName+"--简易记事本");
	}
	
	//在构造函数中设置file的默认选项
	//make the default saving file path to desktop
	public Controller(){
		File path = new File(file.getCurrentDirectory().getParent()+"\\Desktop\\");
		file.setCurrentDirectory(path);
	}
}
