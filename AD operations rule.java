System.out.println("Provisioner is running...");


  import org.apache.log4j.Logger;
   import sailpoint.object.*;
   import sailpoint.object.ProvisioningPlan;
   import sailpoint.object.ProvisioningPlan.AccountRequest;
   import sailpoint.object.ProvisioningPlan.AttributeRequest;
   import sailpoint.api.Provisioner;
   import java.util.List;
   import java.util.ArrayList;
   import java.util.Map;
   import java.util.HashMap;
  
  Logger logger=Logger.getLogger("MyLogger");
  
  logger.info("Provisioning Plan Initiated");
  
  //identity to provision in AD
  String idenname=identityName;
  String username=identityName;
String ent=selectmanagedAttributeName;



  System.out.println("Selec iden");
 System.out.println(selectIdentity);
System.out.println("Identity: "+identityName);
System.out.println("Entitlement: "+ent);
System.out.println(ADoperation);

logger.info(selectIdentity);

  Identity iden=context.getObjectByName(Identity.class,idenname);
  
  logger.info("Username " +idenname);
  logger.info("iden " +iden);

  
  //Create a new plan 
  ProvisioningPlan plan= new ProvisioningPlan();
  plan.setIdentity(iden);
  logger.info("Plan" +plan);
  
  //Provisioning plan Account Request
  AccountRequest accountRequest= new AccountRequest();
  
  logger.info("Account Request Started");

  String dn="CN="+username+",OU=Texas,OU=Dexperts,DC=dexperts,DC=local";
  logger.info(username);
  logger.info(dn);
  
 
	
	  
	  accountRequest.add(new AttributeRequest("IIQDisabled",ProvisioningPlan.Operation.Add,false));
      logger.info("IIQ is enabled");
      accountRequest.setNativeIdentity(dn); 

      //AD Application Name
      accountRequest.setApplication("Active Directory Application");
  
  switch(ADoperation)
  { 
	case CREATE: 
		  System.out.println("Account Creation in AD started");	
       	  accountRequest.setOperation(ProvisioningPlan.AccountRequest.Operation.Create);
		  accountRequest.add(new AttributeRequest("samAccountName",ProvisioningPlan.Operation.Set,username));
          accountRequest.add(new AttributeRequest("password",ProvisioningPlan.Operation.Add,"Dexperts123."));
          accountRequest.add(new AttributeRequest("distinguishedName",ProvisioningPlan.Operation.Set,dn));
		  accountRequest.add(new AttributeRequest("memberOf",ProvisioningPlan.Operation.Add,ent));
  
		 
	  //Create new operation for the application
      // accountRequest.setOperation(ProvisioningPlan.AccountRequest.Operation.Modify);
       //System.out.println("Modify acc request");
 // accountRequest.add(new AttributeRequest("samAccountName",ProvisioningPlan.Operation.Set,username));
  //accountRequest.add(new AttributeRequest("password",ProvisioningPlan.Operation.Add,"Dexperts123."));
  //accountRequest.add(new AttributeRequest("distinguishedName",ProvisioningPlan.Operation.Set,dn));
  //accountRequest.add(new AttributeRequest("memberOf",ProvisioningPlan.Operation.Add,ent));
  
	case ADD:
	 System.out.println("Entitlement Added");	
	 accountRequest.setOperation(ProvisioningPlan.AccountRequest.Operation.Modify);
	 accountRequest.add(new AttributeRequest("memberOf",ProvisioningPlan.Operation.Add,ent));
  
	case REMOVE:
	 System.out.println("Entitlement Removed");	
	 accountRequest.setOperation(ProvisioningPlan.AccountRequest.Operation.Modify);
	 accountRequest.remove(new AttributeRequest("memberOf",ProvisioningPlan.Operation.Add,ent));
  
    case ENABLE:
	 System.out.println("Account Enabled");	
	 accountRequest.setOperation(ProvisioningPlan.AccountRequest.Operation.Enable);
	// accountRequest.add(new AttributeRequest("memberOf",ProvisioningPlan.Operation.Add,ent));
	
	case DISABLE:
	 System.out.println("Account is disabled");	
	 accountRequest.setOperation(ProvisioningPlan.AccountRequest.Operation.Disable);
	 //accountRequest.add(new AttributeRequest("memberOf",ProvisioningPlan.Operation.Add,ent));
	 
	default: System.out.println("Invalid Operation");
  }

  List list = new ArrayList(); 
  list.add(accountRequest);
  
  //Account Request added to the provisioning plan
  plan.setAccountRequests(list);
  
 System.out.println(plan.toXml());
  return plan;