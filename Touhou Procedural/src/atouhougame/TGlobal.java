package atouhougame;

import java.awt.Color;
import java.awt.Font;

import framework.Game;
import framework.RecycleableClip;

public class TGlobal {
	public static Game mainMenu, favoritesScreen;
	public static EvolutionManager evolutionManager;
	
	public static boolean playNetworked=true;
	
	public static final Color greyBack = new Color(25,25,25);
	public static final Color textTrans = new Color(255,255,255,180);
	public static final Color textSubtleTrans = new Color(255,255,255,80);
	public static final Color textLight = Color.white;
	
	public static final Font fsmall = Font.decode("123123-bold-12");
	public static final Font fmed = Font.decode("123123-bold-20");
	public static final Font fbig = Font.decode("123123-bold-60");
	
	public static RecycleableClip sound_menu_move 		= new RecycleableClip(ClassLoader.getSystemClassLoader().getResourceAsStream("menu_move.wav")) ;
	public static RecycleableClip sound_menu_select 	= new RecycleableClip(ClassLoader.getSystemClassLoader().getResourceAsStream("menu_select.wav")) ;
	public static RecycleableClip sound_menu_escape 	= new RecycleableClip(ClassLoader.getSystemClassLoader().getResourceAsStream("menu_escape.wav")) ;
	
	public static RecycleableClip sound_fire_boss  		= new RecycleableClip(ClassLoader.getSystemClassLoader().getResourceAsStream("menu_move.wav"),100) ;
	public static RecycleableClip sound_fire_player  	= new RecycleableClip(ClassLoader.getSystemClassLoader().getResourceAsStream("menu_move.wav"),100) ;
	
	public static RecycleableClip sound_explode_player  = new RecycleableClip(ClassLoader.getSystemClassLoader().getResourceAsStream("se_enep01.wav")) ;
	public static RecycleableClip sound_explode_boss  	= new RecycleableClip(ClassLoader.getSystemClassLoader().getResourceAsStream("se_enep00.wav")) ;
	
	public static RecycleableClip sound_damage_player 	= new RecycleableClip(ClassLoader.getSystemClassLoader().getResourceAsStream("damage_0.wav"),100) ;
	public static RecycleableClip sound_damage_boss 	= new RecycleableClip(ClassLoader.getSystemClassLoader().getResourceAsStream("damage_1.wav"),100) ;
	
	public static RecycleableClip sound_Laser_warn 		= new RecycleableClip(ClassLoader.getSystemClassLoader().getResourceAsStream("laser_fire_alt.wav")) ;
	public static RecycleableClip sound_Laser_fire 		= new RecycleableClip(ClassLoader.getSystemClassLoader().getResourceAsStream("laser_fire.wav")) ;
	
	public static final String aboutScreenTitle = "This is an Experiment.";
	public static final String[] aboutScreenText = 
		new String[] {
		"This is a different take on the Bullet Hell genre.",
		"When you play this game, you won't be following the paths of any spaceships or magical girls.",
		"There are no levels, no monsters, no highscores (at least not for you).",
		"",
		"Just bosses.",
		"...And bosses...",
		"...And bosses.",
		"","",
		"This is an experiment in evolving boss patterns. As you play, the bosses will adapt.",
		"Those that survive the best will reproduce, until optimal solutions are reached.",
		"",
		"Or at least that's the hope.",
		"",
		"Z to shoot, X to bomb, Shift to move slowly, Space to move quickly.",
		"",
		"",
		"Concept and Programming by Maxwell Huang-Hobbs",
		"Design help by Griffin Brodman"
	};
	

}
