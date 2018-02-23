package ie.droidfactory.fibarodemoapp.model;

/**
 * Created by kudlaty on 2018-02-18.
 */

public class Info {

        private String serialNumber;
        private String mac;
        private String softVersion;
        private boolean beta;
        private String zwaveVersion;
        private int serverStatus;
        private String defaultLanguage;
        private String sunsetHour;
        private String sunriseHour;
        private String hotelMode;
        private boolean updateStableAvailable;
        private boolean updateBetaAvailable;
        private boolean batteryLowNotification;

        private static Info info;


    public static Info getInfo() {
        return info;
    }

    public static void setInfo(Info info) {
        Info.info = info;
    }

    public String getSerialNumber() {
            return serialNumber;
        }

        public String getMac() {
            return mac;
        }

        public String getSoftVersion() {
            return softVersion;
        }

        public boolean isBeta() {
            return beta;
        }

        public String getZwaveVersion() {
            return zwaveVersion;
        }

        public int getServerStatus() {
            return serverStatus;
        }

        public String getDefaultLanguage() {
            return defaultLanguage;
        }

        public String getSunsetHour() {
            return sunsetHour;
        }

        public String getSunriseHour() {
            return sunriseHour;
        }

        public String getHotelMode() {
            return hotelMode;
        }

        public boolean isUpdateStableAvailable() {
            return updateStableAvailable;
        }

        public boolean isUpdateBetaAvailable() {
            return updateBetaAvailable;
        }

        public boolean isBatteryLowNotification() {
            return batteryLowNotification;
        }

/*
        "serialNumber": "HC2-005587",
        "mac": "38:60:77:6a:c0:8a",
        "softVersion": "3.550",
        "beta": true,
        "zwaveVersion": "3.42",
        "serverStatus": 1372264253,
        "defaultLanguage": "pl",
        "sunsetHour": "21:58",
        "sunriseHour": "05:17",
        "hotelMode": false,
        "updateStableAvailable": false,
        "updateBetaAvailable": false,
        "batteryLowNotification": true
*/

}
