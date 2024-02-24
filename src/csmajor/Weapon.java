package csmajor;

public class Weapon {
	double damageBase;
	double attackSpeed;
	double dmgMultiplier;
	String type;
	Weapon(double db, double as, String t){
		damageBase = db;
		attackSpeed = as;
		type = t;
		dmgMultiplier = 1.0;
	}
	double getBaseDamage() {
		return damageBase;
	}
	double getDamage() {
		return (damageBase + game.getRand(-5, 5)) * dmgMultiplier;
		//return damageBase * dmgMultiplier;
	}
	
	String getType() {
		return type;
	}
	
	double getAttackSpeed() {
		return attackSpeed;
	}
	
	void setDmgMultiplier(double n) {
		dmgMultiplier = n;
	}
	double getDmgMultiplier() {
		return dmgMultiplier;
	}
}
