package model.world;

import java.util.ArrayList;

import model.effects.Stun;

public class AntiHero extends Champion {

	public AntiHero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}

	@Override
	public void useLeaderAbility(ArrayList<Champion> targets) {
		for (Champion c: targets)
		{
			if(c instanceof Hero) {
				Stun s = new Stun(5);
				c.getAppliedEffects().add(s);
				s.apply(c);
			}
			if(c instanceof AntiHero) {
				Stun s = new Stun(3);
				c.getAppliedEffects().add(s);
				s.apply(c);
			}
			if(c instanceof Villain) {
				Stun s = new Stun(4);
				c.getAppliedEffects().add(s);
				s.apply(c);
			}
		}
	}
}
