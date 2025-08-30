/*    */ package com.softhub.softpostbox;
/*    */ import com.softhub.softframework.command.CommandRegister;
/*    */ import com.softhub.softpostbox.config.ConfigManager;
/*    */ import com.softhub.softpostbox.database.CachedDataService;
/*    */ import com.softhub.softpostbox.database.StorageDataService;
/*    */ import com.softhub.softpostbox.listener.PlayerListener;
/*    */ import java.util.Iterator;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ 
/*    */ public final class BukkitInitializer extends JavaPlugin {
/*    */   @Generated
/*    */   public static BukkitInitializer getInstance() {
/* 16 */     return instance;
/*    */   }
/*    */   private static BukkitInitializer instance;
/*    */   
/*    */   public void onEnable() {
/* 21 */     instance = this;
/* 22 */     ConfigManager.init();
/* 23 */     StorageDataService.init();
/* 24 */     CommandRegister.registerCommands(new PostboxCommand());
/* 25 */     getServer().getPluginManager().registerEvents((Listener)new PlayerListener(), (Plugin)this);
/* 26 */     for (Iterator<Player> iterator = Bukkit.getOnlinePlayers().iterator(); iterator.hasNext(); ) { Player player = iterator.next();
/* 27 */       StorageDataService.get(player.getUniqueId()).thenAccept(postboxData -> CachedDataService.set(player.getUniqueId(), postboxData)); }
/*    */   
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 35 */     for (Player player : Bukkit.getOnlinePlayers()) {
/* 36 */       PostboxData postboxData = CachedDataService.get(player.getUniqueId());
/* 37 */       if (postboxData == null)
/* 38 */         return;  StorageDataService.setSync(postboxData);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Downloads\SoftPostbox-1.0.jar!\com\softhub\softpostbox\BukkitInitializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */