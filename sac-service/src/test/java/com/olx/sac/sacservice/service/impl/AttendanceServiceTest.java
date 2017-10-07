package com.olx.sac.sacservice.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.olx.sac.sacservice.Application;
import com.olx.sac.sacservice.config.TestConfig;
import com.olx.sac.sacservice.service.AttendanceService;
import com.olx.sac.sacservice.service.StateService;
import com.olx.sac.sacserviceapi.attendance.CallDTO;
import com.olx.sac.sacserviceapi.attendance.StateDTO;
import com.olx.sac.sacserviceapi.constants.ReasonCalled;
import com.olx.sac.sacserviceapi.constants.TypeOfCall;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, TestConfig.class})
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@IntegrationTest
@TestExecutionListeners({	DependencyInjectionTestExecutionListener.class,
							DirtiesContextTestExecutionListener.class,
							TransactionalTestExecutionListener.class,
							DbUnitTestExecutionListener.class})
public class AttendanceServiceTest {

	@Autowired
	private AttendanceService attendanceService;
	
	@Autowired
	private StateService stateService; 
	
	@After
	public void setup() {
		
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	@DatabaseSetup(type=DatabaseOperation.CLEAN_INSERT, value="/xml/AttendanceServiceImplTest/insertServiceSucess.xml")
	@DatabaseTearDown(type=DatabaseOperation.DELETE_ALL, value="/xml/AttendanceServiceImplTest/insertServiceSucess.xml")
	@ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value="/xml/AttendanceServiceImplTest/insertServiceSucessUpdate.xml")
	public void testInsertAttendancesSuccess() throws Exception {

		SimpleDateFormat formato = new SimpleDateFormat( "dd/MM/yyyy" ); 
		Date data = formato.parse( "23/11/2015" ); 
		
		CallDTO call = new CallDTO();
		StateDTO state = stateService.findOnByUf("RJ");
		
		
		call.setCareDay(data);
		call.setDetails("ligacao para duvidas de compra 2");
		call.setPhone("2126212701");
		call.setReasonCalled(ReasonCalled.CRITICAL);
		call.setTypeOfCall(TypeOfCall.CHAT);
		call.setUf(state);
		
		attendanceService.insertCall(call);
	}
	
	@Test
	@DatabaseSetup(type=DatabaseOperation.CLEAN_INSERT, value="/xml/AttendanceServiceImplTest/selectServiceSucess.xml")
	@DatabaseTearDown(type=DatabaseOperation.DELETE_ALL, value="/xml/AttendanceServiceImplTest/selectServiceSucess.xml")
	public void testfindCallGroupByCareDayAndUfOrderByCareDayDescSuccess() throws Exception {

		List<CallDTO> callsDTO = attendanceService.findCallGroupByCareDayAndUfOrderByCareDayDesc();
		
		Assert.assertNotNull(callsDTO);
		Assert.assertEquals(10, callsDTO.size());
	}
}
