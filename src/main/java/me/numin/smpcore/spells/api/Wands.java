package me.numin.smpcore.spells.api;

import me.numin.smpcore.SMPCore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class Wands {

    public Wands() {
        Bukkit.addRecipe(damagingRecipe());
        Bukkit.addRecipe(familiarRecipe());
        Bukkit.addRecipe(healingRecipe());
    }

    public ShapedRecipe damagingRecipe() {
        NamespacedKey key = new NamespacedKey(SMPCore.plugin, "spell_of_damaging");
        ShapedRecipe recipe = new ShapedRecipe(key, WandItem.SPELL_OF_DAMAGING.getWand());
        recipe.shape(" N ", "FSF", " N ");
        recipe.setIngredient('S', Material.STICK);
        recipe.setIngredient('N', Material.NETHER_WART);
        recipe.setIngredient('F', Material.FERMENTED_SPIDER_EYE);
        return recipe;
    }

    public ShapedRecipe familiarRecipe() {
        NamespacedKey key = new NamespacedKey(SMPCore.plugin, "spell_of_familiar");
        ShapedRecipe recipe = new ShapedRecipe(key, WandItem.SPELL_OF_FAMILIAR.getWand());
        recipe.shape(" N ", "BSE", " N ");
        recipe.setIngredient('S', Material.STICK);
        recipe.setIngredient('N', Material.NETHER_WART);
        recipe.setIngredient('B', Material.BONE);
        recipe.setIngredient('E', Material.EGG);
        return recipe;
    }

    public ShapedRecipe healingRecipe() {
        NamespacedKey key = new NamespacedKey(SMPCore.plugin, "spell_of_healing");
        ShapedRecipe recipe = new ShapedRecipe(key, WandItem.SPELL_OF_HEALING.getWand());
        recipe.shape(" N ", "GSG", " N ");
        recipe.setIngredient('S', Material.STICK);
        recipe.setIngredient('N', Material.NETHER_WART);
        recipe.setIngredient('G', Material.GLISTERING_MELON_SLICE);
        return recipe;
    }

    public enum WandItem {
        SPELL_OF_DAMAGING(spellOfDamaging()),
        SPELL_OF_FAMILIAR(spellOfFamiliar()),
        SPELL_OF_HEALING(spellOfHealing());

        private final ItemStack wand;

        WandItem(ItemStack wand) {
            this.wand = wand;
        }

        public ItemStack getWand() {
            return wand;
        }
    }

    public static ItemStack spellOfDamaging() {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Spell of Damaging");
        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> lore = Arrays.asList("Spells left:","100");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack spellOfFamiliar() {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Spell of Familiar");
        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack spellOfHealing() {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Spell of Healing");
        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> lore = Arrays.asList("Spells left:","100");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isWand(ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS);
    }
}
