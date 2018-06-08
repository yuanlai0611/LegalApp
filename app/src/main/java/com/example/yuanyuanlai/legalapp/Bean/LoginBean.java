package com.example.yuanyuanlai.legalapp.Bean;

public class LoginBean {


    private ObjectBean object;

    private Status status;

    public ObjectBean getObject() {
        return object;
    }

    public void setObject(ObjectBean object) {
        this.object = object;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static class ObjectBean {

        private String bluetoothId;
        private String createTime;
        private String deviceId;
        private boolean faceCheck;
        private int id;
        private String idCard;
        private String phone;
        private String userName;

        public String getBluetoothId() {
            return bluetoothId;
        }

        public void setBluetoothId(String bluetoothId) {
            this.bluetoothId = bluetoothId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public boolean isFaceCheck() {
            return faceCheck;
        }

        public void setFaceCheck(boolean faceCheck) {
            this.faceCheck = faceCheck;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }


}
