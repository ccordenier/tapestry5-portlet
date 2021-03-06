// Copyright 2009, 2010, 2011 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.portlet;

import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.Wait;

/**
 * Tests related to the Index Portlet.
 */
public class IndexPortletTests extends SeleniumTestCase
{
	String indexLocation = "/tapestry5-portlet/portal/Index";
	String AboutLocation = "/tapestry5-portlet/portal/About";
	String GridLocation = "/tapestry5-portlet/portal/Grid";
	
    @Test
    public void PublishEvent()
    {
    	open(indexLocation);
        clickAndWait("link=Publish event");
        assertTextPresent("sampleEvent");
    }
    
    @Test
    public void To_Block_Caller()
    {
    	open(indexLocation);
    	click("link=To Block Caller");
    	waitForPageToLoad();
    	clickAndWait("link=1");
    	assertTextPresent("Page activation context: 1");
    	clickAndWait("link=2");
    	assertTextPresent("Page activation context: 2");
    	clickAndWait("link=3");
    	assertTextPresent("Page activation context: 3");
    }

   
    
    @Test
    public void form_validation_test()
    {
    	String SubmitLastName = "//form[contains(@id,'lastName')]/input[@type='submit']";
    	String InputLastName = "//input[starts-with(@id,'lastName')]";
    	
    	open(indexLocation);
    	
        type(InputLastName, "");

        click(SubmitLastName);

        assertText("//p[starts-with(@data-error-block-for,'lastName')]", "You must provide a value for Last Name.");

        type(InputLastName, "fac");
        click(SubmitLastName);
        waitForPageToLoad();
        assertTextPresent(" fac is stored in portlet session scope Application with @Persist!");
    }
    
    
    @Test
    public void Ajax_form_with_client_Id_test()
    {
    	String SubmitFirstName = "//form[contains(@id,'firstName')]/input[@type='submit']";
    	String InputFirstName = "//input[starts-with(@id,'firstName')]";
    	
    	open(indexLocation);	
        type(InputFirstName, "fra"); 
        click(SubmitFirstName);
        
        waitForAjaxRequestsToComplete("1000");
        assertTextPresent("Hi fra from Ajax Form!");
    }
    
    @Test
    public void Ajax_form_without_client_Id_test()
    {
    	String SubmitSurname = "//form[contains(@id,'surname')]/input[@type='submit']";
    	String InputSurname = "//input[starts-with(@id,'surname')]";
    	
    	open(indexLocation);	
        type(InputSurname, "frafac"); 
        click(SubmitSurname);
        
        waitForAjaxRequestsToComplete("1000");
        assertTextPresent("your surname is frafac from Ajax Form without client id!");
    }
    
    @Test
    public void cookies_test()
    {
    	
    	open(AboutLocation);	
    	waitForPageToLoad();
    	click("link=resetCount");
    	waitForPageToLoad();
        //waitForAjaxRequestsToComplete("1000");
        
        assertTextPresent("nb view = 1");
        open(AboutLocation);
        assertTextPresent("nb view = 2");
        
    }
    
    
    @Test
    public void AjaxFormLoop_test()
    {
    	String InputName = "//input[starts-with(@id,'nom')]";
    	String InputId = "//input[starts-with(@id,'id')]";
    	final String removeRow ="//a[contains(@data-afl-behavior,'remove')]";
    	String textarea = "//textarea[starts-with(@id,'texte')]";
   
    	
    	open(AboutLocation);	

    	    	
    	new Wait()
        {
            @Override
            public boolean until()
            {
                return getXpathCount("//input[contains(@id,'nom')]").equals(1);
            }
        }.wait("We should have just one input with id nom.", 1000);
    	
    	
    	type(InputName, "AjaxFormLoop");
    	    	
    	click("xpath=//a[contains(@data-afl-trigger,'add')]");
    	    	
    	
    	new Wait()
        {
            @Override
            public boolean until()
            {
                return getXpathCount(removeRow).equals(1);
            }
        }.wait("We should have just one row.", 1000);
    	
        
        type(InputId, "1"); 
        type(textarea, "MyAjaxFormLoopTexte");
        click("//input[starts-with(@id,'save')]");
        waitForPageToLoad();
        
        assertTextPresent("MyAjaxFormLoopTexte");
        
             
        click("xpath="+removeRow);
    	
    	new Wait()
        {
            @Override
            public boolean until()
            {
                return getXpathCount(removeRow).equals(0);
            }
        }.wait("The first row should be deleted.", 1000);
        
       
        
    }
    
}
