package org.jbpm.integrationtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.drools.compiler.compiler.PackageBuilder;
import org.drools.core.RuleBase;
import org.drools.core.RuleBaseFactory;
import org.drools.core.WorkingMemory;
import org.drools.core.rule.Package;
import org.jbpm.integrationtests.handler.TestWorkItemHandler;
import org.jbpm.process.instance.ProcessInstance;
import org.jbpm.test.util.AbstractBaseTest;
import org.junit.Test;
import org.kie.api.runtime.process.WorkItem;

public class ProcessHumanTaskTest extends AbstractBaseTest {
    
    @Test
    public void testHumanTask() {
        PackageBuilder builder = new PackageBuilder();
        Reader source = new StringReader(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<process xmlns=\"http://drools.org/drools-5.0/process\"\n" +
            "         xmlns:xs=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "         xs:schemaLocation=\"http://drools.org/drools-5.0/process drools-processes-5.0.xsd\"\n" +
            "         type=\"RuleFlow\" name=\"flow\" id=\"org.drools.humantask\" package-name=\"org.drools\" version=\"1\" >\n" +
            "\n" +
            "  <header>\n" +
            "  </header>\n" +
            "\n" +
            "  <nodes>\n" +
            "    <start id=\"1\" name=\"Start\" />\n" +
            "    <humanTask id=\"2\" name=\"HumanTask\" >\n" +
            "      <work name=\"Human Task\" >\n" +
            "        <parameter name=\"ActorId\" >\n" +
            "          <type name=\"org.drools.core.process.core.datatype.impl.type.StringDataType\" />\n" +
            "          <value>John Doe</value>\n" +
            "        </parameter>\n" +
            "        <parameter name=\"TaskName\" >\n" +
            "          <type name=\"org.drools.core.process.core.datatype.impl.type.StringDataType\" />\n" +
            "          <value>Do something</value>\n" +
            "        </parameter>\n" +
            "        <parameter name=\"Priority\" >\n" +
            "          <type name=\"org.drools.core.process.core.datatype.impl.type.StringDataType\" />\n" +
            "        </parameter>\n" +
            "        <parameter name=\"Comment\" >\n" +
            "          <type name=\"org.drools.core.process.core.datatype.impl.type.StringDataType\" />\n" +
            "        </parameter>\n" +
            "      </work>\n" +
            "    </humanTask>\n" +
            "    <end id=\"3\" name=\"End\" />\n" +
            "  </nodes>\n" +
            "\n" +
            "  <connections>\n" +
            "    <connection from=\"1\" to=\"2\" />\n" +
            "    <connection from=\"2\" to=\"3\" />\n" +
            "  </connections>\n" +
            "\n" +
            "</process>");
        builder.addRuleFlow(source);
        Package pkg = builder.getPackage();
        RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        ruleBase.addPackage( pkg );
        WorkingMemory workingMemory = ruleBase.newStatefulSession();
        TestWorkItemHandler handler = new TestWorkItemHandler();
        workingMemory.getWorkItemManager().registerWorkItemHandler("Human Task", handler);
        ProcessInstance processInstance = ( ProcessInstance )
            workingMemory.startProcess("org.drools.humantask");
        assertEquals(ProcessInstance.STATE_ACTIVE, processInstance.getState());
        WorkItem workItem = handler.getWorkItem();
        assertNotNull(workItem);
        workingMemory.getWorkItemManager().completeWorkItem(workItem.getId(), null);
        assertEquals(ProcessInstance.STATE_COMPLETED, processInstance.getState());
    }
    
    @Test
    public void testSwimlane() {
        PackageBuilder builder = new PackageBuilder();
        Reader source = new StringReader(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<process xmlns=\"http://drools.org/drools-5.0/process\"\n" +
            "         xmlns:xs=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "         xs:schemaLocation=\"http://drools.org/drools-5.0/process drools-processes-5.0.xsd\"\n" +
            "         type=\"RuleFlow\" name=\"flow\" id=\"org.drools.humantask\" package-name=\"org.drools\" version=\"1\" >\n" +
            "\n" +
            "  <header>\n" +
            "    <swimlanes>\n" +
            "      <swimlane name=\"actor1\" />\n" +
            "    </swimlanes>\n" +
            "  </header>\n" +
            "\n" +
            "  <nodes>\n" +
            "    <start id=\"1\" name=\"Start\" />\n" +
            "    <humanTask id=\"2\" name=\"HumanTask\" swimlane=\"actor1\" >\n" +
            "      <work name=\"Human Task\" >\n" +
            "        <parameter name=\"ActorId\" >\n" +
            "          <type name=\"org.drools.core.process.core.datatype.impl.type.StringDataType\" />\n" +
            "          <value>John Doe</value>\n" +
            "        </parameter>\n" +
            "        <parameter name=\"TaskName\" >\n" +
            "          <type name=\"org.drools.core.process.core.datatype.impl.type.StringDataType\" />\n" +
            "          <value>Do something</value>\n" +
            "        </parameter>\n" +
            "        <parameter name=\"Priority\" >\n" +
            "          <type name=\"org.drools.core.process.core.datatype.impl.type.StringDataType\" />\n" +
            "        </parameter>\n" +
            "        <parameter name=\"Comment\" >\n" +
            "          <type name=\"org.drools.core.process.core.datatype.impl.type.StringDataType\" />\n" +
            "        </parameter>\n" +
            "      </work>\n" +
            "    </humanTask>\n" +
            "    <humanTask id=\"3\" name=\"HumanTask\" swimlane=\"actor1\" >\n" +
            "      <work name=\"Human Task\" >\n" +
            "        <parameter name=\"ActorId\" >\n" +
            "          <type name=\"org.drools.core.process.core.datatype.impl.type.StringDataType\" />\n" +
            "        </parameter>\n" +
            "        <parameter name=\"TaskName\" >\n" +
            "          <type name=\"org.drools.core.process.core.datatype.impl.type.StringDataType\" />\n" +
            "          <value>Do something else</value>\n" +
            "        </parameter>\n" +
            "        <parameter name=\"Priority\" >\n" +
            "          <type name=\"org.drools.core.process.core.datatype.impl.type.StringDataType\" />\n" +
            "        </parameter>\n" +
            "        <parameter name=\"Comment\" >\n" +
            "          <type name=\"org.drools.core.process.core.datatype.impl.type.StringDataType\" />\n" +
            "        </parameter>\n" +
            "      </work>\n" +
            "    </humanTask>\n" +
            "    <end id=\"4\" name=\"End\" />\n" +
            "  </nodes>\n" +
            "\n" +
            "  <connections>\n" +
            "    <connection from=\"1\" to=\"2\" />\n" +
            "    <connection from=\"2\" to=\"3\" />\n" +
            "    <connection from=\"3\" to=\"4\" />\n" +
            "  </connections>\n" +
            "\n" +
            "</process>");
        builder.addRuleFlow(source);
        Package pkg = builder.getPackage();
        RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        ruleBase.addPackage( pkg );
        WorkingMemory workingMemory = ruleBase.newStatefulSession();
        TestWorkItemHandler handler = new TestWorkItemHandler();
        workingMemory.getWorkItemManager().registerWorkItemHandler("Human Task", handler);
        ProcessInstance processInstance = ( ProcessInstance )
            workingMemory.startProcess("org.drools.humantask");
        assertEquals(ProcessInstance.STATE_ACTIVE, processInstance.getState());
        WorkItem workItem = handler.getWorkItem();
        assertNotNull(workItem);
        assertEquals("Do something", workItem.getParameter("TaskName"));
        assertEquals("John Doe", workItem.getParameter("ActorId"));
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("ActorId", "Jane Doe");
        workingMemory.getWorkItemManager().completeWorkItem(workItem.getId(), results);
        workItem = handler.getWorkItem();
        assertNotNull(workItem);
        assertEquals("Do something else", workItem.getParameter("TaskName"));
        assertEquals("Jane Doe", workItem.getParameter("ActorId"));
        workingMemory.getWorkItemManager().completeWorkItem(workItem.getId(), null);
        assertEquals(ProcessInstance.STATE_COMPLETED, processInstance.getState());
    }

    @Test
    public void testHumanTaskCancel() {
        PackageBuilder builder = new PackageBuilder();
        Reader source = new StringReader(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<process xmlns=\"http://drools.org/drools-5.0/process\"\n" +
            "         xmlns:xs=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "         xs:schemaLocation=\"http://drools.org/drools-5.0/process drools-processes-5.0.xsd\"\n" +
            "         type=\"RuleFlow\" name=\"flow\" id=\"org.drools.humantask\" package-name=\"org.drools\" version=\"1\" >\n" +
            "\n" +
            "  <header>\n" +
            "  </header>\n" +
            "\n" +
            "  <nodes>\n" +
            "    <start id=\"1\" name=\"Start\" />\n" +
            "    <humanTask id=\"2\" name=\"HumanTask\" >\n" +
            "      <work name=\"Human Task\" >\n" +
            "        <parameter name=\"ActorId\" >\n" +
            "          <type name=\"org.drools.core.process.core.datatype.impl.type.StringDataType\" />\n" +
            "          <value>John Doe</value>\n" +
            "        </parameter>\n" +
            "        <parameter name=\"TaskName\" >\n" +
            "          <type name=\"org.drools.core.process.core.datatype.impl.type.StringDataType\" />\n" +
            "          <value>Do something</value>\n" +
            "        </parameter>\n" +
            "        <parameter name=\"Priority\" >\n" +
            "          <type name=\"org.drools.core.process.core.datatype.impl.type.StringDataType\" />\n" +
            "        </parameter>\n" +
            "        <parameter name=\"Comment\" >\n" +
            "          <type name=\"org.drools.core.process.core.datatype.impl.type.StringDataType\" />\n" +
            "        </parameter>\n" +
            "      </work>\n" +
            "    </humanTask>\n" +
            "    <end id=\"3\" name=\"End\" />\n" +
            "  </nodes>\n" +
            "\n" +
            "  <connections>\n" +
            "    <connection from=\"1\" to=\"2\" />\n" +
            "    <connection from=\"2\" to=\"3\" />\n" +
            "  </connections>\n" +
            "\n" +
            "</process>");
        builder.addRuleFlow(source);
        Package pkg = builder.getPackage();
        RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        ruleBase.addPackage( pkg );
        WorkingMemory workingMemory = ruleBase.newStatefulSession();
        TestWorkItemHandler handler = new TestWorkItemHandler();
        workingMemory.getWorkItemManager().registerWorkItemHandler("Human Task", handler);
        ProcessInstance processInstance = ( ProcessInstance )
            workingMemory.startProcess("org.drools.humantask");
        assertEquals(ProcessInstance.STATE_ACTIVE, processInstance.getState());
        WorkItem workItem = handler.getWorkItem();
        assertNotNull(workItem);
        processInstance.setState(ProcessInstance.STATE_ABORTED);
        assertTrue(handler.isAborted());
    }
    
    @Test
    public void testHumanTaskCancel2() {
        PackageBuilder builder = new PackageBuilder();
        Reader source = new StringReader(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<process xmlns=\"http://drools.org/drools-5.0/process\"\n" +
            "         xmlns:xs=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "         xs:schemaLocation=\"http://drools.org/drools-5.0/process drools-processes-5.0.xsd\"\n" +
            "         type=\"RuleFlow\" name=\"flow\" id=\"org.drools.humantask\" package-name=\"org.drools\" version=\"1\" >\n" +
            "\n" +
            "  <header>\n" +
            "  </header>\n" +
            "\n" +
            "  <nodes>\n" +
            "    <start id=\"1\" name=\"Start\" />\n" +
            "    <humanTask id=\"2\" name=\"HumanTask\" >\n" +
            "      <work name=\"Human Task\" >\n" +
            "        <parameter name=\"ActorId\" >\n" +
            "          <type name=\"org.drools.core.process.core.datatype.impl.type.StringDataType\" />\n" +
            "          <value>John Doe</value>\n" +
            "        </parameter>\n" +
            "        <parameter name=\"TaskName\" >\n" +
            "          <type name=\"org.drools.core.process.core.datatype.impl.type.StringDataType\" />\n" +
            "          <value>Do something</value>\n" +
            "        </parameter>\n" +
            "        <parameter name=\"Priority\" >\n" +
            "          <type name=\"org.drools.core.process.core.datatype.impl.type.StringDataType\" />\n" +
            "        </parameter>\n" +
            "        <parameter name=\"Comment\" >\n" +
            "          <type name=\"org.drools.core.process.core.datatype.impl.type.StringDataType\" />\n" +
            "        </parameter>\n" +
            "      </work>\n" +
            "      <onExit>\n" +
            "        <action type=\"expression\" name=\"Cancel\" dialect=\"java\" >((org.jbpm.workflow.instance.NodeInstance) kcontext.getNodeInstance()).cancel();</action>\n" +
            "      </onExit>\n" +
            "    </humanTask>\n" +
            "    <end id=\"3\" name=\"End\" />\n" +
            "  </nodes>\n" +
            "\n" +
            "  <connections>\n" +
            "    <connection from=\"1\" to=\"2\" />\n" +
            "    <connection from=\"2\" to=\"3\" />\n" +
            "  </connections>\n" +
            "\n" +
            "</process>");
        builder.addRuleFlow(source);
        Package pkg = builder.getPackage();
        RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        ruleBase.addPackage( pkg );
        WorkingMemory workingMemory = ruleBase.newStatefulSession();
        TestWorkItemHandler handler = new TestWorkItemHandler();
        workingMemory.getWorkItemManager().registerWorkItemHandler("Human Task", handler);
        ProcessInstance processInstance = ( ProcessInstance )
            workingMemory.startProcess("org.drools.humantask");
        assertEquals(ProcessInstance.STATE_ACTIVE, processInstance.getState());
        WorkItem workItem = handler.getWorkItem();
        assertNotNull(workItem);
        workingMemory.getWorkItemManager().completeWorkItem(workItem.getId(), null);
        assertFalse(handler.isAborted());
    }

}
