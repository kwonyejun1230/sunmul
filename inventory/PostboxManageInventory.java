/*     */ package com.softhub.softpostbox.inventory;
/*     */ 
/*     */ import com.softhub.softframework.config.convert.MessageComponent;
/*     */ import com.softhub.softframework.inventory.SimpleClickEvent;
/*     */ import com.softhub.softframework.inventory.SimpleCloseEvent;
/*     */ import com.softhub.softframework.inventory.SimpleInventory;
/*     */ import com.softhub.softframework.inventory.SimpleInventoryProvider;
/*     */ import com.softhub.softframework.item.SimpleItem;
/*     */ import com.softhub.softframework.task.SimpleAsync;
/*     */ import com.softhub.softpostbox.BukkitInitializer;
/*     */ import com.softhub.softpostbox.ItemContainer;
/*     */ import com.softhub.softpostbox.PostboxContainer;
/*     */ import com.softhub.softpostbox.PostboxData;
/*     */ import com.softhub.softpostbox.config.ConfigManager;
/*     */ import com.softhub.softpostbox.database.CachedDataService;
/*     */ import com.softhub.softpostbox.database.StorageDataService;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ public class PostboxManageInventory
/*     */   implements SimpleInventoryProvider {
/*  28 */   private final Map<Integer, ItemContainer> slotItemMap = new HashMap<>();
/*  29 */   private int page = 1;
/*     */   private SimpleItem backButton;
/*     */   private SimpleItem nextButton;
/*     */   private String targetName;
/*     */   private UUID targetId;
/*     */   private PostboxData postboxData;
/*     */   private static final int ITEMS_PER_PAGE = 45;
/*     */   
/*     */   public PostboxManageInventory(String targetName) {
/*  38 */     this.targetName = targetName;
/*  39 */     this.backButton = new SimpleItem(Material.ARROW);
/*  40 */     this.backButton.setName("이전 페이지");
/*  41 */     this.nextButton = new SimpleItem(Material.ARROW);
/*  42 */     this.nextButton.setName("다음 페이지");
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(Player player) {
/*  47 */     Player target = Bukkit.getPlayer(this.targetName);
/*  48 */     if (target != null) {
/*  49 */       this.targetId = target.getUniqueId();
/*  50 */       PostboxData postboxData = CachedDataService.get(target.getUniqueId());
/*  51 */       PostboxContainer container = postboxData.getContainer();
/*  52 */       this.postboxData = postboxData;
/*  53 */       container.sortItems();
/*  54 */       SimpleInventory simpleInventory = new SimpleInventory(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "postbox_inventory_title", new Object[] { Integer.valueOf(this.page) }), 54);
/*  55 */       simpleInventory.register(this);
/*     */       
/*  57 */       this.slotItemMap.clear();
/*     */       
/*  59 */       int startIndex = (this.page - 1) * 45;
/*  60 */       int endIndex = Math.min(startIndex + 45, container.getItemList().size());
/*  61 */       int slot = 0;
/*     */       
/*  63 */       for (int i = startIndex; i < endIndex; i++) {
/*  64 */         ItemContainer item = container.getItemList().get(i);
/*  65 */         if (item != null && item.getType() != Material.AIR) {
/*  66 */           this.slotItemMap.put(Integer.valueOf(slot), item);
/*  67 */           SimpleItem displayItem = new SimpleItem((ItemStack)item);
/*  68 */           displayItem.addLore(" ");
/*  69 */           displayItem.addLore(" 만료까지 남은 시간: " + MessageComponent.formatTime(item.getTimeUntilExpire()));
/*  70 */           simpleInventory.setItem(slot, displayItem);
/*  71 */           slot++;
/*     */         } 
/*     */       } 
/*     */       
/*  75 */       if (this.page > 1) {
/*  76 */         simpleInventory.setItem(46, this.backButton);
/*     */       }
/*  78 */       if (endIndex < container.getItemList().size()) {
/*  79 */         simpleInventory.setItem(52, this.nextButton);
/*     */       }
/*     */       
/*  82 */       simpleInventory.open(player);
/*     */     } else {
/*  84 */       OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(this.targetName);
/*  85 */       if (!offlineTarget.hasPlayedBefore()) {
/*  86 */         player.sendMessage(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "no_target_player", new Object[] { this.targetName }));
/*     */         return;
/*     */       } 
/*  89 */       this.targetId = offlineTarget.getUniqueId();
/*  90 */       StorageDataService.get(offlineTarget.getUniqueId()).thenAccept(postboxData -> {
/*     */             PostboxContainer container = postboxData.getContainer();
/*     */             this.postboxData = postboxData;
/*     */             container.sortItems();
/*     */             SimpleInventory simpleInventory = new SimpleInventory("우편함 - " + this.page + "페이지", 54);
/*     */             simpleInventory.register(this);
/*     */             this.slotItemMap.clear();
/*     */             int startIndex = (this.page - 1) * 45;
/*     */             int endIndex = Math.min(startIndex + 45, container.getItemList().size());
/*     */             int slot = 0;
/*     */             for (int i = startIndex; i < endIndex; i++) {
/*     */               ItemContainer item = container.getItemList().get(i);
/*     */               if (item != null && item.getType() != Material.AIR) {
/*     */                 this.slotItemMap.put(Integer.valueOf(slot), item);
/*     */                 SimpleItem displayItem = new SimpleItem((ItemStack)item);
/*     */                 if (ConfigManager.getExpiredEnabled().booleanValue()) {
/*     */                   displayItem.addLore(" ");
/*     */                   displayItem.addLore(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "expired_time", new Object[] { MessageComponent.formatTime(item.getTimeUntilExpire()) }));
/*     */                 } 
/*     */                 simpleInventory.setItem(slot, displayItem);
/*     */                 slot++;
/*     */               } 
/*     */             } 
/*     */             if (this.page > 1) {
/*     */               simpleInventory.setItem(46, this.backButton);
/*     */             }
/*     */             if (endIndex < container.getItemList().size()) {
/*     */               simpleInventory.setItem(52, this.nextButton);
/*     */             }
/*     */             SimpleAsync.sync(());
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onClick(SimpleClickEvent event) {
/* 131 */     event.setCancelled(true);
/* 132 */     if (event.getClickItem() == null || event.getClickItem().getType() == Material.AIR)
/* 133 */       return;  Player player = event.getPlayer();
/* 134 */     PostboxManageInventory postboxInventory = (PostboxManageInventory)event.getSimpleInventory().getProvider();
/* 135 */     if (postboxInventory == null)
/*     */       return; 
/* 137 */     if (event.getClickItem().equals(postboxInventory.backButton)) {
/* 138 */       if (postboxInventory.page > 1) {
/* 139 */         postboxInventory.page--;
/* 140 */         postboxInventory.init(player);
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 145 */     if (event.getClickItem().equals(postboxInventory.nextButton)) {
/* 146 */       postboxInventory.page++;
/* 147 */       postboxInventory.init(player);
/*     */       
/*     */       return;
/*     */     } 
/* 151 */     PostboxData postboxData = postboxInventory.postboxData;
/* 152 */     ItemContainer itemContainer = postboxInventory.slotItemMap.get(Integer.valueOf(event.getSlot()));
/*     */     
/* 154 */     if (postboxData == null) {
/* 155 */       player.sendMessage(MessageComponent.formatMessage(BukkitInitializer.getInstance().getConfig(), "not_load_data", new Object[0]));
/* 156 */       player.closeInventory();
/*     */       
/*     */       return;
/*     */     } 
/* 160 */     PostboxContainer container = postboxInventory.postboxData.getContainer();
/* 161 */     Player target = Bukkit.getPlayer(postboxInventory.targetId);
/* 162 */     container.give(player, itemContainer);
/* 163 */     if (target == null) {
/* 164 */       StorageDataService.set(postboxInventory.postboxData).thenRun(() -> SimpleAsync.sync(()));
/*     */     }
/*     */     else {
/*     */       
/* 168 */       postboxInventory.init(player);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onClose(SimpleCloseEvent event) {}
/*     */ }


/* Location:              D:\Downloads\SoftPostbox-1.0.jar!\com\softhub\softpostbox\inventory\PostboxManageInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */