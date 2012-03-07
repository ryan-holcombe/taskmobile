package com.honeydothis.json;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JsonTaskGroup
{
   private final Long id;
   private final String name;
   private final List<JsonTask> tasks = new ArrayList<JsonTask>();

   public JsonTaskGroup(final Long idIn, final String nameIn)
   {
      id = idIn;
      name = nameIn;
   }

   public Long getId()
   {
      return id;
   }

   public String getName()
   {
      return name;
   }

   public List<JsonTask> getTasks()
   {
      return tasks;
   }
}
