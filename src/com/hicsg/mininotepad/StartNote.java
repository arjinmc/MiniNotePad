package com.hicsg.mininotepad;

import com.hicsg.mininotepad.controller.Controller;
import com.hicsg.mininotepad.view.MainFrame;
/**
 * ∆Ù∂Ø»Ìº˛¿‡
 * This class will run the application main
 * @author Eminem
 * @email arjinmc@hotmail.com
 * @date 2012.5.8
 */

public class StartNote {

	public static void main(String[] args) {
		MainFrame mf = new MainFrame();
		Controller con = new Controller();
		
		mf.setController(con);
		con.setMainFrame(mf);
		
		mf.setVisible(true);
	}

}
