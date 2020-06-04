package me.rsk.hiroshima;

import com.google.common.base.Converter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.zero.alpine.EventBus;
import me.zero.alpine.EventManager;
import me.rsk.hiroshima.gasda.command.Command;
import me.rsk.hiroshima.gasda.command.CommandManager;
import me.rsk.hiroshima.gasda.event.ForgeEventProcessor;
import me.rsk.hiroshima.jews.gui.hiroshima.HiroshimaGUI;
import me.rsk.hiroshima.jews.gui.rgui.component.AlignedComponent;
import me.rsk.hiroshima.jews.gui.rgui.component.Component;
import me.rsk.hiroshima.jews.gui.rgui.component.container.Container;
import me.rsk.hiroshima.jews.gui.rgui.component.container.use.Frame;
import me.rsk.hiroshima.jews.gui.rgui.util.ContainerHelper;
import me.rsk.hiroshima.jews.gui.rgui.util.Docking;
import me.rsk.hiroshima.jews.module.Module;
import me.rsk.hiroshima.jews.module.ModuleManager;
import me.rsk.hiroshima.gasda.setting.Setting;
import me.rsk.hiroshima.gasda.setting.Settings;
import me.rsk.hiroshima.gasda.setting.SettingsRegister;
import me.rsk.hiroshima.gasda.setting.config.*;
import me.rsk.hiroshima.gasda.util.Friends;
import me.rsk.hiroshima.gasda.util.LagCompensator;
import me.rsk.hiroshima.gasda.util.Wrapper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * Created by 086 on 7/11/2017.
 */
@Mod(modid = HiroshimaClient.MODID, name = HiroshimaClient.MODNAME, version = HiroshimaClient.MODVER)
public class HiroshimaClient {

    public static final String MODID = "hiroshima";
    public static final String MODNAME = "HIROSHIMA";
    public static final String MODVER = "v0.1.9";
    public static final String PREFIX = "HIRO";

    private static final String HIROSHIMA_CONFIG = "HIROSHIMAconfig.json";

    public static final Logger log = LogManager.getLogger("HIROSHIMA");

    public static final EventBus EVENT_BUS = new EventManager();

    @Mod.Instance
    private static HiroshimaClient INSTANCE;

    public HiroshimaGUI guiManager;
    public CommandManager commandManager;
    private Setting<JsonObject> guiStateSetting = Settings.custom("gui", new JsonObject(), new Converter<JsonObject, JsonObject>() {
        @Override
        protected JsonObject doForward(JsonObject jsonObject) {
            return jsonObject;
        }

        @Override
        protected JsonObject doBackward(JsonObject jsonObject) {
            return jsonObject;
        }
    }).buildAndRegister("");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        HiroshimaClient.log.info("\n\nInitializing HIROSHIMA " + MODVER);

        ModuleManager.initialize();

        ModuleManager.getModules().stream().filter(module -> module.alwaysListening).forEach(EVENT_BUS::subscribe);
        MinecraftForge.EVENT_BUS.register(new ForgeEventProcessor());
        LagCompensator.INSTANCE = new LagCompensator();

        Wrapper.init();

        guiManager = new HiroshimaGUI();
        guiManager.initializeGUI();

        commandManager = new CommandManager();

        Friends.initFriends();
        SettingsRegister.register("commandPrefix", Command.commandPrefix);
        loadConfiguration();
        HiroshimaClient.log.info("Settings loaded");

        ModuleManager.updateLookup(); // generate the lookup table after settings are loaded to make custom module names work

        // After settings loaded, we want to let the enabled modules know they've been enabled (since the setting is done through reflection)
        ModuleManager.getModules().stream().filter(Module::isEnabled).forEach(Module::enable);

        HiroshimaClient.log.info("HIROSHIMA initialized!\n");

    }

    public static String getConfigName() {
        Path config = Paths.get("HIROSHIMAconfiglast.txt");
        String hiroshimaConfigName = HIROSHIMA_CONFIG;
        try(BufferedReader reader = Files.newBufferedReader(config)) {
            hiroshimaConfigName = reader.readLine();
            if (!isFilenameValid(hiroshimaConfigName)) hiroshimaConfigName = HIROSHIMA_CONFIG;
        } catch (NoSuchFileException e) {
            try(BufferedWriter writer = Files.newBufferedWriter(config)) {
                writer.write(HIROSHIMA_CONFIG);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hiroshimaConfigName;
    }

    public static void loadConfiguration() {
        try {
            loadConfigurationUnsafe();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadConfigurationUnsafe() throws IOException {
        String hiroshimaConfigName = getConfigName();
        Path hiroshimaConfig = Paths.get(hiroshimaConfigName);
        if (!Files.exists(hiroshimaConfig)) return;
        Configuration.loadConfiguration(hiroshimaConfig);

        JsonObject gui = HiroshimaClient.INSTANCE.guiStateSetting.getValue();
        for (Map.Entry<String, JsonElement> entry : gui.entrySet()) {
            Optional<Component> optional = HiroshimaClient.INSTANCE.guiManager.getChildren().stream().filter(component -> component instanceof Frame).filter(component -> ((Frame) component).getTitle().equals(entry.getKey())).findFirst();
            if (optional.isPresent()) {
                JsonObject object = entry.getValue().getAsJsonObject();
                Frame frame = (Frame) optional.get();
                frame.setX(object.get("x").getAsInt());
                frame.setY(object.get("y").getAsInt());
                Docking docking = Docking.values()[object.get("docking").getAsInt()];
                if (docking.isLeft()) ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.LEFT);
                else if (docking.isRight()) ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.RIGHT);
                else if (docking.isCenterVertical()) ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.CENTER);
                frame.setDocking(docking);
                frame.setMinimized(object.get("minimized").getAsBoolean());
                frame.setPinned(object.get("pinned").getAsBoolean());
            } else {
                System.err.println("Found GUI config entry for " + entry.getKey() + ", but found no frame with that name");
            }
        }
        HiroshimaClient.getInstance().getGuiManager().getChildren().stream().filter(component -> (component instanceof Frame) && (((Frame) component).isPinneable()) && component.isVisible()).forEach(component -> component.setOpacity(0f));
    }

    public static void saveConfiguration() {
        try {
            saveConfigurationUnsafe();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfigurationUnsafe() throws IOException {
        JsonObject object = new JsonObject();
        HiroshimaClient.INSTANCE.guiManager.getChildren().stream().filter(component -> component instanceof Frame).map(component -> (Frame) component).forEach(frame -> {
            JsonObject frameObject = new JsonObject();
            frameObject.add("x", new JsonPrimitive(frame.getX()));
            frameObject.add("y", new JsonPrimitive(frame.getY()));
            frameObject.add("docking", new JsonPrimitive(Arrays.asList(Docking.values()).indexOf(frame.getDocking())));
            frameObject.add("minimized", new JsonPrimitive(frame.isMinimized()));
            frameObject.add("pinned", new JsonPrimitive(frame.isPinned()));
            object.add(frame.getTitle(), frameObject);
        });
        HiroshimaClient.INSTANCE.guiStateSetting.setValue(object);

        Path outputFile = Paths.get(getConfigName());
        if (!Files.exists(outputFile))
            Files.createFile(outputFile);
        Configuration.saveConfiguration(outputFile);
        ModuleManager.getModules().forEach(Module::destroy);
    }

    public static boolean isFilenameValid(String file) {
        File f = new File(file);
        try {
            f.getCanonicalPath();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static HiroshimaClient getInstance() {
        return INSTANCE;
    }

    public HiroshimaGUI getGuiManager() {
        return guiManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }
}
