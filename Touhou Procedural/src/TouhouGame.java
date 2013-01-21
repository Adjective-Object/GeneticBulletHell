import java.awt.Color;
import java.awt.Font;
import java.util.Random;

import framework.*;

public class TouhouGame extends Game{
	
	public Group bossBullets, playerBullets, uiBorder, GUIBombs, particles;
	public Player player;
	public Boss boss;
	public RelativeColorComponent hpBar,bossHpBar, bossMpBar, bossHpBarB, bossMpBarB;
	public BossSeed seed;
	public Text gameOverText, bossClearText, scoreCounter, bossScoreCounter;
	public FlashingText readyStart;
	public Color baseColor;
	
	public int playerScore, bossScore;
	
	//Global variables
	public static Point playFieldLeft = new Point(10,10),
						playFieldRight = new Point(475, 562);
	
	public TouhouGame(BossSeed seed){
		super();
		this.seed=seed;
		this.baseColor = seed.color;
		
		bossBullets = new Group(true);
		playerBullets = new Group(true);
		
		player = new Player(230,500,baseColor);
		player.boundingSmall= new Point(10,10);
		player.boundingLarge= new Point(475,562);
		
		boss=  seed.makeBoss(200,50);
		
		makeUI(baseColor);
		
		particles = new Group(true);
		
		endGameDelay = 6000;
		
		add(boss);
		add(player);
		add(bossBullets);
		add(playerBullets);
		add(particles);
		add(bossHpBarB);
		add(bossHpBar);
		add(bossMpBarB);
		add(bossMpBar);
		add(uiBorder);
		add(gameOverText);
		add(bossClearText);
		add(bossScoreCounter);
		add(scoreCounter);
		add(readyStart);
		add(hpBar);
		add(GUIBombs);
		
		this.bkgColor = new Color(baseColor.getRed()+50,baseColor.getGreen()+50,baseColor.getBlue()+50);
	}
	
	public void update(){
		//System.out.println(particles.size());
		bossHpBar.scale.x=(float)boss.HP/boss.maxHP;
		bossMpBar.scale.x=(float)boss.MP/boss.maxMP;
		hpBar.scale.x=player.hp/player.maxHp;

		scoreCounter.text=Integer.toString(playerScore);
		bossScoreCounter.text=Integer.toString(bossScore);
		
		if(boss.alive && boss.visible && boss.HP<=0){ boss.kill(); }
		if(player.alive && player.visible && player.hp<=0){ player.kill(); }
		
		for(int i=0; i<GUIBombs.content.size();i++){
			GUIBombs.content.get(i).visible=player.mana>=(i+1);
		}
		
		bossBullets.content.addAll(boss.getBullets(true));
		playerBullets.content.addAll(player.getBullets(true));
		
		if(player.responsive && player.alive){
			//checks for collisions between player and boss's bullets, creates explosion if detected
			for(int i=0; i<bossBullets.content.size();i++){
				if(bossBullets.content.get(i).collide(player)){
					Bullet bullet = (Bullet)(bossBullets.content.get(i));
					player.hp-= bullet.power;
					bossBullets.content.remove(i);
					particles.addAll(Global.createSimpleExplosion(bullet));
					i--;
				}
			}
		}
		
		
		//checks for collisions between boss and player's bullets
		if(boss.alive){
			for(int i=0; i<playerBullets.content.size();i++){
				if(playerBullets.content.get(i).collide(boss)){
					Bullet bullet = (Bullet)(playerBullets.content.get(i));
					boss.HP-= bullet.power;
					playerBullets.content.remove(i);
					particles.addAll(Global.createSimpleExplosion(bullet));
					i--;
				}
			}
		}
		
		if(!readyStart.alive && boss.alive && player.alive){
			boss.active=true;
			player.responsive=true;
			player.canshoot=true;
		}
		
		super.update();
		if (player.alive && !boss.alive){
			//TODO sending player's winstate
		}
		else if (!player.alive && boss.alive){
			gameOverText.visible=true;
		}
	}
	
	public void setGroupRelativeTo(Group g, Color c){
		for(int i=0; i<g.content.size(); i++){
			RelativeColorComponent p = (RelativeColorComponent)(g.content.get(i));
			p.setRelativeTo(c);
		}
	}
	
	public void makeUI(Color baseColor){
		gameOverText = new Text("GAME OVER",new Color(baseColor.getRed()+75,baseColor.getGreen()+75,baseColor.getBlue()+75),
				Font.decode("safasdfasdw-bold-60"),Global.width/2-250,Global.height/2-30,450,60);
		gameOverText.visible=false;
		
		bossClearText = new Text("BOSS CLEAR",new Color(baseColor.getRed()+75,baseColor.getGreen()+75,baseColor.getBlue()+75),
				Font.decode("safasdfasdw-bold-60"),Global.width/2-250,Global.height/2-30,450,60);
		bossClearText.visible=false;
		
		readyStart = new FlashingText(new String[] {" ","   3"," ","   2"," ","   1"," ","START!"},
				new Color(baseColor.getRed()+75,baseColor.getGreen()+75,baseColor.getBlue()+75),
				Font.decode("safasdfasdw-bold-60"),Global.width/2-250,Global.height/2-30,450,60,750);
		readyStart.visible=true;
		
		hpBar = new RelativeColorComponent(500,75, 269,10,baseColor,60,60,60);
		bossHpBar = new RelativeColorComponent((int)playFieldLeft.x+10,
				(int)playFieldLeft.y+10,(int)playFieldRight.x-30,10,baseColor,70,70,70);		
		bossHpBarB = new RelativeColorComponent((int)playFieldLeft.x+10,
				(int)playFieldLeft.y+10,(int)playFieldRight.x-30,10,baseColor,20,20,20);
		bossMpBar = new RelativeColorComponent((int)playFieldLeft.x+10,
				(int)playFieldLeft.y+22,(int)playFieldRight.x-30,10,baseColor,60,60,75);
		bossMpBarB = new RelativeColorComponent((int)playFieldLeft.x+10,
				(int)playFieldLeft.y+22,(int)playFieldRight.x-30,10,baseColor,20,20,20);
		
		GUIBombs=new Group();
		for(int i=0; i<player.maxMana;i++){
			GUIBombs.add( new RelativeColorComponent((int) (500+9+(250-10)/(player.maxMana-1)*i),120,
					10,10,baseColor,50,50,50));
		}
		
		uiBorder = new Group();//uiborders 10,10,465,552
		
		uiBorder.add(new RelativeColorComponent(0,0,10,Global.height,baseColor,0,0,0));
		uiBorder.add(new RelativeColorComponent(10,0,Global.width-10,10,baseColor,0,0,0));
		uiBorder.add(new RelativeColorComponent(10,Global.height-10,Global.width-10,10,baseColor,0,0,0));
		uiBorder.add(new RelativeColorComponent(475,10,Global.width-475,Global.height-20,baseColor,0,0,0));
		
		scoreCounter=new Text("0",new Color(baseColor.getRed()+60,baseColor.getGreen()+60,baseColor.getBlue()+60),
				Font.decode("safasdfasdw-bold-20"),(int)playFieldRight.x+10,300,Global.width-((int)(playFieldRight.x+10)),20);
		bossScoreCounter=new Text("0",new Color(baseColor.getRed()+40,baseColor.getGreen()+40,baseColor.getBlue()+40),
				Font.decode("safasdfasdw-bold-20"),(int)playFieldRight.x+10,370,Global.width-((int)(playFieldRight.x+10)),20);
		
	}
}
