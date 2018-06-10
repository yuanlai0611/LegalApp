package com.example.yuanyuanlai.legalapp.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.example.yuanyuanlai.legalapp.Application.GlobalApp;
import com.example.yuanyuanlai.legalapp.R;
import com.sdk.bluetooth.bean.BloodData;
import com.sdk.bluetooth.bean.HeartData;
import com.sdk.bluetooth.bean.SleepData;
import com.sdk.bluetooth.bean.SportsData;
import com.sdk.bluetooth.manage.AppsBluetoothManager;
import com.sdk.bluetooth.manage.GlobalDataManager;
import com.sdk.bluetooth.manage.GlobalVarManager;
import com.sdk.bluetooth.protocol.command.base.BaseCommand;
import com.sdk.bluetooth.protocol.command.base.CommandConstant;
import com.sdk.bluetooth.protocol.command.bind.BindEnd;
import com.sdk.bluetooth.protocol.command.bind.BindStart;
import com.sdk.bluetooth.protocol.command.clear.ClearBloodData;
import com.sdk.bluetooth.protocol.command.clear.ClearHeartData;
import com.sdk.bluetooth.protocol.command.clear.ClearSleepData;
import com.sdk.bluetooth.protocol.command.clear.ClearSportData;
import com.sdk.bluetooth.protocol.command.count.AllDataCount;
import com.sdk.bluetooth.protocol.command.count.SportSleepCount;
import com.sdk.bluetooth.protocol.command.data.DeviceDisplaySportSleep;
import com.sdk.bluetooth.protocol.command.data.GetBloodData;
import com.sdk.bluetooth.protocol.command.data.GetHeartData;
import com.sdk.bluetooth.protocol.command.data.GetSleepData;
import com.sdk.bluetooth.protocol.command.data.GetSportData;
import com.sdk.bluetooth.protocol.command.data.RemindSetting;
import com.sdk.bluetooth.protocol.command.data.SportSleepMode;
import com.sdk.bluetooth.protocol.command.device.BatteryPower;
import com.sdk.bluetooth.protocol.command.device.DateTime;
import com.sdk.bluetooth.protocol.command.device.DeviceVersion;
import com.sdk.bluetooth.protocol.command.device.Language;
import com.sdk.bluetooth.protocol.command.device.Motor;
import com.sdk.bluetooth.protocol.command.device.RestoreFactory;
import com.sdk.bluetooth.protocol.command.device.TimeSurfaceSetting;
import com.sdk.bluetooth.protocol.command.device.Unit;
import com.sdk.bluetooth.protocol.command.device.WatchID;
import com.sdk.bluetooth.protocol.command.expands.AutomaticCorrectionTime;
import com.sdk.bluetooth.protocol.command.expands.CorrectionTime;
import com.sdk.bluetooth.protocol.command.expands.FastCorrectTime;
import com.sdk.bluetooth.protocol.command.expands.FinishCorroctionTime;
import com.sdk.bluetooth.protocol.command.expands.Point2Zero;
import com.sdk.bluetooth.protocol.command.expands.RemindCount;
import com.sdk.bluetooth.protocol.command.expands.TurnPointers;
import com.sdk.bluetooth.protocol.command.other.BloodStatus;
import com.sdk.bluetooth.protocol.command.other.HeartStatus;
import com.sdk.bluetooth.protocol.command.push.MsgCountPush;
import com.sdk.bluetooth.protocol.command.setting.AutoSleep;
import com.sdk.bluetooth.protocol.command.setting.GoalsSetting;
import com.sdk.bluetooth.protocol.command.setting.HeartRateAlarm;
import com.sdk.bluetooth.protocol.command.setting.SwitchSetting;
import com.sdk.bluetooth.protocol.command.user.UserInfo;
import com.sdk.bluetooth.utils.DateUtil;

/**
 * github.com/GongYunHaoyyy
 * Creste by GongYunHao on 2018/6/5
 */
public class BlueToothUtil {

    private DeviceId mDeviceId = null;
    private DeviceVersion2 mDeviceVersion = null;
    private HeartRate mHeartRate = null;
    private BatteryPower2 mbattery = null;
    private AlertDialog alertDialog;
    private Context context;

    public BlueToothUtil(Context context) {
        this.context = context;
    }

    public void showTipDialog(String tip){
        if (alertDialog==null){
            alertDialog=new AlertDialog.Builder( context )
                    .setTitle( R.string.activityNotification )
                    .setCancelable( true )
                    .setMessage( tip )
                    .create();
        }else {
            alertDialog.setMessage( tip );
        }
        alertDialog.show();
    }

    public interface DeviceId{
        void getDeviceId(String id);
    }
    public void setBattery(BatteryPower2 batteryPower2){
        mbattery = batteryPower2;
    }

    public void setDeviceId(DeviceId deviceId){
        mDeviceId = deviceId;
    }

    public interface DeviceVersion2{
        Void getDeviceVersion(String deviceVersion);
    }

    public interface BatteryPower2{
        void getBatteryPower(int batteryPower);
    }

//         "heartRate":101,
//         "deviceId":"haha",
//         "timestamp":"2018-06-04 00:00:06",
//         "sportSteps":101,
//         "sportCal":21,
//         "sportEnergy":41,
//         "longitude":"101",
//         "latitude":"201"

    public interface HeartRate{
        void getHeartRate(int heartrate);
    }

    public void setHeartRate(HeartRate heartRate){
        mHeartRate = heartRate;
    }

    /**
     * 处理蓝牙命令回调
     */
    private BaseCommand.CommandResultCallback commandResultCallback = new BaseCommand.CommandResultCallback() {
        @Override
        public void onSuccess(BaseCommand command) {
            if ((command instanceof WatchID)) {
                String watch = GlobalVarManager.getInstance().getWatchID();
                mDeviceId.getDeviceId( watch );
                showTipDialog(watch);
            } else if (command instanceof DeviceVersion) {
                String localVersion = GlobalVarManager.getInstance().getSoftVersion();
                mDeviceVersion.getDeviceVersion( localVersion );
                showTipDialog(localVersion);
            } else if (command instanceof BatteryPower) {
                showTipDialog(GlobalVarManager.getInstance().getBatteryPower() + "%");
                mbattery.getBatteryPower( GlobalVarManager.getInstance().getBatteryPower() );
            } else if (command instanceof TimeSurfaceSetting || command instanceof Unit) {
                showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.successful));
            } else if (command instanceof Motor) {
                showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.successful) + "  " + GlobalVarManager.getInstance().getMotor());
            } else if (command instanceof SportSleepMode) {
                if (GlobalVarManager.getInstance().getSportSleepMode() == 0) {
                    showTipDialog("sport model");
                } else {
                    showTipDialog("sleep model");
                }
            } else if (command instanceof DeviceDisplaySportSleep) {
                showTipDialog("Step:" + GlobalVarManager.getInstance().getDeviceDisplayStep() + "step" +
                        "\n Calorie:" + GlobalVarManager.getInstance().getDeviceDisplayCalorie() + "cal" +
                        "\n Distance:" + GlobalVarManager.getInstance().getDeviceDisplayDistance() + "m" +
                        "\n Sleep time:" + GlobalVarManager.getInstance().getDeviceDisplaySleep() + "min");
            } else if (command instanceof BindStart || command instanceof UserInfo
                    || command instanceof BindEnd
                    || command instanceof GoalsSetting
                    || command instanceof RestoreFactory || command instanceof Language) {
                showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.successful));
            } else if (command instanceof DateTime) {
                if (command.getAction() == CommandConstant.ACTION_CHECK) {
                    showTipDialog(GlobalVarManager.getInstance().getDeviceDateTime());
                }
                if (command.getAction() == CommandConstant.ACTION_SET) {
                    showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.successful));
                }
            } else if (command instanceof AutoSleep) {
                if (command.getAction() == CommandConstant.ACTION_CHECK) {
                    showTipDialog("enter sleep:" + GlobalVarManager.getInstance().getEnterSleepHour() + "hour" +
                            "\n enter sleep:" + GlobalVarManager.getInstance().getEnterSleepMin() + "min" +
                            "\n quit sleep:" + GlobalVarManager.getInstance().getQuitSleepHour() + "hour" +
                            "\n quit sleep:" + GlobalVarManager.getInstance().getQuitSleepMin() + "min" +
                            "\n remind sleep cycle:" + GlobalVarManager.getInstance().getRemindSleepCycle());
                }
                if (command.getAction() == CommandConstant.ACTION_SET) {
                    showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.successful));
                }
            } else if (command instanceof GetSportData) {
                //
                int step = 0;
                int calorie = 0;
                int distance = 0;
                if (GlobalDataManager.getInstance().getSportsDatas() == null) {
                    showTipDialog("null");
                } else {
                    for (SportsData sportsData : GlobalDataManager.getInstance().getSportsDatas()) {
                        step += sportsData.sport_steps;
                        calorie += sportsData.sport_cal;
                        distance += sportsData.sport_energy;
                    }
                    showTipDialog("Step:" + step + "step" +
                            "\n Calorie:" + calorie + "cal");
                }
            } else if (command instanceof GetSleepData) {
                if (GlobalDataManager.getInstance().getSleepDatas() == null) {
                    showTipDialog("null");
                } else {
                    // sleepData.sleep_type
                    // 0：睡着
                    // 1：浅睡
                    // 2：醒着
                    // 3：准备入睡
                    // 4：退出睡眠
                    // 16：进入睡眠模式
                    // 17：退出睡眠模式（本次睡眠非预设睡眠）
                    // 18：退出睡眠模式（本次睡眠为预设睡眠）
                    String sleepStr = "";
                    for (SleepData sleepData : GlobalDataManager.getInstance().getSleepDatas()) {
                        sleepStr = sleepStr + DateUtil.dateToSec(DateUtil.timeStampToDate(sleepData.sleep_time_stamp * 1000)) + " 类型:" + sleepData.sleep_type + "\n";
                    }
                    showTipDialog(sleepStr);
                }
            } else if (command instanceof ClearSportData) {
                showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.successful));
            } else if (command instanceof ClearSleepData) {
                showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.successful));
            } else if (command instanceof SwitchSetting) {
                if (command.getAction() == CommandConstant.ACTION_CHECK) {
                    // 防丢开关
                    // 自动同步开关
                    // 睡眠开关
                    // 自动睡眠监测开关
                    // 来电提醒开关
                    // 未接来电提醒开关
                    // 短信提醒开关
                    // 社交提醒开关
                    // 邮件提醒开关
                    // 日历开关
                    // 久坐提醒开关
                    // 超低功耗功能开关
                    // 二次提醒开关

                    // 运动心率模式开关
                    // FACEBOOK开关
                    // TWITTER开关
                    // INSTAGRAM开关
                    // QQ开关
                    // WECHAT开关
                    // WHATSAPP开关
                    // LINE开关

                    showTipDialog("isAntiLostSwitch:" + GlobalVarManager.getInstance().isAntiLostSwitch()
                            + "\n isAutoSyncSwitch:" + GlobalVarManager.getInstance().isAutoSyncSwitch()
                            + "\n isSleepSwitch:" + GlobalVarManager.getInstance().isSleepSwitch()
                            + "\n isSleepStateSwitch:" + GlobalVarManager.getInstance().isSleepStateSwitch()
                            + "\n isIncomePhoneSwitch:" + GlobalVarManager.getInstance().isIncomePhoneSwitch()
                            + "\n isMissPhoneSwitch:" + GlobalVarManager.getInstance().isMissPhoneSwitch()
                            + "\n isSmsSwitch:" + GlobalVarManager.getInstance().isSmsSwitch()
                            + "\n isSocialSwitch:" + GlobalVarManager.getInstance().isSocialSwitch()
                            + "\n isMailSwitch:" + GlobalVarManager.getInstance().isMailSwitch()
                            + "\n isCalendarSwitch:" + GlobalVarManager.getInstance().isCalendarSwitch()
                            + "\n isSedentarySwitch:" + GlobalVarManager.getInstance().isSedentarySwitch()
                            + "\n isLowPowerSwitch:" + GlobalVarManager.getInstance().isLowPowerSwitch()
                            + "\n isSecondRemindSwitch:" + GlobalVarManager.getInstance().isSecondRemindSwitch()

                            + "\n isSportHRSwitch:" + GlobalVarManager.getInstance().isSportHRSwitch()
                            + "\n isFacebookSwitch:" + GlobalVarManager.getInstance().isFacebookSwitch()
                            + "\n isTwitterSwitch:" + GlobalVarManager.getInstance().isTwitterSwitch()
                            + "\n isInstagamSwitch:" + GlobalVarManager.getInstance().isInstagamSwitch()
                            + "\n isQqSwitch:" + GlobalVarManager.getInstance().isQqSwitch()
                            + "\n isWechatSwitch:" + GlobalVarManager.getInstance().isWechatSwitch()
                            + "\n isWhatsappSwitch:" + GlobalVarManager.getInstance().isWhatsappSwitch()
                            + "\n isLineSwitch:" + GlobalVarManager.getInstance().isLineSwitch()
                    );
                }
                if (command.getAction() == CommandConstant.ACTION_SET) {
                    showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.successful));
                }
            } else if (command instanceof MsgCountPush) {
                showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.successful));
            } else if (command instanceof TurnPointers) {
                showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.successful));
            } else if (command instanceof AutomaticCorrectionTime) {
                showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.successful));
            } else if (command instanceof CorrectionTime) {
                showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.successful));
            } else if (command instanceof Point2Zero) {
                showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.successful));
            } else if(command instanceof FastCorrectTime){
                showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.successful));
            } else if(command instanceof FinishCorroctionTime){
                showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.successful));
            }
            //提醒相关
            else if (command instanceof RemindCount) {
                showTipDialog(GlobalVarManager.getInstance().getRemindCount() + "");
            } else if (command instanceof RemindSetting) {
                showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.successful));
            }

            // all data count
            if (command instanceof AllDataCount) {
                showTipDialog("SportCount:" + GlobalVarManager.getInstance().getSportCount()
                        + "\n SleepCount:" + GlobalVarManager.getInstance().getSleepCount()
                        + "\n HeartRateCount:" + GlobalVarManager.getInstance().getHeartRateCount()
                        + "\n BloodCount:" + GlobalVarManager.getInstance().getBloodCount()
                );
            }

            // 心率
            if (command instanceof HeartRateAlarm) {
                if (command.getAction() == CommandConstant.ACTION_SET) {   //设置
                    showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.successful));
                }
                if (command.getAction() == CommandConstant.ACTION_CHECK) {    //查询
                    //获取设备的心率预警状态(暂时获取本地的)
                    showTipDialog(
                            "HighLimit：" + GlobalVarManager.getInstance().getHighHeartLimit() + " bpm \n" +
                                    "LowLimit：" + GlobalVarManager.getInstance().getLowHeartLimit() + " bpm \n" +
                                    "AutoHeart：" + GlobalVarManager.getInstance().getAutoHeart() + " min \n" +
                                    "isHeartAlarm：" + GlobalVarManager.getInstance().isHeartAlarm() + "\n" +
                                    "isAutoHeart：" + GlobalVarManager.getInstance().isAutoHeart()
                    );
                }
            }

            if (command instanceof GetHeartData) {
                String heartDatas = "";
                for (HeartData heartData : GlobalDataManager.getInstance().getHeartDatas()) {
                    heartDatas += "value:" + heartData.heartRate_value + "---time:" + DateUtil.dateToSec(DateUtil.timeStampToDate(heartData.time_stamp * 1000)) + "\n";
                    mHeartRate.getHeartRate( heartData.heartRate_value );
                }

                showTipDialog(heartDatas);
            }
            if (command instanceof ClearHeartData) {
                showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.successful));
            }
            if (command instanceof HeartStatus) {
                if (command.getAction() == CommandConstant.ACTION_CHECK) {
                    showTipDialog("status:" + GlobalVarManager.getInstance().isHeartMeasure());
                }
                if (command.getAction() == CommandConstant.ACTION_SET) {
                    showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.successful));
                }
            }

            // 血压
            if (command instanceof GetBloodData) {
                String bloodDatas = "";
                for (BloodData bloodData : GlobalDataManager.getInstance().getBloodDatas()) {
                    bloodDatas += "bigValue:" + bloodData.bigValue + "minValue:" + bloodData.minValue + "---time:" + DateUtil.dateToSec(DateUtil.timeStampToDate(bloodData.time_stamp * 1000)) + "\n";
                }
                showTipDialog(bloodDatas);
            }
            if (command instanceof ClearBloodData) {
                showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.successful));
            }

            if (command instanceof BloodStatus) {
                if (command.getAction() == CommandConstant.ACTION_CHECK) {
                    showTipDialog("status:" + GlobalVarManager.getInstance().isBloodMeasure());
                }
                if (command.getAction() == CommandConstant.ACTION_SET) {
                    showTipDialog(GlobalApp.getAppContext().getResources().getString( R.string.successful));
                }
            }
        }

        @Override
        public void onFail(BaseCommand command) {
            if ((command instanceof WatchID)) {
            } else if (command instanceof DeviceVersion) {
            } else if (command instanceof BatteryPower) {
            } else {
                showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.failed));
            }
        }
    };

    public void getVersion(){
        DeviceVersion deviceVersion = new DeviceVersion(commandResultCallback, DeviceVersion.DeviceSoftVersion);
        AppsBluetoothManager.getInstance(GlobalApp.getAppContext())
                .sendCommand(deviceVersion);
    }

    public void getWatchId(){
        AppsBluetoothManager.getInstance(GlobalApp.getAppContext())
                .sendCommand(new WatchID(commandResultCallback));
    }

    public void getBattery(){
        AppsBluetoothManager.getInstance(GlobalApp.getAppContext())
                .sendCommand(new BatteryPower(commandResultCallback));
    }

    public void getSportDataCounts(){//获取运动条数
        AppsBluetoothManager.getInstance(GlobalApp.getAppContext())
                .sendCommand(new SportSleepCount( new BaseCommand.CommandResultCallback() {
                    @Override
                    public void onSuccess(BaseCommand command) {
                        showTipDialog("SportCount:" + GlobalVarManager.getInstance().getSportCount());
                    }

                    @Override
                    public void onFail(BaseCommand command) {
                        showTipDialog(GlobalApp.getAppContext().getResources().getString(R.string.failed));
                    }
                }, 1, 0));
    }

    public void getSportDetailData(){
        // 获取运动数据详情需要传入运动数据条数，所以在获取运动数据详情之前必需先获取运动数据条数。
        // 如果运动条数为0，则运动详情数必定为0。所以如果获取到运动条数为0，则没有必要再去获取运动详情数据。
        AppsBluetoothManager.getInstance(GlobalApp.getAppContext())
                .sendCommand(new GetSportData(commandResultCallback, (int) GlobalVarManager.getInstance().getSportCount()));
    }

    public void openHeartRateSwitch(){
        AppsBluetoothManager.getInstance(GlobalApp.getAppContext())
                .sendCommand(new HeartStatus(commandResultCallback, 1));
    }
    public void getHeartRateData(){
        AppsBluetoothManager.getInstance(GlobalApp.getAppContext())
                .sendCommand(new GetHeartData(commandResultCallback, 1, 0, (int) GlobalVarManager.getInstance().getHeartRateCount()));
    }

}
