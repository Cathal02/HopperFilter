package org.cathal02.hopperfilter.data;

import com.google.gson.*;
import org.cathal02.hopperfilter.data.HopperData;

import java.lang.reflect.Type;
import java.util.Arrays;

public class HopperSerializer implements JsonSerializer<HopperData> {

    @Override
    public JsonElement serialize(HopperData src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.add("location", new JsonObject());

        obj.addProperty("allowedItems", src.allowedItems);
        JsonObject location = (JsonObject)obj.get("location");
        location.addProperty("world",src.location.getWorld().getName() );
        location.addProperty("x", src.location.getBlockX());
        location.addProperty("y", src.location.getBlockY());
        location.addProperty("z", src.location.getBlockZ());

        String friends = "";
        if(src.friends != null && src.friends.size() > 0)
        {
            for(String player : src.friends)
            {
                friends = friends.concat(player+",");
            }
        }


        obj.addProperty("friends", friends);
        obj.addProperty("isPublic", src.isPublic);
        obj.addProperty("owner", src.owner);
        obj.addProperty("blacklistEnabled", src.blacklistEnabled);
        obj.addProperty("advancedFilter", src.advancedFilter);

        String items = BukkitSerializer.itemStackArrayToBase64(src.hopperItems);
        obj.addProperty("items", items);
        return obj;
    }
}
