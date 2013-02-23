package atouhougame.gamescreens;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import atouhougame.Boss;
import atouhougame.BossSeed;
import atouhougame.Generation;
import atouhougame.TGlobal;
import framework.BakedGameComponent;
import framework.GameComponent;
import framework.Global;
import framework.ParagraphText;
import framework.Text;

public class ThreadedGenerationLoader extends GameComponent implements Runnable{

	GalleryScreen parent;
	int genNumber;
	
	BufferedImage winnerCircle=null;
	
	public ThreadedGenerationLoader(double x, double y, GalleryScreen parent, int generationNumber) {
		super(x,y);
		this.parent=parent;
		this.genNumber = generationNumber;
		this.boundaryState=GameComponent.BOUNDARY_NONE;
		Thread t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		if(winnerCircle==null){//only run once: make generated grpahic
			winnerCircle = new BufferedImage(parent.boxsize,parent.boxsize, BufferedImage.TYPE_INT_RGB);
			int thick=20;
			int beg=5;
			
			Graphics g = winnerCircle.getGraphics();
			g.setColor(TGlobal.greyBack);
			g.fillRect(0, 0, parent.boxsize, parent.boxsize);
			g.setColor(TGlobal.textSubtleTrans);
			g.fillOval(beg, beg, parent.boxsize-beg*2, parent.boxsize-beg*2);
			g.setColor(TGlobal.greyBack);
			g.fillOval(beg+thick, beg+thick, parent.boxsize-beg*2-thick*2, parent.boxsize-beg*2-thick*2);
		}
		
		Generation g;
		try {
			g = parent.manager.getGeneration(genNumber);
			makeRowFromGeneration(g);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		this.visible=false;
		this.kill();
	}
	
	private void makeRowFromGeneration(Generation gen){
		
		long[] winners =gen.getWinnersID();
		
		Text t = new Text(
				"Generation "+gen.generationNumber,
				TGlobal.textLight,TGlobal.fmed,
				x,
				TGlobal.fmed.getSize()+this.y
			);
		parent.lockX.add(t);
		parent.text.add(t);
		for(int y=0; y< gen.size(); y++){
			BossSeed s = gen.get(y);
			if(winners[0]==s.bossID || winners[1]==s.bossID){//indicate who was winner
				parent.images.add(new BakedGameComponent(
						y*parent.boxsize+x,
						this.y,
						winnerCircle)
				);
			}
			BufferedImage d = Boss.makeImage(s);
			BakedGameComponent b = new BakedGameComponent(
					y*parent.boxsize+x,
					this.y,
					d);
			b.imageOffset.x = -parent.boxsize/2+d.getWidth()/2;
			b.imageOffset.y = -parent.boxsize/2+d.getHeight()/2;
			b.size.x = parent.boxsize;
			b.size.y = parent.boxsize;
			
			b.data=s;
			parent.images.add(b);
			parent.bossTiles.add(b);
		}
		for(int y=0; y< gen.size(); y++){
			BossSeed s = gen.get(y);
			parent.text.add(
				new Text(
					s.getName(),
					TGlobal.textLight,TGlobal.fsmall,
					y*parent.boxsize+x,
					this.y+parent.generationHeight-parent.textheight
				)
			);
			
			parent.text.add(
				new ParagraphText(
					new String[]{
						"Times Tested: "+s.timesTested,
						"Overall Score: "+(int)s.score
					},
					TGlobal.textTrans,TGlobal.fsmall,
					y*parent.boxsize+20+x,
					this.y+parent.generationHeight-parent.textheight+TGlobal.fsmall.getSize()+8,
					8
				)
			);
		}
		if(parent.maxoffx<gen.size()*parent.boxsize){
			parent.maxoffx=gen.size()*parent.boxsize-Global.width+parent.margin*2;
		} if(parent.maxoffx<0){parent.maxoffx=0;}
	}
	
	@Override
	public Graphics render(Graphics g){
		if(this.visible){
			g.setFont(TGlobal.fmed);
			g.drawString("Loading Generation...",(int)x+parent.margin,(int)y+parent.generationHeight/2+TGlobal.fmed.getSize());
		}
		return g;
	}

}
