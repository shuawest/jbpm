package org.jbpm.integrationtests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.test.util.AbstractBaseTest;
import org.junit.Test;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;

public class ExecutionFlowControlTest  extends AbstractBaseTest {

    @Test
    public void testRuleFlowUpgrade() throws Exception {
        final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        // Set the system property so that automatic conversion can happen
        System.setProperty( "drools.ruleflow.port", "true" );

        kbuilder.add( ResourceFactory.newClassPathResource("ruleflow.drl", ExecutionFlowControlTest.class), ResourceType.DRL);
        kbuilder.add( ResourceFactory.newClassPathResource("ruleflow40.rfm", ExecutionFlowControlTest.class), ResourceType.DRF);
        KnowledgeBase kbase = kbuilder.newKnowledgeBase();
        final StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        final List list = new ArrayList();
        ksession.setGlobal("list", list);
        ksession.fireAllRules();
        assertEquals(0, list.size());
        final ProcessInstance processInstance = ksession.startProcess("0");
        assertEquals(ProcessInstance.STATE_ACTIVE, processInstance.getState());
        ksession.fireAllRules();
        assertEquals( 4,
                      list.size() );
        assertEquals( "Rule1",
                      list.get( 0 ) );
        list.subList(1,2).contains( "Rule2" );
        list.subList(1,2).contains( "Rule3" );
        assertEquals( "Rule4",
                      list.get( 3 ) );
        assertEquals( ProcessInstance.STATE_COMPLETED,
                      processInstance.getState() );
        // Reset the system property so that automatic conversion should not happen
        System.setProperty("drools.ruleflow.port", "false");
    }

}
