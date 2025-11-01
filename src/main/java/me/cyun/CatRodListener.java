package me.cyun;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.*;
import net.kyori.adventure.text.Component;

import java.util.concurrent.ThreadLocalRandom;

public class CatRodListener implements Listener {

    private static final String OBJECTIVE_NAME = "cat_points";
    private final CatRodPlugin plugin;
    private final NamespacedKey catKey;

    public CatRodListener(CatRodPlugin plugin) {
        this.plugin = plugin;
        this.catKey = new NamespacedKey(plugin, "cat_points");
        ensureObjective();
    }

    /* 确保计分板存在（隐藏） */
    private void ensureObjective() {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        if (board.getObjective(OBJECTIVE_NAME) == null) {
            Objective obj = board.registerNewObjective(OBJECTIVE_NAME, Criteria.DUMMY,
                    Component.text("CatPoints"), RenderType.INTEGER);
            obj.setDisplaySlot(null); // 不显示
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onAttack(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player attacker)) return;
        if (!(e.getEntity() instanceof Player victim)) return;

        ItemStack hand = attacker.getInventory().getItemInMainHand();
        if (hand.getType() != Material.BLAZE_ROD) return;

        // 1. 给予受害者 5 秒生命恢复 II
        victim.addPotionEffect(new org.bukkit.potion.PotionEffect(
                org.bukkit.potion.PotionEffectType.REGENERATION,
                5 * 20, 1, false, false, true));

        // 2. 30% 掉落 5 经验
        if (ThreadLocalRandom.current().nextInt(100) < 30) {
            ExperienceOrb orb = (ExperienceOrb) victim.getWorld().spawnEntity(
                    victim.getLocation(), EntityType.EXPERIENCE_ORB);
            orb.setExperience(40);
        }

        // 3. 增加猫猫数值
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Score score = board.getObjective(OBJECTIVE_NAME).getScore(attacker.getName());
        int catPoints = score.getScore() + 1;
        score.setScore(catPoints);

        attacker.getWorld().playSound(attacker.getLocation(),
                Sound.ENTITY_CAT_AMBIENT, 1F, 1F);

        // 4. 如果 >50，尝试生成猫
        if (catPoints > 50) {
            int chance = (catPoints - 50) * 2;
            if (ThreadLocalRandom.current().nextInt(100) < chance) {
                spawnKitten(attacker, victim);
                score.setScore(0); // 清零
            }
        }
    }

    /* 生成小猫 */
    private void spawnKitten(Player owner, Player target) {
        World w = owner.getWorld();
        Location loc = owner.getLocation().add(
                ThreadLocalRandom.current().nextDouble(-2, 2),
                0,
                ThreadLocalRandom.current().nextDouble(-2, 2));
        loc = loc.getWorld().getHighestBlockAt(loc).getLocation().add(0, 1, 0);

        Cat cat = (Cat) w.spawnEntity(loc, EntityType.CAT);
        cat.setOwner(owner);
        cat.setCustomName(target.getName() + "的猫崽");
        cat.setCustomNameVisible(true);
        cat.setAge(-24000); // 小猫
        cat.setBreed(false); // 不可繁殖
        cat.setPersistent(true);

        // 音效 & 粒子
        w.playSound(loc, Sound.ENTITY_CAT_AMBIENT, 1f, 1f);
        w.spawnParticle(Particle.HEART, loc.add(0, 1, 0), 5, 0.5, 0.5, 0.5, 0);
    }
}