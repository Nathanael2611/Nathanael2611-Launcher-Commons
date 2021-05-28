package fr.nathanael2611.commons.launcher.launch;

public abstract class GameTweak
{
    /**
     * The LaunchWrapper main class
     */
    public static final String LAUNCHWRAPPER_MAIN_CLASS = "net.minecraft.launchwrapper.Launch";

    /**
     * The Forge GameTweak
     */
    public static final GameTweak FORGE = new GameTweak()
    {
        @Override
        public String getName()
        {
            return "FML Tweaker";
        }

        @Override
        public String getTweakClass(GameType type)
        {
            if (type.equals(GameType.V1_8_HIGHER))
                return "net.minecraftforge.fml.common.launcher.FMLTweaker";
            else
                return "cpw.mods.fml.common.launcher.FMLTweaker";
        }
    };

    /**
     * The Optifine GameTweak
     */
    public static final GameTweak OPTIFINE = new GameTweak()
    {
        @Override
        public String getName()
        {
            return "Optifine Tweaker";
        }

        @Override
        public String getTweakClass(GameType type)
        {
            return "optifine.OptiFineTweaker";
        }
    };



    /**
     * Return the name of the tweak
     *
     * @return The tweak name
     */
    public abstract String getName();

    /**
     * Return the name of the tweak class to give to the launch wrapper
     *
     * @param infos The current GameInfos
     *
     * @return The tweak class
     */
    public abstract String getTweakClass(GameType type);
}