package atouhougame.gamescreens;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import atouhougame.Boss;
import atouhougame.BossSeed;
import atouhougame.Bullet;
import atouhougame.EvolutionManager;
import atouhougame.Player;
import atouhougame.TGlobal;
import framework.FlashingText;
import framework.Game;
import framework.GameComponent;
import framework.Global;
import framework.Group;
import framework.Keys;
import framework.Point;
import framework.RelativeColorComponent;
import framework.SwitchGameEvent;
import framework.Text;

public class TouhouGame extends Game{
	
	public Group<Bullet> bossBullets;
	public Group<GameComponent> playerBullets, uiBorder, GUIBombs, particles;
	public Player player;
	public Boss boss;
	public RelativeColorComponent hpBar,bossHpBar, bossMpBar, bossHpBarB, bossMpBarB;
	public BossSeed seed;
	public Text gameOverText, bossClearText, bossScoreCounter;
	public FlashingText readyStart;
	public Color baseColor;
	
	boolean gameRunning = true;
	
	public double bossScore;
	public EvolutionManager evolutionManager;
	
	int endGameDelay = 6000;
	
	//Global variables
	public static Point playFieldLeft = new Point(10,10),
						playFieldRight = new Point(475, 562);
	
	public TouhouGame(EvolutionManager evolutionManager){
		super();
		this.evolutionManager=evolutionManager;
		this.seed=evolutionManager.currentSeed();
		this.baseColor = this.seed.color;
		
		this.bossBullets = new Group<Bullet>(true);
		this.playerBullets = new Group<GameComponent>(true);
		this.particles = new Group<GameComponent>(true);

		this.boss = new Boss(200,50,this.seed);
		
		this.player = new Player(230,500,this.baseColor);
		this.player.boundingSmall= new Point(10,10);
		this.player.boundingLarge= new Point(475,562);
		
		this.makeUI(this.baseColor);

		
		this.add(this.bossBullets);
		this.add(this.boss);
		this.add(this.player);
		this.add(this.playerBullets);
		this.add(this.particles);
		this.add(this.bossHpBarB);
		this.add(this.bossHpBar);
		this.add(this.bossMpBarB);
		this.add(this.bossMpBar);
		this.add(this.uiBorder);
		this.add(this.gameOverText);
		this.add(this.bossClearText);
		this.add(this.bossScoreCounter);
		this.add(this.readyStart);
		this.add(this.hpBar);
		this.add(this.GUIBombs);
		
		this.bkgColor = new Color(this.baseColor.getRed()+50,this.baseColor.getGreen()+50,this.baseColor.getBlue()+50);
	}
	
	@Override
	public void update(){
		
		//System.out.println(particles.size());
		this.bossHpBar.scale.x=(float)this.boss.HP/this.boss.maxHP;
		this.bossMpBar.scale.x=(float)this.boss.MP/this.boss.maxMP;
		this.hpBar.scale.x=this.player.hp/this.player.maxHp;

		this.bossScoreCounter.text=Integer.toString((int)this.bossScore);
		
		if(this.boss.alive && this.boss.visible && this.boss.HP<=0){ this.boss.kill(); }
		if(this.player.alive && this.player.visible && this.player.hp<=0){ this.player.kill(); }
		
		for(int i=0; i<this.GUIBombs.content.size();i++){
			this.GUIBombs.content.get(i).visible=this.player.mana>=(i+1);
		}
		
		this.bossBullets.addAll(this.boss.getBullets(true));
		this.playerBullets.content.addAll(this.player.getBullets(true));
		
		if(this.player.responsive && this.player.alive){
			//checks for collisions between player and boss's bullets, creates explosion if detected
			for(int i=0; i<this.bossBullets.content.size();i++){
				if(this.bossBullets.content.get(i).collide(this.player)){
					Bullet bullet = (this.bossBullets.content.get(i));
					if(this.boss.alive){
						this.player.hp-= bullet.power;
						this.bossScore+=bullet.power*10;//damaging player gives boss score
					}
					this.bossBullets.content.remove(i);
					this.particles.addAll(Global.createSimpleExplosion(bullet));
					i--;
				}
			}
		}
		
		
		//checks for collisions between boss and player's bullets
		if(this.boss.alive){
			for(int i=0; i<this.playerBullets.content.size();i++){
				if(this.playerBullets.content.get(i).collide(this.boss)){
					Bullet bullet = (Bullet)(this.playerBullets.content.get(i));
					this.boss.HP-= bullet.power;
					this.playerBullets.content.remove(i);
					this.particles.addAll(Global.createSimpleExplosion(bullet));
					i--;
				}
			}
		}
		
		//TODO trigger based, not this looping forever
		if(!this.readyStart.alive && this.boss.alive && this.player.alive){
			this.boss.active=true;
			this.player.responsive=true;
			this.player.canshoot=true;
			this.bossScore+=this.elapsedTime/10;//surviving long periods of time gives boss score
		}
		
		super.update();
		
		if (this.player.alive && !this.boss.alive && this.gameRunning){
			this.gameRunning=false;
			this.bossClearText.visible=true;
			//player win
		}
		else if (!this.player.alive && this.boss.alive && this.gameRunning){
			this.gameRunning=false;
			this.gameOverText.visible=true;
			this.bossScore=this.bossScore*2;//killing the player is a huge score boost
			//boss win
		}
		
		if(!gameRunning && endGameDelay>0){
			endGameDelay-=this.elapsedTime;
		}
		if(!gameRunning && (endGameDelay<=0 || Keys.isKeyPressed(KeyEvent.VK_ENTER))){
			TGlobal.bossRushRouter.scoreNext(this.bossScore);
			SwitchGameEvent e = new SwitchGameEvent(this,ActionEvent.ACTION_PERFORMED, TGlobal.bossRushRouter ,endGameDelay);
			switchGame(e);
		}
	}


	public void bossBulletKilled(int numberKilled) {
		if(gameRunning){
			this.bossScore+=numberKilled*2;//forcing the player to use bombs & clear bullets gives the boss score
		}
	}
	
	public void setGroupRelativeTo(Group<RelativeColorComponent> g, Color c){
		for(int i=0; i<g.content.size(); i++){
			RelativeColorComponent p = (g.content.get(i));
			p.setRelativeTo(c);
		}
	}
	
	public void makeUI(Color baseColor){
		this.gameOverText = new Text("GAME OVER",new Color(baseColor.getRed()+75,baseColor.getGreen()+75,baseColor.getBlue()+75),
				TGlobal.fbig,Global.width/2-250,Global.height/2-30);
		this.gameOverText.visible=false;
		
		this.bossClearText = new Text("BOSS CLEAR",new Color(baseColor.getRed()+75,baseColor.getGreen()+75,baseColor.getBlue()+75),
				TGlobal.fbig,Global.width/2-250,Global.height/2-30);
		this.bossClearText.visible=false;
		
		this.readyStart = new FlashingText(new String[] {" ","   3"," ","   2"," ","   1"," ","START!"},
				new Color(baseColor.getRed()+75,baseColor.getGreen()+75,baseColor.getBlue()+75),
				TGlobal.fbig,Global.width/2-250,Global.height/2-30,750);
		this.readyStart.visible=true;
		
		this.hpBar = new RelativeColorComponent(500,75, 269,10,baseColor,60,60,60);
		this.bossHpBar = new RelativeColorComponent((int)playFieldLeft.x+10,
				(int)playFieldLeft.y+10,(int)playFieldRight.x-30,10,baseColor,70,70,70);		
		this.bossHpBarB = new RelativeColorComponent((int)playFieldLeft.x+10,
				(int)playFieldLeft.y+10,(int)playFieldRight.x-30,10,baseColor,20,20,20);
		this.bossMpBar = new RelativeColorComponent((int)playFieldLeft.x+10,
				(int)playFieldLeft.y+22,(int)playFieldRight.x-30,10,baseColor,60,60,75);
		this.bossMpBarB = new RelativeColorComponent((int)playFieldLeft.x+10,
				(int)playFieldLeft.y+22,(int)playFieldRight.x-30,10,baseColor,20,20,20);
		
		this.GUIBombs=new Group();
		for(int i=0; i<this.player.maxMana;i++){
			this.GUIBombs.add( new RelativeColorComponent((int) (500+9+(250-10)/(this.player.maxMana-1)*i),120,
					10,10,baseColor,50,50,50));
		}
		
		this.uiBorder = new Group();//uiborders 10,10,465,552
		
		this.uiBorder.add(new RelativeColorComponent(0,0,10,Global.height,baseColor,0,0,0));
		this.uiBorder.add(new RelativeColorComponent(10,0,Global.width-10,10,baseColor,0,0,0));
		this.uiBorder.add(new RelativeColorComponent(10,Global.height-10,Global.width-10,10,baseColor,0,0,0));
		this.uiBorder.add(new RelativeColorComponent(475,10,Global.width-475,Global.height-20,baseColor,0,0,0));
		
		this.bossScoreCounter=new Text("0",new Color(baseColor.getRed()+40,baseColor.getGreen()+40,baseColor.getBlue()+40),
				TGlobal.fmed,(int)playFieldRight.x+10,370);
		
	}
}
