package com.example.rpggame.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Character {
    protected String name;
    protected int level;
    protected int health;
    protected int maxHealth;
    protected int mana;
    protected int maxMana;
    protected int attack;
    protected int defense;
    protected int speed;
    protected int experience;
    protected int experienceToNextLevel;
    protected List<Item> inventory;
    protected List<Item> equippedItems;
    
    public Character(String name) {
        this.name = name;
        this.level = 1;
        this.experience = 0;
        this.experienceToNextLevel = 100;
        this.inventory = new ArrayList<>();
        this.equippedItems = new ArrayList<>();
    }
    
    public abstract void levelUp();
    public abstract String getCharacterClass();
    
    public void gainExperience(int exp) {
        this.experience += exp;
        while (this.experience >= this.experienceToNextLevel) {
            levelUp();
        }
    }
    
    public void takeDamage(int damage) {
        int actualDamage = Math.max(1, damage - this.defense);
        this.health = Math.max(0, this.health - actualDamage);
    }
    
    public void heal(int amount) {
        this.health = Math.min(this.maxHealth, this.health + amount);
    }
    
    public void restoreMana(int amount) {
        this.mana = Math.min(this.maxMana, this.mana + amount);
    }
    
    public boolean isAlive() {
        return this.health > 0;
    }
    
    public void addItem(Item item) {
        this.inventory.add(item);
    }
    
    public void equipItem(Item item) {
        if (this.inventory.contains(item)) {
            this.inventory.remove(item);
            this.equippedItems.add(item);
            applyItemStats(item);
        }
    }
    
    public void unequipItem(Item item) {
        if (this.equippedItems.contains(item)) {
            this.equippedItems.remove(item);
            this.inventory.add(item);
            removeItemStats(item);
        }
    }
    
    protected abstract void applyItemStats(Item item);
    protected abstract void removeItemStats(Item item);
    
    // Getters e Setters
    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getMana() { return mana; }
    public int getMaxMana() { return maxMana; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getSpeed() { return speed; }
    public int getExperience() { return experience; }
    public int getExperienceToNextLevel() { return experienceToNextLevel; }
    public List<Item> getInventory() { return inventory; }
    public List<Item> getEquippedItems() { return equippedItems; }
    
    public void setHealth(int health) { this.health = health; }
    public void setMana(int mana) { this.mana = mana; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }
    public void setMaxMana(int maxMana) { this.maxMana = maxMana; }
    public void setAttack(int attack) { this.attack = attack; }
    public void setDefense(int defense) { this.defense = defense; }
    public void setSpeed(int speed) { this.speed = speed; }
}
