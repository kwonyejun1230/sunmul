/*    */ package com.softhub.softpostbox.listener;
/*    */ 
/*    */ import com.softhub.softframework.task.SimpleAsync;
/*    */ import com.softhub.softpostbox.PostboxData;
/*    */ import com.softhub.softpostbox.database.CachedDataService;
/*    */ import com.softhub.softpostbox.database.StorageDataService;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.player.PlayerJoinEvent;
/*    */ import org.bukkit.event.player.PlayerQuitEvent;
/*    */ 
/*    */ public class PlayerListener
/*    */   implements Listener {
/*    */   @EventHandler
/*    */   public void onJoin(PlayerJoinEvent event) {
/* 17 */     Player player = event.getPlayer();
/* 18 */     SimpleAsync.syncLater(() -> StorageDataService.get(player.getUniqueId()).thenAccept(()).exceptionally(()), 5L);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @EventHandler
/*    */   public void onQuit(PlayerQuitEvent event) {
/* 30 */     Player player = event.getPlayer();
/* 31 */     PostboxData postboxData = CachedDataService.get(player.getUniqueId());
/* 32 */     if (postboxData == null)
/* 33 */       return;  StorageDataService.set(postboxData).exceptionally(ex -> {
/*    */           ex.printStackTrace();
/*    */           return null;
/* 36 */         }).thenRun(() -> CachedDataService.remove(player.getUniqueId()));
/*    */   }
/*    */ }


/* Location:              D:\Downloads\SoftPostbox-1.0.jar!\com\softhub\softpostbox\listener\PlayerListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */