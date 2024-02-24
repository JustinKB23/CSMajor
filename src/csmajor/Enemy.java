package csmajor;


public class Enemy extends Sprite{
	
	private int wanderTimer;
	private int startWanderTime;
	private int attackTimer;
	private int startAttackTime;

	Enemy(int x, int y, int w, int h, int s, String type, double dmg, double hp) {
		super(x, y, w, h, s, type, new Melee(dmg,4),hp);
		wanderTimer = 0;
		startWanderTime = 0;
		attackTimer = 0;
		startAttackTime = 0;
		// TODO Auto-generated constructor stub
	}
	
	//		 0
	//    3  E	1 this.getTranslateY();
	//		 2    
	
	public DmgMarker goAtPlayer(double px, double py, Sprite p) {
		if(this.getTranslateX() > px)
			this.setMotion(3,true);
		else 
			this.setMotion(3,false);
		
		if(this.getTranslateX() < px)
			this.setMotion(1,true);
		else
			this.setMotion(1, false);
		
		if(this.getTranslateY() > py)
			this.setMotion(0,true);
		else
			this.setMotion(0,false);
		
		if(this.getTranslateY() < py)
			this.setMotion(2, true);
		else
			this.setMotion(2,false);
		return canAttack(px,py,p);
			
	}
	public DmgMarker canAttack(double px, double py, Sprite p) {
		if((attackTimer - startAttackTime) >= game.getRand(10,50)){
			//System.out.println("Attacking");
			if(Math.abs(this.getTranslateX() - px) <= 70 && Math.abs(this.getTranslateY() - py) <= 70) {
				int dmg = 10 + game.getRand(0,6);
				startAttackTime = attackTimer;
				p.damage(dmg);
				return new DmgMarker(p.getTranslateX(), p.getTranslateY(), dmg, 20,(dmg > 13));
			}
		}
		return null;
	}
	
	public DmgMarker smoothHunt(double px, double py, Sprite p) {
		attackTimer ++;
		wanderTimer ++;
		if(true) {
			if((wanderTimer - startWanderTime) >= game.getRand(90,150)) {
				switch(game.getRand(0, 30)) {
				case 1:
					startWanderTime = wanderTimer;
					this.setMotion(0,false);
					this.setMotion(1,false);
					this.setMotion(2,false);
					this.setMotion(3,false);
					break;
				case 2:
					startWanderTime = wanderTimer;
					this.setMotion(0,true);
					this.setMotion(1,false);
					this.setMotion(2,false);
					this.setMotion(3,false);
				case 3:
					startWanderTime = wanderTimer;
					this.setMotion(0,false);
					this.setMotion(1,true);
					this.setMotion(2,false);
					this.setMotion(3,false);
					break;
				case 4:
					startWanderTime = wanderTimer;
					this.setMotion(0,false);
					this.setMotion(1,false);
					this.setMotion(2,true);
					this.setMotion(3,false);
					break;
				case 5:
					startWanderTime = wanderTimer;
					this.setMotion(0,false);
					this.setMotion(1,false);
					this.setMotion(2,false);
					this.setMotion(3,true);
					break;
				case 6:
					startWanderTime = wanderTimer;
					this.setMotion(0,true);
					this.setMotion(1,false);
					this.setMotion(2,false);
					this.setMotion(3,false);
				default:
					startWanderTime = wanderTimer;
					return goAtPlayer(px,py,p);
				}
				return null;
			}
		}
		return null;
	}
	
	public void updateWander() {
		wanderTimer ++;
		if(true) {
			if((wanderTimer - startWanderTime) >= game.getRand(90,150)) {
				//generate a random number to pick next movement
				switch(game.getRand(0, 6)) {
				case 1:
					startWanderTime = wanderTimer;
					this.setMotion(0,false);
					this.setMotion(1,false);
					this.setMotion(2,false);
					this.setMotion(3,false);
					break;
				case 2:
					startWanderTime = wanderTimer;
					this.setMotion(0,true);
					this.setMotion(1,false);
					this.setMotion(2,false);
					this.setMotion(3,false);
				case 3:
					startWanderTime = wanderTimer;
					this.setMotion(0,false);
					this.setMotion(1,true);
					this.setMotion(2,false);
					this.setMotion(3,false);
					break;
				case 4:
					startWanderTime = wanderTimer;
					this.setMotion(0,false);
					this.setMotion(1,false);
					this.setMotion(2,true);
					this.setMotion(3,false);
					break;
				case 5:
					startWanderTime = wanderTimer;
					this.setMotion(0,false);
					this.setMotion(1,false);
					this.setMotion(2,false);
					this.setMotion(3,true);
					break;
				case 6:
					startWanderTime = wanderTimer;
					this.setMotion(0,true);
					this.setMotion(1,false);
					this.setMotion(2,false);
					this.setMotion(3,false);
						
				}
			}
		}
	}

}
