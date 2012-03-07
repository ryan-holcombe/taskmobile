package com.honeydothis.json;

public class JsonTask
{
   private final Long id;
   private final String title;

   public JsonTask(final Long idIn, final String titleIn)
   {
      id = idIn;
      title = titleIn;
   }

   public Long getId()
   {
      return id;
   }

   public String getTitle()
   {
      return title;
   }
}
