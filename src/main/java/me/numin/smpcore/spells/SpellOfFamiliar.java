package me.numin.smpcore.spells;

import me.numin.smpcore.spells.api.Spell;
import me.numin.smpcore.utils.Familiar;
import org.bukkit.entity.Player;

public class SpellOfFamiliar extends Spell {

    public SpellOfFamiliar(Player player) {
        super(player);
    }

    @Override
    public void cast() {
        for (Familiar familiar : Familiar.familiars) {
            if (familiar.getHost().equals(getPlayer())) {
                familiar.kill();
                getPlayer().sendMessage("Your familiar has returned to your wand.");
                remove();
                return;
            }
        }
        new Familiar(getPlayer());
        remove();
    }
}
