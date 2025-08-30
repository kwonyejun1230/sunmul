/*     */ package com.softhub.softpostbox;
/*     */ import com.softhub.softframework.config.convert.MessageComponent;
/*     */ import com.softhub.softpostbox.config.ConfigManager;
/*     */ import java.time.Instant;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import lombok.Generated;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ 
/*     */ public class PostboxContainer {
/*  16 */   private List<ItemContainer> itemList = new ArrayList<>(); @Generated public List<ItemContainer> getItemList() { return this.itemList; } @Generated
/*  17 */   public void setItemList(List<ItemContainer> itemList) { this.itemList = itemList; }
/*     */ 
/*     */   
/*     */   public PostboxContainer(List<ItemContainer> itemList) {
/*  21 */     this.itemList = itemList;
/*  22 */     sortItems();
/*     */   }
/*     */   
/*     */   public void addItem(ItemContainer item) {
/*  26 */     this.itemList.add(item);
/*  27 */     sortItems();
/*     */   }
/*     */   
/*     */   public void removeItem(ItemContainer item) {
/*  31 */     int amountToRemove = item.getAmount();
/*  32 */     Iterator<ItemContainer> iterator = this.itemList.iterator();
/*     */     
/*  34 */     while (iterator.hasNext() && amountToRemove > 0) {
/*  35 */       ItemContainer currentItem = iterator.next();
/*  36 */       if (currentItem.isSimilar((ItemStack)item)) {
/*  37 */         int currentAmount = currentItem.getAmount();
/*  38 */         if (currentAmount > amountToRemove) {
/*  39 */           currentItem.setAmount(currentAmount - amountToRemove);
/*  40 */           amountToRemove = 0; continue;
/*     */         } 
/*  42 */         amountToRemove -= currentAmount;
/*  43 */         iterator.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sortItems() {
/*  50 */     Map<String, List<ItemContainer>> itemMap = new HashMap<>();
/*     */     
/*  52 */     for (ItemContainer item : this.itemList) {
/*  53 */       if (item.isExpired() && ConfigManager.getExpiredEnabled().booleanValue()) {
/*     */         continue;
/*     */       }
/*     */       
/*  57 */       String key = item.getType().toString() + ":" + item.getDurability() + ":" + item.getItemMeta().toString();
/*  58 */       itemMap.putIfAbsent(key, new ArrayList<>());
/*     */       
/*  60 */       List<ItemContainer> stacks = itemMap.get(key);
/*  61 */       int amount = item.getAmount();
/*     */       
/*  63 */       for (ItemContainer stack : stacks) {
/*  64 */         int space = 64 - stack.getAmount();
/*  65 */         if (space > 0) {
/*  66 */           int toAdd = Math.min(space, amount);
/*  67 */           stack.setAmount(stack.getAmount() + toAdd);
/*  68 */           amount -= toAdd;
/*  69 */           if (amount <= 0) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/*  75 */       while (amount > 0) {
/*  76 */         int toAdd = Math.min(64, amount);
/*  77 */         ItemContainer newStack = item.clone();
/*  78 */         newStack.setAmount(toAdd);
/*  79 */         stacks.add(newStack);
/*  80 */         amount -= toAdd;
/*     */       } 
/*     */     } 
/*     */     
/*  84 */     this.itemList = new ArrayList<>();
/*  85 */     for (List<ItemContainer> stacks : itemMap.values()) {
/*  86 */       ItemContainer mergedItem = mergeItemContainers(stacks);
/*  87 */       this.itemList.add(mergedItem);
/*     */     } 
/*     */   }
/*     */   
/*     */   private ItemContainer mergeItemContainers(List<ItemContainer> containers) {
/*  92 */     if (containers.isEmpty()) {
/*  93 */       return null;
/*     */     }
/*     */     
/*  96 */     ItemContainer result = ((ItemContainer)containers.get(0)).clone();
/*  97 */     int totalAmount = 0;
/*  98 */     Instant maxExpireTime = result.getExpireTime();
/*     */     
/* 100 */     for (ItemContainer container : containers) {
/* 101 */       totalAmount += container.getAmount();
/* 102 */       if (maxExpireTime == null || (container.getExpireTime() != null && container.getExpireTime().isAfter(maxExpireTime))) {
/* 103 */         maxExpireTime = container.getExpireTime();
/*     */       }
/*     */     } 
/*     */     
/* 107 */     result.setAmount(totalAmount);
/* 108 */     result.setExpireTime(maxExpireTime);
/* 109 */     return result;
/*     */   }
/*     */   
/*     */   public void give(Player player, ItemContainer item) {
/* 113 */     PlayerInventory playerInventory = player.getInventory();
/* 114 */     if (playerInventory.firstEmpty() == -1) {
/* 115 */       player.sendMessage(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "full_inventory", new Object[0]));
/*     */       
/*     */       return;
/*     */     } 
/* 119 */     boolean itemFound = false;
/* 120 */     Iterator<ItemContainer> iterator = this.itemList.iterator();
/*     */     
/* 122 */     while (iterator.hasNext()) {
/* 123 */       ItemContainer currentItem = iterator.next();
/* 124 */       if (currentItem.isSameItem(item)) {
/* 125 */         itemFound = true;
/*     */         
/* 127 */         ItemContainer itemToGive = currentItem.clone();
/*     */         
/* 129 */         for (ItemStack remainingItem : player.getInventory().addItem(new ItemStack[] { (ItemStack)itemToGive }).values()) {
/* 130 */           player.getWorld().dropItem(player.getLocation(), remainingItem);
/*     */         }
/*     */         
/* 133 */         iterator.remove();
/*     */         
/* 135 */         player.sendMessage(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "success_receive_item", new Object[0]));
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 140 */     if (!itemFound) {
/* 141 */       player.sendMessage(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "no_data_item", new Object[0]));
/*     */     }
/*     */   }
/*     */   
/*     */   public void giveSilent(Player player, ItemContainer item) {
/* 146 */     PlayerInventory playerInventory = player.getInventory();
/* 147 */     if (playerInventory.firstEmpty() == -1) {
/*     */       return;
/*     */     }
/*     */     
/* 151 */     boolean itemFound = false;
/* 152 */     Iterator<ItemContainer> iterator = this.itemList.iterator();
/*     */     
/* 154 */     while (iterator.hasNext()) {
/* 155 */       ItemContainer currentItem = iterator.next();
/* 156 */       if (currentItem.isSameItem(item)) {
/* 157 */         itemFound = true;
/*     */         
/* 159 */         ItemContainer itemToGive = currentItem.clone();
/*     */         
/* 161 */         for (ItemStack remainingItem : player.getInventory().addItem(new ItemStack[] { (ItemStack)itemToGive }).values()) {
/* 162 */           player.getWorld().dropItem(player.getLocation(), remainingItem);
/*     */         }
/*     */         
/* 165 */         iterator.remove();
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/* 171 */     if (!itemFound) {
/* 172 */       player.sendMessage(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "no_data_item", new Object[0]));
/*     */     }
/*     */   }
/*     */   
/*     */   public void collectAll(Player player) {
/* 177 */     List<ItemContainer> itemsToCollect = new ArrayList<>(getItemList());
/* 178 */     int totalItems = itemsToCollect.size();
/* 179 */     int itemsCollected = 0;
/* 180 */     int itemsFailed = 0;
/*     */     
/* 182 */     player.sendMessage(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "start_collect_item", new Object[] { Integer.valueOf(totalItems) }));
/*     */     
/* 184 */     for (ItemContainer item : itemsToCollect) {
/* 185 */       if (player.getInventory().firstEmpty() != -1) {
/* 186 */         giveSilent(player, item);
/* 187 */         itemsCollected++; continue;
/*     */       } 
/* 189 */       itemsFailed++;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 194 */     if (itemsFailed > 0) {
/* 195 */       player.sendMessage(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "error_collect_item", new Object[] { Integer.valueOf(itemsFailed) }));
/*     */     }
/*     */     
/* 198 */     player.sendMessage(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "success_collect_item", new Object[] { Integer.valueOf(totalItems), Integer.valueOf(itemsCollected) }));
/*     */   }
/*     */ }


/* Location:              D:\Downloads\SoftPostbox-1.0.jar!\com\softhub\softpostbox\PostboxContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */