package com.lpu.AuthService.dto.response;

public class UserSettingsResponse {

    private boolean emailNotifications;
    private boolean sessionReminders;
    private boolean marketingUpdates;
    private String profileVisibility;
    private boolean twoFactorEnabled;

    public boolean isEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public boolean isSessionReminders() {
        return sessionReminders;
    }

    public void setSessionReminders(boolean sessionReminders) {
        this.sessionReminders = sessionReminders;
    }

    public boolean isMarketingUpdates() {
        return marketingUpdates;
    }

    public void setMarketingUpdates(boolean marketingUpdates) {
        this.marketingUpdates = marketingUpdates;
    }

    public String getProfileVisibility() {
        return profileVisibility;
    }

    public void setProfileVisibility(String profileVisibility) {
        this.profileVisibility = profileVisibility;
    }

    public boolean isTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }
}