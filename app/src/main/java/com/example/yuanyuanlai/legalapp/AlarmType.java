package com.example.yuanyuanlai.legalapp;
// 心率,手机手表电池,gps:
//      >每三分钟测试一次(时间是可以动态设置的),
//      >依次检查:若心率三次为0,则设置为1;若手机或者手表电量低于20%,则报警为3或4;GPS无信号则报警为5;
//      >若存在重复,则组合的id为13,14,15,34,35,45,134,135,145,345,1345(算法为:从小到大查,报警情况为true,就乘以10加新的报警id)
//      >若存在gps无法获取,返回的经纬度为-1
// 蓝牙断开报警:
//      >蓝牙断开报警为2
// 人脸签到
//      >拒绝单位时间签到为7
//      >签到识别失败为6
//      >签到成功为8
// 9,10是服务器检查,app不用管
public enum  AlarmType {
    HEART_ZERO(1, "心率连续3次为0"),
    BLUETOOTH_DISCONNECT(2, "蓝牙断开连接"),
    PHONE_LOW_ELECTRICITY(3, "手机电量低"),
    WATCH_LOW_ELECTRICITY(4, "手表电量低"),
    GPS_WEAK_SIGNAL(5, "GPS无法获取"),
    FACE_SIGN_IN_FAIL(6, "人脸签到失败"),
    FACE_SIGN_IN_REFUSE(7, "拒绝单位时间签到"),
    FACE_SIGN_IN_SUCCESS(8, "人脸签到成功"),
    NO_DATA_UPLOAD(9, "连续三个周期没有提交数据到服务器"),
    OUT_OF_CONTROL_FROM(10,"一个小时未收到手机数据,存在脱管倾向"),
    COMBINE13(13,"心率连续3次为0,手机电量低"),
    COMBINE14(14,"心率连续3次为0,手表电量低"),
    COMBINE15(15,"心率连续3次为0,GPS无法获取"),
    COMBINE34(34,"手机电量低,手表电量低"),
    COMBINE35(35,"手机电量低,GPS无法获取"),
    COMBINE45(45,"手表电量低,GPS无法获取"),
    COMBINE134(134,"心率连续3次为0,手机电量低,手表电量低"),
    COMBINE135(135,"心率连续3次为0,手机电量低,GPS无法获取"),
    COMBINE145(145,"心率连续3次为0,手表电量低,GPS无法获取"),
    COMBINE345(345,"手机电量低,手表电量低,GPS无法获取"),
    COMBINE1345(1345,"心率连续3次为0,手机电量低,手表电量低,GPS无法获取")
    ;

    private int alarmId;
    private String description;

    AlarmType(int alarmId, String description){
        this.alarmId = alarmId;
        this.description = description;
    }

    public int getAlarmId(){
        return alarmId;
    }

    public String getDescription() {
        return description;
    }

    public static AlarmType getType(int alarmId) {
        for (AlarmType alarmType : AlarmType.values()) {
            if (alarmType.alarmId == alarmId) {
                return alarmType;
            }
        }
        return null;
    }

    public static String getDescription(int alarmId) {
        return getType(alarmId).getDescription();
    }
}
