package me.aglerr.mobcoins.utils;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
import fr.mrmicky.fastinv.ItemBuilder;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.shops.items.TypeItem;
import me.aglerr.mobcoins.utils.libs.Common;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ItemManager {

    public static ItemStack createItemStackWithHeadTextures(Player player, TypeItem item, List<String> lore){
        ItemStack stack;

        if(item.getMaterial().contains(";")){
            String[] split = item.getMaterial().split(";");
            if(split[0].equalsIgnoreCase("head")){
                stack = XMaterial.PLAYER_HEAD.parseItem();
                SkullMeta skullMeta = (SkullMeta) stack.getItemMeta();
                skullMeta.setDisplayName(PlaceholderAPI.setPlaceholders(player, item.getName()));
                skullMeta.setLore(PlaceholderAPI.setPlaceholders(player, lore));
                skullMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                SkullUtils.applySkin(skullMeta, PlaceholderAPI.setPlaceholders(player, split[1]));
                if(item.isGlow()){
                    skullMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                    skullMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                stack.setItemMeta(skullMeta);
                stack.setAmount(item.getAmount() <= 0 ? 1 : item.getAmount());
                return stack;
            }
        }

        ItemStack optionalStack;
        Optional<XMaterial> optionalMaterial = XMaterial.matchXMaterial(item.getMaterial());
        if(optionalMaterial.isPresent()){
            optionalStack = optionalMaterial.get().parseItem();
        } else {
            optionalStack = new ItemStack(Material.BARREL);
            ItemMeta im = optionalStack.getItemMeta();
            im.setDisplayName(Common.color("&c&lERROR!"));
            im.setLore(Common.color(Collections.singletonList("&7Please check the configuration!")));
            optionalStack.setItemMeta(im);
            return optionalStack;
        }

        ItemBuilder builder = new ItemBuilder(optionalStack)
                .name(PlaceholderAPI.setPlaceholders(player, item.getName()))
                .lore(PlaceholderAPI.setPlaceholders(player, lore))
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .amount(item.getAmount() <= 0 ? 1 : item.getAmount());

        if(item.isGlow()) builder.enchant(Enchantment.ARROW_INFINITE).flags(ItemFlag.HIDE_ENCHANTS);
        stack = builder.build();
        return stack;
    }

    public static ItemStack createMobCoinItem(double amount){

        ItemStack stack = null;

        List<String> parsedLore = new ArrayList<>();
        ConfigValue.MOBCOINS_ITEM_LORE.forEach(line -> parsedLore.add(line.replace("{amount}", String.valueOf(amount))));

        if(ConfigValue.MOBCOINS_ITEM_MATERIAL.contains(";")){
            String[] split = ConfigValue.MOBCOINS_ITEM_MATERIAL.split(";");
            if(split[0].equalsIgnoreCase("head")){
                stack = XMaterial.PLAYER_HEAD.parseItem();
                SkullMeta skullMeta = (SkullMeta) stack.getItemMeta();
                skullMeta.setDisplayName(Common.color(ConfigValue.MOBCOINS_ITEM_NAME));
                skullMeta.setLore(Common.color(parsedLore));
                if(ConfigValue.MOBCOINS_ITEM_GLOW){
                    skullMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                    skullMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                skullMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                stack.setItemMeta(skullMeta);
            }
        } else {
            ItemBuilder builder = new ItemBuilder(XMaterial.matchXMaterial(ConfigValue.MOBCOINS_ITEM_MATERIAL).get().parseItem())
                    .name(Common.color(ConfigValue.MOBCOINS_ITEM_NAME))
                    .lore(Common.color(parsedLore))
                    .flags(ItemFlag.HIDE_ATTRIBUTES);

            if(ConfigValue.MOBCOINS_ITEM_GLOW) builder.enchant(Enchantment.ARROW_INFINITE).flags(ItemFlag.HIDE_ENCHANTS);
            stack = builder.build();
        }

        NBTItem nbtItem = new NBTItem(stack);
        nbtItem.setDouble("mobCoinsAmount", amount);
        return nbtItem.getItem();
    }

}
