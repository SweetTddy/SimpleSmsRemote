package tranquvis.simplesmsremote.CommandManagement;

import android.Manifest;
import android.content.Context;
import android.os.Build;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.Data.ControlModuleUserData;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.PermissionUtils;

/**
 * Created by Andreas Kaltenleitner on 23.08.2016.
 */
public class ControlModule
{
    public static final ControlModule
            WIFI_HOTSPOT, MOBILE_DATA, BATTERY, LOCATION, WIFI, BLUETOOTH, AUDIO;

    static {
        WIFI_HOTSPOT = new ControlModule("wifi_hotspot",
                new ControlCommand[]{
                        ControlCommand.WIFI_HOTSPOT_ENABLE,
                        ControlCommand.WIFI_HOTSPOT_DISABLE,
                        ControlCommand.WIFI_HOTSPOT_IS_ENABLED
                },
                -1, -1,
                new String[]{
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.WRITE_SETTINGS
                },
                R.string.control_module_title_wifi_hotspot,
                R.string.control_module_desc_wifi_hotspot,
                R.drawable.ic_wifi_tethering_grey_700_36dp,
                -1);

        MOBILE_DATA = new ControlModule("mobile_data",
                new ControlCommand[]{
                        ControlCommand.MOBILE_DATA_ENABLE,
                        ControlCommand.MOBILE_DATA_DISABLE,
                        ControlCommand.MOBILE_DATA_IS_ENABLED
                },
                -1, Build.VERSION_CODES.LOLLIPOP,
                new String[]{
                    Manifest.permission.CHANGE_NETWORK_STATE,
                    Manifest.permission.ACCESS_NETWORK_STATE
                },
                R.string.control_module_title_mobile_data,
                R.string.control_module_desc_mobile_data,
                R.drawable.ic_network_cell_grey_700_36dp,
                -1);

        BATTERY = new ControlModule("battery",
                new ControlCommand[]{
                        ControlCommand.BATTERY_LEVEL_GET,
                        ControlCommand.BATTERY_IS_CHARGING
                },
                -1, -1,
                new String[]{},
                R.string.control_module_title_battery,
                R.string.control_module_desc_battery,
                R.drawable.ic_battery_50_grey_700_36dp,
                -1);

        LOCATION = new ControlModule("location",
                new ControlCommand[]{
                    ControlCommand.LOCATION_GET
                },
                -1, -1,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                },
                R.string.control_module_title_location,
                R.string.control_module_desc_location,
                R.drawable.ic_location_on_grey_700_36dp,
                -1);

        WIFI = new ControlModule("wifi",
                new ControlCommand[]{
                        ControlCommand.WIFI_ENABLE,
                        ControlCommand.WIFI_DISABLE,
                        ControlCommand.WIFI_IS_ENABLED
                },
                -1, -1,
                new String[]{
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.WRITE_SETTINGS
                },
                R.string.control_module_title_wifi,
                R.string.control_module_desc_wifi,
                R.drawable.ic_signal_wifi_2_bar_grey_700_36dp,
                -1);

        BLUETOOTH = new ControlModule("bluetooth",
                new ControlCommand[]{
                        ControlCommand.BLUETOOTH_ENABLE,
                        ControlCommand.BLUETOOTH_DISABLE,
                        ControlCommand.BLUETOOTH_IS_ENABLED
                },
                -1, -1,
                new String[]{
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN
                },
                R.string.control_module_title_bluetooth,
                R.string.control_module_desc_bluetooth,
                R.drawable.ic_bluetooth_grey_700_36dp,
                -1);

        AUDIO = new ControlModule("audio",
                new ControlCommand[]{
                    ControlCommand.AUDIO_SET_VOLUME,
                    ControlCommand.AUDIO_GET_VOLUME,
                    ControlCommand.AUDIO_GET_VOLUME_PERCENTAGE
                },
                -1, -1,
                new String[]{

                },
                R.string.control_module_title_audio,
                R.string.control_module_desc_audio,
                R.drawable.ic_volume_up_grey_700_36dp,
                R.string.control_module_param_desc_audio);
    }

    public static ControlModule getFromId(String id)
    {
        for (ControlModule controlModule : GetAllModules())
        {
            if (controlModule.getId().equals(id))
                return controlModule;
        }
        return null;
    }

    public static ControlModule getFromCommand(ControlCommand command)
    {
        for (ControlModule controlModule : GetAllModules())
        {
            if (ArrayUtils.contains(controlModule.getCommands(), command))
                return controlModule;
        }
        return null;
    }

    private String id;
    private ControlCommand[] commands;
    private int sdkMin;
    private int sdkMax;
    private String[] requiredPermissions;

    private int titleRes;
    private int descriptionRes;
    private int iconRes;
    private int paramInfoRes;

    private ControlModule(String id, ControlCommand[] commands, int sdkMin, int sdkMax,
                          String[] requiredPermissions, int titleRes, int descriptionRes,
                          int iconRes, int paramInfoRes) {
        this.id = id;
        this.commands = commands;
        this.sdkMin = sdkMin;
        this.sdkMax = sdkMax;
        this.requiredPermissions = requiredPermissions;
        this.titleRes = titleRes;
        this.descriptionRes = descriptionRes;
        this.iconRes = iconRes;
        this.paramInfoRes = paramInfoRes;
    }

    public String getId() {
        return id;
    }

    private ControlCommand[] getCommands() {
        return commands;
    }

    public String getCommandsString(){
        String str = "";
        for (ControlCommand com : commands) {
            if(com != commands[0])
                str += "\r\n";
            str += com.toString();
        }
        return str;
    }

    public int getSdkMin() {
        return sdkMin;
    }

    public int getSdkMax() {
        return sdkMax;
    }

    public int getTitleRes()
    {
        return titleRes;
    }

    public int getDescriptionRes()
    {
        return descriptionRes;
    }

    public int getIconRes() {
        return iconRes;
    }

    public int getParamInfoRes() {
        return paramInfoRes;
    }

    /**
     * get required permissions that are not granted so far
     * @param context app context
     * @return permissions
     */
    public String[] getRequiredPermissions(Context context)
    {
        return PermissionUtils.FilterAppPermissions(context, requiredPermissions);
    }

    public ControlModuleUserData getUserData()
    {
        return DataManager.getUserDataForControlModule(this);
    }

    /**
     * check if control module is compatible with the executing android system
     * @return true if compatible
     */
    public boolean isCompatible()
    {
        return Build.VERSION.SDK_INT >= sdkMin && (sdkMax == -1 || Build.VERSION.SDK_INT <= sdkMax);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ControlModule that = (ControlModule) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }

    /**
     * check if required permissions for this module are granted
     * @param context app context
     * @return true if granted
     */
    boolean checkPermissions(Context context)
    {
        return  PermissionUtils.AppHasPermissions(context, requiredPermissions);
    }

    public static List<ControlModule> GetAllModules()
    {
        List<ControlModule> modules = new ArrayList<>();

        for (Field field : ControlModule.class.getDeclaredFields()) {

            if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) && field.getType() == ControlModule.class) {
                try {
                    modules.add((ControlModule) field.get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return modules;
    }
}
