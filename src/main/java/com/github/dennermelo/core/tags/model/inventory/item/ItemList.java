package com.github.dennermelo.core.tags.model.inventory.item;

import com.github.dennermelo.core.tags.model.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ItemList {

    public static ItemStack filter,
            show_equipped,
            show_owned,
            not_owned,
            all_tags,
            next_page,
            previous_page;


    public void load() {
        // 8 cinza; 10 verde;
        filter = new ItemBuilder(Material.ANVIL).setName("§8Filtrar tags").setLore(Arrays.asList("", "§7Clique para filtrar tags por raridade.")).build();
        show_equipped = new ItemBuilder(Material.INK_SACK).setDurability((short) 8).setName("§7Mostrar Tags Equipadas").setLore(Arrays.asList("", "§7Clique para mostrar tags equipadas.")).build();
        show_owned = new ItemBuilder(Material.INK_SACK).setDurability((short) 8).setName("§7Mostrar Tags Compradas").setLore(Arrays.asList("", "§7Clique para mostrar tags compradas.")).build();
        not_owned = new ItemBuilder(Material.INK_SACK).setDurability((short) 8).setName("§7Mostrar Tags Não Compradas").setLore(Arrays.asList("", "§7Clique para mostrar tags não compradas.")).build();
        all_tags = new ItemBuilder(Material.INK_SACK).setDurability((short) 8).setName("§7Mostrar Todas as Tags").setLore(Arrays.asList("", "§7Clique para visualizar todas as tags do servidor.")).build();

        next_page = new ItemBuilder(Material.ARROW).setName("§aPróxima Página").build();
        previous_page = new ItemBuilder(Material.ARROW).setName("§aPágina Anterior").build();
    }
}
