package net.slipp.support.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-schemaexport-applicationContext.xml" })
public class ExportSchemaTest {
    
    @Autowired
    private JpaSpringSchemaExport jpaSpringSchemaExport;
    
    @Test
    public void testExportSchema() {
        jpaSpringSchemaExport.export();
    }
}
