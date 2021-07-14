package org.cathal02.hopperfilter.data;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.cathal02.hopperfilter.data.HopperData;
import org.cathal02.hopperfilter.XMaterial;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HopperDeserializer  implements JsonDeserializer<HopperData> {
    @Override
    public HopperData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
       JsonObject obj = json.getAsJsonObject();
       JsonObject locObj = obj.getAsJsonObject("location");

        int x = locObj.get("x").getAsInt();
        int y = locObj.get("y").getAsInt();
        int z = locObj.get("z").getAsInt();
        String world = locObj.get("world").getAsString();

        String friendsString = obj.get("friends").getAsString();
        List<String> friends = new ArrayList<>(Arrays.asList(friendsString.split(",")));
        ItemStack[] items = null;
        try
        {
           items =  BukkitSerializer.itemStackArrayFromBase64(obj.get("items").getAsString());
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        Integer allowedItems = obj.get("allowedItems").getAsInt();
        boolean advancedSearch = obj.get("advancedFilter").getAsBoolean();
        boolean isPublic = obj.get("isPublic").getAsBoolean();
        boolean blacklistEnabled = obj.get("blacklistEnabled").getAsBoolean();

        return new HopperData(new Location(Bukkit.getWorld(world),x,y,z),items,allowedItems,friends,isPublic, obj.get("owner").getAsString(),blacklistEnabled, advancedSearch);
    }
}
