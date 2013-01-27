package framework;

import java.awt.Color;
import java.awt.Font;

public class FlashingText extends Text{
	
	protected String[] texts;
	protected long flashtime, cumtime=0;
	protected int activeIndex=0;
	public boolean killonfinish=true;
	
	public FlashingText(String[] texts, Color color, Font font, int x, int y, long flashtime){
		super(texts[0],color,font,x,y);
		this.texts=texts; //TODO baking animation frames ahead of time
		this.flashtime=flashtime;
		activeIndex=0;
		cumtime=0;
		killonfinish=true;
	}
	
	@Override
	public void update(long elapsedTime){
		cumtime = cumtime+elapsedTime;
		if(flashtime<cumtime && this.alive){
			cumtime = cumtime%flashtime;
			activeIndex++;
			if(activeIndex>=texts.length){
				if(killonfinish){
					this.kill();
				}
				else{
					activeIndex=activeIndex%texts.length;
				}
			}
			if(this.alive){
				this.text=this.texts[activeIndex];
			}
		}
	}
	
	public void reset(){
		this.cumtime=0;
	}
	
}
