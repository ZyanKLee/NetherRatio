package org.doraji.netherratio;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages localized messages for the plugin.
 * 
 * @author xDxRAx (Original Author)
 * @author NetherRatio Team
 * @author ZyanKLee (Maintainer)
 */
public class MessagesManager {
    
    private final NetherRatio plugin;
    private FileConfiguration messages;
    private String currentLanguage;
    
    public MessagesManager(NetherRatio plugin) {
        this.plugin = plugin;
        this.currentLanguage = plugin.getConfig().getString("language", "en");
        loadMessages();
    }
    
    /**
     * Loads messages for the configured language.
     */
    public void loadMessages() {
        String language = plugin.getConfig().getString("language", "en");
        this.currentLanguage = language;
        
        // Save default message files if they don't exist
        File messagesDir = new File(plugin.getDataFolder(), "messages");
        if (!messagesDir.exists()) {
            messagesDir.mkdirs();
        }
        
        // Save default language files
        saveDefaultMessageFile("en.yml");
        saveDefaultMessageFile("de.yml");
        saveDefaultMessageFile("fr.yml");
        saveDefaultMessageFile("it.yml");
        saveDefaultMessageFile("ko.yml");
        
        // Load the selected language file
        File messageFile = new File(messagesDir, language + ".yml");
        if (!messageFile.exists()) {
            plugin.getLogger().warning("Language file not found: " + language + ".yml, falling back to en.yml");
            messageFile = new File(messagesDir, "en.yml");
        }
        
        this.messages = YamlConfiguration.loadConfiguration(messageFile);
        plugin.getLogger().info("Loaded messages for language: " + language);
    }
    
    /**
     * Saves a default message file from resources if it doesn't exist.
     */
    private void saveDefaultMessageFile(String fileName) {
        File file = new File(plugin.getDataFolder(), "messages/" + fileName);
        if (!file.exists()) {
            plugin.saveResource("messages/" + fileName, false);
        }
    }
    
    /**
     * Gets a message from the current language file.
     * 
     * @param path The message path
     * @return The message with color codes translated
     */
    public String getMessage(String path) {
        String message = messages.getString(path, "Missing message: " + path);
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    /**
     * Gets a message with placeholders replaced.
     * 
     * @param path The message path
     * @param replacements Map of placeholder names to values
     * @return The formatted message with color codes translated
     */
    public String getMessage(String path, Map<String, String> replacements) {
        String message = getMessage(path);
        
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        
        return message;
    }
    
    /**
     * Gets a message with a single placeholder replaced.
     * 
     * @param path The message path
     * @param placeholder The placeholder name (without braces)
     * @param value The value to replace with
     * @return The formatted message
     */
    public String getMessage(String path, String placeholder, String value) {
        Map<String, String> replacements = new HashMap<>();
        replacements.put(placeholder, value);
        return getMessage(path, replacements);
    }
    
    /**
     * Reloads messages from the language file.
     */
    public void reload() {
        loadMessages();
    }
    
    /**
     * Gets the current language code.
     * 
     * @return The language code (e.g., "en", "ko")
     */
    public String getCurrentLanguage() {
        return currentLanguage;
    }
}
