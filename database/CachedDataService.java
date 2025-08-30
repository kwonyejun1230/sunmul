/*    */ package com.softhub.softpostbox.database;
/*    */ 
/*    */ import com.softhub.softframework.database.cache.CacheStorage;
/*    */ import com.softhub.softpostbox.PostboxContainer;
/*    */ import com.softhub.softpostbox.PostboxData;
/*    */ import java.util.ArrayList;
/*    */ import java.util.UUID;
/*    */ 
/*    */ 
/*    */ public class CachedDataService
/*    */ {
/*    */   public static PostboxData get(UUID userId) {
/* 13 */     PostboxData postboxData = new PostboxData(userId, new PostboxContainer(new ArrayList()));
/* 14 */     if (CacheStorage.get("postboxdata", userId.toString()) instanceof PostboxData) {
/* 15 */       postboxData = (PostboxData)CacheStorage.get("postboxdata", userId.toString());
/*    */     }
/* 17 */     return postboxData;
/*    */   }
/*    */   
/*    */   public static void set(UUID userId, PostboxData flyData) {
/* 21 */     CacheStorage.set("postboxdata", userId.toString(), flyData);
/*    */   }
/*    */   
/*    */   public static void remove(UUID userId) {
/* 25 */     CacheStorage.remove("postboxdata", userId.toString());
/*    */   }
/*    */ }


/* Location:              D:\Downloads\SoftPostbox-1.0.jar!\com\softhub\softpostbox\database\CachedDataService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */