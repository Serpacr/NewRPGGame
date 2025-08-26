package com.example.rpggame.controller;

import com.example.rpggame.model.Character;
import com.example.rpggame.model.Enemy;
import com.example.rpggame.model.Item;
import com.example.rpggame.service.CombatService;
import com.example.rpggame.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@Controller
public class GameController {
    
    @Autowired
    private GameService gameService;
    
    @Autowired
    private CombatService combatService;
    
    private GameService.GameState currentGameState;
    
    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }
    
    @GetMapping("/character-creation")
    public String characterCreation(Model model) {
        return "character-creation";
    }
    
    @PostMapping("/start-game")
    public String startGame(@RequestParam String characterName, 
                           @RequestParam String characterClass, 
                           Model model) {
        currentGameState = gameService.startNewGame(characterName, characterClass);
        model.addAttribute("gameState", currentGameState);
        return "redirect:/game";
    }
    
    @GetMapping("/game")
    public String game(Model model) {
        if (currentGameState == null) {
            return "redirect:/";
        }
        
        List<Enemy> currentWave = gameService.generateWave(currentGameState.getCurrentWave());
        model.addAttribute("gameState", currentGameState);
        model.addAttribute("currentWave", currentWave);
        model.addAttribute("player", currentGameState.getPlayer());
        
        return "game";
    }
    
    @PostMapping("/battle")
    @ResponseBody
    public CombatService.CombatResult battle(@RequestParam int enemyIndex) {
        if (currentGameState == null) {
            // Retornar um resultado de erro em vez de null
            CombatService.CombatResult errorResult = new CombatService.CombatResult();
            List<String> errorLog = new ArrayList<>();
            errorLog.add("Erro: Nenhum jogo ativo. Crie um personagem primeiro.");
            errorResult.setBattleLog(errorLog);
            errorResult.setVictory(false);
            return errorResult;
        }
        
        List<Enemy> currentWave = gameService.generateWave(currentGameState.getCurrentWave());
        if (enemyIndex >= 0 && enemyIndex < currentWave.size()) {
            Enemy enemy = currentWave.get(enemyIndex);
            CombatService.CombatResult result = combatService.battle(currentGameState.getPlayer(), enemy);
            
            // Adicionar o item dropado ao inventário do jogador se houver
            if (result.isVictory() && result.getDroppedItem() != null) {
                currentGameState.getPlayer().addItem(result.getDroppedItem());
            }
            
            return result;
        }
        
        // Retornar erro se o índice do inimigo for inválido
        CombatService.CombatResult errorResult = new CombatService.CombatResult();
        List<String> errorLog = new ArrayList<>();
        errorLog.add("Erro: Índice de inimigo inválido.");
        errorResult.setBattleLog(errorLog);
        errorResult.setVictory(false);
        return errorResult;
    }
    
    @PostMapping("/next-wave")
    public String nextWave(Model model) {
        if (currentGameState != null) {
            currentGameState = gameService.progressWave(currentGameState.getPlayer());
            
            // Verificar se o jogo foi completado
            if (gameService.isGameComplete(currentGameState.getPlayer(), currentGameState.getCurrentWave())) {
                model.addAttribute("victory", true);
                model.addAttribute("player", currentGameState.getPlayer());
                return "victory";
            }
            
            model.addAttribute("gameState", currentGameState);
            return "redirect:/game";
        }
        
        return "redirect:/";
    }
    
    @PostMapping("/equip-item")
    @ResponseBody
    public String equipItem(@RequestParam int itemIndex) {
        if (currentGameState != null && currentGameState.getPlayer() != null) {
            Character player = currentGameState.getPlayer();
            if (itemIndex >= 0 && itemIndex < player.getInventory().size()) {
                player.equipItem(player.getInventory().get(itemIndex));
                return "Item equipado com sucesso!";
            }
        }
        return "Erro ao equipar item!";
    }
    
    @PostMapping("/unequip-item")
    @ResponseBody
    public String unequipItem(@RequestParam int itemIndex) {
        if (currentGameState != null && currentGameState.getPlayer() != null) {
            Character player = currentGameState.getPlayer();
            if (itemIndex >= 0 && itemIndex < player.getEquippedItems().size()) {
                player.unequipItem(player.getEquippedItems().get(itemIndex));
                return "Item desequipado com sucesso!";
            }
        }
        return "Erro ao desequipar item!";
    }
    
    @PostMapping("/use-potion")
    @ResponseBody
    public String usePotion() {
        if (currentGameState != null && currentGameState.getPlayer() != null) {
            Character player = currentGameState.getPlayer();
            
            // Procurar por poções no inventário
            for (int i = 0; i < player.getInventory().size(); i++) {
                Item item = player.getInventory().get(i);
                if (item.getType() == com.example.rpggame.model.Item.ItemType.POTION) {
                    player.heal(item.getHealthBonus());
                    player.getInventory().remove(i);
                    return "Poção usada! HP restaurado em " + item.getHealthBonus();
                }
            }
            
            return "Nenhuma poção encontrada no inventário!";
        }
        
        return "Erro ao usar poção!";
    }
    
    @GetMapping("/inventory")
    public String inventory(Model model) {
        if (currentGameState != null) {
            model.addAttribute("player", currentGameState.getPlayer());
            return "inventory";
        }
        return "redirect:/";
    }
    
    @GetMapping("/restart")
    public String restart() {
        currentGameState = null;
        return "redirect:/";
    }
}
