/*    */ package com.softhub.softpostbox.database;
/*    */ import com.softhub.softframework.SoftFramework;
/*    */ import com.softhub.softpostbox.PostboxContainer;
/*    */ import com.softhub.softpostbox.PostboxData;
/*    */ import com.softhub.softpostbox.util.SerializeUtil;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.UUID;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ 
/*    */ public class StorageDataService {
/*    */   public static void init() {
/* 15 */     SoftFramework.getDatabaseManager().createTable("postbox_data", "user_id VARCHAR(36), container LONGBLOB");
/*    */   }
/*    */   
/*    */   public static CompletableFuture<Void> set(PostboxData postboxData) {
/* 19 */     String table = "postbox_data";
/* 20 */     String uuid = postboxData.getUserId().toString();
/* 21 */     byte[] container = SerializeUtil.serialize(postboxData.getContainer().getItemList());
/*    */     
/* 23 */     String selected = "user_id, container";
/* 24 */     Object[] values = { uuid, container };
/*    */     
/* 26 */     return SoftFramework.getDatabaseManager().set(selected, values, "user_id", "=", uuid, table);
/*    */   }
/*    */   
/*    */   public static void setSync(PostboxData postboxData) {
/* 30 */     String table = "postbox_data";
/* 31 */     String uuid = postboxData.getUserId().toString();
/* 32 */     byte[] container = SerializeUtil.serialize(postboxData.getContainer().getItemList());
/*    */     
/* 34 */     String selected = "user_id, container";
/* 35 */     Object[] values = { uuid, container };
/*    */     
/* 37 */     SoftFramework.getDatabaseManager().setSync(selected, values, "user_id", "=", uuid, table);
/*    */   }
/*    */   
/*    */   public static CompletableFuture<PostboxData> get(UUID userId) {
/* 41 */     String table = "postbox_data";
/* 42 */     String column = "user_id";
/* 43 */     String logicGate = "=";
/* 44 */     String data = userId.toString();
/*    */     
/* 46 */     String selectedColumns = "container";
/*    */     
/* 48 */     return SoftFramework.getDatabaseManager()
/* 49 */       .getMultipleColumnsList(selectedColumns, table, column, logicGate, data, rs -> {
/*    */           byte[] container = rs.getBytes("container");
/*    */           
/*    */           PostboxContainer postboxContainer = new PostboxContainer(SerializeUtil.deserialize(container));
/*    */           return new PostboxData(userId, postboxContainer);
/* 54 */         }).thenApply(resultList -> resultList.isEmpty() ? new PostboxData(userId, new PostboxContainer(new ArrayList())) : resultList.get(0));
/*    */   }
/*    */ }


/* Location:              D:\Downloads\SoftPostbox-1.0.jar!\com\softhub\softpostbox\database\StorageDataService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */