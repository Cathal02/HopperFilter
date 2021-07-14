package org.cathal02.hopperfilter.data;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.cathal02.hopperfilter.HopperFilter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DataManager {


    HopperFilter plugin;

    File file;
    public DataManager(HopperFilter plugin)
    {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder().getPath() + File.separator+"hoppers.json");
        if (!file.exists()) plugin.saveResource(file.getName(), false);
    }

    public void saveHoppers(Collection<HopperData> values)
    {
        Gson gson = new GsonBuilder().registerTypeAdapter(HopperData.class, new HopperSerializer()).setPrettyPrinting().create();
        List<HopperData> data = new ArrayList<>(values);
        data.toArray();
        try
        {
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.flush();
            gson.toJson(data, fileWriter);
            fileWriter.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }


    public HopperData[] getHopperData() {
        try
        {
            Gson gson = null;
            JsonArray arr = new JsonArray();

            JsonReader reader = new JsonReader( new FileReader(file));
            JsonParser parser = new JsonParser();
            JsonElement parsedElement = parser.parse(reader);
            if(parsedElement.isJsonArray())
            {
                arr = parsedElement.getAsJsonArray();
            }

            gson = new GsonBuilder().registerTypeAdapter(HopperData.class, new HopperDeserializer()).create();
            reader.close();

            return gson.fromJson(arr, HopperData[].class);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
