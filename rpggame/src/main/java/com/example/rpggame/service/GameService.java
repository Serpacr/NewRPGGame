package com.example.rpggame.service;

import com.example.rpggame.model.*;
import com.example.rpggame.model.Character;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GameService {
    
    @Autowired
    private CombatService combatService;
    
    private Random random = new Random();
    private int currentWave = 1;
    private int enemiesPerWave = 3;
    private boolean bossWave = false;
    
    public GameState startNewGame(String characterName, String characterClass) {
        Character player = createCharacter(characterName, characterClass);
        return new GameState(player, currentWave, enemiesPerWave, bossWave);
    }
    
    public Character createCharacter(String name, String characterClass) {
        return switch (characterClass.toLowerCase()) {
            case "tank" -> new Tank(name);
            case "warrior" -> new Warrior(name);
            case "archer" -> new Archer(name);
            case "mage" -> new Mage(name);
            default -> new Warrior(name);
        };
    }
    
    public List<Enemy> generateWave(int waveNumber) {
        List<Enemy> enemies = new ArrayList<>();
        
        // A cada 5 ondas, é uma onda de boss
        if (waveNumber % 5 == 0) {
            bossWave = true;
            enemies.add(createBoss(waveNumber));
        } else {
            bossWave = false;
            for (int i = 0; i < enemiesPerWave; i++) {
                enemies.add(createRandomEnemy(waveNumber));
            }
        }
        
        return enemies;
    }
    
    private Enemy createRandomEnemy(int waveNumber) {
        String[] enemyTypes = {"Goblin", "Orc", "Troll", "Skeleton", "Zombie", "Demon", "Vampire", "Werewolf", "Dragonkin"};
        String[] enemyNames = {"Guardião", "Sentinela", "Defensor", "Protetor", "Vigilante", "Assassino", "Berserker", "Mago Negro"};
        
        String enemyType = enemyTypes[random.nextInt(enemyTypes.length)];
        String enemyName = enemyNames[random.nextInt(enemyNames.length)] + " " + enemyType;
        int enemyLevel = Math.max(1, waveNumber - 1);
        
        // Criar inimigo com dificuldade baseada no tipo
        Enemy enemy = new Enemy(enemyName, enemyType, enemyLevel, false);
        
        // Ajustar dificuldade baseada no tipo de inimigo
        adjustEnemyDifficulty(enemy, enemyType, waveNumber);
        
        return enemy;
    }
    
    private void adjustEnemyDifficulty(Enemy enemy, String enemyType, int waveNumber) {
        int difficultyMultiplier = 1;
        
        switch (enemyType.toLowerCase()) {
            case "goblin":
                difficultyMultiplier = 1; // Fácil
                break;
            case "orc":
                difficultyMultiplier = 2; // Médio
                break;
            case "troll":
                difficultyMultiplier = 3; // Difícil
                break;
            case "skeleton":
                difficultyMultiplier = 2; // Médio
                break;
            case "zombie":
                difficultyMultiplier = 1; // Fácil
                break;
            case "demon":
                difficultyMultiplier = 4; // Muito difícil
                break;
            case "vampire":
                difficultyMultiplier = 3; // Difícil
                break;
            case "werewolf":
                difficultyMultiplier = 3; // Difícil
                break;
            case "dragonkin":
                difficultyMultiplier = 5; // Extremamente difícil
                break;
        }
        
        // Aplicar multiplicador de dificuldade
        enemy.setMaxHealth(enemy.getMaxHealth() * difficultyMultiplier);
        enemy.setHealth(enemy.getMaxHealth());
        enemy.setAttack(enemy.getAttack() * difficultyMultiplier);
        enemy.setDefense(enemy.getDefense() * difficultyMultiplier);
        enemy.setSpeed(enemy.getSpeed() * difficultyMultiplier);
        
        // Ajustar recompensas baseadas na dificuldade
        enemy.setGoldReward(enemy.getGoldReward() * difficultyMultiplier);
        enemy.setExperienceReward(enemy.getExperienceReward() * difficultyMultiplier);
        
        // Definir qualidade do loot baseada na dificuldade
        enemy.setLootQuality(difficultyMultiplier);
    }
    
    private Enemy createBoss(int waveNumber) {
        String[] bossTypes = {"Dragão", "Demônio", "Gigante", "Lich", "Titã"};
        String[] bossNames = {"Rei", "Imperador", "Senhor", "Mestre", "Comandante"};
        
        String bossType = bossTypes[random.nextInt(bossTypes.length)];
        String bossName = bossTypes[random.nextInt(bossTypes.length)] + " " + bossNames[random.nextInt(bossNames.length)];
        int bossLevel = waveNumber + 2;
        
        return new Enemy(bossName, bossType, bossLevel, true);
    }
    
    public GameState progressWave(Character player) {
        currentWave++;
        enemiesPerWave = Math.min(10, 3 + (currentWave / 3)); // Aumentar número de inimigos a cada 3 ondas
        
        return new GameState(player, currentWave, enemiesPerWave, currentWave % 5 == 0);
    }
    
    public boolean isGameComplete(Character player, int waveNumber) {
        // O jogo termina quando o jogador derrota o boss final (onda 25)
        return waveNumber >= 25 && bossWave;
    }
    
    public static class GameState {
        private Character player;
        private int currentWave;
        private int enemiesPerWave;
        private boolean bossWave;
        
        public GameState(Character player, int currentWave, int enemiesPerWave, boolean bossWave) {
            this.player = player;
            this.currentWave = currentWave;
            this.enemiesPerWave = enemiesPerWave;
            this.bossWave = bossWave;
        }
        
        // Getters
        public Character getPlayer() { return player; }
        public int getCurrentWave() { return currentWave; }
        public int getEnemiesPerWave() { return enemiesPerWave; }
        public boolean isBossWave() { return bossWave; }
    }
}
