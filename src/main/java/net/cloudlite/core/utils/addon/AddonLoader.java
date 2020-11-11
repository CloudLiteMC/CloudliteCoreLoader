package net.cloudlite.core.utils.addon;

import net.cloudlite.core.Core;
import net.cloudlite.core.api.addon.Addon;
import net.cloudlite.core.utils.exceptions.InvalidAddonException;
import net.cloudlite.core.utils.exceptions.MaterialNotFoundException;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class AddonLoader {

    private final Core core;

    public AddonLoader(@Nonnull final Core core) {
        this.core = core;

        final File addonsFolder = new File(this.core.getDataFolder(), "/addons/");
        if (!addonsFolder.exists()) if (addonsFolder.mkdirs());

        final File[] addonsInFolder = addonsFolder.listFiles();
        if (addonsInFolder == null) return;

        for (final File file : addonsInFolder) {
            if (!file.getName().endsWith(".jar")) continue;
            try {
                final ZipFile addonJarFile = new ZipFile(file);
                final InputStream stream = this.getZipFile(addonJarFile, "addon.yml");
                if (stream == null) throw new RuntimeException("addon.yml file not found for addon: " + file.getName());
                final YamlConfiguration addonConfig = this.getYamlFromStream(stream);

                final Addon addon = this.loadAddon(file, addonConfig);
                if (addon == null) return;
                this.core.getAddonManager().addAddon(addon);
                addon.onEnable();
                addon.setEnabled(true);
            } catch (@Nonnull final Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    /**
     * Gets a File (InputStream) of a ZipFile
     * @param jarFile ZipFile of file (Addon.JAR)
     * @param name Name of file to find
     * @return null when ot found
     */
    private InputStream getZipFile(@Nonnull final ZipFile jarFile, @Nonnull final String name) {
        final ZipEntry entry = jarFile.getEntry(name);
        if (entry == null) return null;
        try {
            return jarFile.getInputStream(entry);
        } catch (@Nonnull final IOException ex) {
            throw new RuntimeException("File not found in ZipFIle: " + name);
        }
    }

    /**
     * Gets a YamlConfiguration from the contents of a InputStream
     * @param stream InputStream to use
     * @return YamlConfiguration
     */
    private YamlConfiguration getYamlFromStream(@Nonnull final InputStream stream) {
        final StringBuilder contents = new StringBuilder();
        final YamlConfiguration config = new YamlConfiguration();

        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String line = bufferedReader.readLine();
            while (line != null) {
                contents.append(line).append("\n");
                line = bufferedReader.readLine();
            }

            config.loadFromString(contents.toString());
        } catch (@Nonnull final IOException | InvalidConfigurationException ex) {
            ex.printStackTrace();
        }

        return config;
    }

    private Addon loadAddon(@Nonnull final File jarFile, @Nonnull final YamlConfiguration addonConfig) {
        final String addonName = addonConfig.getString("Name");
        final String mainClass = addonConfig.getString("MainClass");
        final String version = addonConfig.getString("Version");
        final String material = addonConfig.getString("Material");

        if (addonName == null) throw new InvalidAddonException("Addon name could not be found for: " + jarFile.getName());
        if (mainClass == null) throw new InvalidAddonException("Addon MainClass could not be found for: " + jarFile.getName());
        if (version == null) throw new InvalidAddonException("Addon version could not be found for: " + jarFile.getName());
        if (material == null) throw new InvalidAddonException("Addon material could not be found for: " + jarFile.getName());

        final ClassLoader loader = this.getClass().getClassLoader();
        Addon addon;
        try {
            final URLClassLoader classLoader = new URLClassLoader(new URL[] {jarFile.toURI().toURL()}, loader);
            final Class<?> mainClassFromJar = Class.forName(mainClass, true, classLoader);
            final Class<? extends Addon> extendedClass = mainClassFromJar.asSubclass(Addon.class);

            addon = extendedClass.getDeclaredConstructor().newInstance();
        } catch (@Nonnull final MalformedURLException | ClassNotFoundException | NoSuchMethodException
                | IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            ex.printStackTrace();
            throw new InvalidAddonException("Failed to initialize addon: " + jarFile.getName());
        }

        final Material mat = Material.getMaterial(material);
        if (mat == null) throw new MaterialNotFoundException("Material could not be found: " + material);

        addon.setAddonName(addonName);
        addon.setVersion(version);
        addon.setMaterial(mat);
        return addon;
    }
}
