package atouhougame;

public class BossScoring {

	public static double scoreDamage(double damageToPlayer){
		return damageToPlayer;
	}
	
	public static double scoreBulletKill(double bulletsKilled){
		return bulletsKilled*2;
	}
	
	public static double scorePlayerKill(long millisToDeath){
		return 1000* (1000*60/millisToDeath);
	}
	
	public static double scoreBossKill(long millisToDeath){
		return (1000*60/millisToDeath);
	}
	
}
