package com.example.rpggame.service;

import com.example.rpggame.model.Character;
import com.example.rpggame.model.Enemy;
import com.example.rpggame.model.Item;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CombatService {
    private Random random = new Random();
    
    public CombatResult battle(Character player, Enemy enemy) {
        CombatResult result = new CombatResult();
        List<String> battleLog = new ArrayList<>();
        
        battleLog.add("=== BATALHA INICIADA ===");
        battleLog.add(player.getName() + " vs " + enemy.getDisplayName());
        battleLog.add("");
        
        // Determinar quem ataca primeiro baseado na velocidade
        boolean playerFirst = player.getSpeed() >= enemy.getSpeed();
        
        while (player.isAlive() && enemy.isAlive()) {
            if (playerFirst) {
                // Jogador ataca primeiro
                if (player.isAlive()) {
                    int damage = calculateDamage(player, enemy);
                    enemy.takeDamage(damage);
                    battleLog.add(player.getName() + " ataca " + enemy.getName() + " causando " + damage + " de dano!");
                    battleLog.add(enemy.getName() + " HP: " + enemy.getHealth() + "/" + enemy.getMaxHealth());
                }
                
                if (enemy.isAlive()) {
                    int damage = calculateDamage(enemy, player);
                    player.takeDamage(damage);
                    battleLog.add(enemy.getName() + " ataca " + player.getName() + " causando " + damage + " de dano!");
                    battleLog.add(player.getName() + " HP: " + player.getHealth() + "/" + player.getMaxHealth());
                }
            } else {
                // Inimigo ataca primeiro
                if (enemy.isAlive()) {
                    int damage = calculateDamage(enemy, player);
                    player.takeDamage(damage);
                    battleLog.add(enemy.getName() + " ataca " + player.getName() + " causando " + damage + " de dano!");
                    battleLog.add(player.getName() + " HP: " + player.getHealth() + "/" + player.getMaxHealth());
                }
                
                if (player.isAlive()) {
                    int damage = calculateDamage(player, enemy);
                    enemy.takeDamage(damage);
                    battleLog.add(player.getName() + " ataca " + enemy.getName() + " causando " + damage + " de dano!");
                    battleLog.add(enemy.getName() + " HP: " + enemy.getHealth() + "/" + enemy.getMaxHealth());
                }
            }
            
            battleLog.add("");
        }
        
        // Determinar vencedor
        if (player.isAlive()) {
            result.setVictory(true);
            result.setExperienceGained(enemy.getExperienceReward());
            result.setGoldGained(enemy.getGoldReward());
            
            // Chance de drop de item baseada na qualidade do loot
            double dropChance = 0.3 + (enemy.getLootQuality() * 0.1); // 40% a 80% baseado na dificuldade
            if (random.nextDouble() < dropChance) {
                Item droppedItem = generateRandomItem(enemy.getLevel(), enemy.getLootQuality());
                result.setDroppedItem(droppedItem);
                battleLog.add("🎁 " + droppedItem.getDisplayName() + " foi encontrado!");
            }
            
            battleLog.add("🎉 VITÓRIA! " + player.getName() + " derrotou " + enemy.getName());
            battleLog.add("Experiência ganha: " + enemy.getExperienceReward());
            battleLog.add("Ouro ganho: " + enemy.getGoldReward());
            
            // Aplicar recompensas
            player.gainExperience(enemy.getExperienceReward());
        } else {
            result.setVictory(false);
            battleLog.add("💀 DERROTA! " + player.getName() + " foi derrotado por " + enemy.getName());
        }
        
        result.setBattleLog(battleLog);
        return result;
    }
    
    private int calculateDamage(Character attacker, Character defender) {
        int baseDamage = attacker.getAttack();
        int defense = defender.getDefense();
        
        // Adicionar variação aleatória (±20%)
        double variation = 0.8 + (random.nextDouble() * 0.4);
        int finalDamage = (int) (baseDamage * variation);
        
        // Aplicar defesa
        int actualDamage = Math.max(1, finalDamage - defense);
        
        // Chance de crítico (10%)
        if (random.nextDouble() < 0.1) {
            actualDamage = (int) (actualDamage * 1.5);
        }
        
        return actualDamage;
    }
    
    private Item generateRandomItem(int enemyLevel, int lootQuality) {
        String[] weaponNames = {"Espada", "Machado", "Lança", "Arco", "Cajado", "Adaga", "Katana", "Martelo de Guerra", "Lança de Gelo"};
        String[] armorNames = {"Armadura", "Couraça", "Túnica", "Robe", "Gibão", "Placas de Dragão", "Vestes Élficas", "Couraça de Titânio"};
        String[] accessoryNames = {"Anel", "Amuleto", "Botas", "Luvas", "Capacete", "Coroa", "Tiara", "Botas de Velo"};
        
        Item.ItemType[] types = Item.ItemType.values();
        Item.ItemRarity[] rarities = Item.ItemRarity.values();
        
        Item.ItemType randomType = types[random.nextInt(types.length)];
        
        // Qualidade do loot afeta a raridade dos itens
        Item.ItemRarity randomRarity;
        if (lootQuality >= 4) {
            // Inimigos muito difíceis têm maior chance de itens raros
            randomRarity = random.nextDouble() < 0.6 ? Item.ItemRarity.EPIC : Item.ItemRarity.LEGENDARY;
        } else if (lootQuality >= 3) {
            // Inimigos difíceis têm chance de itens raros
            randomRarity = random.nextDouble() < 0.7 ? Item.ItemRarity.RARE : Item.ItemRarity.EPIC;
        } else if (lootQuality >= 2) {
            // Inimigos médios têm chance de itens incomuns
            randomRarity = random.nextDouble() < 0.8 ? Item.ItemRarity.UNCOMMON : Item.ItemRarity.RARE;
        } else {
            // Inimigos fáceis dão itens comuns
            randomRarity = random.nextDouble() < 0.9 ? Item.ItemRarity.COMMON : Item.ItemRarity.UNCOMMON;
        }
        
        String itemName;
        switch (randomType) {
            case WEAPON:
                itemName = weaponNames[random.nextInt(weaponNames.length)];
                break;
            case ARMOR:
                itemName = armorNames[random.nextInt(armorNames.length)];
                break;
            default:
                itemName = accessoryNames[random.nextInt(accessoryNames.length)];
                break;
        }
        
        return new Item(itemName, randomType, randomRarity, enemyLevel);
    }
    
    public static class CombatResult {
        private boolean victory;
        private int experienceGained;
        private int goldGained;
        private Item droppedItem;
        private List<String> battleLog;
        
        // Getters e Setters
        public boolean isVictory() { return victory; }
        public void setVictory(boolean victory) { this.victory = victory; }
        
        public int getExperienceGained() { return experienceGained; }
        public void setExperienceGained(int experienceGained) { this.experienceGained = experienceGained; }
        
        public int getGoldGained() { return goldGained; }
        public void setGoldGained(int goldGained) { this.goldGained = goldGained; }
        
        public Item getDroppedItem() { return droppedItem; }
        public void setDroppedItem(Item droppedItem) { this.droppedItem = droppedItem; }
        
        public List<String> getBattleLog() { return battleLog; }
        public void setBattleLog(List<String> battleLog) { this.battleLog = battleLog; }
    }
}
