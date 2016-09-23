package com.telekom.m2m.cot.restsdk.alarm;

import com.telekom.m2m.cot.restsdk.CloudOfThingsPlatform;
import com.telekom.m2m.cot.restsdk.inventory.ManagedObject;
import com.telekom.m2m.cot.restsdk.util.TestHelper;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Date;

/**
 * Created by breucking on 30.01.16.
 */
public class AlarmApiIT {

    CloudOfThingsPlatform cotPlat = new CloudOfThingsPlatform(TestHelper.TEST_HOST, TestHelper.TEST_TENANT, TestHelper.TEST_USERNAME, TestHelper.TEST_PASSWORD);
    private ManagedObject testManagedObject;

    @BeforeClass
    public void setUp() {
        testManagedObject = TestHelper.createRandomManagedObjectInPlatform(cotPlat, "fake_name");
    }

    @AfterClass
    public void tearDown() {
        TestHelper.deleteManagedObjectInPlatform(cotPlat, testManagedObject);
    }


    @Test
    public void testCreateAlarm() throws Exception {

        Alarm alarm = new Alarm();
        alarm.setText("Strange thing happend!");
        alarm.setType("com_telekom_TestType");
        alarm.setTime(new Date());
        alarm.setSource(testManagedObject);
        alarm.set("foo", "{ \"alt\": 99.9, \"lng\": 8.55436, \"lat\": 50.02868 }");
        alarm.setStatus(Alarm.STATE_ACTIVE);
        alarm.setSeverity(Alarm.SEVERITY_MAJOR);

        AlarmApi alarmApi = cotPlat.getAlarmApi();

        Alarm createdAlarm = alarmApi.create(alarm);
        Assert.assertNotNull("Should now have an Id", createdAlarm.getId());
    }

    @Test
    public void testCreateAndRead() throws Exception {
        Date timeOfEventHappening = new Date();

        Alarm alarm = new Alarm();
        alarm.setText("Strange thing happend!");
        alarm.setType("com_telekom_TestType");
        alarm.setTime(new Date());
        alarm.setSource(testManagedObject);
        alarm.set("foo", "{ \"alt\": 99.9, \"lng\": 8.55436, \"lat\": 50.02868 }");
        alarm.setStatus(Alarm.STATE_ACTIVE);
        alarm.setSeverity(Alarm.SEVERITY_MAJOR);

        AlarmApi alarmApi = cotPlat.getAlarmApi();

        Alarm createdAlarm = alarmApi.create(alarm);
        Assert.assertNotNull("Should now have an Id", createdAlarm.getId());

        Alarm retrievedAlarm = alarmApi.getAlarm(createdAlarm.getId());
        Assert.assertEquals(retrievedAlarm.getId(), createdAlarm.getId());
        Assert.assertEquals(retrievedAlarm.getType(), "com_telekom_TestType");
        Assert.assertEquals(retrievedAlarm.getText(), "Strange thing happend!");
        Assert.assertEquals(retrievedAlarm.getTime().compareTo(timeOfEventHappening), 0);
        Assert.assertNotNull(retrievedAlarm.getCreationTime());
        Assert.assertEquals(retrievedAlarm.getCreationTime().compareTo(new Date()), -1);

    }


}
