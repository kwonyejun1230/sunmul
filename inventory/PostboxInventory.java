/*     */ package com.softhub.softpostbox.inventory;
/*     */ 
/*     */ import com.softhub.softframework.config.convert.ItemConponent;
/*     */ import com.softhub.softframework.config.convert.MessageComponent;
/*     */ import com.softhub.softframework.inventory.SimpleClickEvent;
/*     */ import com.softhub.softframework.inventory.SimpleCloseEvent;
/*     */ import com.softhub.softframework.inventory.SimpleInventory;
/*     */ import com.softhub.softframework.inventory.SimpleInventoryProvider;
/*     */ import com.softhub.softframework.item.SimpleItem;
/*     */ import com.softhub.softpostbox.BukkitInitializer;
/*     */ import com.softhub.softpostbox.ItemContainer;
/*     */ import com.softhub.softpostbox.PostboxContainer;
/*     */ import com.softhub.softpostbox.PostboxData;
/*     */ import com.softhub.softpostbox.config.ConfigManager;
/*     */ import com.softhub.softpostbox.database.CachedDataService;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ public class PostboxInventory
/*     */   implements SimpleInventoryProvider {
/*  24 */   private final Map<Integer, ItemContainer> slotItemMap = new HashMap<>();
/*  25 */   private int page = 1;
/*     */   private SimpleItem backButton;
/*     */   private SimpleItem nextButton;
/*     */   private SimpleItem collectButton;
/*     */   private static final int ITEMS_PER_PAGE = 45;
/*     */   
/*     */   public PostboxInventory() {
/*  32 */     this.backButton = ItemConponent.loadItem(BukkitInitializer.getInstance().getConfig(), "back_button", new Object[0]);
/*  33 */     this.nextButton = ItemConponent.loadItem(BukkitInitializer.getInstance().getConfig(), "next_button", new Object[0]);
/*  34 */     this.collectButton = ItemConponent.loadItem(BukkitInitializer.getInstance().getConfig(), "collect_button", new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(Player player) {
/*  39 */     PostboxData postboxData = CachedDataService.get(player.getUniqueId());
/*  40 */     PostboxContainer container = postboxData.getContainer();
/*  41 */     container.sortItems();
/*  42 */     SimpleInventory simpleInventory = new SimpleInventory(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "postbox_inventory_title", new Object[] { Integer.valueOf(this.page) }), 54);
/*  43 */     simpleInventory.register(this);
/*     */     
/*  45 */     this.slotItemMap.clear();
/*     */     
/*  47 */     int startIndex = (this.page - 1) * 45;
/*  48 */     int endIndex = Math.min(startIndex + 45, container.getItemList().size());
/*  49 */     int slot = 0;
/*     */     
/*  51 */     for (int i = startIndex; i < endIndex; i++) {
/*  52 */       ItemContainer item = container.getItemList().get(i);
/*  53 */       if (item != null && item.getType() != Material.AIR) {
/*  54 */         this.slotItemMap.put(Integer.valueOf(slot), item);
/*  55 */         SimpleItem displayItem = new SimpleItem((ItemStack)item);
/*  56 */         if (ConfigManager.getExpiredEnabled().booleanValue()) {
/*  57 */           displayItem.addLore(" ");
/*  58 */           displayItem.addLore(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "expired_time", new Object[] { MessageComponent.formatTime(item.getTimeUntilExpire()) }));
/*     */         } 
/*  60 */         simpleInventory.setItem(slot, displayItem);
/*  61 */         slot++;
/*     */       } 
/*     */     } 
/*     */     
/*  65 */     if (this.page > 1) {
/*  66 */       simpleInventory.setItem(46, this.backButton);
/*     */     }
/*  68 */     if (endIndex < container.getItemList().size()) {
/*  69 */       simpleInventory.setItem(52, this.nextButton);
/*     */     }
/*     */     
/*  72 */     simpleInventory.setItem(49, this.collectButton);
/*     */     
/*  74 */     simpleInventory.open(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClick(SimpleClickEvent event) {
/*  79 */     event.setCancelled(true);
/*  80 */     if (event.getClickItem() == null || event.getClickItem().getType() == Material.AIR)
/*  81 */       return;  Player player = event.getPlayer();
/*  82 */     PostboxInventory postboxInventory = (PostboxInventory)event.getSimpleInventory().getProvider();
/*  83 */     if (postboxInventory == null)
/*     */       return; 
/*  85 */     if (event.getClickItem().equals(postboxInventory.backButton)) {
/*  86 */       if (postboxInventory.page > 1) {
/*  87 */         postboxInventory.page--;
/*  88 */         postboxInventory.init(player);
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*  93 */     if (event.getClickItem().equals(postboxInventory.nextButton)) {
/*  94 */       postboxInventory.page++;
/*  95 */       postboxInventory.init(player);
/*     */       
/*     */       return;
/*     */     } 
/*  99 */     if (event.getClickItem().equals(postboxInventory.collectButton)) {
/* 100 */       PostboxData postboxData1 = CachedDataService.get(player.getUniqueId());
/* 101 */       postboxData1.getContainer().collectAll(player);
/* 102 */       player.closeInventory();
/*     */       
/*     */       return;
/*     */     } 
/* 106 */     PostboxData postboxData = CachedDataService.get(player.getUniqueId());
/* 107 */     ItemContainer itemContainer = postboxInventory.slotItemMap.get(Integer.valueOf(event.getSlot()));
/*     */     
/* 109 */     if (postboxData == null) {
/* 110 */       player.sendMessage(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "not_load_data", new Object[0]));
/* 111 */       player.closeInventory();
/*     */       
/*     */       return;
/*     */     } 
/* 115 */     PostboxContainer container = postboxData.getContainer();
/* 116 */     container.give(player, itemContainer);
/* 117 */     postboxInventory.init(player);
/*     */   }
/*     */   
/*     */   public void onClose(SimpleCloseEvent event) {}
/*     */ }


/* Location:              D:\Downloads\SoftPostbox-1.0.jar!\com\softhub\softpostbox\inventory\PostboxInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */