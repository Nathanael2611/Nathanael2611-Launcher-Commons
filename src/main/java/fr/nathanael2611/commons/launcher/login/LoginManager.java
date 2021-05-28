package fr.nathanael2611.commons.launcher.login;

import com.google.gson.*;
import fr.nathanael2611.commons.launcher.BaseLauncher;
import fr.nathanael2611.commons.launcher.login.model.AuthAgent;
import fr.nathanael2611.commons.launcher.login.model.AuthError;
import fr.nathanael2611.commons.launcher.login.model.AuthProfile;
import fr.nathanael2611.commons.launcher.login.model.response.AuthResponse;
import fr.nathanael2611.commons.launcher.util.Helpers;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;

public class LoginManager
{

    private final File userInfos;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private String email = "";
    private String password = "";
    private double ram = 2;

    private boolean saveEmail = false;
    private boolean savePassword = false;

    private boolean crack = false;

    private AuthResponse authResponse = null;

    private JsonObject customData;

    public LoginManager(BaseLauncher launcher)
    {
        this.userInfos = launcher.USER_INFOS;
        this.userInfos.getParentFile().mkdirs();
        if(!userInfos.exists())
        {
            try {
                userInfos.createNewFile();
                FileWriter writer = new FileWriter(userInfos);
                writer.write("{}");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.read();
    }

    public void read()
    {
        JsonObject object = new JsonParser().parse("{}").getAsJsonObject();
        try {
             object = new JsonParser().parse(FileUtils.readFileToString(this.userInfos, "UTF-8")).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.email = object.has("email") ? object.get("email").getAsString() : "";
        this.password = object.has("password") ? object.get("password").getAsString() : "";
        this.ram = object.has("ram") ? object.get("ram").getAsDouble() : 2;
        this.crack = object.has("crack") && object.get("crack").getAsBoolean();
        this.customData = object.has("customData") ? object.getAsJsonObject("customData") : new JsonObject();
    }

    public void save()
    {
        JsonObject object = new JsonObject();
        if(this.saveEmail) object.add("email", new JsonPrimitive(this.email));
        if(this.savePassword) object.add("password", new JsonPrimitive(this.password));
        object.add("ram", new JsonPrimitive(this.ram));
        object.add("crack", new JsonPrimitive(this.crack));
        object.add("customData", this.customData);
        try
        {
            FileUtils.write(this.userInfos, gson.toJson(object), "UTF-8");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void setInfos(String email, String password)
    {
        try {
            this.email = IOUtils.toString(Base64.getEncoder().encode(email.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.password = IOUtils.toString(Base64.getEncoder().encode(password.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        save();
    }

    public String getEmail()
    {
        try {
            return IOUtils.toString(Base64.getDecoder().decode(email));
        } catch (IOException e) {
            return "";
        }
    }

    public String getPassword()
    {
        try {
            return IOUtils.toString(Base64.getDecoder().decode(password));
        } catch (IOException e) {
            return "";
        }
    }

    public void setRam(double ram)
    {
        this.ram = ram;
        save();
    }

    public double getRam()
    {
        return ram;
    }

    public void tryLogin() throws AuthenticationException
    {
        Authenticator authenticator = new Authenticator(Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);
        try
        {
            if(this.crack)
            {
                if(this.email.length() < 4) throw new AuthenticationException(new AuthError("Username too short", "Your username has to be at least 4 characters", ""));
                this.authResponse = new AuthResponse("non", "non", new AuthProfile[]{}, new AuthProfile(getEmail(), "non"));
            } else {
                this.authResponse = authenticator.authenticate(AuthAgent.MINECRAFT, this.getEmail(), this.getPassword(), null);
            }
        } catch (AuthenticationException e)
        {
            this.authResponse = null;
            throw e;
        }
    }

    public boolean isLoggedIn()
    {
        return this.authResponse != null;
    }

    public void setCrack(boolean crack)
    {
        this.crack = crack;
    }

    public boolean isCrackAccount()
    {
        return this.crack;
    }

    public AuthResponse getAuthResponse()
    {
        return authResponse;
    }

    public void disconnect()
    {
        this.email = "";
        this.password = "";
        this.authResponse = null;
        this.save();
    }

    public void setRemeberOptions(boolean saveEmail, boolean savePassword)
    {
        this.saveEmail = saveEmail;
        this.savePassword = savePassword;
    }

    public JsonObject getCustomData()
    {
        return customData;
    }
}
