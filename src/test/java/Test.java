import fr.nathanael2611.commons.launcher.BaseLauncher;
import fr.nathanael2611.commons.launcher.login.AuthenticationException;
import fr.nathanael2611.commons.launcher.update.EazyUpdater;
import fr.nathanael2611.commons.launcher.update.minecraft.download.DownloadManager;

import java.io.IOException;

public class Test {

    public static final String EAZYUPDATE_SERVER_URL = "https://launcher.saofrance-mc.net/launcherV1/base/";
    public static final String EAZYUPDATE_CRACK_PATCH_SERVER_URL = "https://launcher.saofrance-mc.net/launcherV1/crack/";

    public static void main(String[] args) throws AuthenticationException
    {
        BaseLauncher launcher = new BaseLauncher("Issou").setVersion("1.12.2", null).complete();
        launcher.getUpdateManager().updateAll();
        launcher.getLoginManager().setRemeberOptions(false, false);
        launcher.getLoginManager().setCrack(true);
        launcher.getLoginManager().setInfos("BonJour", "");
        launcher.getLoginManager().tryLogin();
        launcher.getLaunchManager().launch();
    }

}
