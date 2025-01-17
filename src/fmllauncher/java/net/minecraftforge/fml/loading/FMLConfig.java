/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.fml.loading;

import com.electronwill.nightconfig.core.ConfigSpec;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.ParsingException;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.mohistmc.util.i18n.i18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;

import static net.minecraftforge.fml.loading.LogMarkers.CORE;

public class FMLConfig
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static FMLConfig INSTANCE = new FMLConfig();
    private static ConfigSpec configSpec = new ConfigSpec();
    static {
        configSpec.define("splashscreen", Boolean.TRUE);
        configSpec.define("maxThreads", -1);
        configSpec.define("versionCheck", Boolean.TRUE);
        configSpec.define("defaultConfigPath",  "defaultconfigs");
    }

    private CommentedFileConfig configData;

    private void loadFrom(final Path configFile)
    {
        configData = CommentedFileConfig.builder(configFile).sync().
                defaultResource("/META-INF/defaultfmlconfig.toml").
                autosave().autoreload().
                writingMode(WritingMode.REPLACE).
                build();
        try
        {
            configData.load();
        }
        catch (ParsingException e)
        {
            throw new RuntimeException(i18n.get("fmlconfig.1") + configFile.toString(), e);
        }
        if (!configSpec.isCorrect(configData)) {
            LOGGER.debug(CORE, i18n.get("fmlconfig.2", configFile));
            configSpec.correct(configData, (action, path, incorrectValue, correctedValue) ->
                    LOGGER.debug(CORE, i18n.get("fmlconfig.3", path, incorrectValue, correctedValue))
                    );
        }
        configData.save();
    }

    public static void load()
    {
        final Path configFile = FMLPaths.FMLCONFIG.get();
        INSTANCE.loadFrom(configFile);
        LOGGER.trace(CORE, i18n.get("fmlconfig.4", FMLPaths.FMLCONFIG.get()));
        LOGGER.trace(CORE, i18n.get("fmlconfig.5", FMLConfig.splashScreenEnabled()));
        LOGGER.trace(CORE, i18n.get("fmlconfig.6", FMLConfig.loadingThreadCount()));
        LOGGER.trace(CORE, i18n.get("fmlconfig.7", FMLConfig.runVersionCheck()));
        LOGGER.trace(CORE, i18n.get("fmlconfig.8", FMLConfig.defaultConfigPath()));
        FMLPaths.getOrCreateGameRelativePath(Paths.get(FMLConfig.defaultConfigPath()), "default config directory");
    }

    public static boolean splashScreenEnabled() {
        return INSTANCE.configData.<Boolean>getOptional("splashscreen").orElse(Boolean.FALSE);
    }

    public static int loadingThreadCount() {
        int val = INSTANCE.configData.<Integer>getOptional("maxThreads").orElse(-1);
        if (val <= 0) return Runtime.getRuntime().availableProcessors();
        return val;
    }

    public static boolean runVersionCheck() {
        return INSTANCE.configData.<Boolean>getOptional("versionCheck").orElse(Boolean.FALSE);
    }

    public static String defaultConfigPath() {
        return INSTANCE.configData.<String>getOptional("defaultConfigPath").orElse("defaultconfigs");
    }
}
