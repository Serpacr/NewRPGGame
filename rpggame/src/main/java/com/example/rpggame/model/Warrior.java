package com.example.rpggame.model;

public class Warrior extends Character {
    
    public Warrior(String name) {
        super(name);
        this.maxHealth = 120;
        this.health = 120;
        this.maxMana = 40;
        this.mana = 40;
        this.attack = 25;
        this.defense = 15;
        this.speed = 12;
    }
    
    @Override
    public void levelUp() {
        this.level++;
        this.maxHealth += 15;
        this.health = this.maxHealth;
        this.maxMana += 3;
        this.mana = this.maxMana;
        this.attack += 5;
        this.defense += 3;
        this.speed += 2;
        this.experienceToNextLevel = (int) (this.experienceToNextLevel * 1.5);
    }
    
    @Override
    public String getCharacterClass() {
        return "Warrior";
    }
    
    @Override
    protected void applyItemStats(Item item) {
        this.attack += item.getAttackBonus();
        this.defense += item.getDefenseBonus();
        this.maxHealth += item.getHealthBonus();
        this.health += item.getHealthBonus();
        this.maxMana += item.getManaBonus();
        this.mana += item.getManaBonus();
        this.speed += item.getSpeedBonus();
    }
    
    @Override
    protected void removeItemStats(Item item) {
        this.attack -= item.getAttackBonus();
        this.defense -= item.getDefenseBonus();
        this.maxHealth -= item.getHealthBonus();
        this.health = Math.min(this.health, this.maxHealth);
        this.maxMana -= item.getManaBonus();
        this.mana = Math.min(this.mana, this.maxMana);
        this.speed -= item.getSpeedBonus();
    }
    
    public void berserkerRage() {
        // Habilidade especial do Warrior - aumenta ataque mas diminui defesa
        this.attack += 15;
        this.defense -= 5;
    }
    
    public void whirlwind() {
        // Habilidade de área
        if (this.mana >= 20) {
            this.mana -= 20;
            // Efeito especial: ataque múltiplo
        }
    }
}
