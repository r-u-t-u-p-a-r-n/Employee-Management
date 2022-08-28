package site.operations.ServiceImplementation;

import site.operations.Models.*;
import site.operations.Services.*;
import site.operations.Repositories.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

@Service
public class OrgServicesExe implements OrgServices,ExtraFunctions
{
	@Autowired
	private EmployeeRepo employeeRepo ;

	@Autowired
	private AssetsRepo assetsRepo ;

	@Autowired
	private BasicAuthRepository basicAuthRepository ;

	@Autowired
	private OrgRepo orgRepo ;

	public OrgServicesExe(BasicAuthRepository basicAuthRepository, OrgRepo orgRepo,
		                  EmployeeRepo employeeRepo, AssetsRepo assetsRepo)
	{
		this.basicAuthRepository = basicAuthRepository ;
		this.orgRepo = orgRepo ;
		this.employeeRepo = employeeRepo ;
		this.assetsRepo = assetsRepo ;
	}

	@Override
	public ResponseEntity<OrgData> addNewOrg(OrgData orgData)
	{
		if(orgData.getOrgId() == null || orgData.getOrgId().length() == 0)
		{
			orgData.setOrgId("Id cant\'t be null");
			return new ResponseEntity<OrgData> (orgData,HttpStatus.BAD_REQUEST);
		}

		if(orgData.getName() == null || orgData.getName().length() == 0)
		{
			orgData.setName("Name cant\'t be null");
			return new ResponseEntity<OrgData> (orgData,HttpStatus.BAD_REQUEST);
		}

		if(!(checkId(orgData.getOrgId())))
		{
			orgData.setOrgId("Id \'"+orgData.getOrgId()+"\' already exists");
			return new ResponseEntity<OrgData> (orgData,HttpStatus.BAD_REQUEST);
		}

		BasicAuth BA = new BasicAuth();
		BA.setUserId(orgData.getOrgId());
		BA.setEmail(orgData.getOrgId()+"@myapi");
		BA.setPassword(passwordEncoder().encode(orgData.getOrgId()));
		BA.setRole("ORG");
		basicAuthRepository.save(BA);

		return new ResponseEntity<OrgData> (orgRepo.save(orgData),HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<String> deleteOrgData()
	{
		OrgData orgData = getOrgDetails();
		if(orgData != null)
		{
			List<EmployeeData> E = employeeRepo.findAll();
			for(EmployeeData ED : E)
			{
				if(ED.getOrganizationId().equals(orgData.getOrgId()))
					employeeRepo.deleteById(ED.getEmployeeId());
			}
			List<AssetsData> A = assetsRepo.findAll();
			for(AssetsData AD : A)
			{
				if(AD.getOrganizationId().equals(orgData.getOrgId()))
					assetsRepo.deleteById(AD.getAssetId());
			}
			basicAuthRepository.deleteById(orgData.getOrgId());
			orgRepo.deleteById(orgData.getOrgId());
			return new ResponseEntity<String> ("Organization is deleted, successfully",HttpStatus.OK);
		}
		
		return new ResponseEntity<String> ("Organization Data not found",HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<OrgData> updateOrgData(OrgData orgData)
	{		
		OrgData OD = getOrgDetails();
		if(OD != null)
		{
			if(orgData.getName() == null || orgData.getName().length() == 0)
			{
				orgData.setName("Name cant\'t be null");
				return new ResponseEntity<OrgData> (orgData,HttpStatus.BAD_REQUEST);
			}
			OD.setName(orgData.getName());
			return new ResponseEntity<OrgData> (orgRepo.save(OD),HttpStatus.OK);
		}

		return new ResponseEntity<OrgData> (orgData,HttpStatus.NOT_FOUND);
	}

	@Override
    public List<String> deleteEmployeeData(String id)
    {
        OrgData orgData = getOrgDetails();
        List<String> LR = new <String>ArrayList();
        EmployeeData Emp ;
        Set<String> values = ExtraFunctions.getValues(id,',');
        if(orgData!=null)
        {
	        for(String v : values)
	        {
	            try
	            {
	            	Emp = employeeRepo.findById(v).orElseThrow(Exception::new);
	            	if(!(Emp.getOrganizationId().equals(orgData.getOrgId())))
	                    throw new Exception();
	            	basicAuthRepository.deleteById(v);
	                employeeRepo.deleteById(v);
	                LR.add("Element at id \'"+v+"\' is deleted sucessfully");
	            }
	            catch(Exception e)
	            {
	                LR.add("Element not found at id \'"+v+"\'");
	            }
	        }
	    }
	    else
	    	LR.add("Organization is empty"); 

        return LR ;
    }

    @Override
    public List<String> deleteAssetsData(String id)
    {
        AssetsData AD ;
        List<String> LR = new ArrayList<String>();
        Set<String> values = ExtraFunctions.getValues(id,',');
        
        OrgData orgData = getOrgDetails();
        if(orgData!=null)
        {
	        for(String v : values)
	        {
	            try
	            {
	                AD = assetsRepo.findById(v).orElseThrow(Exception::new);
	                if(!(AD.getOrganizationId().equals(orgData.getOrgId())))
	                    throw new Exception();
	                
	              	assetsRepo.deleteById(v);
	                LR.add("Element at id \'"+v+"\' is deleted sucessfully");
	            }
	            catch(Exception e)
	            {
	                LR.add("Element not found at id \'"+v+"\'");
	            }
	        }
	    }
	    
	    else
	    	LR.add("Organization is empty");    

        return LR ;
    }

/*---------------------------------------------------------------------------------------------*/

	private final OrgData getOrgDetails()
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		OrgData orgData = null ;
		List<BasicAuth> L = basicAuthRepository.findAll();
		for(BasicAuth BA : L)
		{
			if(BA.getEmail().equals(auth.getName()))
			{
				try
				{
					return orgRepo.findById(BA.getUserId()).orElseThrow(Exception::new) ;
				}

				catch(Exception e) {}
			}
		}

		return null ;
	}

	private final boolean checkId(String id)
	{
		List<OrgData> L0 = orgRepo.findAll();
		for(OrgData OD : L0)
		{
			if(id.equals(OD.getOrgId()))
				return false ;
		}

		List<EmployeeData> L1 = employeeRepo.findAll();
		for(EmployeeData ED : L1)
		{
			if(id.equals(ED.getEmployeeId()))
				return false ;
		}

		List<AssetsData> L2 = assetsRepo.findAll();
		for(AssetsData AD : L2)
		{
			if(id.equals(AD.getAssetId()))
				return false ;
		}
		
		return true ;
	}

/*---------------------------------------------------------------------------------------------*/

	private final PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}