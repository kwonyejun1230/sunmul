/*     */ package com.softhub.softpostbox.manager;
/*     */ 
/*     */ import com.softhub.softframework.config.convert.MessageComponent;
/*     */ import com.softhub.softframework.task.SimpleAsync;
/*     */ import com.softhub.softpostbox.BukkitInitializer;
/*     */ import com.softhub.softpostbox.ItemContainer;
/*     */ import com.softhub.softpostbox.PostboxContainer;
/*     */ import com.softhub.softpostbox.PostboxData;
/*     */ import com.softhub.softpostbox.database.CachedDataService;
/*     */ import com.softhub.softpostbox.database.StorageDataService;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ public class PostboxManager
/*     */ {
/*     */   public static void gift(Player player, String targetName) {
/*  20 */     if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
/*  21 */       player.sendMessage(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "no_add_air", new Object[0]));
/*     */       return;
/*     */     } 
/*  24 */     SimpleAsync.async(() -> {
/*     */           Player target = Bukkit.getPlayer(targetName);
/*     */           if (target != null) {
/*     */             PostboxData postboxData = CachedDataService.get(target.getUniqueId());
/*     */             PostboxContainer container = postboxData.getContainer();
/*     */             ItemContainer item = new ItemContainer(player.getItemInHand().clone());
/*     */             player.setItemInHand(new ItemStack(Material.AIR));
/*     */             ItemContainer itemContainer = new ItemContainer((ItemStack)item);
/*     */             itemContainer.registerItem();
/*     */             container.addItem(itemContainer);
/*     */             String itemDisplay = item.getType().name();
/*     */             if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
/*     */               itemDisplay = item.getItemMeta().getDisplayName();
/*     */             }
/*     */             player.sendMessage(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "success_gift", new Object[] { targetName, itemDisplay }));
/*     */             target.sendMessage(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "success_gift_target", new Object[] { player.getName(), itemDisplay }));
/*     */             StorageDataService.set(postboxData);
/*     */           } else {
/*     */             OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(targetName);
/*     */             if (!offlineTarget.hasPlayedBefore()) {
/*     */               player.sendMessage(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "no_target_player", new Object[] { targetName }));
/*     */               return;
/*     */             } 
/*     */             StorageDataService.get(offlineTarget.getUniqueId()).thenAccept(());
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void give(Player player, String targetName, Integer amount) {
/*  64 */     if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
/*  65 */       player.sendMessage(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "no_add_air", new Object[0]));
/*     */       return;
/*     */     } 
/*  68 */     SimpleAsync.async(() -> {
/*     */           Player target = Bukkit.getPlayer(targetName);
/*     */           if (target != null) {
/*     */             PostboxData postboxData = CachedDataService.get(target.getUniqueId());
/*     */             if (postboxData == null) {
/*     */               player.sendMessage(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "not_load_data", new Object[0]));
/*     */               return;
/*     */             } 
/*     */             PostboxContainer container = postboxData.getContainer();
/*     */             ItemStack item = player.getItemInHand().clone();
/*     */             item.setAmount(amount.intValue());
/*     */             ItemContainer itemContainer = new ItemContainer(item);
/*     */             itemContainer.registerItem();
/*     */             container.addItem(itemContainer);
/*     */             StorageDataService.set(postboxData);
/*     */             String itemDisplay = item.getType().name();
/*     */             if (item.hasItemMeta() && item.getItemMeta().hasDisplayName())
/*     */               itemDisplay = item.getItemMeta().getDisplayName(); 
/*     */             player.sendMessage(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "success_give", new Object[] { targetName, itemDisplay }));
/*     */             target.sendMessage(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "success_give_target", new Object[] { player.getName(), itemDisplay }));
/*     */           } else {
/*     */             OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(targetName);
/*     */             if (!offlineTarget.hasPlayedBefore()) {
/*     */               player.sendMessage(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "no_target_player", new Object[] { targetName }));
/*     */               return;
/*     */             } 
/*     */             StorageDataService.get(offlineTarget.getUniqueId()).thenAccept(());
/*     */           } 
/*     */         });
/*     */   }
/*     */ }


/* Location:              D:\Downloads\SoftPostbox-1.0.jar!\com\softhub\softpostbox\manager\PostboxManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */