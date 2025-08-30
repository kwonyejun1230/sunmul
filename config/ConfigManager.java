/*    */ package com.softhub.softpostbox.config;
/*    */ import com.softhub.softpostbox.BukkitInitializer;
/*    */ import lombok.Generated;
/*    */ 
/*    */ public class ConfigManager {
/*    */   @Generated
/*    */   public static Boolean getExpiredEnabled() {
/*  8 */     return expiredEnabled;
/*  9 */   } private static Boolean expiredEnabled = Boolean.valueOf(true); @Generated
/* 10 */   public static Integer getExpiredTime() { return expiredTime; }
/*    */   
/*    */   private static Integer expiredTime;
/*    */   public static void init() {
/* 14 */     BukkitInitializer.getInstance().saveDefaultConfig();
/* 15 */     BukkitInitializer.getInstance().reloadConfig();
/* 16 */     expiredTime = Integer.valueOf(BukkitInitializer.getInstance().getConfig().getInt("settings.expired_time"));
/* 17 */     if (expiredTime.intValue() == -1)
/* 18 */       expiredEnabled = Boolean.valueOf(false); 
/*    */   }
/*    */ }


/* Location:              D:\Downloads\SoftPostbox-1.0.jar!\com\softhub\softpostbox\config\ConfigManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */