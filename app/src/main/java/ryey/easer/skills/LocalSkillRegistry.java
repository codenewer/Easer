/*
 * Copyright (c) 2016 - 2019 Rui Zhao <renyuneyun@gmail.com>
 *
 * This file is part of Easer.
 *
 * Easer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Easer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Easer.  If not, see <http://www.gnu.org/licenses/>.
 */

package ryey.easer.skills;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ryey.easer.commons.CommonSkillHelper;
import ryey.easer.commons.local_skill.Skill;
import ryey.easer.commons.local_skill.SkillView;
import ryey.easer.commons.local_skill.StorageData;
import ryey.easer.commons.local_skill.conditionskill.ConditionData;
import ryey.easer.commons.local_skill.conditionskill.ConditionSkill;
import ryey.easer.commons.local_skill.eventskill.EventData;
import ryey.easer.commons.local_skill.eventskill.EventSkill;
import ryey.easer.commons.local_skill.operationskill.OperationData;
import ryey.easer.commons.local_skill.operationskill.OperationSkill;
import ryey.easer.skills.condition.battery.BatteryConditionSkill;
import ryey.easer.skills.condition.bluetooth_device.BTDeviceConditionSkill;
import ryey.easer.skills.condition.bluetooth_enabled.BluetoothEnabledConditionSkill;
import ryey.easer.skills.condition.calendar.CalendarConditionSkill;
import ryey.easer.skills.condition.cell_location.CellLocationConditionSkill;
import ryey.easer.skills.condition.connectivity.ConnectivityConditionSkill;
import ryey.easer.skills.condition.date.DateConditionSkill;
import ryey.easer.skills.condition.day_of_week.DayOfWeekConditionSkill;
import ryey.easer.skills.condition.headset.HeadsetConditionSkill;
import ryey.easer.skills.condition.ringer_mode.RingerModeConditionSkill;
import ryey.easer.skills.condition.screen.ScreenConditionSkill;
import ryey.easer.skills.condition.time.TimeConditionSkill;
import ryey.easer.skills.condition.wifi.WifiConditionSkill;
import ryey.easer.skills.condition.wifi_enabled.WifiEnabledConditionSkill;
import ryey.easer.skills.event.battery.BatteryEventSkill;
import ryey.easer.skills.event.bluetooth_device.BTDeviceEventSkill;
import ryey.easer.skills.event.broadcast.BroadcastEventSkill;
import ryey.easer.skills.event.calendar.CalendarEventSkill;
import ryey.easer.skills.event.cell_location.CellLocationEventSkill;
import ryey.easer.skills.event.condition_event.ConditionEventEventSkill;
import ryey.easer.skills.event.connectivity.ConnectivityEventSkill;
import ryey.easer.skills.event.date.DateEventSkill;
import ryey.easer.skills.event.dayofweek.DayOfWeekEventSkill;
import ryey.easer.skills.event.headset.HeadsetEventSkill;
import ryey.easer.skills.event.nfc_tag.NfcTagEventSkill;
import ryey.easer.skills.event.notification.NotificationEventSkill;
import ryey.easer.skills.event.screen.ScreenEventSkill;
import ryey.easer.skills.event.sms.SmsEventSkill;
import ryey.easer.skills.event.tcp_trip.TcpTripEventSkill;
import ryey.easer.skills.event.time.TimeEventSkill;
import ryey.easer.skills.event.timer.TimerEventSkill;
import ryey.easer.skills.event.wifi.WifiEventSkill;
import ryey.easer.skills.operation.airplane_mode.AirplaneModeOperationSkill;
import ryey.easer.skills.operation.alarm.AlarmOperationSkill;
import ryey.easer.skills.operation.bluetooth.BluetoothOperationSkill;
import ryey.easer.skills.operation.brightness.BrightnessOperationSkill;
import ryey.easer.skills.operation.broadcast.BroadcastOperationSkill;
import ryey.easer.skills.operation.cellular.CellularOperationSkill;
import ryey.easer.skills.operation.command.CommandOperationSkill;
import ryey.easer.skills.operation.hotspot.HotspotOperationSkill;
import ryey.easer.skills.operation.launch_app.LaunchAppOperationSkill;
import ryey.easer.skills.operation.media_control.MediaControlOperationSkill;
import ryey.easer.skills.operation.network_transmission.NetworkTransmissionOperationSkill;
import ryey.easer.skills.operation.play_media.PlayMediaOperationSkill;
import ryey.easer.skills.operation.ringer_mode.RingerModeOperationSkill;
import ryey.easer.skills.operation.rotation.RotationOperationSkill;
import ryey.easer.skills.operation.send_notification.SendNotificationOperationSkill;
import ryey.easer.skills.operation.send_sms.SendSmsOperationSkill;
import ryey.easer.skills.operation.state_control.StateControlOperationSkill;
import ryey.easer.skills.operation.synchronization.SynchronizationOperationSkill;
import ryey.easer.skills.operation.ui_mode.UiModeOperationSkill;
import ryey.easer.skills.operation.volume.VolumeOperationSkill;
import ryey.easer.skills.operation.wifi.WifiOperationSkill;

/**
 * Used to tell the app what skills can be used.
 *
 * To register a new plugin, simply write a new line in the constructor of this class.
 */
final public class LocalSkillRegistry {

    private final Registry<EventSkill, EventData> eventSkillRegistry = new Registry<>(CommonSkillHelper.TYPE_EVENT);
    private final Registry<OperationSkill, OperationData> operationSkillRegistry = new Registry<>(CommonSkillHelper.TYPE_OPERATION, new String[][]{
            {"event control", "state control"},
    });
    private final Registry<ConditionSkill, ConditionData> conditionSkillRegistry = new Registry<>(CommonSkillHelper.TYPE_CONDITION);
    private final OverallRegistry overallRegistry = new OverallRegistry(new SkillLookupper<?, ?>[] {
            eventSkillRegistry, operationSkillRegistry, conditionSkillRegistry,
    });

    {
        event().registerSkill(ConditionEventEventSkill.class);
        event().registerSkill(TimeEventSkill.class);
        event().registerSkill(DateEventSkill.class);
        event().registerSkill(WifiEventSkill.class);
        event().registerSkill(CellLocationEventSkill.class);
        event().registerSkill(BatteryEventSkill.class);
        event().registerSkill(DayOfWeekEventSkill.class);
        event().registerSkill(BTDeviceEventSkill.class);
        event().registerSkill(ConnectivityEventSkill.class);
        event().registerSkill(CalendarEventSkill.class);
        event().registerSkill(BroadcastEventSkill.class);
        event().registerSkill(SmsEventSkill.class);
        event().registerSkill(NotificationEventSkill.class);
        event().registerSkill(TimerEventSkill.class);
        event().registerSkill(NfcTagEventSkill.class);
        event().registerSkill(HeadsetEventSkill.class);
        event().registerSkill(TcpTripEventSkill.class);
        event().registerSkill(ScreenEventSkill.class);

        condition().registerSkill(BatteryConditionSkill.class);
        condition().registerSkill(BTDeviceConditionSkill.class);
        condition().registerSkill(CalendarConditionSkill.class);
        condition().registerSkill(CellLocationConditionSkill.class);
        condition().registerSkill(ConnectivityConditionSkill.class);
        condition().registerSkill(DateConditionSkill.class);
        condition().registerSkill(DayOfWeekConditionSkill.class);
        condition().registerSkill(HeadsetConditionSkill.class);
        condition().registerSkill(RingerModeConditionSkill.class);
        condition().registerSkill(ScreenConditionSkill.class);
        condition().registerSkill(TimeConditionSkill.class);
        condition().registerSkill(WifiConditionSkill.class);
        condition().registerSkill(WifiEnabledConditionSkill.class);
        condition().registerSkill(BluetoothEnabledConditionSkill.class);

        operation().registerSkill(WifiOperationSkill.class);
        operation().registerSkill(CellularOperationSkill.class);
        operation().registerSkill(BluetoothOperationSkill.class);
        operation().registerSkill(RotationOperationSkill.class);
        operation().registerSkill(BroadcastOperationSkill.class);
        operation().registerSkill(BrightnessOperationSkill.class);
        operation().registerSkill(RingerModeOperationSkill.class);
        operation().registerSkill(CommandOperationSkill.class);
        operation().registerSkill(HotspotOperationSkill.class);
        operation().registerSkill(SynchronizationOperationSkill.class);
        operation().registerSkill(NetworkTransmissionOperationSkill.class);
        operation().registerSkill(MediaControlOperationSkill.class);
        operation().registerSkill(AirplaneModeOperationSkill.class);
        operation().registerSkill(SendSmsOperationSkill.class);
        operation().registerSkill(SendNotificationOperationSkill.class);
        operation().registerSkill(AlarmOperationSkill.class);
        operation().registerSkill(StateControlOperationSkill.class);
        operation().registerSkill(VolumeOperationSkill.class);
        operation().registerSkill(LaunchAppOperationSkill.class);
        operation().registerSkill(UiModeOperationSkill.class);
        operation().registerSkill(PlayMediaOperationSkill.class);
        //TODO: write more skills
    }

    private static final LocalSkillRegistry instance = new LocalSkillRegistry();

    public static LocalSkillRegistry getInstance() {
        return instance;
    }

    private LocalSkillRegistry() {}

    public Registry<EventSkill, EventData> event() {
        return eventSkillRegistry;
    }

    public Registry<OperationSkill, OperationData> operation() {
        return operationSkillRegistry;
    }

    public Registry<ConditionSkill, ConditionData> condition() {
        return conditionSkillRegistry;
    }

    public SkillLookupper<Skill, StorageData> all() {
        return overallRegistry;
    }

    public interface SkillLookupper<T extends Skill, T_data extends StorageData> {
        List<T> getEnabledSkills(@NonNull Context context);
        List<T> getAllSkills();
        boolean hasSkill(String id);
        @Nullable T findSkill(T_data data);
        @Nullable T findSkill(String id);
        @Nullable T findSkill(SkillView view);
    }

    public static class Registry<T extends Skill, T_data extends StorageData> implements SkillLookupper<T, T_data> {
        final int type;
        final List<Class<? extends T>> skillClassList = new ArrayList<>();
        final List<T> skillList = new ArrayList<>();
        //TODO: use Set instead of List for the above two variables && add an "ordered" method to return a List
        final Map<String, String> backwardNameMap = new ArrayMap<>(); // Backward-compatible name conversion

        private Registry(int type) {
            this.type = type;
        }

        private Registry(int type, String[][] backwardNameMap) {
            this(type);
            for (String[] pair : backwardNameMap) {
                this.backwardNameMap.put(pair[0], pair[1]);
            }
        }

        synchronized void registerSkill(Class<? extends T> pluginClass) {
            for (Class<? extends T> klass : skillClassList) {
                if (klass == pluginClass)
                    return;
            }
            skillClassList.add(pluginClass);
            try {
                T plugin = pluginClass.newInstance();
                skillList.add(plugin);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        public List<Class<? extends T>> getSkillClasses() {
            return skillClassList;
        }

        public List<T> getEnabledSkills(@NonNull Context context) {
            List<T> enabledPlugins = new ArrayList<>(skillList.size());
            SharedPreferences settingsPreference =
                    PreferenceManager.getDefaultSharedPreferences(context);
            for (T plugin : skillList) {
                if (settingsPreference.getBoolean(CommonSkillHelper.pluginEnabledKey(plugin), true)
                        && plugin.isCompatible(context)) {
                    enabledPlugins.add(plugin);
                }
            }
            return enabledPlugins;
        }

        @Override
        public List<T> getAllSkills() {
            return skillList;
        }

        /**
         * Test if plugin is available as local plugin
         * TODO: optimize performance
         * @param id
         * @return
         */
        @Override
        public boolean hasSkill(String id) {
            if (findSkill(id) == null)
                return false;
            return true;
        }

        @Nullable
        public T findSkill(T_data data) {
            for (T plugin : getAllSkills()) {
                if (data.getClass() == plugin.dataFactory().dataClass()) {
                    return plugin;
                }
            }
            return null;
        }

        @Nullable
        public T findSkill(String id) {
            if (backwardNameMap.size() > 0)
                Logger.d(backwardNameMap);
            if (backwardNameMap.containsKey(id))
                id = backwardNameMap.get(id);
            for (T plugin : getAllSkills()) {
                if (id.equals(plugin.id())) {
                    return plugin;
                }
            }
            return null;
        }

        @Nullable
        @Override
        public T findSkill(SkillView view) {
            for (T plugin : getAllSkills()) {
                if (view.getClass().equals(plugin.view().getClass()))
                    return plugin;
            }
            return null;
        }

    }

    public static class OverallRegistry implements SkillLookupper<Skill, StorageData> {

        final SkillLookupper<? extends Skill, ? extends StorageData>[] lookupers;

        OverallRegistry(SkillLookupper<? extends Skill, ? extends StorageData>[] lookupers) {
            this.lookupers = lookupers;
        }

        public List<Skill> getEnabledSkills(@NonNull Context context) {
            List<Skill> list = new ArrayList<>();
            for (SkillLookupper<? extends Skill, ? extends StorageData> lookupper : lookupers) {
                list.addAll(lookupper.getEnabledSkills(context));
            }
            return list;
        }

        @Override
        public List<Skill> getAllSkills() {
            List<Skill> list = new ArrayList<>();
            for (SkillLookupper<? extends Skill, ? extends StorageData> lookupper : lookupers) {
                list.addAll(lookupper.getAllSkills());
            }
            return list;
        }

        @Override
        public boolean hasSkill(String id) {
            if (findSkill(id) == null)
                return false;
            return true;
        }

        @Nullable
        @Override
        public Skill findSkill(StorageData storageData) {
            for (Skill plugin : getAllSkills()) {
                if (storageData.getClass().equals(plugin.dataFactory().dataClass()))
                    return plugin;
            }
            return null;
        }

        @Nullable
        @Override
        public Skill findSkill(String id) {
            for (Skill plugin : getAllSkills()) {
                if (id.equals(plugin.id()))
                    return plugin;
            }
            throw new IllegalAccessError();
        }

        @Nullable
        @Override
        public Skill findSkill(SkillView view) {
            for (Skill plugin : getAllSkills()) {
                if (view.getClass().equals(plugin.view().getClass()))
                    return plugin;
            }
            throw new IllegalAccessError();
        }
    }
}
