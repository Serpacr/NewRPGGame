package com.example.rpggame.model;

public class Enemy extends Character {
    private String enemyType;
    private int goldReward;
    private int experienceReward;
    private boolean isBoss;
    private int lootQuality;
    
    public Enemy(String name, String enemyType, int level, boolean isBoss) {
        super(name);
        this.enemyType = enemyType;
        this.level = level;
        this.isBoss = isBoss;
        this.experienceToNextLevel = Integer.MAX_VALUE; // Inimigos não ganham experiência
        
        // Ajustar stats baseado no tipo e nível
        adjustStats();
    }
    
    private void adjustStats() {
        int levelMultiplier = 1 + (level - 1) * 2;
        int bossMultiplier = isBoss ? 3 : 1;
        
        this.maxHealth = (50 + (level * 15)) * bossMultiplier;
        this.health = this.maxHealth;
        this.maxMana = (20 + (level * 5)) * bossMultiplier;
        this.mana = this.maxMana;
        this.attack = (10 + (level * 3)) * bossMultiplier;
        this.defense = (5 + (level * 2)) * bossMultiplier;
        this.speed = (8 + (level * 1)) * bossMultiplier;
        
        // Recompensas
        this.goldReward = (10 + (level * 5)) * bossMultiplier;
        this.experienceReward = (20 + (level * 10)) * bossMultiplier;
    }
    
    @Override
    public void levelUp() {
        // Inimigos não ganham nível
    }
    
    @Override
    public String getCharacterClass() {
        return enemyType;
    }
    
    @Override
    protected void applyItemStats(Item item) {
        // Inimigos não equipam itens
    }
    
    @Override
    protected void removeItemStats(Item item) {
        // Inimigos não equipam itens
    }
    
    public String getDisplayName() {
        if (isBoss) {
            return "BOSS: " + name + " (Nível " + level + ")";
        }
        return name + " (Nível " + level + ")";
    }
    
    // Getters necessários
    public String getEnemyType() { return enemyType; }
    public int getGoldReward() { return goldReward; }
    public int getExperienceReward() { return experienceReward; }
    public boolean isBoss() { return isBoss; }
    public int getLootQuality() { return lootQuality; }
    
    // Setters necessários
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }
    public void setHealth(int health) { this.health = health; }
    public void setAttack(int attack) { this.attack = attack; }
    public void setDefense(int defense) { this.defense = defense; }
    public void setSpeed(int speed) { this.speed = speed; }
    public void setGoldReward(int goldReward) { this.goldReward = goldReward; }
    public void setExperienceReward(int experienceReward) { this.experienceReward = experienceReward; }
    public void setLootQuality(int lootQuality) { this.lootQuality = lootQuality; }
}
