/*    */ package com.softhub.softpostbox;
/*    */ 
/*    */ import com.softhub.softframework.item.SimpleItem;
/*    */ import com.softhub.softpostbox.config.ConfigManager;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.Serializable;
/*    */ import java.time.Instant;
/*    */ import java.time.temporal.ChronoUnit;
/*    */ import lombok.Generated;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ public class ItemContainer extends SimpleItem implements Serializable {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private Instant expireTime;
/*    */   
/*    */   @Generated
/*    */   public Instant getExpireTime() {
/* 21 */     return this.expireTime; } @Generated
/* 22 */   public void setExpireTime(Instant expireTime) { this.expireTime = expireTime; }
/*    */ 
/*    */   
/*    */   public ItemContainer(Material material) {
/* 26 */     super(material);
/*    */   }
/*    */   
/*    */   public ItemContainer(ItemStack item) {
/* 30 */     super(item);
/*    */   }
/*    */   
/*    */   public ItemContainer() {
/* 34 */     super(Material.AIR);
/*    */   }
/*    */   
/*    */   public void registerItem() {
/* 38 */     Instant now = Instant.now();
/* 39 */     this.expireTime = now.plus(ConfigManager.getExpiredTime().intValue(), ChronoUnit.HOURS);
/*    */   }
/*    */   
/*    */   public int getTimeUntilExpire() {
/* 43 */     if (this.expireTime == null) {
/* 44 */       return Integer.MAX_VALUE;
/*    */     }
/* 46 */     long secondsUntilExpire = this.expireTime.getEpochSecond() - Instant.now().getEpochSecond();
/* 47 */     return (int)Math.max(0L, Math.min(secondsUntilExpire, 2147483647L));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isExpired() {
/* 52 */     return (this.expireTime != null && Instant.now().isAfter(this.expireTime));
/*    */   }
/*    */   
/*    */   public boolean isSameItem(SimpleItem other) {
/* 56 */     if (other == null) return false; 
/* 57 */     return (getType() == other.getType() && 
/* 58 */       getAmount() == other.getAmount() && (
/* 59 */       (getItemMeta() == null) ? (other.getItemMeta() == null) : getItemMeta().equals(other.getItemMeta())));
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemContainer clone() {
/* 64 */     ItemContainer clone = new ItemContainer((ItemStack)this);
/* 65 */     clone.setExpireTime(this.expireTime);
/* 66 */     return clone;
/*    */   }
/*    */   
/*    */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 70 */     out.defaultWriteObject();
/* 71 */     if (getType() != Material.AIR) {
/* 72 */       out.writeObject(serializeAsBytes());
/*    */     } else {
/* 74 */       out.writeObject(null);
/*    */     } 
/*    */   }
/*    */   
/*    */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 79 */     in.defaultReadObject();
/* 80 */     byte[] itemBytes = (byte[])in.readObject();
/* 81 */     if (itemBytes != null) {
/* 82 */       ItemStack itemStack = ItemStack.deserializeBytes(itemBytes);
/* 83 */       setType(itemStack.getType());
/* 84 */       setAmount(itemStack.getAmount());
/* 85 */       setItemMeta(itemStack.getItemMeta());
/*    */     } else {
/* 87 */       setType(Material.AIR);
/* 88 */       setAmount(0);
/* 89 */       setItemMeta(null);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Downloads\SoftPostbox-1.0.jar!\com\softhub\softpostbox\ItemContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */