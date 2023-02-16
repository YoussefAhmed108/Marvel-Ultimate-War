package model.abilities;

import java.util.ArrayList;

import model.world.Damageable;

public abstract class Ability {
	@Override
	public String toString() {
		return name +" ";
	}

	private String name;
	private int manaCost;
	private int baseCooldown;
	private int currentCooldown;
	private int castRange;
	private AreaOfEffect castArea;
	private int requiredActionPoints;

	public Ability(String name, int cost, int baseCoolDown, int castRange, AreaOfEffect area, int required) {
		this.name = name;
		this.manaCost = cost;
		this.baseCooldown = baseCoolDown;
		this.currentCooldown = 0;
		this.castRange = castRange;
		this.castArea = area;
		this.requiredActionPoints = required;
	}

	public int getCurrentCooldown() {
		return currentCooldown;
	}
	public abstract void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException;

	public void setCurrentCooldown(int currentCoolDown) {
		if (currentCoolDown < 0)
			currentCoolDown = 0;
		else if (currentCoolDown > baseCooldown)
			currentCoolDown = baseCooldown;
		this.currentCooldown = currentCoolDown;
	}

	public String getName() {
		return name;
	}

	public int getManaCost() {
		return manaCost;
	}

	public int getBaseCooldown() {
		return baseCooldown;
	}

	public int getCastRange() {
		return castRange;
	}

	public AreaOfEffect getCastArea() {
		return castArea;
	}

	public int getRequiredActionPoints() {
		return requiredActionPoints;
	}
	
	public String displayAbility() {
		String type = "";
		if(this instanceof DamagingAbility) {
			type = "Damaging Ability";
		}
		if(this instanceof HealingAbility) {
			type = "Healing Ability";
		}
		if(this instanceof CrowdControlAbility) {
			type = "Crowd Control Ability";
		}
		return "Name: " + this.name + "\n" + "Type: " + type + "\n" + "Area Of Effect: " + this.castArea + "\n" + "Cast Range: " + this.castRange + "\n" + "Mana Cost: "+ this.manaCost +
				 "\n" + "Action Points Cost: " + this.requiredActionPoints + "\n" + "Base CoolDown: " + this.baseCooldown + "\n" + "Current CoolDown: " + this.currentCooldown +
				"\n";
	}

}
