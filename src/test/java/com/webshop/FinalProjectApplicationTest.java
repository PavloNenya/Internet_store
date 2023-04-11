package com.webshop;

import com.webshop.repositories.AccountsRepository;
import com.webshop.services.AccountsService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class FinalProjectApplicationTest {
    private AccountsService accountsService;
    @Mock
    private AccountsRepository accountsRepository;

//    @BeforeAll
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        myDataService = new MyDataServiceImpl(myRepository);
//        myService = new MyServiceImpl(myDataService);
//    }
//
//    @Test
//    public void getById_ValidId() {
//        doReturn(someMockData).when(myRepository).findOne("1");
//        MyObject myObject = myService.getById("1");
//
//        //Whatever asserts need to be done on the object myObject
//    }
}