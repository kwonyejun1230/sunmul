/*    */ package com.softhub.softpostbox.util;
/*    */ import com.softhub.softpostbox.ItemContainer;
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.util.List;
/*    */ 
/*    */ public class SerializeUtil {
/*    */   public static byte[] serialize(List<ItemContainer> itemList) {
/* 12 */     ByteArrayOutputStream byteStream = new ByteArrayOutputStream(); 
/* 13 */     try { ObjectOutputStream objStream = new ObjectOutputStream(byteStream); 
/* 14 */       try { objStream.writeObject(itemList);
/* 15 */         objStream.close(); } catch (Throwable throwable) { try { objStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/* 16 */     { e.printStackTrace(); }
/*    */     
/* 18 */     return byteStream.toByteArray();
/*    */   }
/*    */   
/*    */   public static List<ItemContainer> deserialize(byte[] bytes) {
/* 22 */     ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes); 
/* 23 */     try { ObjectInputStream objStream = new ObjectInputStream(byteStream); 
/* 24 */       try { List<ItemContainer> list = (List)objStream.readObject();
/* 25 */         objStream.close(); return list; } catch (Throwable throwable) { try { objStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException|ClassNotFoundException e)
/* 26 */     { e.printStackTrace();
/*    */       
/* 28 */       return new ArrayList<>(); }
/*    */   
/*    */   }
/*    */ }


/* Location:              D:\Downloads\SoftPostbox-1.0.jar!\com\softhub\softpostbo\\util\SerializeUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */