package com.example.rpggame.model;

public class Tank extends Character {
    
    public Tank(String name) {
        super(name);
        this.maxHealth = 150;
        this.health = 150;
        this.maxMana = 50;
        this.mana = 50;
        this.attack = 15;
        this.defense = 25;
        this.speed = 8;
    }
    
    @Override
    public void levelUp() {
        this.level++;
        this.maxHealth += 20;
        this.health = this.maxHealth;
        this.maxMana += 5;
        this.mana = this.maxMana;
        this.attack += 3;
        this.defense += 5;
        this.speed += 1;
        this.experienceToNextLevel = (int) (this.experienceToNextLevel * 1.5);
    }
    
    @Override
    public String getCharacterClass() {
        return "Tank";
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
    
    public void taunt() {
        // Habilidade especial do Tank - aumenta defesa temporariamente
        this.defense += 10;
    }
    
    public void shieldWall() {
        // Habilidade defensiva
        this.defense += 15;
        this.health = Math.min(this.maxHealth, this.health + 20);
    }
}
