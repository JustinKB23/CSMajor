package csmajor;

public class rangedWeapon extends Weapon{
	private int cooldown;
	private double range;
	private double speed;
	rangedWeapon(double bd, double as, double r, double s){
		super(bd, as, "ranged");
		cooldown = 0;
		range = r;
		speed = s;
	}
	void update() {
		if(cooldown > 0)
			cooldown--;
	}
	boolean canFire() {
		return (cooldown <= 0);
	}
	void fired() {
		cooldown = (int) getAttackSpeed();
	}
	double getSpeed() {
		return speed;
	}
	void setSpeed(double s) {
		speed = s;
	}
	double getRange() {
		return range;
	}
	void setRange(double r) {
		range = r;
	}
	
}
