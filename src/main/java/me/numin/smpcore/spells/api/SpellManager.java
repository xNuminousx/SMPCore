package me.numin.smpcore.spells.api;

import me.numin.smpcore.utils.Familiar;
import org.bukkit.entity.Player;

public class SpellManager implements Runnable {

    @Override
    public void run() {
        for (Spell spell : Spell.spells.values()) {
            Player player = spell.getPlayer();

            if (player.isDead() || !player.isOnline()) {
                return;
            }

            try {
                spell.cast();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Familiar familiar : Familiar.familiars) {
            Player host = familiar.getHost();

            if (host.isDead() || !host.isOnline()) {
                familiar.kill();
                return;
            }

            try {
                familiar.move();
            } catch (Exception e) {
                familiar.kill();
                e.printStackTrace();
            }
        }
    }
}
