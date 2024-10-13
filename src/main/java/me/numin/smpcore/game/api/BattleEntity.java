package me.numin.smpcore.game.api;

import me.numin.smpcore.game.MobBattle;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleEntity {

    ItemStack[] HELMETS = {new ItemStack(Material.LEATHER_HELMET), new ItemStack(Material.IRON_HELMET), new ItemStack(Material.DIAMOND_HELMET)};
    ItemStack[] CHESTPLATES = {new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.DIAMOND_CHESTPLATE)};
    ItemStack[] LEGGINGS = {new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.DIAMOND_LEGGINGS)};
    ItemStack[] BOOTS = {new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.DIAMOND_BOOTS)};

    private final Entity entity;
    private final Location location;
    private final MobBattle game;
    private final World world;

    private final boolean doArmor;

    public BattleEntity(MobBattle game, Location location, boolean doArmor) {
        this.game = game;
        this.location = location;
        this.world = location.getWorld();
        this.entity = getRandomEntity();
        this.doArmor = doArmor;

        if (entity instanceof LivingEntity)
            setupArmor((LivingEntity) entity);
    }

    public Entity getEntity() {
        return entity;
    }

    //TODO: Add in other mobs (like creepers after stage 10 or blazes after stage 15 etc)
    public List<Class<? extends Entity>> getMonsters() {
        List<Class<? extends Entity>> monsters = new ArrayList<>();
        monsters.add(Zombie.class);
        monsters.add(Skeleton.class);
        monsters.add(Spider.class);

        if (game.getWave().getStage() >= 5)
            monsters.add(Blaze.class);

        return monsters;
    }

    public Entity getRandomEntity() {
        int i = new Random().nextInt(getMonsters().size());
        return world.spawn(location, getMonsters().get(i));
    }

    public void setupArmor(LivingEntity entity) {
        if (entity == null || !doArmor)
            return;

        if (Math.random() < 0.03) { // full armor
            entity.getEquipment().setHelmet(HELMETS[new Random().nextInt(HELMETS.length)]);
            entity.getEquipment().setChestplate(CHESTPLATES[new Random().nextInt(CHESTPLATES.length)]);
            entity.getEquipment().setLeggings(LEGGINGS[new Random().nextInt(LEGGINGS.length)]);
            entity.getEquipment().setBoots(BOOTS[new Random().nextInt(BOOTS.length)]);
        } else {
            if (Math.random() < 0.1) entity.getEquipment().setHelmet(HELMETS[new Random().nextInt(HELMETS.length)]);
            if (Math.random() < 0.1) entity.getEquipment().setChestplate(CHESTPLATES[new Random().nextInt(CHESTPLATES.length)]);
            if (Math.random() < 0.1) entity.getEquipment().setLeggings(LEGGINGS[new Random().nextInt(LEGGINGS.length)]);
            if (Math.random() < 0.1) entity.getEquipment().setBoots(BOOTS[new Random().nextInt(BOOTS.length)]);
        }
    }
}
