package net.cloudlite.core.api.addon;

import net.cloudlite.core.Core;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public abstract class Addon {

    private Core core;
    private File addonJarFile;
    private boolean enabled;
    private String addonName;
    private String version;
    private Material material;
    private File configFile;
    private YamlConfiguration config;

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void onReload();

    public final boolean isEnabled() {
        return enabled;
    }

    public String getAddonName() {
        return this.addonName;
    }

    public String getVersion() {
        return this.version;
    }

    public final Material getMaterial() {
        return this.material;
    }

    public void setAddonName(@Nonnull final String addonName) {
        this.addonName = addonName;
    }

    public void setVersion(@Nonnull final String version) {
        this.version = version;
    }

    public void setMaterial(@Nonnull final Material mat) {
        this.material = mat;
    }

    public void setEnabled(final boolean value) {
        this.enabled = value;
    }

    public void setAddonJarFile(@Nonnull final File file) {
        this.addonJarFile = file;
    }

    public void setCore(@Nonnull final Core core) {
        this.core = core;
    }

    /**
     * Gets the data folder of the specified Addon
     * @return File of the data folder
     */
    public final File getDataFolder() {
        return new File(this.core.getDataFolder(), "/addons/" + this.addonName);
    }

    /**
     * Gets a file content stream inside an addons jar
     * @param fileName File name to get
     * @return Embedded File's InputStream
     */
    private InputStream getResource(@Nonnull final String fileName) {
        try {
            final ZipFile zipFile = new ZipFile(this.addonJarFile.getAbsolutePath());
            final ZipEntry zipEntry = zipFile.getEntry(fileName);
            if (zipEntry == null) throw new RuntimeException("Embedded resource for Addon: " + this.addonName +
                    " Does not exist (" + fileName + ")");
            return zipFile.getInputStream(zipEntry);
        } catch (@Nonnull final IOException ex) {
            return null;
        }
    }

    /**
     * Saves a resource embedded into an addons jar file
     * @param pathToFile Path to the file to save
     * @param shouldReplace Should this file be replaced if it already exists?
     */
    public void saveResource(@Nonnull String pathToFile, final boolean shouldReplace) {
        pathToFile = pathToFile.replace('\\', '/');
        final InputStream inStream = getResource(pathToFile);
        if (inStream == null) throw new RuntimeException("The resource " + pathToFile + " for addon " + this.addonName + " could not be found");
        final File out = new File(this.getDataFolder(), pathToFile);
        final int lastPath = pathToFile.lastIndexOf('/');
        final File outDirs = new File(this.getDataFolder(), pathToFile.substring(0, lastPath >= 0 ? lastPath : 0));

        if (!outDirs.exists()) if(outDirs.mkdirs()); // This is ugly ik lol

        if (!out.exists() || shouldReplace) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
            BufferedWriter writer;
            String line;
            try {
                writer = new BufferedWriter(new FileWriter(out));
                while ((line = reader.readLine()) != null) {
                    writer.write(line + "\n");
                }
                reader.close();
                writer.close();
            } catch (@Nonnull final IOException ex) {
                ex.printStackTrace();
            }
        } else throw new RuntimeException ("Could not save file " + out.getName() + " for addon " + this.addonName
                    + " Because it already exists");
    }

    /**
     * Saves the default config.yml in a addons jar file
     */
    public void saveDefaultConfig() {
        if (!this.configFile.exists()) {
            saveResource("config.yml", true);
            this.configFile = new File(this.getDataFolder(), "config.yml");
            this.config = YamlConfiguration.loadConfiguration(configFile);
        }
    }

    /**
     * Returns the YamlConfiguration for gthe file
     * @return YamlConfiguration of the default config
     */
    public YamlConfiguration getConfig() {
        if (this.config == null) {
            this.reloadConfig();
            return this.config;
        } else return this.config;
    }

    /**
     * Reloads the configuration file (config.yml)
     */
    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    /**
     * Saves the configuration file (config.yml)
     */
    public void saveConfig() {
        try {
            this.config.save(this.configFile);
        } catch (@Nonnull final IOException ex) {
            ex.printStackTrace();
        }
    }
}
