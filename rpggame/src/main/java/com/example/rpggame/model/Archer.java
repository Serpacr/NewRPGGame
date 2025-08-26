package com.example.rpggame.model;

public class Archer extends Character {
    
    public Archer(String name) {
        super(name);
        this.maxHealth = 90;
        this.health = 90;
        this.maxMana = 60;
        this.mana = 60;
        this.attack = 20;
        this.defense = 8;
        this.speed = 18;
    }
    
    @Override
    public void levelUp() {
        this.level++;
        this.maxHealth += 10;
        this.health = this.maxHealth;
        this.maxMana += 8;
        this.mana = this.maxMana;
        this.attack += 4;
        this.defense += 2;
        this.speed += 3;
        this.experienceToNextLevel = (int) (this.experienceToNextLevel * 1.5);
    }
    
    @Override
    public String getCharacterClass() {
        return "Archer";
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
    
    public void preciseShot() {
        // Habilidade especial do Archer - ataque crítico
        if (this.mana >= 15) {
            this.mana -= 15;
            // Efeito: próximo ataque tem 100% de chance de crítico
        }
    }
    
    public void rapidFire() {
        // Habilidade de múltiplos ataques
        if (this.mana >= 25) {
            this.mana -= 25;
            // Efeito: 3 ataques rápidos
        }
    }
    
    public void dodge() {
        // Habilidade defensiva baseada em velocidade
        this.speed += 5;
    }
}
