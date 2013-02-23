package atouhougame.gamescreens;

import atouhougame.TGlobal;
import framework.Game;
import framework.Global;
import framework.Keys;
import framework.Mouse;
import framework.ParagraphText;
import framework.Text;

public class ErrorScreen extends Game{

	public ErrorScreen(String title, String[] minitext) {
		super();
		
		this.bkgColor = TGlobal.greyBack;
	
		
		int height = TGlobal.fbig.getSize()+15;
		this.add(
				new Text(
						title,
						TGlobal.textTrans,TGlobal.fbig,
						25,
						height
				));
		height+=TGlobal.fsmall.getSize()+15;
		this.add(
				new ParagraphText(
						minitext,
						TGlobal.textLight,TGlobal.fsmall,
						40,
						height,
						8
				));
		this.add(new Text(
			"(Press any button to the exit)",
			TGlobal.textTrans,TGlobal.fsmall,
			45,
			Global.height-TGlobal.fsmall.getSize()-16
		));
	}

	
	@Override
	public void update(){
		if(Keys.anyKeyPressed() || Mouse.anyButton()){
			System.exit(0);
		}
		super.update();
	}
	
}
