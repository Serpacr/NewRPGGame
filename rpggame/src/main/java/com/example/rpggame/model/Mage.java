package com.example.rpggame.model;

public class Mage extends Character {
    
    public Mage(String name) {
        super(name);
        this.maxHealth = 80;
        this.health = 80;
        this.maxMana = 120;
        this.mana = 120;
        this.attack = 12;
        this.defense = 6;
        this.speed = 10;
    }
    
    @Override
    public void levelUp() {
        this.level++;
        this.maxHealth += 8;
        this.health = this.maxHealth;
        this.maxMana += 15;
        this.mana = this.maxMana;
        this.attack += 2;
        this.defense += 1;
        this.speed += 1;
        this.experienceToNextLevel = (int) (this.experienceToNextLevel * 1.5);
    }
    
    @Override
    public String getCharacterClass() {
        return "Mage";
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
    
    public void fireball() {
        // Habilidade de magia de fogo
        if (this.mana >= 20) {
            this.mana -= 20;
            // Efeito: dano mágico alto
        }
    }
    
    public void iceShield() {
        // Habilidade defensiva mágica
        if (this.mana >= 15) {
            this.mana -= 15;
            this.defense += 10;
        }
    }
    
    public void lightningBolt() {
        // Habilidade de raio
        if (this.mana >= 30) {
            this.mana -= 30;
            // Efeito: dano mágico crítico
        }
    }
    
    public void manaRegeneration() {
        // Habilidade de regeneração de mana
        this.mana = Math.min(this.maxMana, this.mana + 20);
    }
}
