package atouhougame;

import java.awt.Color;
import java.awt.Font;

import atouhougame.gamescreens.BossRushRouter;
import framework.Game;

public class TGlobal {
	public static Game mainMenu;
	public static BossRushRouter bossRushRouter;
	public static LocalEvolutionManager evolutionManager;
	
	public static boolean playNetworked=true;
	
	public static final Color greyBack = new Color(25,25,25);
	public static final Color textTrans = new Color(255,255,255,180);
	public static final Color textSubtleTrans = new Color(255,255,255,80);
	public static final Color textLight = Color.white;
	
	public static final Font fsmall = Font.decode("123123-bold-12");
	public static final Font fmed = Font.decode("123123-bold-20");
	public static final Font fbig = Font.decode("123123-bold-60");
	
	
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
		"Z to shoot, X to bomb, Shift to move slowly, Space to move quickly."
	};
	

}
