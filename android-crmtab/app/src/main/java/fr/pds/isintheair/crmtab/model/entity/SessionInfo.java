package fr.pds.isintheair.crmtab.model.entity;

public class SessionInfo {
    private Integer          userId;
    private DeviceType       deviceType;
    private NotificationType notificationType;

    private SessionInfo(Builder messageInfoBuilder) {
        this.userId = messageInfoBuilder.userId;
        this.deviceType = messageInfoBuilder.deviceType;
        this.notificationType = messageInfoBuilder.notificationType;
    }

    public Integer getUserId() {
        return userId;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public static class Builder {
        private NotificationType notificationType;
        private DeviceType       deviceType;
        private Integer          userId;

        public Builder addUserId(Integer userId) {
            this.userId = userId;

            return this;
        }

        public Builder addDeviceType(DeviceType deviceType) {
            this.deviceType = deviceType;

            return this;
        }

        public Builder addNotificationType(NotificationType notificationType) {
            this.notificationType = notificationType;

            return this;
        }

        public SessionInfo build() {
            return new SessionInfo(this);
        }
    }
}
