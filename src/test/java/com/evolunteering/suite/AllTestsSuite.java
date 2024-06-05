package com.evolunteering.suite;

import com.evolunteering.service.UserServiceTest;
import com.evolunteering.service.RoleServiceTest;
import com.evolunteering.service.OrganizationServiceTest;
import com.evolunteering.service.VITypeServiceTest;
import com.evolunteering.service.VolunteerInitiativeServiceTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        UserServiceTest.class,
        RoleServiceTest.class,
        OrganizationServiceTest.class,
        VITypeServiceTest.class,
        VolunteerInitiativeServiceTest.class
})
public class AllTestsSuite {
    // пустий клас, тут нічого не потрібно
}
