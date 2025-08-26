package com.example.rpggame.model;

import java.util.Random;

public class Item {
    private String name;
    private ItemType type;
    private ItemRarity rarity;
    private int attackBonus;
    private int defenseBonus;
    private int healthBonus;
    private int manaBonus;
    private int speedBonus;
    private String specialEffect;
    private int level;
    
    public enum ItemType {
        WEAPON, ARMOR, HELMET, BOOTS, GLOVES, RING, AMULET, POTION, SCROLL
    }
    
    public enum ItemRarity {
        COMMON(1.0), UNCOMMON(1.5), RARE(2.0), EPIC(3.0), LEGENDARY(5.0);
        
        private final double multiplier;
        
        ItemRarity(double multiplier) {
            this.multiplier = multiplier;
        }
        
        public double getMultiplier() {
            return multiplier;
        }
    }
    
    public Item(String name, ItemType type, ItemRarity rarity, int level) {
        this.name = name;
        this.type = type;
        this.rarity = rarity;
        this.level = level;
        generateStats();
    }
    
    private void generateStats() {
        Random random = new Random();
        int baseValue = 5 + (level * 2);
        
        switch (type) {
            case WEAPON:
                this.attackBonus = (int) (baseValue * rarity.getMultiplier());
                this.defenseBonus = random.nextInt(5);
                break;
            case ARMOR:
                this.defenseBonus = (int) (baseValue * rarity.getMultiplier());
                this.healthBonus = random.nextInt(10) + 5;
                break;
            case HELMET:
                this.defenseBonus = (int) (baseValue * 0.7 * rarity.getMultiplier());
                this.manaBonus = random.nextInt(15) + 10;
                break;
            case BOOTS:
                this.speedBonus = (int) (baseValue * rarity.getMultiplier());
                this.defenseBonus = random.nextInt(5);
                break;
            case GLOVES:
                this.attackBonus = (int) (baseValue * 0.6 * rarity.getMultiplier());
                this.defenseBonus = (int) (baseValue * 0.4 * rarity.getMultiplier());
                break;
            case RING:
                this.attackBonus = random.nextInt(10) + 5;
                this.manaBonus = random.nextInt(10) + 5;
                break;
            case AMULET:
                this.healthBonus = random.nextInt(15) + 10;
                this.manaBonus = random.nextInt(15) + 10;
                break;
            case POTION:
                this.healthBonus = 50 + (level * 10);
                break;
            case SCROLL:
                this.manaBonus = 30 + (level * 8);
                break;
        }
        
        // Adicionar efeitos especiais baseados na raridade
        if (rarity == ItemRarity.RARE || rarity == ItemRarity.EPIC || rarity == ItemRarity.LEGENDARY) {
            generateSpecialEffect();
        }
    }
    
    private void generateSpecialEffect() {
        Random random = new Random();
        String[] effects = {
            "Vampiric Strike", "Critical Hit", "Dodge", "Counter Attack", 
            "Elemental Damage", "Life Steal", "Mana Steal", "Double Strike"
        };
        this.specialEffect = effects[random.nextInt(effects.length)];
    }
    
    public String getDisplayName() {
        return rarity.name() + " " + name;
    }
    
    public int getTotalValue() {
        return attackBonus + defenseBonus + healthBonus + manaBonus + speedBonus;
    }
    
    // Getters necessários
    public String getName() { return name; }
    public ItemType getType() { return type; }
    public ItemRarity getRarity() { return rarity; }
    public int getAttackBonus() { return attackBonus; }
    public int getDefenseBonus() { return defenseBonus; }
    public int getHealthBonus() { return healthBonus; }
    public int getManaBonus() { return manaBonus; }
    public int getSpeedBonus() { return speedBonus; }
    public String getSpecialEffect() { return specialEffect; }
    public int getLevel() { return level; }
}
