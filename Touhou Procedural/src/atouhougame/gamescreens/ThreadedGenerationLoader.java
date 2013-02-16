package atouhougame.gamescreens;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import atouhougame.Boss;
import atouhougame.BossSeed;
import atouhougame.Generation;
import atouhougame.TGlobal;
import framework.BakedGameComponent;
import framework.GameComponent;
import framework.ParagraphText;
import framework.Text;

public class ThreadedGenerationLoader extends GameComponent implements Runnable{

	GalleryScreen parent;
	int genNumber;
	
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
		Generation g = parent.manager.getGeneration(genNumber);
		makeRowFromGeneration(g);
		this.kill();
	}
	
	private void makeRowFromGeneration(Generation gen){
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
			BufferedImage d = Boss.makeImage(s);
			parent.images.add(new BakedGameComponent(
					y*parent.boxsize+parent.boxsize/2-d.getWidth()/2+x,
					parent.boxsize/2-d.getHeight()/2+this.y,
					d)
			);
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
	}
	
	@Override
	public Graphics render(Graphics g){
		g.setFont(TGlobal.fmed);
		g.drawString("Loading Generation...",(int)x,(int)y+TGlobal.fmed.getSize());
		return g;
	}

}
