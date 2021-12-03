package com.livechatinc.inappchat;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by szymonjarosz on 20/07/2017.
 */

public class ChatWindowConfiguration implements Serializable {
    public static final String KEY_LICENCE_NUMBER = "KEY_LICENCE_NUMBER";
    public static final String KEY_GROUP_ID = "KEY_GROUP_ID";
    public static final String KEY_VISITOR_NAME = "KEY_VISITOR_NAME";
    public static final String KEY_VISITOR_EMAIL = "KEY_VISITOR_EMAIL";
    public static final String KEY_LICENCE_REGION = "KEY_LICENCE_REGION";

    private static final String DEFAULT_GROUP_ID = "0";
    public static final String CUSTOM_PARAM_PREFIX = "#LCcustomParam_";

    public final String licenceNumber;
    public final String groupId;
    public final String visitorName;
    public final String visitorEmail;
    public final Region licenceRegion;
    public final HashMap<String, String> customVariables;

    public ChatWindowConfiguration(
            @NonNull String licenceNumber,
            @Nullable String groupId,
            @Nullable String visitorName,
            @Nullable String visitorEmail,
            @Nullable Region licenceRegion,
            @Nullable HashMap<String, String> customVariables) {
        this.licenceNumber = licenceNumber;
        this.groupId = groupId;
        this.visitorName = visitorName;
        this.visitorEmail = visitorEmail;
        this.customVariables = customVariables;
        this.licenceRegion = licenceRegion != null ? licenceRegion : Region.DAL;
    }

    public static ChatWindowConfiguration fromBundle(Bundle arguments) {
        HashMap<String, String> customParams = new HashMap();
        for (String key : arguments.keySet()) {
            if (key.startsWith(CUSTOM_PARAM_PREFIX)) {
                customParams.put(key.replaceFirst(CUSTOM_PARAM_PREFIX, ""), arguments.getString(key));
            }
        }
        return new ChatWindowConfiguration.Builder()
                .setLicenceNumber(arguments.getString(KEY_LICENCE_NUMBER))
                .setGroupId(arguments.getString(KEY_GROUP_ID))
                .setVisitorName(arguments.getString(KEY_VISITOR_NAME))
                .setVisitorEmail(arguments.getString(KEY_VISITOR_EMAIL))
                .setLicenceRegion((Region) arguments.getSerializable(KEY_LICENCE_REGION))
                .setCustomParams(customParams).build();
    }

    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put(KEY_LICENCE_NUMBER, licenceNumber);
        params.put(KEY_GROUP_ID, groupId != null ? groupId : DEFAULT_GROUP_ID);
        if (!TextUtils.isEmpty(visitorName))
            params.put(KEY_VISITOR_NAME, visitorName);
        if (!TextUtils.isEmpty(visitorEmail))
            params.put(KEY_VISITOR_EMAIL, visitorEmail);
        params.put(KEY_LICENCE_REGION, licenceRegion.toString());
        if (customVariables != null) {
            for (String key : customVariables.keySet()) {
                params.put(CUSTOM_PARAM_PREFIX + key, customVariables.get(key));
            }
        }
        return params;
    }

    public Bundle asBundle() {
        Bundle bundle = new Bundle();
        for (Map.Entry<String, String> entry : getParams().entrySet()) {
            if (entry.getKey().equals(KEY_LICENCE_REGION)) {
                for (Region region : Region.values()) {
                    if (region.toString().equals(entry.getValue())) {
                        bundle.putSerializable(KEY_LICENCE_REGION, region);
                    }
                }
            } else {
                bundle.putString(entry.getKey(), entry.getValue());
            }
        }
        return bundle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatWindowConfiguration that = (ChatWindowConfiguration) o;

        if (!licenceNumber.equals(that.licenceNumber)) return false;
        if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null) return false;
        if (visitorName != null ? !visitorName.equals(that.visitorName) : that.visitorName != null)
            return false;
        if (visitorEmail != null ? !visitorEmail.equals(that.visitorEmail) : that.visitorEmail != null)
            return false;
        if (licenceRegion != that.licenceRegion)
            return false;
        return customVariables != null ? customVariables.equals(that.customVariables) : that.customVariables == null;
    }

    @Override
    public int hashCode() {
        int result = licenceNumber.hashCode();
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + (visitorName != null ? visitorName.hashCode() : 0);
        result = 31 * result + (visitorEmail != null ? visitorEmail.hashCode() : 0);
        result = 31 * result + licenceRegion.hashCode();
        result = 31 * result + (customVariables != null ? customVariables.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return
                "licenceNumber='" + licenceNumber + "'\n" +
                        "groupId='" + groupId + "'\n" +
                        "visitorName='" + visitorName + "'\n" +
                        "visitorEmail='" + visitorEmail + "'\n" +
                        "licenceRegion='" + licenceRegion + "'\n" +
                        "customVariables=" + customVariables;
    }

    public static class Builder {
        private String licenceNumber;
        private String groupId;
        private String visitorName;
        private String visitorEmail;
        private Region licenceRegion;
        private HashMap<String, String> customParams;

        public ChatWindowConfiguration build() {
            if (TextUtils.isEmpty(licenceNumber))
                throw new IllegalStateException("Licence Number cannot be null");
            return new ChatWindowConfiguration(licenceNumber, groupId, visitorName, visitorEmail, licenceRegion, customParams);
        }

        public Builder setLicenceNumber(String licenceNr) {
            this.licenceNumber = licenceNr;
            return this;
        }

        public Builder setGroupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder setVisitorName(String visitorName) {
            this.visitorName = visitorName;
            return this;
        }

        public Builder setVisitorEmail(String visitorEmail) {
            this.visitorEmail = visitorEmail;
            return this;
        }

        public Builder setLicenceRegion(Region licenceRegion) {
            this.licenceRegion = licenceRegion;
            return this;
        }

        public Builder setCustomParams(HashMap<String, String> customParams) {
            this.customParams = customParams;
            return this;
        }
    }
}

enum Region {
    DAL, FRA
}
