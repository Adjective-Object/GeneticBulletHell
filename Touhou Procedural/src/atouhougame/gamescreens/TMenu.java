package atouhougame.gamescreens;

import java.awt.Color;
import java.awt.Font;

import atouhougame.TGlobal;
import framework.Game;
import framework.Menu;

public class TMenu extends Menu{

	public TMenu(String[] items, Game[] sendTo, Color bkgColor, Font font) {
		super(items, sendTo, bkgColor, font);
	}

	@Override
	protected void onMove(){
		TGlobal.sound_menu_move.play();
	}
	
	@Override
	protected void onSelect(){
		TGlobal.sound_menu_select.play();
	}
	
}
