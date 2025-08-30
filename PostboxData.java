/*    */ package com.softhub.softpostbox;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class PostboxData {
/*    */   private UUID userId;
/*    */   private PostboxContainer container;
/*    */   
/*    */   @Generated
/*    */   public UUID getUserId() {
/* 10 */     return this.userId;
/*    */   }
/*    */   @Generated
/* 13 */   public PostboxContainer getContainer() { return this.container; } @Generated
/* 14 */   public void setContainer(PostboxContainer container) { this.container = container; }
/*    */ 
/*    */   
/*    */   public PostboxData(UUID userId, PostboxContainer container) {
/* 18 */     this.userId = userId;
/* 19 */     this.container = container;
/*    */   }
/*    */ }


/* Location:              D:\Downloads\SoftPostbox-1.0.jar!\com\softhub\softpostbox\PostboxData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */