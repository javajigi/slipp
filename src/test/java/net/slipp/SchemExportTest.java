package net.slipp;

import javax.persistence.EntityManagerFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-jpa-applicationContext.xml")
public class SchemExportTest {
	@Autowired
	private EntityManagerFactory emf;
	
	@Test
	public void export() throws Exception {
		
	}
}
