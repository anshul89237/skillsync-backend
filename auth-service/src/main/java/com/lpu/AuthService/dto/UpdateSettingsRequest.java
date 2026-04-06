package com.lpu.AuthService.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateSettingsRequest {

    private boolean emailNotifications;
    private boolean sessionReminders;
    private boolean marketingUpdates;
    private boolean twoFactorEnabled;

    @NotBlank(message = "Profile visibility is required")
    private String profileVisibility;

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

    public boolean isTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public String getProfileVisibility() {
        return profileVisibility;
    }

    public void setProfileVisibility(String profileVisibility) {
        this.profileVisibility = profileVisibility;
    }
}